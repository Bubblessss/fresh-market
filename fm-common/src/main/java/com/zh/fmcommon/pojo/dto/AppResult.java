package com.zh.fmcommon.pojo.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhanghang
 * @date 2019/6/5
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppResult {

    protected int code;

    protected String msg;

    protected JSONObject data;

}
