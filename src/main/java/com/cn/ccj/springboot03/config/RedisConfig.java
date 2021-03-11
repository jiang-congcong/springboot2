package com.cn.ccj.springboot03.config;

import com.cn.ccj.springboot03.redis.RedisOperate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {

    @Bean
    public RedisOperate redisOperate(){
        return new RedisOperate();
    }
}
