package com.lucky.mall.interceptor;

import com.lucky.mall.pojo.Admin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
/**
 * @Description 管理员拦截器
 * @Author shuxian.xiao
 * @Date 2019/8/17 14:40
 */
@Component
public class AdminInterceptor extends HandlerInterceptorAdapter {


    /**
     * 管理员登录session key
     */
    private static final String LOGGED_ADMIN = "loggedAdmin";
    /**
     * 日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminInterceptor.class);

    /**
     *
     * @param request 请求
     * @param response 响应
     * @param handler 处理器
     * @return true执行请求，false中断请求
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            AdminRequired adminRequired = method.getAnnotation(AdminRequired.class);
            if (adminRequired!=null){
                LOGGER.info("验证管理员是否登陆");
                Admin admin = (Admin)request.getSession().getAttribute(LOGGED_ADMIN);
                if (admin==null){
                    response.sendRedirect("/adminLogin");
                    return false;
                }
            }

        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }
}
