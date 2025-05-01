package com.base.config.redis;

import com.base.authenticate.dto.AuthenticationDetails;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
@RequiredArgsConstructor
public class CacheManager {

    private final ObjectMapper objectMapper;

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        return RedisCacheManagerBuilder
            .fromConnectionFactory(redisConnectionFactory)
            .cacheDefaults(defaultConfiguration(Object.class, Duration.ofDays(1)))
            .withInitialCacheConfigurations(initialCacheConfigurations())
            .build();
    }

    private <T> RedisCacheConfiguration defaultConfiguration(Class<T> clazz, Duration ttl) {
        return RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(new Jackson2JsonRedisSerializer<T>(clazz)))
            .entryTtl(ttl);
    }

    private <T> RedisCacheConfiguration listConfiguration(Class<T> clazz, Duration ttl) {

        TypeFactory typeFactory = objectMapper.getTypeFactory();
        JavaType javaType = typeFactory.constructCollectionType(List.class, clazz);
        Jackson2JsonRedisSerializer<List<T>> serializer = new Jackson2JsonRedisSerializer<>(javaType);

        return RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
            .entryTtl(ttl);
    }

    private Map<String, RedisCacheConfiguration> initialCacheConfigurations() {
        return Map.of(
            "loadUserByUsernameCache", defaultConfiguration(AuthenticationDetails.class, Duration.ofDays(1))
        );
    }
}
