package com.max.reactivedockercompose.company;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/companies")
public record CompanyController(CompanyService service) {
    @GetMapping
    public Flux<Company> findAll() {
        return service.findAll();
    }
}
