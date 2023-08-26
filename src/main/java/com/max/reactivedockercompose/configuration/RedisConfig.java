package com.max.reactivedockercompose.configuration;

import com.max.reactivedockercompose.company.Company;
import com.max.reactivedockercompose.stock.Stock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
public class RedisConfig {
    @Bean
    public ReactiveRedisTemplate<String, Stock> reactiveRedisStockTemplate(ReactiveRedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer<Stock> serializer = new Jackson2JsonRedisSerializer<>(Stock.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, Stock> builder =
                RedisSerializationContext.newSerializationContext(new Jackson2JsonRedisSerializer<>(String.class));
        RedisSerializationContext<String, Stock> context = builder.value(serializer).build();

        return new ReactiveRedisTemplate<>(factory, context);
    }

    @Bean
    public ReactiveRedisTemplate<String, Company> reactiveRedisCompanyTemplate(ReactiveRedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer<Company> serializer = new Jackson2JsonRedisSerializer<>(Company.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, Company> builder =
                RedisSerializationContext.newSerializationContext(new Jackson2JsonRedisSerializer<>(String.class));
        RedisSerializationContext<String, Company> context = builder.value(serializer).build();

        return new ReactiveRedisTemplate<>(factory, context);
    }
}
