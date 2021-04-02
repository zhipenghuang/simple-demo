package com.hzp.test.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hzp.test.config.CustomRequestWrapper;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @Description: 请求日志工具类
 * @Date 2021/3/15 10:17
 */
public class ReqLogUtil {

    private static final String USER_AGENT = "user-agent";

    //从request中获取header
    public static String getHeaderFromRequest(final HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaderNames();
        List<String> headersList = new ArrayList<>();
        while (headers.hasMoreElements()) {
            String header = headers.nextElement();
            if (USER_AGENT.equalsIgnoreCase(header)) {
                headersList.add(header + "=" + request.getHeader(header));
            }
        }
        return headersList.toString();
    }

    //从request中获取请求参数
    public static String getParamFromRequest(final HttpServletRequest request) {
        String requestParam = "";
        if (request instanceof CustomRequestWrapper) {
            //post从body获取
            try {
                requestParam = IoUtil.read(request.getReader());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //get直接获取
            requestParam = JSONUtil.parse(request.getParameterMap() == null ? JSONUtil.createObj() : request.getParameterMap()).toJSONString(0);
        }
        return requestParam;
    }

    public static String buildLogInfo(String requestPath, String requestHeader, String requestParam, Object responseObj, long start, long end) {
        return "\n请求地址：" + requestPath +
                "\n请求头　：" + requestHeader +
                "\n请求时间：" + DateUtil.date(start).toString("yyyy-MM-dd HH:mm:ss.sss") +
                "\n花费时间：" + (end - start) + "ms" +
                "\n请求参数：" + StrUtil.cleanBlank(requestParam) +
                "\n请求结果：" + JSONUtil.parse(responseObj == null ? JSONUtil.createObj() : responseObj).toJSONString(0);
    }

    public static String buildLogInfo(String requestPath, String requestHeader, String requestParam) {
        return "\n请求地址：" + requestPath +
                "\n请求头　：" + requestHeader +
                "\n请求参数：" + StrUtil.cleanBlank(requestParam);
    }
}
