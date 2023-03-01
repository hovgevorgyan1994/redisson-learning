package com.redisson.test.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.config.Config;

public class RedissonConfig {

    private RedissonClient redissonClient;

    public RedissonClient getRedissonClient() {
        if (redissonClient == null) {
            Config config = new Config();
            config.useSingleServer()
                .setAddress("redis://localhost:6379");
            redissonClient = Redisson.create(config);
        }
        return redissonClient;
    }

    public RedissonReactiveClient getReactiveClient() {
        return getRedissonClient().reactive();
    }
}
