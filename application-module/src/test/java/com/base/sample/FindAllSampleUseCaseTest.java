package com.base.sample;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.base.config.SimpleFixtureMonkey;
import com.base.sample.ports.in.FindAllSampleQuery.FindAllResult;
import com.base.sample.ports.out.FindAll;
import com.base.sample.ports.out.FindAll.SampleQueryResult;
import com.base.sample.usecase.FindAllSampleUseCase;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.generator.ArbitraryGeneratorContext;
import com.navercorp.fixturemonkey.api.jqwik.JavaArbitraryResolver;
import com.navercorp.fixturemonkey.api.jqwik.JqwikPlugin;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.arbitraries.LongArbitrary;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class FindAllSampleUseCaseTest {

    @Mock
    private FindAll findAll;

    @InjectMocks
    private FindAllSampleUseCase findAllSampleUseCase;


    @DisplayName("Sample 생성")
    @Nested
    class FinAllSample {

        private static FixtureMonkey fixtureMonkey;

        @BeforeAll
        static void setup() {
            fixtureMonkey = SimpleFixtureMonkey.giveMe(
                true,
                new JakartaValidationPlugin(),
                new JqwikPlugin().javaArbitraryResolver(
                    new JavaArbitraryResolver() {
                        @Override
                        public Arbitrary<Long> longs(LongArbitrary longArbitrary, ArbitraryGeneratorContext context) {
                            return Arbitraries.longs().greaterOrEqual(1);
                        }
                    }
                )
            );
        }

        @DisplayName("성공")
        @Test
        void success() {

            Pageable request = PageRequest.of(1, 10);
            Integer size = 10;
            given(findAll.fetch(any(Pageable.class)))
                .willReturn(new PageImpl<>(fixtureMonkey.giveMe(SampleQueryResult.class, size)));

            Page<FindAllResult> result = findAllSampleUseCase.findAll(request);

            assertThat(result.getSize()).isEqualTo(size);
            verify(findAll, times(1)).fetch(any(Pageable.class));
        }
    }

}
