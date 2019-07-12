package com.zh.fmcommon.enums;


/**
 * @author zhanghang
 * @date 2019/6/5
 */
public enum DataCenterIdEnum {

    DataCenter_0(0);

    private final long code;

    DataCenterIdEnum(long code) {
        this.code = code;
    }

    public long getCode() {
        return code;
    }

}
