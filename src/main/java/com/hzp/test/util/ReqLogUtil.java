package com.hzp.test.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.base.Stopwatch;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    public static String buildLogInfo(String requestPath, String requestHeader, String requestParam, Object responseObj, Stopwatch stopwatch) {
        return "\npath   ：" + requestPath +
                "\nheader ：" + requestHeader +
                "\nelapsed：" + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms" +
                "\nparam  ：" + StrUtil.cleanBlank(requestParam) +
                "\nresult ：" + JSONUtil.parse(responseObj == null ? JSONUtil.createObj() : responseObj).toJSONString(0);
    }

    public static String buildLogInfo(String requestPath, String requestHeader, String requestParam) {
        return "\npath   ：" + requestPath +
                "\nheader ：" + requestHeader +
                "\nparam  ：" + StrUtil.cleanBlank(requestParam);
    }
}
