package com.zh.fmzuul;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.zh")
@EnableZuulProxy
@EnableFeignClients
@SpringCloudApplication
public class FmZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(FmZuulApplication.class, args);
    }

}
