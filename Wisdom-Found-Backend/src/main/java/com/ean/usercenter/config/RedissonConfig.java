package com.ean.usercenter.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:TODO
 * @author:Povlean
 */
@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient() {
        // 1. 创建配置
        Config config = new Config();
        String redisAddress = "redis://127.0.0.1:6379";
        config.useSingleServer()
                .setAddress(redisAddress)
                .setDatabase(3);
        // or read config from file
        // Sync and Async API
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }

}
