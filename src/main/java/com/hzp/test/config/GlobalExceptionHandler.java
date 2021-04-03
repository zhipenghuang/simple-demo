package com.hzp.test.config;

import com.hzp.test.exception.*;
import com.hzp.test.dto.common.ResponseEntity;
import com.hzp.test.util.ReqLogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 全局异常捕获
 * @Date 2021/3/15 10:17
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //Http请求时，参数异常
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity handlerRequstParams(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        log.error(ReqLogUtil.buildReqLogOfException(request), e);
        return new ResponseEntity(SystemErrors.REQUEST_BASE_EXCEPTION.getCode(), e.getMessage());
    }

    //ServletException
    @ExceptionHandler(ServletException.class)
    public ResponseEntity ServletException(ServletException e, HttpServletRequest request) {
        log.error(ReqLogUtil.buildReqLogOfException(request), e);
        return new ResponseEntity(SystemErrors.REQUEST_BASE_EXCEPTION.getCode(), e.getMessage());
    }

    //自定义系统内部异常
    @ExceptionHandler(SysException.class)
    public ResponseEntity SysException(SysException e, HttpServletRequest request) {
        log.error(ReqLogUtil.buildReqLogOfException(request), e);
        return new ResponseEntity(e.getError().getCode(), e.getError().getMessage());
    }

    //请求超时
    @ExceptionHandler(RequestTimeOutException.class)
    public ResponseEntity RequestTimeOutException(RequestTimeOutException e, HttpServletRequest request) {
        log.error(ReqLogUtil.buildReqLogOfException(request), e);
        return new ResponseEntity(SystemErrors.REQUEST_BASE_EXCEPTION.getCode(), e.getMessage());
    }

    //参数出错
    @ExceptionHandler(ParamsException.class)
    public ResponseEntity ParamsException(ParamsException e, HttpServletRequest request) {
        log.error(ReqLogUtil.buildReqLogOfException(request), e);
        return new ResponseEntity(e.getError().getCode(), e.getError().getMessage());
    }

    //基础异常处理
    @ExceptionHandler(BaseException.class)
    public ResponseEntity BaseException(HttpServletRequest request, BaseException e) {
        log.error(ReqLogUtil.buildReqLogOfException(request), e);
        return new ResponseEntity(e.getError().getCode(), e.getError().getMessage());
    }

    //统一未知异常处理
    @ExceptionHandler(Exception.class)
    public ResponseEntity handleGlobal(HttpServletRequest request, Exception e) {
        log.error(ReqLogUtil.buildReqLogOfException(request), e);
        return new ResponseEntity(SystemErrors.SYSTEM_ERROR);
    }

    //请求参数格式异常处理
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity MethodArgumentNotValidHandler(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error(ReqLogUtil.buildReqLogOfException(request), e);
        return new ResponseEntity(SystemErrors.REQUEST_BASE_EXCEPTION.getCode(), e.getMessage());
    }

    //请求参数绑定异常
    @ExceptionHandler(value = BindException.class)
    public ResponseEntity BindException(BindException e, HttpServletRequest request) {
        log.error(ReqLogUtil.buildReqLogOfException(request), e);
        return new ResponseEntity(SystemErrors.REQUEST_BASE_EXCEPTION.getCode(), e.getMessage());
    }

    //请求方法不支持
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.error(ReqLogUtil.buildReqLogOfException(request), e);
        return new ResponseEntity(SystemErrors.REQUEST_BASE_EXCEPTION.getCode(), e.getMessage());
    }


}
