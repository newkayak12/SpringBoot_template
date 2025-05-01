package com.base.config.redis;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.File;
import java.util.Objects;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import redis.embedded.RedisServer;

@Configuration
@Profile(value = {"temporary", "local"})
public class EmbeddedRedisConfig {

    private final RedisServer redisServer;

    public EmbeddedRedisConfig(Environment environment) {
        Integer port = environment.getProperty("spring.data.redis.port", Integer.class);
        if (this.isArmMac()) {
            redisServer = new RedisServer(Objects.requireNonNull(getRedisFileForArcMac()), port);
        } else {
            redisServer = RedisServer.builder().port(port).setting("maxmemory 128M").build();
        }
    }

    @PostConstruct
    public void startRedisMockServer() {
        redisServer.start();
    }

    @PreDestroy
    public void finalizeRedisMockServer() {
        redisServer.stop();
    }

    private boolean isArmMac() {
        return Objects.equals(System.getProperty("os.arch"), "aarch64") &&
            Objects.equals(System.getProperty("os.name"), "Mac OS X");
    }

    private File getRedisFileForArcMac() {
        try {
            File file = new ClassPathResource("redis/redis-server").getFile();
            file.setExecutable(true);
            return file;
        } catch (Exception e) {
            throw new RuntimeException("REDIS SERVER HAS WRONG");
        }
    }
}
