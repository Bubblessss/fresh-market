package com.zh.fmzuul.service.fmuser;

import com.zh.fmcommon.pojo.dto.Result;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("fm-user")
public interface UserApiService {

    @GetMapping("/findUserById")
    Result findUserById(@RequestParam("userId") Integer userId);
}
