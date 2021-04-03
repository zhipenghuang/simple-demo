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
    private static final String PATH = "\npath   ：";
    private static final String HEADER = "\nheader ：";
    private static final String ELAPSED = "\nelapsed：";
    private static final String PARAM = "\nparam  ：";
    private static final String RESULT = "\nresult ：";

    //请求正常返回时的日志打印
    public static String buildReqLogOfNormal(final HttpServletRequest request, final Object result, final Stopwatch stopwatch) {
        //获取请求地址
        String requestPath = request.getRequestURL().toString();
        //获取请求头
        String requestHeader = getHeaderFromRequest(request);
        //获取请求参数
        String requestParam = getParamFromRequest(request);
        //计时器关闭
        stopwatch.stop();
        //返回请求日志
        String reqLog = PATH + requestPath + HEADER + requestHeader + ELAPSED + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms" +
                PARAM + StrUtil.cleanBlank(requestParam) +
                RESULT + JSONUtil.parse(result == null ? JSONUtil.createObj() : result).toJSONString(0);
        return reqLog;
    }

    //请求出现异常时的日志打印
    public static String buildReqLogOfException(final HttpServletRequest request) {
        //获取请求地址
        String requestPath = request.getRequestURL().toString();
        //获取请求头
        String requestHeader = getHeaderFromRequest(request);
        //获取请求参数
        String requestParam = getParamFromRequest(request);
        return PATH + requestPath + HEADER + requestHeader + PARAM + StrUtil.cleanBlank(requestParam);
    }

    //从request中获取header
    private static String getHeaderFromRequest(final HttpServletRequest request) {
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
    private static String getParamFromRequest(final HttpServletRequest request) {
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
}
