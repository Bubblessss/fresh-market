package com.zh.fmcommon.exception.handler;

import com.alibaba.fastjson.JSONObject;
import com.zh.fmcommon.enums.AppResultCodeEnum;
import com.zh.fmcommon.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.zh.fmcommon.pojo.dto.Result;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.Optional;
import java.util.StringJoiner;

/**
 * @author zhanghang
 * @date 2019/6/5
 */
@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @Autowired
    private HttpServletRequest request;

    private Result getExceptionResult(AppResultCodeEnum appResultCodeEnum){
        String appVisitLogSequenceId = Optional.ofNullable(this.request.getParameter("appVisitLogSequenceId")).orElse(null);
        return Result.genFailResult(appResultCodeEnum,appVisitLogSequenceId);
    }

    @ExceptionHandler(BusinessException.class)
    public Result businessExceptionHandler(BusinessException ex){
        log.error("BusinessException异常信息：[{}]", ex.getMsg(),ex);
        return this.getExceptionResult(ex.getAppResultCodeEnum());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public Result bindingResultExceptionHandler(Exception ex){
        BindingResult bindingResult;
        String msg;
        if(ex instanceof MethodArgumentNotValidException) {
            msg = "BindException异常信息";
            bindingResult = ((MethodArgumentNotValidException) ex).getBindingResult();
        }else{
            bindingResult = ((BindException) ex).getBindingResult();
            msg = "BindException异常信息";
        }
        StringJoiner sj = new StringJoiner(";");
        bindingResult.getAllErrors().forEach(e -> sj.add(e.getDefaultMessage()));
        JSONObject jsonResult = new JSONObject();
        jsonResult.put(bindingResult.getObjectName(),sj.toString());
        log.error("{}：[{}]",msg,jsonResult.toJSONString(),ex);
        return this.getExceptionResult(AppResultCodeEnum.SYSTEM_ERROR);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public Result constraintViolationExceptionHandler(ConstraintViolationException ex){
        log.error("ConstraintViolationException异常信息：[{}]", ex.getMessage(),ex);
        JSONObject jsonResult = new JSONObject();
        ex.getConstraintViolations().forEach(e -> jsonResult.put(e.getPropertyPath().toString(),e.getMessageTemplate()));
        return this.getExceptionResult(AppResultCodeEnum.SYSTEM_ERROR);
    }

    @ExceptionHandler(value = Exception.class)
    public Result exceptionHandler(Exception ex){
        log.error("Exception异常信息：[{}]", ex.getMessage(),ex);
        return this.getExceptionResult(AppResultCodeEnum.SYSTEM_ERROR);
    }

}
