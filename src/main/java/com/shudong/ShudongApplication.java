package com.shudong;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.shudong.mapper")
public class ShudongApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShudongApplication.class, args);
    }

}