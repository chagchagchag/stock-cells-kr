package io.stock.evaluation.reactive_data.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Profile("test-docker")
@Configuration
public class TestRedisConfig {
    @Bean
    public ReactiveRedisConnectionFactory redisConnectionFactory(){
        // 추후 Embedded Redis 로 전환 예정.
        return new LettuceConnectionFactory("localhost", 16379);
    }

    @Bean
    public ReactiveRedisOperations<String, String> tickerMapReactiveRedisTemplate(
        ReactiveRedisConnectionFactory redisConnectionFactory
    ){
        StringRedisSerializer serializer = new StringRedisSerializer();

        RedisSerializationContext<String, String> serializationContext =
                RedisSerializationContext.<String, String>newSerializationContext()
                    .key(serializer)
                    .value(serializer)
                    .hashKey(serializer)
                    .hashValue(serializer)
                    .build();

        ReactiveRedisTemplate<String, String> redisTemplate =
                new ReactiveRedisTemplate<>(redisConnectionFactory, serializationContext);

        return redisTemplate;
    }
}
