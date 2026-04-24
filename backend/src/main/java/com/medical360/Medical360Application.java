package com.medical360;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.medical360.mapper")
@EnableScheduling
public class Medical360Application {
    public static void main(String[] args) {
        SpringApplication.run(Medical360Application.class, args);
    }
}
