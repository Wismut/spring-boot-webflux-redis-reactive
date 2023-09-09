package com.max.stock_feed_api.loader;

import com.max.stock_feed_api.company.Company;
import com.max.stock_feed_api.company.CompanyService;
import com.max.stock_feed_api.stock.Stock;
import com.max.stock_feed_api.stock.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.XSlf4j;
import net.datafaker.Faker;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.UUID;

@XSlf4j
@Component
@RequiredArgsConstructor
public class DataLoader {
    private final CompanyService companyService;
    private final StockService stockService;
    private final Faker faker = new Faker();

    @EventListener(ApplicationReadyEvent.class)
    public void loadRandomDataToRedis() {
        var now = LocalDateTime.now();
        log.entry(now);
        var stockByKey = new HashMap<String, Stock>();
        var companyByKey = new HashMap<String, Company>();
        for (var i = 0; i < 100_000; i++) {
            var company = new Company(faker.company().name(), UUID.randomUUID().toString());
            var stock = new Stock(faker.stock().nsdqSymbol(), UUID.randomUUID().toString(), faker.random().nextFloat(), LocalDateTime.now(), company.symbol());
            companyByKey.put(CompanyService.KEY + company.symbol(), company);
            stockByKey.put(StockService.KEY + stock.code(), stock);
        }
        companyService.saveAll(companyByKey).subscribe();
        stockService.saveAll(stockByKey).subscribe();
        log.exit("Was completed in " + ChronoUnit.SECONDS.between(now, LocalDateTime.now()) + " seconds");
    }
}
