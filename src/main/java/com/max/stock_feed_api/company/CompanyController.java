package com.max.stock_feed_api.company;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/companies")
public record CompanyController(CompanyService service) {
    @GetMapping
    public Flux<Company> findAll() {
        Flux<Company> all = service.findAll();
        System.out.println("CompanyController findAll");
        return all;
    }
}