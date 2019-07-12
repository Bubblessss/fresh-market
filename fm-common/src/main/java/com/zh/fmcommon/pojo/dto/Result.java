package com.zh.fmcommon.pojo.dto;

import com.zh.fmcommon.enums.AppResultCodeEnum;
import lombok.Data;

/**
 * @author zhanghang
 * @date 2019/6/5
 */
@Data
public class Result {

    private int code;

    private String msg;

    private Object data;

    public Result() {}

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result(AppResultCodeEnum appResultCodeEnum) {
        this.code = appResultCodeEnum.getCode();
        this.msg = appResultCodeEnum.getMsg();
    }

    public Result(AppResultCodeEnum appResultCodeEnum, Object data) {
        this.code = appResultCodeEnum.getCode();
        this.msg = appResultCodeEnum.getMsg();
        this.data = data;
    }

    public static Result genSuccessResult() {
        return new Result(AppResultCodeEnum.SUCCESS);
    }

    public static Result genSuccessResult(Object data) {
        return new Result(AppResultCodeEnum.SUCCESS,data);
    }

    public static Result genFailResult(AppResultCodeEnum appResultCodeEnum) {
        return new Result(appResultCodeEnum);
    }

    public static Result genFailResult() {
        return new Result(AppResultCodeEnum.FAIL);
    }

    public static Result genFailResult(Object data) {
        return new Result(AppResultCodeEnum.FAIL,data);
    }
}
