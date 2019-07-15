package com.zh.fmzuul.filter;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.zh.fmcommon.constance.CacheConstance;
import com.zh.fmcommon.constance.DateConstance;
import com.zh.fmcommon.enums.DataCenterIdEnum;
import com.zh.fmcommon.enums.WorkIdIdEnum;
import com.zh.fmcommon.utils.IpUtil;
import com.zh.fmzuul.service.ZuulService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 请求日志过滤器
 * @author zhanghang
 * @date 2019/7/12
 */
@Slf4j
@Component
public class LogPreFilter extends ZuulFilter {

    @Autowired
    private ZuulService zuulService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String appVisitLogSequenceId = this.buildAppVisitLog2Durable(ctx);
        Map<String,String> extraParams = new HashMap<>(16);
        extraParams.put("appVisitLogSequenceId",appVisitLogSequenceId);
        this.buildExtraParams2Request(ctx,extraParams);
        return null;
    }

    private String buildAppVisitLog2Durable(RequestContext ctx){
        HttpServletRequest request = ctx.getRequest();
        String requestUrl = request.getRequestURL().toString();
        String requestMethod = request.getMethod();
        Map<String, String[]> paramsMap =  request.getParameterMap();
        String requestParam = JSONObject.toJSONString(paramsMap);
        //workId和dataCenterId都是[0,31]
        long workId = WorkIdIdEnum.FM_ZUUL.getCode();
        long dataCenterId = DataCenterIdEnum.DataCenter_0.getCode();
        String appVisitLogSequenceId = String.valueOf(IdUtil.createSnowflake(workId,dataCenterId).nextId());
        String ip = IpUtil.getRequestIp(request);
        String userAgent = request.getHeader("User-Agent");
        String token = request.getHeader("token");
        Date requestTime = new Date();
        String key = CacheConstance.APP_VISIT_LOG_REQUEST_TIME_PRE + appVisitLogSequenceId;
        String value = DateConstance.FORMATTER_YYYY_MM_DD_HH_MM_SS_SSS.format(LocalDateTime.ofInstant(requestTime.toInstant(), ZoneId.systemDefault()));
        this.stringRedisTemplate.opsForValue().set(key,value,3, TimeUnit.MINUTES);
        log.info("=====================appVisitLogSequenceId:{},请求路径:{}========================",appVisitLogSequenceId,requestUrl);
        log.info("=====================appVisitLogSequenceId:{},请求方法:{}========================",appVisitLogSequenceId,requestMethod);
        log.info("=====================appVisitLogSequenceId:{},客户端ip地址:{}========================",appVisitLogSequenceId,ip);
        log.info("=====================appVisitLogSequenceId:{},请求参数:{}========================",appVisitLogSequenceId,requestParam);
        log.info("=====================appVisitLogSequenceId:{},请求token:{}========================",appVisitLogSequenceId,token);
        log.info("=====================appVisitLogSequenceId:{},请求头:{}========================",appVisitLogSequenceId,userAgent);
        log.info("=====================appVisitLogSequenceId:{},请求时间:{}========================",appVisitLogSequenceId,requestTime);
        this.zuulService.saveRequestVisitLog(appVisitLogSequenceId,token,ip,userAgent,requestUrl,requestParam,requestTime);
        return appVisitLogSequenceId;
    }

    private void buildExtraParams2Request(RequestContext ctx,Map<String, String> extraParams) {
        Map<String, List<String>> requestQueryParams = ctx.getRequestQueryParams();
        if (requestQueryParams == null) {
            requestQueryParams = new HashMap<>(16);
        }
        List<String> list;
        for (Map.Entry<String,String> entry : extraParams.entrySet()){
            list = new ArrayList<>();
            list.add(entry.getValue());
            requestQueryParams.put(entry.getKey(),list);
        }
        ctx.setRequestQueryParams(requestQueryParams);
    }
}
