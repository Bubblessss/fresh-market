package com.zh.fmzuul.service;

import com.zh.fmcommon.pojo.po.User;

import java.util.Date;

/**
 * @author zhanghang
 * @date 2019/7/12
 */
public interface ZuulService {

    void saveRequestVisitLog(String sequenceId, String token, String ip, String userAgent, String requestUrl, String requestParam, Date requestTime);

    void saveResponseVisitLog(String sequenceId,String serviceId,String responseContent, Integer status);

    User findUserById(Integer userId);
}
