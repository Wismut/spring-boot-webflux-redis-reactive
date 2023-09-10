package com.max.stock_feed_api.company;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static com.max.stock_feed_api.company.CompanyController.PATH;

@RestController
@RequestMapping(PATH)
public record CompanyController(CompanyService service) {
    public static final String PATH = "/api/companies";

    @GetMapping
    public Flux<Company> findAll() {
        return service.findAll();
    }
}
