package com.max.stock_feed_api.loader;

import com.max.stock_feed_api.stock.Stock;
import com.max.stock_feed_api.stock.StockService;
import lombok.extern.slf4j.XSlf4j;
import net.datafaker.Faker;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.max.stock_feed_api.stock.StockService.KEY;

@XSlf4j
@Service
public record StockPriceUpdater(@NonNull StockService stockService,
                                @NonNull ReactiveRedisTemplate<String, Stock> reactiveRedisStockTemplate) {
    private static final Faker faker = new Faker();

    @Scheduled(fixedDelay = 60_000)
    public void updatePrices() {
        var i = new int[1];
        reactiveRedisStockTemplate.keys(KEY + "*")
                .flatMap(key ->
                        reactiveRedisStockTemplate.opsForValue().get(key)
                                .flatMap(stock -> {
                                    i[0]++;
                                    var updatedStock = updatePrice(stock);
                                    return reactiveRedisStockTemplate.opsForValue().set(KEY + stock.code(), updatedStock);
                                })
                )
                .then()
                .doFinally(signalType -> log.info("Total stocks updates: " + i[0]))
                .block();
    }

    public Stock updatePrice(@NonNull Stock stock) {
        // TODO: 8/28/2023 implement stock price update logic
        return new Stock(stock.name(), stock.code(), faker.random().nextFloat(), LocalDateTime.now(), stock.companySymbol());
    }
}
