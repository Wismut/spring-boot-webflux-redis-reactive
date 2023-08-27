package com.max.stock_feed_api.company;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final ReactiveRedisTemplate<String, Company> reactiveRedisCompanyTemplate;

    private static final String KEY = "company:";

    public Mono<Company> findBySymbol(@NonNull String symbol) {
        var key = KEY + symbol;
        return reactiveRedisCompanyTemplate.opsForValue().get(key);
    }

    public Flux<Company> findAll() {
        return reactiveRedisCompanyTemplate.keys(KEY + "*")
                .flatMap(key -> reactiveRedisCompanyTemplate.opsForValue().get(key));
    }

    public Mono<Company> save(@NonNull Company company) {
        return reactiveRedisCompanyTemplate.opsForValue().set(KEY + company.symbol(), company).thenReturn(company);
    }

    public Mono<Boolean> deleteById(@NonNull Long id) {
        return reactiveRedisCompanyTemplate.opsForValue().delete(KEY + id);
    }
}
