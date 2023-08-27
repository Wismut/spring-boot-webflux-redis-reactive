package com.max.reactivedockercompose.loader;

import com.max.reactivedockercompose.company.Company;
import com.max.reactivedockercompose.company.CompanyService;
import com.max.reactivedockercompose.stock.Stock;
import com.max.reactivedockercompose.stock.StockService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.XSlf4j;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@XSlf4j
@Component
@RequiredArgsConstructor
public class DataLoader {
    private final CompanyService companyService;
    private final StockService stockService;
    private final Faker faker = new Faker();

    @PostConstruct
    public void loadRandomDataToRedis() {
        for (int i = 0; i < 1_000_000; i++) {
            var company = new Company(faker.company().name(), UUID.randomUUID().toString());
            var stock = new Stock(faker.stock().nsdqSymbol(), UUID.randomUUID().toString(), faker.random().nextFloat(), LocalDateTime.now(), company.symbol());
            companyService.save(company).subscribe();
            stockService.save(stock).subscribe();
        }
        log.exit(LocalDateTime.now());
    }
}
