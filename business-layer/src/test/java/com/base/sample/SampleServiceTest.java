package com.base.sample;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

import com.base.sample.dto.SampleQueryResult;
import com.base.sample.dto.out.SampleDto;
import com.base.sample.repository.SampleRepository;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FailoverIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.plugin.SimpleValueJqwikPlugin;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SampleServiceTest {

    @Mock
    private SampleRepository repository;

    @InjectMocks
    private SampleService service;

    private static FixtureMonkey fixtureMonkey;


    @BeforeAll
    static void setFixtureMonkey() {
        fixtureMonkey = FixtureMonkey.builder()
            .plugin(new SimpleValueJqwikPlugin())
            .plugin(new JakartaValidationPlugin())
            .objectIntrospector(
                new FailoverIntrospector(
                    List.of(
                        ConstructorPropertiesArbitraryIntrospector.INSTANCE,
                        FieldReflectionArbitraryIntrospector.INSTANCE
                    )
                )
            )
            .build();
    }

    @DisplayName("전체 조회")
    @Test
    void findAll() {
        //given
        List<SampleQueryResult> mockList = fixtureMonkey.giveMe(SampleQueryResult.class, 10);

        //when
        doReturn(mockList).when(repository).findAll();
        List<SampleDto> result = service.findAll();

        //then
        assertThat(result).size().isEqualTo(10);
    }

}
