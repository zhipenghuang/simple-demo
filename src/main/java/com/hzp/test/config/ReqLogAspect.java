package com.hzp.test.config;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Aspect
@Slf4j
@Order(value = 1)
@Component
public class ReqLogAspect {

    private static final String USER_AGENT = "user-agent";

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController) @within(org.springframework.stereotype.Controller)")
    public void logPointCut() {}

    //环绕触发
    @Around("logPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        //请求地址
        String requestPath;
        //请求头
        String requestHeader;
        //传入参数
        String requestParam;
        //存放输出结果
        Object responseObj;
        //开始时间
        long startTimestamp = System.currentTimeMillis();
        //结束时间
        long endTimestamp;
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        //获取请求地址
        requestPath = request.getRequestURL().toString();
        //获取请求头
        Enumeration<String> headers = request.getHeaderNames();
        List<String> headersList = new ArrayList<>();
        while (headers.hasMoreElements()) {
            String header = headers.nextElement();
            if (USER_AGENT.equalsIgnoreCase(header)) {
                headersList.add(header + "=" + request.getHeader(header));
            }
        }
        requestHeader = headersList.toString();
        //获取请求参数
        if (request instanceof ReqReReadWrapper) {
            //post从body获取
            ReqReReadWrapper requestWrapper = (ReqReReadWrapper) request;
            requestParam = getBodyString(requestWrapper);
        } else {
            //get直接获取
            requestParam = JSONUtil.parse(request.getParameterMap() == null ? JSONUtil.createObj() : request.getParameterMap()).toJSONString(0);
        }
        // 执行完方法的返回值：调用proceed()方法，就会触发切入点方法执行,result的值就是被拦截方法的返回值
        Object result = pjp.proceed();
        responseObj = result;
        endTimestamp = System.currentTimeMillis();
        printOptLog(requestPath, requestHeader, requestParam, responseObj, startTimestamp, endTimestamp);
        return result;
    }

    //获取请求Body流
    private String getBodyString(final ServletRequest request) {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            ServletInputStream originInputStream = request.getInputStream();
            if (originInputStream == null) {
                return sb.toString();
            }
            inputStream = cloneInputStream(originInputStream);
            reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    //复制输入流
    private InputStream cloneInputStream(ServletInputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buffer)) > -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            byteArrayOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        return byteArrayInputStream;
    }

    //输出日志
    private void printOptLog(String requestPath, String requestHeader, String requestParam, Object responseObj, long start, long end) {
        DateTime date = DateUtil.date(start);
        log.info("\n请求地址：" + requestPath +
                "\n请求头　：" + requestHeader +
                "\n请求时间：" + date.toString("yyyy-MM-dd HH:mm:ss.sss") +
                "\n花费时间：" + (end - start) + "ms" +
                "\n请求参数：" + requestParam +
                "\n请求结果：" + JSONUtil.parse(responseObj == null ? JSONUtil.createObj() : responseObj).toJSONString(0));
    }
}
