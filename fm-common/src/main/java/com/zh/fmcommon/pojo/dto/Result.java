package com.zh.fmcommon.pojo.dto;

import com.alibaba.fastjson.JSONObject;
import com.zh.fmcommon.enums.AppResultCodeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhanghang
 * @date 2019/6/5
 */
@Data
@NoArgsConstructor
public class Result extends AppResult{

    private String appVisitLogSequenceId;

    public Result(AppResultCodeEnum appResultCodeEnum) {
        this.code = appResultCodeEnum.getCode();
        this.msg = appResultCodeEnum.getMsg();
    }

    public Result(AppResultCodeEnum appResultCodeEnum, JSONObject data) {
        this.code = appResultCodeEnum.getCode();
        this.msg = appResultCodeEnum.getMsg();
        this.data = data;
    }

    public Result(AppResultCodeEnum appResultCodeEnum, String appVisitLogSequenceId) {
        this.code = appResultCodeEnum.getCode();
        this.msg = appResultCodeEnum.getMsg();
        this.appVisitLogSequenceId = appVisitLogSequenceId;
    }

    public static Result genSuccessResult() {
        return new Result(AppResultCodeEnum.SUCCESS);
    }

    public static Result genSuccessResult(JSONObject data) {
        return new Result(AppResultCodeEnum.SUCCESS,data);
    }

    public static Result genFailResult(AppResultCodeEnum appResultCodeEnum) {
        return new Result(appResultCodeEnum);
    }

    public static Result genFailResult(AppResultCodeEnum appResultCodeEnum,String appVisitLogSequenceId) {
        return new Result(appResultCodeEnum,appVisitLogSequenceId);
    }

}
