package com.zh.fmorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.zh")
@EnableEurekaClient
@SpringBootApplication
public class FmOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(FmOrderApplication.class, args);
    }

}
