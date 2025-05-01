package com.base.sample;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.base.config.SimpleFixtureMonkey;
import com.base.sample.entity.Sample;
import com.base.sample.ports.in.UpdateSampleCommand.UpdateSampleCommandRequest;
import com.base.sample.ports.out.LoadSample;
import com.base.sample.usecase.UpdateSampleUseCase;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.generator.ArbitraryGeneratorContext;
import com.navercorp.fixturemonkey.api.jqwik.JavaArbitraryResolver;
import com.navercorp.fixturemonkey.api.jqwik.JqwikPlugin;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;
import java.util.NoSuchElementException;
import java.util.Optional;
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
import uuid.generator.UuidGenerator;

@ExtendWith(MockitoExtension.class)
class UpdateSampleUseCaseTest {

    @Mock
    private LoadSample loadSample;

    @InjectMocks
    private UpdateSampleUseCase updateSampleUseCase;


    @DisplayName("Sample 수정 ")
    @Nested
    class UpdateSample {

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

            String sampleId = UuidGenerator.generate();
            UpdateSampleCommandRequest request = fixtureMonkey.giveMeOne(UpdateSampleCommandRequest.class);

            given(loadSample.findById(anyString())).willReturn(
                Optional.ofNullable(fixtureMonkey.giveMeOne(Sample.class))
            );

            boolean result = updateSampleUseCase.updateSample(sampleId, request);

            assertTrue(result);
            verify(loadSample, times(1)).findById(anyString());
        }

        @DisplayName("찾을 수 없는 경우")
        @Test
        void failure() {

            String sampleId = UuidGenerator.generate();
            UpdateSampleCommandRequest request = fixtureMonkey.giveMeOne(UpdateSampleCommandRequest.class);

            given(loadSample.findById(anyString())).willReturn(Optional.empty());

            assertThrows(NoSuchElementException.class, () -> updateSampleUseCase.updateSample(sampleId, request));
            verify(loadSample, times(1)).findById(anyString());
        }
    }

}
