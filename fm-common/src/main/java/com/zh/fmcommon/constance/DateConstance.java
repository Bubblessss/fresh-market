package com.zh.fmcommon.constance;

import java.time.format.DateTimeFormatter;

/**
 * @author zhanghang
 * @date 2019/6/14
 */
public class DateConstance {

    public static final String YYYY_MM_DD = "yyyy/MM/dd";

    public static final DateTimeFormatter FORMATTER_YYYY_MM_DD = DateTimeFormatter.ofPattern(DateConstance.YYYY_MM_DD);

    public static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy/MM/dd HH:mm:ss.SSS";

    public static final DateTimeFormatter FORMATTER_YYYY_MM_DD_HH_MM_SS_SSS = DateTimeFormatter.ofPattern(DateConstance.YYYY_MM_DD_HH_MM_SS_SSS);
}
