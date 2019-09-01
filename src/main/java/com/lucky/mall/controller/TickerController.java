package com.lucky.mall.controller;

import com.github.pagehelper.PageInfo;
import com.lucky.mall.interceptor.AccountRequired;
import com.lucky.mall.interceptor.AdminRequired;
import com.lucky.mall.pojo.Account;
import com.lucky.mall.pojo.Goods;
import com.lucky.mall.pojo.Ticker;
import com.lucky.mall.service.CartService;
import com.lucky.mall.service.GoodsService;
import com.lucky.mall.service.TickerService;
import com.lucky.mall.utils.redis.RedisUtil;
import com.lucky.mall.utils.restful.Result;
import com.lucky.mall.utils.snowflakeid.SnowflakeIdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 订单controller
 * @Author shuxian.xiao
 * @Date 2019/8/15 13:55
 */
@Controller
public class TickerController {
    /**
     * 订单service
     */
    @Autowired
    private TickerService tickerService;
    /**
     * 商品sservice
     */
    @Autowired
    private GoodsService goodsService;
    /**
     * 请求对象
     */
    @Autowired
    private HttpServletRequest request;

    /**
     * 登录用户session key
     */
    private static final String LOGGED_USER = "loggedUser";
    /**
     * 日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TickerController.class);

    /**
     * redis操作对象
     */
    @Autowired
    private RedisUtil redisUtil;
    /**
     * 购物车业务
     */
    @Autowired
    private CartService cartService;

    /**
     * 雪花id生成
     */
    private SnowflakeIdUtils idWorker = new SnowflakeIdUtils(3, 1);

    /**
     * 当前等待支付的订单
     */
    private static final String CURRENT_TICKERS = "currentTickers";
    /**
     * 前端返回的数据的切割字符
     */
    private static final String SPLIT_SYMBOL = ",";
    /**
     * 订单保存时间
     */
    private static final Long TICKER_SAVE_TIME = 1800L;
    /**
     * 订单id保存时间
     */
    private static final Long ID_SAVE_TIME = 3600L;
    /**
     * 当前订单保存时间
     */
    private static final Long CURRENT_TICKERS_TIME = 300L;


    /**
     * 创建订单并放入redis
     *
     * @param number  数量数组
     * @param goodsId 商品id数组
     * @param cartId 购物车id
     * @return 订单页面
     */
    @AccountRequired
    @PostMapping(value = "/createTicker")
    @ResponseBody
    public Result<String> createTicker(int[] number, int[] goodsId, int[] cartId) {
        List<Ticker> tickerList = new ArrayList<>();
        Account loggedAccount = (Account) request.getSession().getAttribute(LOGGED_USER);
        LOGGER.info("登陆的用户为:{}", loggedAccount);
        boolean success = false;
        for (int i = 0; i < number.length; i++) {
            Goods goods = goodsService.selectGoodsDetailById(goodsId[i]);
            cartService.deleteCartById(cartId[i]);
            Ticker ticker = new Ticker(loggedAccount.getId(), number[i], goods);
            ticker.setId(idWorker.nextId());
            tickerList.add(ticker);
        }
        success = addTickerToRedis(tickerList);
        LOGGER.info("订单集合:{}", tickerList);
        return success ? Result.success("下单成功").redirect("/payment") : Result.fail("下单失败");
    }

    /**
     * 添加订单id集合redis key为用户名，添加订单集合到redis，key为订单id
     * @param list 订单集合
     * @return true 添加成功，false添加失败
     */
    @SuppressWarnings("unchecked")
    @AccountRequired
    public boolean addTickerToRedis(List<Ticker> list) {
        if (list.size() > 0) {
            Account loggedAccount = (Account) request.getSession().getAttribute(LOGGED_USER);
            //通过用户名获取订单id集合
            List<Long> tickerIdList = (List<Long>) redisUtil.get(loggedAccount.getUsername());
            if (tickerIdList == null) {
                tickerIdList = new ArrayList<>();
            }
            //存放当前订单id
            List<Long> currentTickers = new ArrayList<>();
            for (Ticker ticker : list) {
                ticker.setCreateTime(System.currentTimeMillis());
                tickerIdList.add(ticker.getId());
                currentTickers.add(ticker.getId());
                //将订单存放到redis中30分钟
                redisUtil.set(String.valueOf(ticker.getId()), ticker, TICKER_SAVE_TIME);
            }
            LOGGER.info("用户未完成订单id表:{}", tickerIdList);
            //将用户未完成的订单的id存放一个小时
            redisUtil.set(loggedAccount.getUsername(), tickerIdList, ID_SAVE_TIME );
            //当前订单id存放5分钟
            String key = loggedAccount.getUsername() + CURRENT_TICKERS;
            redisUtil.set(key, currentTickers, CURRENT_TICKERS_TIME);
            return true;
        }
        return false;
    }

    /**
     * 支付页面
     *
     * @return 支付页面
     */
    @SuppressWarnings("unchecked")
    @AccountRequired
    @GetMapping(value = "/payment")
    public ModelAndView payment() {
        Account loggedAccount = (Account) request.getSession().getAttribute(LOGGED_USER);
        ModelAndView modelAndView = new ModelAndView("user/order");
        String key = loggedAccount.getUsername() + CURRENT_TICKERS;
        List<Long> currentTickers = (List<Long>) redisUtil.get(key);
        LOGGER.info("待支付的订单id为:{}", currentTickers);
        List<Ticker> tickerList = new ArrayList<>();
        if (currentTickers != null) {
            for (long tickerId : currentTickers) {
                Ticker ticker = (Ticker) redisUtil.get(String.valueOf(tickerId));
                if (ticker != null) {
                    tickerList.add(ticker);
                }
            }
        }
        modelAndView.addObject("tickerList", tickerList);
        return modelAndView;
    }

    /**
     * 完成支付，生成订单
     *
     * @param tickersIdStr 订单id串
     * @return 购买成功或失败信息
     */
    @AccountRequired
    @PostMapping(value = "/payment")
    @ResponseBody
    public Result<String> finishTicker(String tickersIdStr) {
        if (tickersIdStr != null && tickersIdStr.length() > 0) {
            LOGGER.info("订单id为:{}", tickersIdStr);
            String[] tickersId = tickersIdStr.split(SPLIT_SYMBOL);
            boolean success = false;
            for (String tickerId : tickersId) {
                Ticker ticker = (Ticker) redisUtil.get(tickerId);
                redisUtil.remove(tickerId);
                LOGGER.info("订单信息为:{}", ticker);
                ticker.finishTicker();
                success = tickerService.insertTicker(ticker);
                if (!success) {
                    return Result.fail("支付失败").redirect("/payment");
                }
            }
            return Result.success("支付成功").redirect("/cart");
        }
        return Result.fail("订单无效").redirect("/cart");
    }

    /**
     * 已完成订单
     *
     * @param page   页码
     * @param status 订单状态 1为已经完成但未评价，2为已评价
     * @return 页面以及已经完成的订单信息
     */
    @AccountRequired
    @GetMapping(value = "/finishedTickers")
    public ModelAndView finishedTickers(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "1") int status) {
        ModelAndView modelAndView = new ModelAndView("user/finishedOrder");
        Account loggedAccount = (Account) request.getSession().getAttribute(LOGGED_USER);
        //页大小
        int pageSize = 4;
        PageInfo<Ticker> pageInfo = tickerService.selectByAccountIdAndStatus(loggedAccount.getId(), status, page, pageSize);
        LOGGER.info("当前页面信息为:{}", pageInfo.getList());
        modelAndView.addObject("pageInfo", pageInfo);
        return modelAndView;
    }

    /**
     * 未完成订单
     *
     * @param page 页码
     * @return 未完成的订单
     */
    @SuppressWarnings("unchecked")
    @AccountRequired
    @GetMapping(value = "/unFinishedTickers")
    public ModelAndView unfinishedTickers(@RequestParam(defaultValue = "1") int page) {
        ModelAndView modelAndView = new ModelAndView("user/unFinishedOrder");
        Account loggedAccount = (Account) request.getSession().getAttribute(LOGGED_USER);
        List<Long> tickerIdList = (List<Long>) redisUtil.get(loggedAccount.getUsername());
        LOGGER.info("订单id集合未:{}", tickerIdList);
        List<Ticker> tickerList = new ArrayList<>();
        if (tickerIdList != null) {
            for (Long tickerId : tickerIdList) {
                Ticker ticker = (Ticker) redisUtil.get(String.valueOf(tickerId));
                if (ticker != null) {
                    tickerList.add(ticker);
                }
            }
            LOGGER.info("订单集合未:{}", tickerList);
        }
        modelAndView.addObject("tickerList", tickerList);
        return modelAndView;
    }

    /**
     * 删除未支付订单
     *
     * @param tickerId 订单id
     * @return 删除成功或失败信息
     */
    @AccountRequired
    @PostMapping(value = "/deleteUnFinishTicker")
    @ResponseBody
    public Result<String> deleteUnFinishTicker(String tickerId) {
        LOGGER.info("订单id:{}",tickerId);
        if (tickerId != null && redisUtil.exists(tickerId)) {
            redisUtil.remove(tickerId);
            return Result.success("删除成功");
        }
        return Result.fail("删除成功");
    }

    /**
     * 显示所有订单
     * @param page 页码
     * @param pageSize 页大小
     * @return 订单信息和页面
     */
    @AdminRequired
    @GetMapping(value = "/showAllTickers")
    public ModelAndView showAllTicker(@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "20") int pageSize){
        ModelAndView modelAndView = new ModelAndView("admin/ticker");
        PageInfo<Ticker> pageInfo = tickerService.selectAllTicker(page,pageSize);
        LOGGER.info("当前页的订单信息:{}",pageInfo.getList());
        modelAndView.addObject("pageInfo",pageInfo);
        return modelAndView;
    }

    /**
     * 订单评价
     * @param ticker 订单
     * @return 评价成功或失败信息
     */
    @AccountRequired
    @PostMapping(value = "/addComment")
    @ResponseBody
    public Result<String> getComment(Ticker ticker){
        if (ticker==null||ticker.getId()==null||ticker.getEvaluation()==null||ticker.getEvaluation().length()==0){
            return Result.fail("请完整填写信息");
        }
        int commentTicker = 2;
        ticker.setStatus(commentTicker);
        boolean success = tickerService.updateEvaluation(ticker);
        return success?Result.success("评价成功"):Result.fail("评价失败");
    }

    /**
     * 获取订单总数
     * @return 商品总数
     */
    @AdminRequired
    @PostMapping(value = "/tickerCount")
    @ResponseBody
    public Result<Integer> tickerCount(){
        int count = tickerService.selectCount();
        return Result.success(count);
    }

}
