package com.zh.fmcommon.annotation;

import java.lang.annotation.*;

/**
 * token注解
 * 注释在需要认证的接口上
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Token {
}
