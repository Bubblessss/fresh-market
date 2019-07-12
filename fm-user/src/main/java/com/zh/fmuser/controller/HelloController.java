package com.zh.fmuser.controller;

import com.zh.fmcommon.enums.AppResultCodeEnum;
import com.zh.fmcommon.exception.BusinessException;
import com.zh.fmcommon.pojo.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HelloController {

    @GetMapping("/hello")
    public Result hello(String sequenceId){
        log.info("hello fm-user:{}",sequenceId);
        throw new BusinessException(AppResultCodeEnum.FAIL);
    }
}
