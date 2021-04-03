package com.hzp.test.config;

import com.google.common.base.Stopwatch;
import com.hzp.test.util.ReqLogUtil;
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

/**
 * @Description: 请求日志打印切面
 * @Date 2021/3/15 10:17
 */
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
        //计时器开启
        Stopwatch started = Stopwatch.createStarted();
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
        //计时器关闭
        started.stop();
        //打印请求日志
        String reqLog = ReqLogUtil.buildLogInfo(requestPath, requestHeader, requestParam, result, started);
        log.info(reqLog);
        return result;
    }

}
