package com.max.stock_feed_api.loader;

import com.max.stock_feed_api.stock.Stock;
import com.max.stock_feed_api.stock.StockService;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@XSlf4j
@Service
public record StockPriceUpdater(@NonNull StockService stockService,
                                @NonNull ReactiveRedisTemplate<String, Stock> reactiveRedisStockTemplate) {
    @Scheduled(fixedDelay = 60_000, initialDelay = 60_000)
    public void updatePricesMulti() {
        stockService.findAll()
                .map(this::updatePrice)
                .flatMap(stockService::save)
                .reduce(0, (count, element) -> count + 1)
                .doOnSuccess(count -> log.info("{} stocks have been updated", count))
                .subscribe(s -> Mono.just(s).subscribe());
    }

    public Stock updatePrice(@NonNull Stock stock) {
        // TODO: 8/28/2023 implement stock price update logic
        return new Stock(stock.name(), stock.code(), (float) Math.random(), stock.companySymbol());
    }
}
