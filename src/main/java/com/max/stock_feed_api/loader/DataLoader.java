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
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
        final var now = LocalDateTime.now();
        Flux.range(0, 100_000)
                .map(i -> UUID.randomUUID().toString())
                .flatMap(symbol -> Mono.zip(Mono.fromSupplier(() -> new Company(faker.company().name(), symbol)),
                        Mono.fromSupplier(() -> new Stock(faker.stock().nsdqSymbol(), UUID.randomUUID().toString(), faker.random().nextFloat(), symbol))))
                .flatMap(companyAndStock -> Mono.fromCallable(() -> Mono.zip(companyService.save(companyAndStock.getT1()), stockService.save(companyAndStock.getT2()))))
                .subscribe(Mono::subscribe);
        log.exit("Was completed in " + ChronoUnit.SECONDS.between(now, LocalDateTime.now()) + " seconds");
    }
}
