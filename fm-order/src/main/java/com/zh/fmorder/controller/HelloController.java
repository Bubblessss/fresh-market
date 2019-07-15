package com.zh.fmorder.controller;

import com.zh.fmcommon.enums.AppResultCodeEnum;
import com.zh.fmcommon.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        log.info("hello fm-order");
        throw new BusinessException(AppResultCodeEnum.USER_NOT_EXIST);
    }
}
