package com.aarhankhan.scalablechatserver.config;

import com.aarhankhan.scalablechatserver.redis.RedisMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

import static com.aarhankhan.scalablechatserver.config.ServerConstants.*;

@Slf4j
@Configuration(proxyBeanMethods=false)
public class RedisConfig {
    @Bean
    ReactiveRedisConnectionFactory reactiveRedisConnectionFactory(RedisProperties redisProperties) {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
        redisStandaloneConfiguration.setPassword(redisProperties.getPassword());
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    ReactiveStringRedisTemplate reactiveStringRedisTemplate(ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
        return new ReactiveStringRedisTemplate(reactiveRedisConnectionFactory);
    }

    @Bean
    RedisAtomicInteger chatMessageCounter(RedisConnectionFactory redisConnectionFactory) {
        return new RedisAtomicInteger(MESSAGE_COUNTER, redisConnectionFactory);
    }

    @Bean
    RedisAtomicLong activeUserCounter(RedisConnectionFactory redisConnectionFactory) {
        return new RedisAtomicLong(ACTIVE_USER, redisConnectionFactory);
    }

    @Bean
    ApplicationRunner applicationRunner(RedisMessageListener redisMessageListener) {
        return args -> {
            redisMessageListener.subscribeMessageChannelAndPublishOnWebSocket()
                    .doOnSubscribe(subscription -> log.info("Redis Listener Started"))
                    .doOnError(throwable -> log.error("Error listening Redis", throwable))
                    .doFinally(signalType -> log.info("Stopped Listener, Signal Type: {}", signalType))
                    .subscribe();
        };
    }
}
