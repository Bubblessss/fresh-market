package com.zh.fmcommon.enums;


/**
 * @author zhanghang
 * @date 2019/6/5
 */
public enum AppResultCodeEnum {

    SUCCESS(200,"成功"),

    FAIL(-1, "失败"),

    USER_NOT_EXIST(100, "用户不存在!"),
    USER_NO_LOGIN(101, "登录超时,请重新登录!"),


    SYSTEM_ERROR(500, "系统异常!");

    private final int code;

    private final String msg;

    AppResultCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
