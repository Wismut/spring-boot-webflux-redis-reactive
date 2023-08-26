package com.max.reactivedockercompose.company;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/companies")
public record CompanyController(CompanyService service) {

    @GetMapping("/{id}")
    public Mono<Company> findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping
    public Flux<Company> findAll() {
        return service.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Company> save(@RequestBody Company payload) {
        var company = new Company(payload.name(), payload.symbol());
        return service.save(company);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteById(@PathVariable Long id) {
        return service.deleteById(id).then();
    }
}