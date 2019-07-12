package com.zh.fmzuul.service;

import java.util.Date;

/**
 * @author zhanghang
 * @date 2019/7/12
 */
public interface AppVisitLogService {

    void saveRequestVisitLog(String sequenceId, String ip, String userAgent, String requestUrl, String requestParam, Date requestTime);

    void saveResponseVisitLog(String sequenceId, Date requestTime, Date responseTime, Long costTime, String responseContent,Integer status);

}
