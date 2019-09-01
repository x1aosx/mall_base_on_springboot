package com.lucky.mall.controller;

import com.github.pagehelper.PageInfo;
import com.lucky.mall.interceptor.AccountRequired;
import com.lucky.mall.pojo.Account;
import com.lucky.mall.pojo.Ticker;
import com.lucky.mall.service.GoodsService;
import com.lucky.mall.service.PointsService;
import com.lucky.mall.service.TickerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description 搜索控制类
 * @Author shuxian.xiao
 * @Date 2019/8/21 15:19
 */

@Controller
public class SearchController {
    /**
     * 商品业务对象
     */
    @Autowired
    GoodsService goodsService;
    /**
     * 订单业务对象
     */
    @Autowired
    TickerService tickerService;
    /**
     * 积分业务对象
     */
    @Autowired
    PointsService pointsService;

    /**
     * 等级名字
     */
    private static final String [] LEVEL_NAME = {" ","工业","军规","受限","保密","隐秘","传说"};

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
     * 日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);

    /**
     * 通过商品名查询饰品
     * @param content 饰品模糊名字
     * @param page 页码
     * @param pageSize 页大小
     * @return 商品信息
     */
    @GetMapping("/searchGoodsByName")
    public ModelAndView searchGoodsByName(String content,@RequestParam(defaultValue = "1") Integer page,
                                          @RequestParam(defaultValue = "8")int pageSize){
        ModelAndView modelAndView = new ModelAndView("user/searchResult" );
        PageInfo pageInfo = goodsService.selectGoodsByNameByLIke(content,page,pageSize);
        LOGGER.info("商品:{}",pageInfo.getList());
        modelAndView.addObject("pageInfo",pageInfo);
        modelAndView.addObject("levelName",LEVEL_NAME);
        return modelAndView;
    }

    /**
     * 通过商品名查询订单
     * @param content 商品名
     * @param page 页码
     * @param pageSize 也大小
     * @return 查询的到的订单
     */
    @AccountRequired
    @GetMapping(value = "/searchTickersByGoodsName")
    public ModelAndView searchTickers(String content,@RequestParam(defaultValue = "1") Integer page,@RequestParam(defaultValue = "4")int pageSize){
        ModelAndView modelAndView = new ModelAndView("user/orderSearchResult");
        Account loggedAccount = (Account) request.getSession().getAttribute(LOGGED_USER);
        PageInfo<Ticker> pageInfo = tickerService.selectTickersByGoodsNameByLike(loggedAccount.getId(),content,page,pageSize);
        LOGGER.info("订单信息:{}",pageInfo.getList());
        modelAndView.addObject("pageInfo",pageInfo);
        return modelAndView;

    }
}
