package com.zh.fmeurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class FmEurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FmEurekaServerApplication.class, args);
    }

}
