package com.hzp.test.config;

import com.hzp.test.intercepter.AuthIntercepter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Collections;

@Configuration()
@Primary
public class WebConfiguration implements WebMvcConfigurer {

    @Autowired
    private AuthIntercepter authIntercepter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authIntercepter).addPathPatterns("/manage/**").excludePathPatterns(whitelistUri());
    }

    /**
     * 白名单
     *
     * @return
     */
    private ArrayList<String> whitelistUri() {
        ArrayList<String> list = new ArrayList<>();
        String[] urls = {
                "/manage/login"
        };
        Collections.addAll(list, urls);
        return list;
    }
}
