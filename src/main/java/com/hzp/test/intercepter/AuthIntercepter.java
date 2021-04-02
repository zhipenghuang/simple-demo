package com.hzp.test.intercepter;

import com.alibaba.fastjson.JSON;
import com.hzp.test.exception.SystemErrors;
import com.hzp.test.dto.common.JwtInfo;
import com.hzp.test.util.JwtTokenUtil;
import com.hzp.test.dto.common.ResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class AuthIntercepter extends HandlerInterceptorAdapter {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        try {
            JwtInfo jwtInfo = jwtTokenUtil.validateToken(token);
            request.setAttribute("userId", jwtInfo.getUserId());
            request.setAttribute("username", jwtInfo.getUsername());
        } catch (Exception e) {
            log.error("token校验失败", e);
            response.setHeader("Content-type", "application/json;charset=UTF-8");
            ResponseEntity result = new ResponseEntity(SystemErrors.TOKEN_ERROR);
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        return super.preHandle(request, response, handler);
    }
}
