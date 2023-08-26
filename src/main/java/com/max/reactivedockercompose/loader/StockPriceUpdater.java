package com.max.reactivedockercompose.loader;

import com.max.reactivedockercompose.stock.StockService;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@XSlf4j
@Service
public record StockPriceUpdater(StockService stockService) {
    @Scheduled(fixedDelay = 60_000)
    public void updatePrices() {
        log.entry(LocalDateTime.now());
        stockService.updatePrices();
        log.exit(LocalDateTime.now());
    }
}
