package com.loststars.shiroboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.loststars.shiroboot.dao")
public class ShiroBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShiroBootApplication.class, args);
    }
}
