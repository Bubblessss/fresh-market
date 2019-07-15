package com.zh.fmzuul.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zh.fmcommon.constance.CacheConstance;
import com.zh.fmcommon.constance.DateConstance;
import com.zh.fmcommon.constance.MongoDBConstance;
import com.zh.fmcommon.pojo.bo.AppVisitLog;
import com.zh.fmcommon.pojo.dto.Result;
import com.zh.fmcommon.utils.JwtUtil;
import com.zh.fmzuul.service.AppVisitLogService;
import com.zh.fmzuul.service.fmmongodb.AppVisitLogApiService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author zhanghang
 * @date 2019/7/12
 */
@Slf4j
@Service
public class AppVisitLogServiceImpl implements AppVisitLogService {

    @Autowired
    private AppVisitLogApiService appVisitLogApiService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 根据集合创建时间获取的集合名
     * 如果不存在则创建
     * 按天分表
     * @return
     */
//    private String getCollectionNameIfAbsent(Date createTime) {
//        LocalDateTime localDateTime = LocalDateTime.ofInstant(createTime.toInstant(), ZoneId.systemDefault());
//        String collectionName = MongoDBConstance.COLLECTION_NAME_PRE_APP_VISIT_LOG + DateConstance.FORMATTER_YYYY_MM_DD.format(localDateTime.toLocalDate());
//        if (!this.mongoTemplate.collectionExists(collectionName)){
//            this.mongoTemplate.createCollection(collectionName);
//            this.mongoTemplate.indexOps(collectionName).ensureIndex(new Index().on("sequenceId", Sort.Direction.ASC).unique());
//        }
//        return collectionName;
//    }

    /**
     * 根据日志创建日期获取集合名
     * @param createTime
     * @return
     */
    private String getCollectionName(Date createTime) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(createTime.toInstant(), ZoneId.systemDefault());
        return MongoDBConstance.COLLECTION_NAME_PRE_APP_VISIT_LOG + DateConstance.FORMATTER_YYYY_MM_DD.format(localDateTime.toLocalDate());
    }

    @Override
    @Async("appVisitLogExecutor")
    public void saveRequestVisitLog(String sequenceId, String token, String ip, String userAgent, String requestUrl, String requestParam, Date requestTime) {
        log.info("===========调用fm-mongodb服务保存应用请求日志===========");
        Integer userId = null;
        if (StringUtils.isNotBlank(token)){
            userId =  JwtUtil.getUserId(token);
        }
        //构建请求日志
        AppVisitLog appVisitLog = new AppVisitLog();
        appVisitLog.setSequenceId(sequenceId);
        appVisitLog.setLogType(MongoDBConstance.LOG_TYPE_REQUEST);
        appVisitLog.setToken(token);
        appVisitLog.setUserId(userId);
        appVisitLog.setIp(ip);
        appVisitLog.setUserAgent(userAgent);
        appVisitLog.setRequestUrl(requestUrl);
        appVisitLog.setRequestParam(requestParam);
        appVisitLog.setRequestTime(requestTime);
        Result result = this.appVisitLogApiService.saveRequestAppVisitLog(appVisitLog);
        log.info("fm-mongodb result:{}", JSONObject.toJSONString(result, SerializerFeature.WriteMapNullValue));
    }

    @Override
    @Async("appVisitLogExecutor")
    public void saveResponseVisitLog(JSONObject resultJson) {
        if (resultJson != null && !resultJson.isEmpty()) {
            log.info("===========调用fm-mongodb服务保存应用响应日志===========");
            log.info("响应日志:{}",resultJson.toJSONString());
            Integer status = resultJson.getInteger("code");
            String sequenceId = resultJson.getString("appVisitLogSequenceId");
            String requestTimeKey = CacheConstance.APP_VISIT_LOG_REQUEST_TIME_PRE + sequenceId;
            String requestTimeStr = this.stringRedisTemplate.opsForValue().get(requestTimeKey);
            String classMethodKey = CacheConstance.APP_VISIT_LOG_CLASS_METHOD_PRE + sequenceId;
            String classMethod = this.stringRedisTemplate.opsForValue().get(classMethodKey);
            Date responseTime = new Date();
            LocalDateTime requestLocalDateTime = LocalDateTime.from(DateConstance.FORMATTER_YYYY_MM_DD_HH_MM_SS_SSS.parse(requestTimeStr));
            Date requestTime = Date.from(requestLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
            Long costTime = responseTime.getTime() - requestTime.getTime();
            //构建响应日志
            AppVisitLog appVisitLog = new AppVisitLog();
            appVisitLog.setSequenceId(sequenceId);
            appVisitLog.setLogType(MongoDBConstance.LOG_TYPE_RESPONSE);
            appVisitLog.setRequestClazz(classMethod);
            appVisitLog.setRequestTime(responseTime);
            appVisitLog.setCostTime(costTime);
            appVisitLog.setResponseContent(resultJson.toJSONString());
            appVisitLog.setStatus(status);
            Result result = this.appVisitLogApiService.saveRequestAppVisitLog(appVisitLog);
            log.info("fm-mongodb result:{}", JSONObject.toJSONString(result, SerializerFeature.WriteMapNullValue));
        }
    }

}
