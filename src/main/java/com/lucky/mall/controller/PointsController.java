package com.lucky.mall.controller;

import com.github.pagehelper.PageInfo;
import com.lucky.mall.interceptor.AccountRequired;
import com.lucky.mall.interceptor.AdminRequired;
import com.lucky.mall.pojo.Account;
import com.lucky.mall.pojo.Points;
import com.lucky.mall.service.PointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;

/**
 * @Description 积分控制层
 * @Author shuxian.xiao
 * @Date 2019/8/19 13:10
 */

@Controller
public class PointsController {
    /**
     * 积分Service
     */
    @Autowired
    private PointsService pointsService;

    /**
     * 登录用户session key
     */
    private static final String LOGGED_USER = "loggedUser";

    /**
     * 请求对象
     */
    @Autowired
    private HttpServletRequest request;

    /**
     *
     * @param type
     * @param page 页码
     * @return
     */
    /**
     * 显示不同类型的积分
     * @param type 积分类型
     * @param page 页码
     * @param pageSize 页大小
     * @return 积分页面
     */
    @GetMapping("/showPointsByType")
    @AdminRequired
    public ModelAndView showPointsByType(@RequestParam(defaultValue = "1") Integer type,
                                         @RequestParam(defaultValue = "1") Integer page ,@RequestParam(defaultValue = "20") int pageSize){
        ModelAndView modelAndView = new ModelAndView("admin/points");
        PageInfo<Points> pageInfo = pointsService.selectByType(type,page,pageSize);
        modelAndView.addObject("pageInfo",pageInfo);
        return modelAndView;
    }

    /**
     * 显示积分明细
     * @param page 页码
     * @param pageSize 页大小
     * @return 积分明细
     */
    @GetMapping("/showPointsByAccountId")
    @AccountRequired
    public ModelAndView showPointsByAccountId(@RequestParam(defaultValue = "1") Integer page,@RequestParam(defaultValue = "10")int pageSize ){
        ModelAndView modelAndView = new ModelAndView("user/points");
        Account loggedAccount = (Account)request.getSession().getAttribute(LOGGED_USER);
        PageInfo<Points> pageInfo = pointsService.selectByAccountId(loggedAccount.getId(),page,pageSize);
        modelAndView.addObject("pageInfo",pageInfo);
        return modelAndView;
    }



}
