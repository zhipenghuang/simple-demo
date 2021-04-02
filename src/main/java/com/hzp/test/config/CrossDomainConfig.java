package com.hzp.test.config;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class CrossDomainConfig {

    @Bean
    public FilterRegistrationBean setFilter() {

        FilterRegistrationBean filterBean = new FilterRegistrationBean();
        filterBean.setFilter(new CrossDomainFilter());
        filterBean.setName("CrossDomainFilter");
        filterBean.addUrlPatterns("/*");
        return filterBean;
    }

    class CrossDomainFilter extends OncePerRequestFilter {

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            //跨域处理
            response.addHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
            response.addHeader("Access-Control-Allow-Credentials", "true");

            if (request.getHeader("Access-Control-Request-Method") != null && "OPTIONS".equalsIgnoreCase(request.getMethod())) {
                response.addHeader("Access-Control-Allow-Methods", "GET, POST");
                response.addHeader("Access-Control-Allow-Headers", "X-Requested-With, Origin, Content-Type, Cookie,Authorization,tk");
            }
            //封装可重复读body的request
            String POST = "post";
            String JSON = "application/json";
            if (POST.equalsIgnoreCase(request.getMethod()) && StrUtil.indexOfIgnoreCase(request.getContentType(), JSON) > -1) {
                filterChain.doFilter(new CustomRequestWrapper(request), response);
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
