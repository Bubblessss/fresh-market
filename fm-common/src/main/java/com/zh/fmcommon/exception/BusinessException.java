package com.zh.fmcommon.exception;

import com.zh.fmcommon.enums.AppResultCodeEnum;
import lombok.Data;

/**
 * @author zhanghang
 * @date 2019/6/5
 */
@Data
public class BusinessException extends RuntimeException {

    private int code;

    private String msg;

    private AppResultCodeEnum appResultCodeEnum;

    public BusinessException() {
        super();
    }

    public BusinessException(AppResultCodeEnum appResultCodeEnum) {
        super(appResultCodeEnum.getMsg());
        this.code = appResultCodeEnum.getCode();
        this.msg = appResultCodeEnum.getMsg();
        this.appResultCodeEnum = appResultCodeEnum;
    }
}
