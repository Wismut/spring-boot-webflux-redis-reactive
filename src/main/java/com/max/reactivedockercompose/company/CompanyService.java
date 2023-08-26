package com.max.reactivedockercompose.company;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class CompanyService {
//    private final CompanyRepository repository;

    @Qualifier("reactiveRedisCompanyTemplate")
    private final ReactiveRedisTemplate<String, Company> reactiveRedisCompanyTemplate;
    private static final String KEY = "company:";

    public CompanyService(ReactiveRedisTemplate<String, Company> reactiveRedisTemplate) {
//        this.repository = repository;
        this.reactiveRedisCompanyTemplate = reactiveRedisTemplate;
    }

    public Mono<Company> findById(@NonNull Long id) {
//        String key = KEY + id;
//        return reactiveRedisCompanyTemplate.opsForValue().get(key)
//                .switchIfEmpty(repository.findById(id)
//                        .flatMap(company -> reactiveRedisCompanyTemplate.opsForValue()
//                                .set(key, company, Duration.ofMinutes(5))
//                                .thenReturn(company))
//                );
        return Mono.just(new Company("", ""));
    }

    public Flux<Company> findAll() {
//        return reactiveRedisCompanyTemplate.keys("book:*")
//                .flatMap(key -> reactiveRedisCompanyTemplate.opsForValue().get(key))
//                .switchIfEmpty(repository.findAll()
//                        .flatMap(company -> reactiveRedisCompanyTemplate.opsForValue()
//                                .set(KEY + company.id(), company, Duration.ofMinutes(5)))
//                        .thenMany(reactiveRedisCompanyTemplate.keys(KEY + "*")
//                                .flatMap(key -> reactiveRedisCompanyTemplate.opsForValue().get(key)))
//                );
        return reactiveRedisCompanyTemplate.keys(KEY + "*")
                .flatMap(key -> reactiveRedisCompanyTemplate.opsForValue().get(key));
    }

    public Mono<Company> save(@NonNull Company company) {
//        return repository.save(company)
//                .flatMap(savedCompany -> reactiveRedisCompanyTemplate.opsForValue()
//                        .set(KEY + company.id(), company, Duration.ofMinutes(5)).thenReturn(company));
        return reactiveRedisCompanyTemplate.opsForValue().set(KEY + company.symbol(), company).thenReturn(company);
    }

    public Mono<Boolean> deleteById(@NonNull Long id) {
        return Mono.just(true);
//        return repository.deleteById(id)
//                .then(reactiveRedisCompanyTemplate.opsForValue().delete(KEY + id));
    }
}

