package com.max.reactivedockercompose.loader;

import com.max.reactivedockercompose.company.Company;
import com.max.reactivedockercompose.company.CompanyService;
import com.max.reactivedockercompose.stock.Stock;
import com.max.reactivedockercompose.stock.StockService;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataLoader {
    private final CompanyService companyService;
    private final StockService stockService;

    private final Faker faker = new Faker();

    public void loadRandomDataToRedis() {
        for (int i = 0; i < 1_000_000; i++) {
            var company = new Company(faker.company().name(), UUID.randomUUID().toString());
            var stock = new Stock(faker.stock().nsdqSymbol(), UUID.randomUUID().toString(), faker.random().nextFloat(), LocalDateTime.now(), company.symbol());
            companyService.save(company).subscribe();
            stockService.save(stock).subscribe();
        }
        System.out.println("Companies and stocks have been saved at " + LocalDateTime.now());
//        System.out.println(companyService.findAll().count().block());
//        System.out.println(stockService.findAll().count().block());
    }
}
