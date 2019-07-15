package com.zh.fmcommon.component.command;

import com.zh.fmcommon.constance.CacheConstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author zhanghang
 * @date 2019/7/15
 */
@Slf4j
@Order(1)
@Component
public class TokenCommandRunner implements CommandLineRunner {

    @Value("${spring.application.name}")
    private String serviceId;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private WebApplicationContext applicationContext;

    @Override
    public void run(String... args) {
        log.info("===================初始化认证路径持久化redis======================");
        String serviceTag = this.serviceId.split("-")[1];
        String key = CacheConstance.TOKEN_PATH_PRE + serviceTag;
        this.redisTemplate.delete(key);
        RequestMappingHandlerMapping mapping = this.applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        String checkTokenPath = "com.zh.fmcommon.annotation.Token";
        Annotation[] annotationArr;
        Set<String> path;
        Set<String> pathList = new HashSet<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> e : map.entrySet()){
            annotationArr = e.getValue().getMethod().getDeclaredAnnotations();
            for (Annotation a : annotationArr){
                if (checkTokenPath.equals(a.annotationType().getName())){
                    path = e.getKey().getPatternsCondition().getPatterns();
                    pathList.addAll(path);
                    break;
                }
            }
        }
        if (!CollectionUtils.isEmpty(pathList)) {
            log.info("认证路径共:{}个",pathList.size());
            pathList.stream().forEach(System.out :: println);
            redisTemplate.opsForSet().add(key, pathList.toArray());
        }
    }

}
