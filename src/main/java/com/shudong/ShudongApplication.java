package com.shudong;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({
    "com.shudong.user.mapper",
    "com.shudong.post.mapper",
    "com.shudong.pick.mapper",
    "com.shudong.message.mapper",
    "com.shudong.admin.mapper"
})
public class ShudongApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShudongApplication.class, args);
    }

}