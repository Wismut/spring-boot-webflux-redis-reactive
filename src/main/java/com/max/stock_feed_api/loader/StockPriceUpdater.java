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

@XSlf4j
@Service
public record StockPriceUpdater(@NonNull StockService stockService,
                                @NonNull ReactiveRedisTemplate<String, Stock> reactiveRedisStockTemplate) {
    private static final Faker faker = new Faker();

    @Scheduled(fixedDelay = 60_000, initialDelay = 60_000)
    public void updatePricesMulti() {
        var now = LocalDateTime.now();
        var i = new int[1];
        stockService.findAll()
                .flatMap(stock -> Mono.fromSupplier(() -> updatePrice(stock)))
                .flatMap(stock -> Mono.fromCallable(() -> stockService.save(stock)))
                .doFinally(signalType -> log.info("Total stocks updates updatePricesMulti: " + i[0] + " in " + ChronoUnit.SECONDS.between(now, LocalDateTime.now()) + " seconds"))
                .subscribe(Mono::subscribe);
    }

    public Stock updatePrice(@NonNull Stock stock) {
        // TODO: 8/28/2023 implement stock price update logic
        return new Stock(stock.name(), stock.code(), faker.random().nextFloat(), stock.companySymbol());
    }
}
