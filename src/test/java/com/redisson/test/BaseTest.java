package com.redisson.test;

import com.redisson.test.config.RedissonConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.redisson.api.RedissonReactiveClient;

@TestInstance(Lifecycle.PER_CLASS)
public abstract class BaseTest {
    private final RedissonConfig redissonConfig = new RedissonConfig();
    protected RedissonReactiveClient client;

    @BeforeAll
    void setClient() {
        client = redissonConfig.getReactiveClient();
    }

    @AfterAll
    void shutDown() {
        client.shutdown();
    }

    protected void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
