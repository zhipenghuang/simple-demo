package com.hzp.test.config;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.joda.time.DateTime;
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
import java.util.*;

@Aspect
@Slf4j
@Order(value = 1)
@Component
public class ReqLogAspect {

    private static final String USER_AGENT = "user-agent";

    //请求地址
    private String requestPath;
    //请求头
    private String requestHeader;
    //传入参数
    private String requestParam;
    //存放输出结果
    private Object responseObj;
    //开始时间
    private long startTimestamp;
    //结束时间
    private long endTimestamp;

    //方法调用前触发 记录开始时间
    @Before("execution(* com.hzp.test.controller..*.*(..))")
    public void doBeforeInServiceLayer(JoinPoint joinPoint) {
        // 记录方法开始执行的时间
        startTimestamp = System.currentTimeMillis();
    }

    //方法调用后触发 记录结束时间
    @After("execution(* com.hzp.test.controller..*.*(..))")
    public void doAfterInServiceLayer(JoinPoint joinPoint) {
        // 记录方法执行完成的时间
        endTimestamp = System.currentTimeMillis();
        printOptLog();
    }

    //环绕触发
    @Around("execution(* com.hzp.test.controller..*.*(..))")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
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
            requestParam = new Gson().toJson(request.getParameterMap());
        }
        // 执行完方法的返回值：调用proceed()方法，就会触发切入点方法执行,result的值就是被拦截方法的返回值
        Object result = pjp.proceed();
        responseObj = result;
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
    private void printOptLog() {
        // 需要用到google的gson解析包
        Gson gson = new Gson();
        DateTime dateTime = new DateTime(startTimestamp);
        log.info("\n请求地址：" + requestPath +
                "\n请求头　：" + requestHeader +
                "\n请求时间：" + dateTime.toString("yyyy-MM-dd HH:mm:ss.sss") +
                "\n花费时间：" + (endTimestamp - startTimestamp) + "ms" +
                "\n请求参数：" + requestParam +
                "\n请求结果：" + gson.toJson(responseObj));
    }
}
