package com.zh.fmcommon.utils;

import cn.hutool.core.net.NetUtil;
import cn.hutool.extra.servlet.ServletUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * ip工具类
 * @author : zhanghang
 * @date 2019/6/14
 */
@Slf4j
public class IpUtil extends NetUtil {

    public static String getRequestIp(HttpServletRequest request) {
        String ip = ServletUtil.getClientIP(request);
        if(ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1")){
            //根据网卡取本机配置的IP
            ip = getLocalhostStr();
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if(ip != null && ip.length() > 15){
            //"***.***.***.***".length() = 15
            if(ip.indexOf(",")>0){
                ip = ip.substring(0,ip.indexOf(","));
            }
        }
        return ip;
    }

    public static void main(String[] args) {
        String ip = getLocalhost().getHostAddress();
        System.out.println(ip);
    }

}
