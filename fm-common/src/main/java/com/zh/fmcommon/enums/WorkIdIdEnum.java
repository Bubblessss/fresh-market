package com.zh.fmcommon.enums;


/**
 * @author zhanghang
 * @date 2019/6/5
 */
public enum WorkIdIdEnum {

    FM_ZUUL(0);

    private final long code;

    WorkIdIdEnum(long code) {
        this.code = code;
    }

    public long getCode() {
        return code;
    }

}
