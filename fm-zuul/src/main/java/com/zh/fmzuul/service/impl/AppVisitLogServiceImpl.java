package com.zh.fmzuul.service.impl;

import com.zh.fmcommon.constance.DateConstance;
import com.zh.fmcommon.constance.MongoDBConstance;
import com.zh.fmcommon.pojo.bo.AppVisitLog;
import com.zh.fmzuul.service.AppVisitLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author zhanghang
 * @date 2019/7/12
 */
@Slf4j
@Service
public class AppVisitLogServiceImpl implements AppVisitLogService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 根据集合创建时间获取的集合名
     * 如果不存在则创建
     * 按天分表
     * @return
     */
    private String getCollectionNameIfAbsent(Date createTime) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(createTime.toInstant(), ZoneId.systemDefault());
        String collectionName = MongoDBConstance.COLLECTION_NAME_PRE_APP_VISIT_LOG + DateTimeFormatter.ofPattern(DateConstance.FORMATTER_YYYY_MM_DD).format(localDateTime.toLocalDate());
        if (!this.mongoTemplate.collectionExists(collectionName)){
            this.mongoTemplate.createCollection(collectionName);
            this.mongoTemplate.indexOps(collectionName).ensureIndex(new Index().on("uuid", Sort.Direction.ASC).unique());
        }
        return collectionName;
    }

    /**
     * 根据日志创建日期获取集合名
     * @param createTime
     * @return
     */
    private String getCollectionName(Date createTime) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(createTime.toInstant(), ZoneId.systemDefault());
        return MongoDBConstance.COLLECTION_NAME_PRE_APP_VISIT_LOG + DateTimeFormatter.ofPattern(DateConstance.FORMATTER_YYYY_MM_DD).format(localDateTime.toLocalDate());
    }

    @Async
    @Override
    public void saveRequestVisitLog(String sequenceId, String ip, String userAgent, String requestUrl, String requestParam, Date requestTime) {
        AppVisitLog appVisitLog = new AppVisitLog();
        appVisitLog.setSequenceId(sequenceId);
        appVisitLog.setIp(ip);
        appVisitLog.setUserAgent(userAgent);
        appVisitLog.setRequestUrl(requestUrl);
        appVisitLog.setRequestParam(requestParam);
        appVisitLog.setRequestTime(requestTime);
//        this.mongoTemplate.save(appVisitLog,this.getCollectionNameIfAbsent(requestTime));
    }

    @Async
    @Override
    public void saveResponseVisitLog(String sequenceId, Date requestTime, Date responseTime, Long costTime, String responseContent,Integer status) {
        Query query = Query.query(Criteria.where("sequenceId").is(sequenceId));
        Update update = Update.update("responseTime", responseTime).set("costTime", costTime).set("responseContent",responseContent).set("status",status);
        this.mongoTemplate.updateFirst(query,update,this.getCollectionName(requestTime));
    }

}
