package com.max.reactivedockercompose.stock;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class StockService {
    private final ReactiveRedisTemplate<String, Stock> reactiveRedisStockTemplate;
    private static final String KEY = "stock:";

    public Mono<Stock> findByCode(@NonNull String code) {
        var key = KEY + code;
        return reactiveRedisStockTemplate.opsForValue().get(key);
    }

    public Flux<Stock> findAll() {
        return reactiveRedisStockTemplate.keys(KEY + "*")
                .flatMap(key -> reactiveRedisStockTemplate.opsForValue().get(key));
    }

    public Mono<Stock> save(@NonNull Stock stock) {
        return reactiveRedisStockTemplate.opsForValue().set(KEY + stock.code(), stock).thenReturn(stock);
    }

    public Mono<Boolean> deleteById(@NonNull Long id) {
        return reactiveRedisStockTemplate.opsForValue().delete(KEY + id);
    }
}
