package com.zh.fmzuul.filter;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.zh.fmcommon.pojo.dto.AppResult;
import com.zh.fmzuul.service.ZuulService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * 响应日志过滤器
 * @author zhanghang
 * @date 2019/7/13
 */
@Slf4j
@Component
public class LogPostFilter extends ZuulFilter {

    @Autowired
    private ZuulService zuulService;

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        InputStream in = ctx.getResponseDataStream();
        String responseBody = null;
        AppResult appResult = null;
        JSONObject resultJson = null;
        try {
            String resultJsonStr = StreamUtils.copyToString(in, StandardCharsets.UTF_8);
            if (StringUtils.isNotBlank(resultJsonStr)){
                log.info("服务端返回结果:{}",resultJsonStr);
                resultJson = JSONObject.parseObject(resultJsonStr);
                appResult = JSONObject.toJavaObject(resultJson,AppResult.class);
                responseBody = JSONObject.toJSONString(appResult, SerializerFeature.WriteMapNullValue);
            }
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            appResult = AppResult.genFailResult();
            responseBody = JSONObject.toJSONString(appResult, SerializerFeature.WriteMapNullValue);
            resultJson = JSONObject.parseObject(responseBody);
        }
        String sequenceId  = resultJson.getString("appVisitLogSequenceId");
        String serviceId  = ctx.get("serviceId").toString();
        Integer status  = appResult.getCode();
        this.zuulService.saveResponseVisitLog(sequenceId,serviceId,responseBody,status);
        ctx.getResponse().setContentType("application/json;charset=UTF-8");
        ctx.setResponseBody(responseBody);
        return null;
    }

}
