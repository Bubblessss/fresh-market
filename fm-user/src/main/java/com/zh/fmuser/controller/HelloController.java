package com.zh.fmuser.controller;

import com.alibaba.fastjson.JSONObject;
import com.zh.fmcommon.annotation.Token;
import com.zh.fmcommon.pojo.dto.Result;
import com.zh.fmcommon.pojo.po.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HelloController {

    @PostMapping("/hello")
    public Result hello(String appVisitLogSequenceId){
        log.info("hello fm-user appVisitLogSequenceId:{}",appVisitLogSequenceId);
        return Result.genSuccessResult();
    }

    @Token
    @GetMapping("/hi")
    public Result hi(String appVisitLogSequenceId){
        log.info("hi fm-user appVisitLogSequenceId:{}",appVisitLogSequenceId);
        return Result.genSuccessResult();
    }

    @Token
    @GetMapping("/findUserById")
    public Result findUserById(@RequestParam("userId") Integer userId){
        JSONObject result = new JSONObject();
        result.put("user",new User(1,"张三","111111"));
        return Result.genSuccessResult(result);
    }
}
