package com.zh.fmmongodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.zh")
@EnableEurekaClient
@SpringBootApplication
public class FmMongodbApplication {

    public static void main(String[] args) {
        SpringApplication.run(FmMongodbApplication.class, args);
    }

}
