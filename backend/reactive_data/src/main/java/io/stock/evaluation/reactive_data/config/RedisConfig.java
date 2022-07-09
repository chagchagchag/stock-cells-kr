package io.stock.evaluation.reactive_data.config;

import io.stock.evaluation.reactive_data.ticker.meta.dto.TickerMetaItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Profile("live")
@Configuration
public class RedisConfig {
    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Bean(name = "reactiveRedisConnectionFactory")
    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory(){
        // 추후 Embedded Redis 로 전환 예정.
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public ReactiveRedisOperations<String, String> tickerMapReactiveRedisOperation(
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

    @Bean
    public ReactiveRedisOperations<String, TickerMetaItem> tickerMetaMapReactiveRedisOperation(
            ReactiveRedisConnectionFactory redisConnectionFactory
    ){
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<TickerMetaItem> valueSerializer = new Jackson2JsonRedisSerializer<TickerMetaItem>(TickerMetaItem.class);

        RedisSerializationContext<String, TickerMetaItem> serializationContext =
                RedisSerializationContext.<String, TickerMetaItem>newSerializationContext()
                        .key(keySerializer)
                        .value(valueSerializer)
                        .hashKey(keySerializer)
                        .hashValue(valueSerializer)
                        .build();

        return new ReactiveRedisTemplate<String, TickerMetaItem>(redisConnectionFactory, serializationContext);
    }

    @Bean
    public ReactiveRedisOperations<String, String> tickerAutoCompleteRedisOperation(
            ReactiveRedisConnectionFactory redisConnectionFactory
    ){
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        StringRedisSerializer valueSerializer = new StringRedisSerializer();

        RedisSerializationContext<String, String> serializationContext =
                RedisSerializationContext.<String, String>newSerializationContext()
                        .key(keySerializer)
                        .value(valueSerializer)
                        .hashKey(keySerializer)
                        .hashValue(valueSerializer)
                        .build();

        return new ReactiveRedisTemplate<String, String>(redisConnectionFactory, serializationContext);
    }
}
