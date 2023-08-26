package com.max.reactivedockercompose.stock;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/stocks")
public record StockController(StockService service) {
    @GetMapping("/{stock_code}/quote")
    public Mono<Stock> findByCode(@PathVariable("stock_code") String code) {
        return service.findByCode(code);
    }
}
