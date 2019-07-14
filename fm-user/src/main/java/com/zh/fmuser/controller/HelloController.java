package com.zh.fmuser.controller;

import com.zh.fmcommon.enums.AppResultCodeEnum;
import com.zh.fmcommon.exception.BusinessException;
import com.zh.fmcommon.pojo.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HelloController {

    @PostMapping("/hello")
    public Result hello(String appVisitLogSequenceId){
        this.log.info("hello fm-user appVisitLogSequenceId:{}",appVisitLogSequenceId);
        throw new BusinessException(AppResultCodeEnum.FAIL);
    }

    @GetMapping("/hi")
    public Result hi(String appVisitLogSequenceId){
        log.info("hi fm-user appVisitLogSequenceId:{}",appVisitLogSequenceId);
        return Result.genSuccessResult();
    }
}
