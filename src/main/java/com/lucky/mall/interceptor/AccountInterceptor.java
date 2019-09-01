package com.lucky.mall.interceptor;

import com.lucky.mall.pojo.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
/**
 * @Description 用户拦截器
 * @Author shuxian.xiao
 * @Date 2019/8/17 13:41
 */
@Component
public class AccountInterceptor extends HandlerInterceptorAdapter {

    /**
     * 登录用户session key
     */
    private static final String LOGGED_USER = "loggedUser";

    /**
     * http请求
     */
    @Autowired
    private HttpServletRequest httpServletRequest;

    /**
     *
     * @param request 请求
     * @param response 响应
     * @param handler 处理器，对controller层里请求进行处理
     * @return true继续流程，false中断请求
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果是映射到方法开始执行操作
        if (handler instanceof HandlerMethod) {
            // 方法注解级拦截器
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            // 判断接口是否需要登录
            AccountRequired methodAnnotation = method.getAnnotation(AccountRequired.class);
            // 有 @LoginRequired 注解，需要认证
            if (methodAnnotation != null) {
                // 用户未登陆跳转到登陆页面
                Account account = (Account) httpServletRequest.getSession().getAttribute(LOGGED_USER);
                if (account == null) {
                    response.sendRedirect("/login");
                    //不请求下的方法，直接中断
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}

