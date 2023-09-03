package com.max.stock_feed_api.loader;

import com.max.stock_feed_api.stock.Stock;
import com.max.stock_feed_api.stock.StockService;
import lombok.extern.slf4j.XSlf4j;
import net.datafaker.Faker;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static com.max.stock_feed_api.stock.StockService.KEY;
import static java.util.Objects.isNull;

@XSlf4j
@Service
public record StockPriceUpdater(@NonNull StockService stockService,
                                @NonNull ReactiveRedisTemplate<String, Stock> reactiveRedisStockTemplate) {
    private static final Faker faker = new Faker();
    private static LocalDateTime start;
    private static LocalDateTime end;

    @Scheduled(fixedDelay = 60_000, initialDelay = 60_000)
    public void updatePricesMultiSet() {
        var now = LocalDateTime.now();
        var i = new int[1];
        var matchingKeys = reactiveRedisStockTemplate.keys(KEY + "*");
        var updateOperation = matchingKeys
                .flatMap(key -> reactiveRedisStockTemplate.opsForValue().get(key)
                        .map(stock -> {
                            i[0]++;
                            var updatedStock = updatePrice(stock);
                            return Map.entry(KEY + stock.code(), updatedStock);
                        }))
                .collectMap(Map.Entry::getKey, Map.Entry::getValue)
                .flatMap(stockMap -> {
                    if (stockMap.isEmpty()) {
                        return Mono.empty();
                    } else {
                        return stockService.saveAll(stockMap);
                    }
                })
                .then()
                .doFinally(signalType -> {
                    log.info("Total stocks updates updatePricesMultiSet: " + i[0] + " in " + ChronoUnit.SECONDS.between(now, LocalDateTime.now()) + " seconds");
                    log.info("Time diff in seconds between start and end: " + ChronoUnit.SECONDS.between(start, end));
                });
        updateOperation.subscribe();
    }

    public Stock updatePrice(@NonNull Stock stock) {
        if (isNull(start)) {
            start = LocalDateTime.now();
        }
        end = LocalDateTime.now();
        // TODO: 8/28/2023 implement stock price update logic
        return new Stock(stock.name(), stock.code(), faker.random().nextFloat(), LocalDateTime.now(), stock.companySymbol());
    }
}
