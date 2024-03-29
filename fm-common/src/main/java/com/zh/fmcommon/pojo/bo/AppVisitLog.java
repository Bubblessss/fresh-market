package com.zh.fmcommon.pojo.bo;

import lombok.Data;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * @author zhanghang
 * @date 2019/6/14
 */
@Data
@ToString
public class AppVisitLog {

    @Id
    private ObjectId id;

    private String sequenceId;

    private Integer logType;

    private String serviceId;

    private String token;

    private Integer userId;

    private String ip;

    private String userAgent;

    private String requestUrl;

    private String requestClazz;

    private String requestMethod;

    private String requestParam;

    private Date requestTime;

    private Date responseTime;

    private Long costTime;

    private Integer status;

    private String responseContent;
}
