package com.max.stock_feed_api.stock;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/stocks")
public record StockController(StockService service) {
    @GetMapping("/{stock_code}/quote")
    public ResponseEntity<Mono<Stock>> findByCode(@PathVariable("stock_code") String code) {
        var stockMono = service.findByCode(code);
        if (stockMono.blockOptional().isPresent()) {
            return ResponseEntity.ok(stockMono);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}