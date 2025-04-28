package com.base.config;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.FixtureMonkeyBuilder;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FailoverIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.plugin.Plugin;
import java.util.List;

public class SimpleFixtureMonkey {

    @SafeVarargs
    public static FixtureMonkey giveMe(final boolean defaultNotNull, final Plugin... plugins) {
        FixtureMonkeyBuilder builder = FixtureMonkey.builder()
            .enableLoggingFail(false)
            .defaultNotNull(defaultNotNull)
            .objectIntrospector(
                new FailoverIntrospector(
                    List.of(
                        FieldReflectionArbitraryIntrospector.INSTANCE,
                        ConstructorPropertiesArbitraryIntrospector.INSTANCE
                    )
                )
            );

        for (Plugin plugin : plugins) {
            builder.plugin(plugin);
        }

        return builder.build();
    }
}
