package com.max.stock_feed_api.loader;

import com.max.stock_feed_api.stock.StockService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public record StockPriceUpdater(StockService stockService) {
    @Scheduled(fixedDelay = 60_000)
    public void updatePrices() {
        stockService.updatePrices();
    }
}
