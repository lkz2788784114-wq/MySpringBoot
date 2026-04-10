package org.example.test2.config;

import org.example.test2.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册保安，并规定他的管辖范围
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**") // 默认拦截所有请求
                .excludePathPatterns("/api/user/login",
                        "/api/user/add",
                        "/api/user/logout",
                        "/doc.html",           // Knife4j 默认入口网页
                        "/webjars/**",         // 网页静态资源
                        "/swagger-resources",  // Swagger 资源
                        "/v3/api-docs/**"  );    // 接口数据); // 唯独放行登录和注册
    }
}
