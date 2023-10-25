package com.max.stock_feed_api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@MapperScan("com.max.stock_feed_api")
public class StockFeedAPIApplication {
    public static void main(String[] args) {
        SpringApplication.run(StockFeedAPIApplication.class, args);
    }
}
