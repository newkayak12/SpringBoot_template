package com.base.sample;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.base.config.SimpleFixtureMonkey;
import com.base.sample.entity.Sample;
import com.base.sample.ports.in.CreateSampleCommand.CreateSampleCommandRequest;
import com.base.sample.ports.out.SaveSample;
import com.base.sample.usecase.CreateSampleUseCase;
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

@ExtendWith(MockitoExtension.class)
class CreateSampleUseCaseTest {

    @Mock
    private SaveSample saveSample;

    @InjectMocks
    private CreateSampleUseCase createSampleUseCase;


    @DisplayName("Sample 생성")
    @Nested
    class CreateSample {

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

            CreateSampleCommandRequest request = fixtureMonkey.giveMeOne(CreateSampleCommandRequest.class);

            boolean result = createSampleUseCase.createSample(request);

            assertTrue(result);
            verify(saveSample, times(1)).save(any(Sample.class));
        }
    }

}
