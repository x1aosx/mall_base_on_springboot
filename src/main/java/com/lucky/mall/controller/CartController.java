package com.lucky.mall.controller;

import com.github.pagehelper.PageInfo;
import com.lucky.mall.interceptor.AccountRequired;
import com.lucky.mall.pojo.Account;
import com.lucky.mall.pojo.Cart;
import com.lucky.mall.pojo.Goods;
import com.lucky.mall.service.CartService;
import com.lucky.mall.service.GoodsService;
import com.lucky.mall.utils.restful.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description 购物车控制类
 * @Author shuxian.xiao
 * @Date 2019/8/7 10:09
 */
@Controller
public class CartController {
    /**
     * 购物车业务
     */
    @Autowired
    private CartService cartService;

    /**
     * 商品业务
     */
    @Autowired
    private GoodsService goodsService;

    /**
     * http请求
     */
    @Autowired
    private HttpServletRequest request;

    /**
     * 日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CartController.class);

    /**
     * 登录用户session key
     */
    private static final String LOGGED_USER = "loggedUser";

    /**
     * 购物车界面
     * @param page 页码
     * @return 界面和购物车信息
     */
    @GetMapping(value = "/cart")
    @AccountRequired
    public ModelAndView showCart(@RequestParam(defaultValue = "1") int page) {
        ModelAndView modelAndView = new ModelAndView("user/cart");
        Account loggedAccount = (Account) request.getSession().getAttribute(LOGGED_USER);
        PageInfo<Cart> pageInfo = cartService.selectByAccountId(loggedAccount.getId(), page);
        modelAndView.addObject("pageInfo", pageInfo);
        return modelAndView;
    }

    /**
     * 添加购物车
     * @param cart 购物车数据
     * @return 添加成功并跳转页面，添加失败则报网络异常
     */
    @PostMapping(value = "/addCart")
    @ResponseBody
    @AccountRequired
    public Result<String> addCart(Cart cart) {
        if (!cart.cartExist()) {
            return Result.fail("添加失败");
        }
        Account loggedAccount = (Account) request.getSession().getAttribute(LOGGED_USER);
        Cart cartExist = cartService.selectByAccountIdAndGoodsId(loggedAccount.getId(), cart.getGoodsId());
        //如果购物车已经有了此商品，直接对数量进行增加
        if (cartExist != null) {
            cart.setId(cartExist.getId());
            cart.setNumber(cartExist.getNumber() + cart.getNumber());
            LOGGER.info("购物车信息:{}", cart);
            cartService.updateNumber(cart.getId(), cart.getNumber());
        } else {
            //没有此商品，添加到购物车
            Goods goods = goodsService.selectPriceAndDiscountById(cart.getGoodsId());
            cart.setPrice(goods.getPrice() * goods.getDiscount());
            cart.setImage(goodsService.selectImageById(cart.getGoodsId()));
            cart.setAccountId(loggedAccount.getId());
            String time = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
            cart.setCreateTime(Long.parseLong(time));
            LOGGER.info("购物车数据:{}", cart);
            cartService.insertCart(cart);
        }

        return Result.success("添加成功").redirect("/cart");
    }

    /**
     * 删除购物车里的某一条数据
     *
     * @param id 购物车id
     * @return 成功或失败信息
     */
    @PostMapping(value = "/deleteCart")
    @ResponseBody
    @AccountRequired
    public Result<String> deleteCartId(int id) {
        boolean success = cartService.deleteCartById(id);
        return success ? Result.success("删除成功") : Result.fail("删除失败");
    }

    /**
     * 删除某个用户购物车里所有数据
     *
     * @return 删除或成功信息
     */
    @DeleteMapping(value = "/deleteAllCart")
    @ResponseBody
    @AccountRequired
    public Result<String> deleteAllCart() {
        Account loggedAccount = (Account) request.getSession().getAttribute(LOGGED_USER);
        int id = loggedAccount.getId();
        boolean success = cartService.deleteByAccountId(id);
        return success ? Result.success("删除成功") : Result.fail("删除失败");
    }

    /**
     * 更新商品数量
     * @param number 数量
     * @param id 购物车id
     * @return 更新成功或失败信息
     */
    @PostMapping(value = "/updateNumber")
    @ResponseBody
    public Result<String> updateNumber(int number, int id) {
        boolean success =false;
        if (number > 0 && id > 0) {
             success =  cartService.updateNumber(id, number);
        }
        return success?Result.success("修改成功"):Result.fail("修改失败");
    }

    /**
     * 查询购物车总数
     * @return 购物车总数
     */
    @AccountRequired
    @PostMapping(value = "/selectCartCount")
    @ResponseBody
    public Result<Integer> selectCartCount(){
        int count = 0;
        if (request.getSession().getAttribute(LOGGED_USER)!=null){
            Account loggedAccount = (Account) request.getSession().getAttribute(LOGGED_USER);
            int accountId = loggedAccount.getId();
            count = cartService.selectCountByAccountId(accountId);
        }
        return Result.success(count);
    }
}