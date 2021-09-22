package com.nd.xcw.tmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TmallApplication {
    public static void main(String[] args) {
        SpringApplication.run(TmallApplication.class, args);
    }
}
