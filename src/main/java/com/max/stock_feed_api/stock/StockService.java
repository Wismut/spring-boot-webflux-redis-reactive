package com.max.stock_feed_api.stock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.XSlf4j;
import net.datafaker.Faker;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@XSlf4j
@Service
@RequiredArgsConstructor
public class StockService {
    private final ReactiveRedisTemplate<String, Stock> reactiveRedisStockTemplate;
    private static final String KEY = "stock:";
    private final Faker faker = new Faker();

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

    public void updatePrices() {
        var i = new int[1];
        reactiveRedisStockTemplate.keys(KEY + "*")
                .flatMap(key ->
                        reactiveRedisStockTemplate.opsForValue().get(key)
                                .flatMap(stock -> {
                                    i[0]++;
                                    var updatedStock = new Stock(stock.name(), stock.code(), faker.random().nextFloat(), LocalDateTime.now(), stock.companySymbol());
                                    return reactiveRedisStockTemplate.opsForValue().set(KEY + stock.code(), updatedStock);
                                })
                )
                .then()
                .doFinally(signalType -> log.info("Total stocks updates: " + i[0]))
                .block();
    }
}
