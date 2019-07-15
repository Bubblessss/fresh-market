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
public class AppResult {

    protected int code;

    protected String msg;

    protected JSONObject data;

    public AppResult(AppResultCodeEnum appResultCodeEnum) {
        this.code = appResultCodeEnum.getCode();
        this.msg = appResultCodeEnum.getMsg();
    }

    public static AppResult genFailResult() {
        return new AppResult(AppResultCodeEnum.SYSTEM_ERROR);
    }

    public static AppResult genFailResult(AppResultCodeEnum appResultCodeEnum) {
        return new AppResult(appResultCodeEnum);
    }

}
