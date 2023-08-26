package com.max.reactivedockercompose.stock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.XSlf4j;
import net.datafaker.Faker;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
                .flatMap(key -> reactiveRedisStockTemplate.opsForValue().get(key))
                .map(stock -> {
                    i[0]++;
                    var updatedStock = new Stock(stock.name(), stock.code(), faker.random().nextFloat(), stock.dateTime(), stock.companySymbol());
                    reactiveRedisStockTemplate.opsForValue().set(KEY + stock.code(), updatedStock);
                    return updatedStock;
                });
        log.exit(i[0]);
    }
}
