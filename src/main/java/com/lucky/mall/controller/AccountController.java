package com.lucky.mall.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.lucky.mall.interceptor.AccountRequired;
import com.lucky.mall.interceptor.AdminRequired;
import com.lucky.mall.pojo.Account;
import com.lucky.mall.pojo.Goods;
import com.lucky.mall.pojo.Ticker;
import com.lucky.mall.service.AccountService;
import com.lucky.mall.service.TickerService;
import com.lucky.mall.utils.email.VerifyMail;
import com.lucky.mall.utils.file.FileUtils;
import com.lucky.mall.utils.redis.RedisUtil;
import com.lucky.mall.utils.restful.Result;
import com.lucky.mall.utils.snowflakeid.SnowflakeIdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description 账户Controller
 * @Author shuxian.xiao
 * @Date 2019/8/2 13:38
 */
@Controller
//@SessionAttributes("loggedUser")
public class AccountController {
    /**
     * 用户业务层
     */
    @Autowired
    private AccountService accountService;

    /**
     * redis工具类
     */
    @Autowired
    private RedisUtil redisUtil;

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
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    /**
     * 抽奖商品key
     */
    private static final String RANDOM_GOODS = "randomGoods";
    /**
     * 订单Service
     */
    @Autowired
    private TickerService tickerService;
    /**
     * 雪花id生成
     */
    private SnowflakeIdUtils idWorker = new SnowflakeIdUtils(3, 1);
    /**
     * 图片大小，10m
     */
    private static final Integer IMAGE_SIZE = 10485760;
    /**
     * 抽奖一次所需积分
     */
    private static final Integer DRAW_POINTS = 50;
    /**
     * 随机生成的位置的最大值
     */
    private static final Integer MAX = 12;

    /**
     * 主页显示控制
     *
     * @return 主页
     */
    @GetMapping(value = "/home")
    public String homePage() {
        if (request.getSession().getAttribute(LOGGED_USER) != null) {
            Account account = (Account) request.getSession().getAttribute(LOGGED_USER);
            LOGGER.debug("用户信息：{}",JSONObject.toJSONString(account));
        }
        return "index";
    }

    /**
     * 验证登录
     *
     * @param account 账号
     * @return 登录成功或失败信息
     * @ModelAttribute(LOGGED_USER)
     */
    @PostMapping(value = "/login")
    @ResponseBody
    public Result<String> login(Account account) {
        Account loggedAccount = accountService.selectByUsername(account.getUsername());
        if (loggedAccount != null && account.getPassword() != null) {
            if (account.getPassword().equals(loggedAccount.getPassword())) {
                if (loggedAccount.getStatus()==1){
                    request.getSession().setAttribute(LOGGED_USER, loggedAccount);
                    LOGGER.info("登录的用户为:{}",loggedAccount);
                    return Result.success("登录成功").redirect("/home");
                }else {
                    return Result.fail("用户已被锁定，请联系管理员");
                }
            }
        }

        return Result.fail("账号或密码错误").redirect("/login");
    }

    /**
     * 登录页面
     *
     * @return 登录页面
     */
    @GetMapping(value = "/login")
    public String loginPage() {
        return "user/login";
    }


    /**
     * 发送邮件
     * @param email 邮箱
     * @return 发送是否成功信息
     */
    @PostMapping(value = "/sendEmail")
    @ResponseBody
    public Result<String> sendEmail(String email) {
        if (accountService.sendEmail(email)) {
            return Result.success("发送成功");
        }
        return Result.fail("发送失败");
    }

    /**
     * 验证邮箱登录
     * @param email 邮箱
     * @param code  验证码
     * @return 登录成功或失败信息
     */
    @PostMapping(value = "/emailLogin")
    @ResponseBody
    public Result<String> emailLogin(String email, String code) {
        if (accountService.checkEmailLogin(email, code)) {
            Account account = (Account) redisUtil.get(email + code);
            if (account.getStatus()==1){
                request.getSession().setAttribute(LOGGED_USER, account);
                return Result.success("登录成功").redirect("/home");
            }
            return Result.fail("用户已被锁定");
        }
        return Result.fail("验证码错误").redirect("/login");
    }

    /**
     * 跳转至注册页面
     *
     * @return 注册页面
     */
    @GetMapping(value = "/register")
    public String registerPage() {
        return "user/register";
    }

    /**
     * 为用户注册发送邮件
     *
     * @param account 用户
     * @return 发送是否成功信息
     */
    @PostMapping(value = "/sendRegisterEmail")
    @ResponseBody
    public Result<String> sendRegisterEmail(Account account) {
        LOGGER.info("正在注册的用户为:{}",account);
        String username = accountService.selectUsername(account.getUsername());
        String email = accountService.selectEmail(account.getEmail());
        if (username!=null||email!=null){
            return Result.fail("用户名或邮箱已被占用");
        }
        if (VerifyMail.isLegal(account.getEmail())) {
            accountService.sendRegisterEmail(account);
            return Result.success("发送成功");
        } else {
            Result.fail("邮箱非法");
        }
        return Result.fail("发送失败");
    }

    /**
     * 注册用户，往数据库中添加用户
     *
     * @param email 邮箱
     * @param code  验证码
     * @return 注册是否成功信息
     */
    @PostMapping(value = "/register")
    @ResponseBody
    public Result<String> register(String email, String code) {
        int primaryKey = accountService.accountRegister(email, code);
        if (primaryKey > 0) {
            return Result.success("注册成功").redirect("/login");
        }
        return Result.fail("验证码错误");
    }

    /**
     * 获取不同状态的用户
     * @param status 用户状态
     * @param page 页码
     * @param pageSiz 页大小
     * @return Account数组
     */
    @AdminRequired
    @GetMapping(value = "/getAccountByStatus")
    public ModelAndView getAccountByStatus(@RequestParam(defaultValue = "1")  Integer status,
                                           @RequestParam(defaultValue = "1")   Integer page,
                                           @RequestParam(defaultValue = "18") Integer pageSiz) {
        ModelAndView modelAndView = new ModelAndView("admin/member");
        PageInfo<Account> accountPageInfo = accountService.selectAccountByStatus(status, page,pageSiz);
        LOGGER.debug("页面信息为:{}",accountPageInfo);
        modelAndView.addObject("pageInfo", accountPageInfo);
        return modelAndView;
    }

    /**
     * 改变用户的状态
     *
     * @param id     用户id
     * @param status 需要设置成的状态
     * @return 是否设置成功的信息
     */
    @AdminRequired
    @PostMapping(value = "/changeStatus")
    @ResponseBody
    public Result<String> changeStatus(int id, int status) {
        boolean success = accountService.updateStatus(id, status);
        if (success) {
            return Result.success("修改成功");
        }
        return Result.fail("修改失败");
    }

    /**
     * 个人中心
     *
     * @return 个人中心页面
     */
    @AccountRequired
    @GetMapping(value = "/personCenter")
    public String personCenter() {
        return "user/personCenter";
    }

    /**
     * 修改用户基本信息
     * @param account 用户
     * @param imageFile 图片
     * @return 修改成功或失败的信息
     */
    @AccountRequired
    @PostMapping(value = "/saveChange")
    @ResponseBody
    public Result<String> saveChange(Account account, MultipartFile imageFile) {

        Account loggedAccount = (Account) request.getSession().getAttribute(LOGGED_USER);
        //设置昵称
        loggedAccount.setNickName(account.getNickName().length()==0?loggedAccount.getNickName():account.getNickName());
        //设置邮箱
        loggedAccount.setEmail(account.getEmail().length()==0?loggedAccount.getEmail():account.getEmail());

        if (imageFile!=null&& imageFile.getSize()> IMAGE_SIZE){
            Result.fail("文件过大");
        }
        String filePath = "/static/images/account/";
        String savePath = FileUtils.saveFile(imageFile,filePath,loggedAccount.getUsername() + System.currentTimeMillis());
        loggedAccount.setImage("error".equals(savePath)?loggedAccount.getImage():savePath);
        boolean success = accountService.updateAccount(loggedAccount);
        return success?Result.success("修改成功"):Result.fail("修改失败");
    }

    /**
     * 修改密码页面
     * @return 页面路径
     */
    @AccountRequired
    @GetMapping(value = "/changePassword")
    public String changePassword(){
        return "user/changePassword";
    }

    /**
     * 修改密码
     * @param prePassword 旧密码
     * @param newPassword 新密码
     * @return 修改成功或失败信息
     */
    @AccountRequired
    @PostMapping(value = "/changePassword")
    @ResponseBody
    public Result<String> changePassword(String prePassword,String newPassword){
        Account loggedAccount = (Account) request.getSession().getAttribute(LOGGED_USER);
        if (prePassword.length()>0&&newPassword.length()>0&&prePassword.equals(loggedAccount.getPassword())){
            Account account = new Account();
            account.setId(loggedAccount.getId());
            account.setPassword(newPassword);
            boolean success = accountService.updatePassword(account);
            return success ? Result.success("修改成功"):Result.fail("修改失败");
        }
        return Result.fail("密码有误");
    }

    /**
     * 验证旧密码是否正确
     * @param prePassword 旧密码
     * @return 正确或错误信息
     */
    @AccountRequired
    @PostMapping(value = "/checkPrePassword")
    @ResponseBody
    public Result<String> checkPrePassword(String prePassword){
        Account loggedAccount = (Account) request.getSession().getAttribute(LOGGED_USER);
        if (prePassword.length()>0&&prePassword.equals(loggedAccount.getPassword())){
            return Result.success("密码正确");
        }
        return Result.fail("密码错误");
    }

    /**
     * 抽奖
     * @return 停止的位置
     */
    @AccountRequired
    @PostMapping(value = "/randomPrice")
    @ResponseBody
    public Result<Integer> randomPrice(){
        Account loggedAccount = (Account) request.getSession().getAttribute(LOGGED_USER);
        if (loggedAccount.getPoints()<DRAW_POINTS){
            return Result.fail("您的积分不足，每抽奖一次50积分");
        }
        List<Goods> goodsList = (List<Goods>)redisUtil.get(RANDOM_GOODS);

        int stop  = (int)(Math.random()* MAX);
        LOGGER.info("停止的位置为:{}",stop);
        Goods goods = goodsList.get(stop);
        LOGGER.info("停止的位置为:{}",stop);
        //商品数量
        if (goods.getId()>0){
            int number = 1;
            Ticker ticker = new Ticker(loggedAccount.getId(),number,goods);
            //3代表订单为抽奖类型
            int status = 3;
            ticker.setStatus(status);
            //抽奖一次积分扣去50
            int points = -1 * DRAW_POINTS;
            loggedAccount.setPoints(loggedAccount.getPoints()+points);
            ticker.setPoints(points);
            ticker.setAccountId(loggedAccount.getId());
            ticker.setId(idWorker.nextId());
            LOGGER.info("抽奖产生的订单为:{}",ticker);
            tickerService.insertTicker(ticker);
            return Result.success(stop);
        }
        return Result.fail("谢谢参与");
    }

    /**
     * 注销登陆
     * @return 主页面
     */
    @AccountRequired
    @GetMapping(value = "/logout")
    public String logout(){
        request.getSession().removeAttribute(LOGGED_USER);
        return "index";
    }

    /**
     * 获取用户总数
     * @return 用户总数
     */
    @AdminRequired
    @PostMapping(value = "/accountCount")
    @ResponseBody
    public Result<Integer> accountCount(){
        int count = accountService.selectCount();
        return Result.success(count);
    }

}