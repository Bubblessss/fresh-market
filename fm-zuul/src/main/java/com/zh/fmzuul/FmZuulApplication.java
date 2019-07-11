package com.zh.fmzuul;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@SpringCloudApplication
public class FmZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(FmZuulApplication.class, args);
    }

}
