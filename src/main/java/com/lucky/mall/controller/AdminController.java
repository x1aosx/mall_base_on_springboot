package com.lucky.mall.controller;

import com.lucky.mall.interceptor.AdminRequired;
import com.lucky.mall.pojo.Admin;
import com.lucky.mall.service.*;
import com.lucky.mall.utils.restful.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description 管理员控制器
 * @Author shuxian.xiao
 * @Date 2019/8/7 09:54
 */
@Controller
public class AdminController {
    /**
     * 管理员service
     */
    @Autowired
    private AdminService adminService;

    /**
     * 积分Service
     */
    @Autowired
    private PointsService pointsService;

    /**
     * 用户service
     */
    @Autowired
    private AccountService accountService;
    /**
     * 订单service
     */
    @Autowired
    private TickerService tickerService;
    /**
     * 商品service
     */
    @Autowired
    private GoodsService goodsService;

    /**
     * 请求对象
     */
    @Autowired
    private HttpServletRequest request;
    /**
     * 管理员登陆的session id
     */
    private static final String LOGGED_ADMIN = "loggedAdmin";
    /**
     * 日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    /**
     * 管理员登录页
     * @return 登录页面
     */
    @GetMapping(value = "/adminLogin")
    public String adminLogin() {
        return "admin/login";
    }

    /**
     * 管理员主页
     * @return 主页页面,以及统计信息
     */
    @AdminRequired
    @GetMapping(value = "/adminHome")
    public ModelAndView adminHome(){
        ModelAndView modelAndView = new ModelAndView("admin/adminHome");
        modelAndView.addObject("pointsCount",pointsService.selectCount());
        modelAndView.addObject("goodsCount",goodsService.selectCount());
        modelAndView.addObject("accountCount",accountService.selectCount());
        modelAndView.addObject("tickerCount",tickerService.selectCount());
        return modelAndView;
    }



    /**
     * 验证管理员登陆
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录成功或失败的信息
     */
    @PostMapping(value = "/adminLogin")
    @ResponseBody
    public Result<String> checkAdminLogin(String username, String password) {
        LOGGER.info("用户名:{}",username);
        LOGGER.info("密码:{}",password);
        if (username.length() == 0 || password.length() == 0) {
            return Result.fail("账户名或密码不能为空");
        }
        Admin admin = adminService.selectByUsername(username);
        if (password.equals(admin.getPassword())){
            request.getSession().setAttribute(LOGGED_ADMIN,admin);
            return Result.success("登陆成功").redirect("/adminHome");
        }
        return Result.fail("密码错误");
    }

    /**
     * 注销
     * @return 登陆页面
     */
    @GetMapping(value = "/adminLogout")
    public String adminLogout(){
        request.getSession().removeAttribute(LOGGED_ADMIN);
        return "admin/Login";

    }}
