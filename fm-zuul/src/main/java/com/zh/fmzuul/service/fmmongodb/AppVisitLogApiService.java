package com.zh.fmzuul.service.fmmongodb;

import com.zh.fmcommon.pojo.bo.AppVisitLog;
import com.zh.fmcommon.pojo.dto.Result;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("fm-mongodb")
public interface AppVisitLogApiService {

    @PostMapping("/appVisitLog/saveAppVisitLog")
    Result saveAppVisitLog(@RequestBody AppVisitLog appVisitLog);

//    @PostMapping("/appVisitLog/saveResponseVisitLog")
//    Result saveResponseVisitLog(@RequestParam Query query, @RequestParam Update update, @RequestParam String collectionName);
}
