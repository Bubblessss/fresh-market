package com.zh.fmmongodb.controller;

import com.zh.fmcommon.pojo.bo.AppVisitLog;
import com.zh.fmcommon.pojo.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/appVisitLog")
public class AppVisitLogController {

    @PostMapping("/saveAppVisitLog")
    public Result saveAppVisitLog(@RequestBody AppVisitLog appVisitLog){
        log.info("fm-mongodb:{}",appVisitLog);
        return Result.genSuccessResult();
    }
}
