package com.max.reactivedockercompose;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class StockFeedAPIApplication {
    public static void main(String[] args) {
        SpringApplication.run(StockFeedAPIApplication.class, args);
    }

//    @Bean
//    public ApplicationRunner loadData(DataLoader loader) {
//        return args -> loader.loadRandomDataToRedis();
//    }
}
