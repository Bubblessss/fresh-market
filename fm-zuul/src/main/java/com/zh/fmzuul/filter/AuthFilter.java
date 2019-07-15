package com.zh.fmzuul.filter;

import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.zh.fmcommon.constance.CacheConstance;
import com.zh.fmcommon.enums.AppResultCodeEnum;
import com.zh.fmcommon.pojo.dto.Result;
import com.zh.fmcommon.pojo.po.User;
import com.zh.fmcommon.utils.JwtUtil;
import com.zh.fmzuul.service.ZuulService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

/**
 * token认证过滤器
 * @author zhanghang
 * @date 2019/7/15
 */
@Slf4j
@Component
public class AuthFilter extends ZuulFilter {

    @Autowired
    private ZuulService zuulService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 5;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String[] uris = request.getRequestURI().split("/");
        String serviceTag = uris[1];
        String serviceUri = "/" + uris[2];
        return this.redisTemplate.opsForSet().isMember(CacheConstance.TOKEN_PATH_PRE + serviceTag,serviceUri);
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String token = request.getHeader("token");
        boolean pass = true;
        if (StringUtils.isBlank(token)){
            pass = false;
        }else{
            Integer userId = JwtUtil.getUserId(token);
            User user = this.zuulService.findUserById(userId);
            if (!JwtUtil.verifyToken(token,user.getPassword())){
                pass = false;
            }else{
                ctx.addZuulRequestHeader("token", token);
            }
        }
        if (!pass){
            ctx.setSendZuulResponse(false);
            Result result = Result.genFailResult(AppResultCodeEnum.USER_NO_LOGIN);
            String sequenceId  = ctx.getRequestQueryParams().get("appVisitLogSequenceId").get(0);
            result.setAppVisitLogSequenceId(sequenceId);
            ctx.setResponseDataStream(new ByteArrayInputStream(JSONObject.toJSONString(result).getBytes(StandardCharsets.UTF_8)));
        }
        return null;
    }
}
