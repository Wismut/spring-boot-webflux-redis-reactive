package com.max.reactivedockercompose;

import com.max.reactivedockercompose.loader.DataLoader;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ReactiveDockerComposeApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReactiveDockerComposeApplication.class, args);
    }

    @Bean
    public ApplicationRunner loadData(DataLoader loader) {
        return args -> loader.loadRandomDataToRedis();
    }
}
