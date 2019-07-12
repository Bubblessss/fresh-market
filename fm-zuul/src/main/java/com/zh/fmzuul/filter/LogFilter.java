package com.zh.fmzuul.filter;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.zh.fmcommon.enums.DataCenterIdEnum;
import com.zh.fmcommon.enums.WorkIdIdEnum;
import com.zh.fmcommon.utils.IpUtil;
import com.zh.fmzuul.service.AppVisitLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author zhanghang
 * @date 2019/7/12
 */
@Slf4j
@Component
public class LogFilter extends ZuulFilter {

    @Autowired
    private AppVisitLogService appVisitLogService;

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
        HttpServletRequest request = ctx.getRequest();
        String requestUrl = request.getRequestURL().toString();
        String requestMethod = request.getMethod();
        Map<String, String[]> map =  request.getParameterMap();
        String requestParam = JSONObject.toJSONString(map.size() == 0 ? null : map);
        //workId和dataCenterId都是[0,31]
        long workId = WorkIdIdEnum.FM_ZUUL.getCode();
        long dataCenterId = DataCenterIdEnum.DataCenter_0.getCode();
        String sequenceId = String.valueOf(IdUtil.createSnowflake(workId,dataCenterId).nextId());
        String ip = IpUtil.getRequestIp(request);
        String userAgent = request.getHeader("User-Agent");
        String token = request.getHeader("token");
        Date requestTime = new Date();
        request.setAttribute("requestTime",requestTime);
        Map<String,String> param = new HashMap<>(16);
        param.put("sequenceId",sequenceId);
        this.buildExtraParams2Request(request,param);
        log.info("=====================sequenceId:{},请求路径:{}========================",sequenceId,requestUrl);
        log.info("=====================sequenceId:{},请求方法:{}========================",sequenceId,requestMethod);
        log.info("=====================sequenceId:{},客户端ip地址:{}========================",sequenceId,ip);
        log.info("=====================sequenceId:{},请求参数:{}========================",sequenceId,requestParam);
        log.info("=====================sequenceId:{},请求token:{}========================",sequenceId,token);
        log.info("=====================sequenceId:{},请求头:{}========================",sequenceId,userAgent);
        log.info("=====================sequenceId:{},请求时间:{}========================",sequenceId,requestTime);
        this.appVisitLogService.saveRequestVisitLog(sequenceId,ip,userAgent,requestUrl,requestParam,requestTime);
        return null;
    }

    private void buildExtraParams2Request(HttpServletRequest request,Map<String, String> param) {
//        if (RequestMethod.GET.name().equals(request.getMethod())){
//            String requestUrl = request.getRequestURL().toString();
//            StringJoiner sj = new StringJoiner("&");
//            param.entrySet().forEach(e -> );
//
//        }else{
//            //post
//        }
    }
}
