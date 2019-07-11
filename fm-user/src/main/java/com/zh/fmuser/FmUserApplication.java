package com.zh.fmuser;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class FmUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(FmUserApplication.class, args);
    }

}
