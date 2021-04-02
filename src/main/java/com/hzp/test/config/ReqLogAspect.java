package com.hzp.test.config;

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

import javax.servlet.http.HttpServletRequest;

@Aspect
@Slf4j
@Order(0)
@Component
public class ReqLogAspect {

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController) @within(org.springframework.stereotype.Controller)")
    public void logPointCut() {}

    //环绕触发
    @Around("logPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        //开始时间
        long startTimestamp = System.currentTimeMillis();
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        //执行完方法的返回值：调用proceed()方法，就会触发切入点方法执行,result的值就是被拦截方法的返回值
        Object result = pjp.proceed();
        //获取请求地址
        String requestPath = request.getRequestURL().toString();
        //获取请求头
        String requestHeader = ReqLogUtil.getHeaderFromRequest(request);
        //获取请求参数
        String requestParam = ReqLogUtil.getParamFromRequest(request);
        //打印请求日志
        String reqLog = ReqLogUtil.buildLogInfo(requestPath, requestHeader, requestParam, result, startTimestamp, System.currentTimeMillis());
        log.info(reqLog);
        return result;
    }

}
