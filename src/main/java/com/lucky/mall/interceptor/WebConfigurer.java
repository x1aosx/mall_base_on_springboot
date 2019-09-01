package com.lucky.mall.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 * @Description 配置拦截请求
 * @Author shuxian.xiao
 * @Date 2019/8/17 14:47
 */

@Configuration
public class WebConfigurer implements WebMvcConfigurer {
    /**
     * 用户拦截
     */
    @Autowired
    private AccountInterceptor accountInterceptor;
    /**
     * 管理员拦截
     */
    @Autowired
    private AdminInterceptor adminInterceptor;

    /**
     * 添加拦截器
     * @param registry 拦截器注册对象
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accountInterceptor).addPathPatterns("/**");
        registry.addInterceptor(adminInterceptor).addPathPatterns("/**");
    }

    /**
     * 静态资源配置
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }
}
