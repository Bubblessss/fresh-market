package com.zh.fmcommon.component.aop;

import com.zh.fmcommon.constance.CacheConstance;
import com.zh.fmcommon.pojo.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @author zhanghang
 * @date 2019/6/3
 */
@Slf4j
@Aspect
@Component
public class AppVisitPostLogAspect {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Pointcut("execution( * com.zh.*.controller.*.*(..))")
    public void appLogPointCut() {}

    @Before("appLogPointCut()")
    public void doBefore(JoinPoint joinPoint){
        log.info("====================AOP:before拦截开启====================");
        Signature signature = joinPoint.getSignature();
        String clazzMethod = signature.getDeclaringTypeName() + "." + signature.getName();
        String appVisitLogSequenceId = request.getParameter("appVisitLogSequenceId");
        if (StringUtils.isNotBlank(appVisitLogSequenceId)) {
            String key = CacheConstance.APP_VISIT_LOG_CLASS_METHOD_PRE + appVisitLogSequenceId;
            this.stringRedisTemplate.opsForValue().set(key, clazzMethod, 3, TimeUnit.MINUTES);
        }
    }

    @AfterReturning(pointcut = "appLogPointCut()", returning = "ret")
    public void doAfterReturning(Object ret){
        log.info("====================AOP:afterReturning拦截开启====================");
        String appVisitLogSequenceId = request.getParameter("appVisitLogSequenceId");
        if (StringUtils.isNotBlank(appVisitLogSequenceId)) {
            Result result = (Result) ret;
            result.setAppVisitLogSequenceId(appVisitLogSequenceId);
        }
    }

}
