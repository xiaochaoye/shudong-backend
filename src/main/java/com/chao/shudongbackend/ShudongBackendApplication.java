package com.chao.shudongbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.chao.shudongbackend.mapper")
public class ShudongBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShudongBackendApplication.class, args);
    }

}