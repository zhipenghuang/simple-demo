package com.hzp.test.config;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hzp.test.config.ReqLogHandler;
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
@Order(value = 1)
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
        //获取请求地址
        String requestPath = request.getRequestURL().toString();
        //获取请求头
        String requestHeader = ReqLogHandler.getHeaderFromRequest(request);
        //获取请求参数
        String requestParam = ReqLogHandler.getParamFromRequest(request);
        //执行完方法的返回值：调用proceed()方法，就会触发切入点方法执行,result的值就是被拦截方法的返回值
        Object result = pjp.proceed();
        printOptLog(requestPath, requestHeader, requestParam, result, startTimestamp, System.currentTimeMillis());
        return result;
    }

    //输出日志
    private void printOptLog(String requestPath, String requestHeader, String requestParam, Object responseObj, long start, long end) {
        log.info("\n请求地址：" + requestPath +
                "\n请求头　：" + requestHeader +
                "\n请求时间：" + DateUtil.date(start).toString("yyyy-MM-dd HH:mm:ss.sss") +
                "\n花费时间：" + (end - start) + "ms" +
                "\n请求参数：" + StrUtil.cleanBlank(requestParam) +
                "\n请求结果：" + JSONUtil.parse(responseObj == null ? JSONUtil.createObj() : responseObj).toJSONString(0));
    }
}
