package com.lucky.mall.service.impl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lucky.mall.mapper.AccountMapper;
import com.lucky.mall.pojo.Account;
import com.lucky.mall.service.AccountService;
import com.lucky.mall.utils.email.SendEmailUtil;
import com.lucky.mall.utils.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @Description 用户业务接口实现
 * @Author shuxian.xiao@luckincoffee.com
 * @Date 2019/8/2 10:16
 */
@Service
public class AccountServiceImpl implements AccountService {
    /**
     * 用户mapper
     */
    @Autowired
    private AccountMapper accountMapper;
    /**
     * redis工具类
     */
    @Autowired
    private RedisUtil redisUtil;
    /**
     * 验证码保存时间
     */
    private static final Long CODE_SAVE_TIME = 300L;
    /**
     * 用户保存时间
     */
    private static final Long ACCOUNT_SAVE_TIME = 1800L;

    @Override
    public Account selectByUsername(String username) {
        return accountMapper.selectAccount(username);
    }


    @Override
    public Account selectByEmail(String email){
        return accountMapper.selectAccountByEmail(email);
    }

    @Override
    public boolean sendEmail(String email) {
        Account account = selectByEmail(email);
        if (account != null) {
            SendEmailUtil sendEmailUtil = new SendEmailUtil(email);
            sendEmailUtil.setSubject("饰品商城验证码");
            String code = sendEmailUtil.send();
            redisUtil.set(email,code, CODE_SAVE_TIME);
            redisUtil.set(email+code,account,ACCOUNT_SAVE_TIME);
            return true;
        }
        return false;
    }

    @Override
    public boolean checkEmailLogin(String email,String code){
        String oldCode = (String) redisUtil.get(email);
        if (code.equals(oldCode)){
            redisUtil.remove(email);
            return true;
        }
        return false;
    }

    @Override
    public void sendRegisterEmail(Account account) {
        SendEmailUtil sendEmailUtil = new SendEmailUtil(account.getEmail());
        sendEmailUtil.setSubject("注册验证码");
        String code = sendEmailUtil.send();
        account.setStatus(0);
        redisUtil.set(account.getEmail(),code,CODE_SAVE_TIME);
        redisUtil.set(account.getEmail()+code,account,ACCOUNT_SAVE_TIME);
    }

    @Override
    public int accountRegister(String email,String code) {
        String oldCode = (String) redisUtil.get(email);
        if (code != null && code.equals(oldCode)) {
            Account account = (Account) redisUtil.get(email + oldCode);
            //移除临时存储的用户对象
            redisUtil.remove(email+code);
            //设置为激活状态
            account.setStatus(1);
            return  accountMapper.insertAccount(account);
        }
        return 0;
    }

    @Override
    public String selectUsername(String username) {
        return accountMapper.selectUsername(username);
    }

    @Override
    public String selectEmail(String email) {
        return accountMapper.selectEmail(email);
    }

    @Override
    public PageInfo<Account> selectAccountByStatus(int status, int pageNum,  int pageSize) {
        Page<Account> accountPage = PageHelper.startPage(pageNum, pageSize);
        //PageHelp自动拦截dao层获取的数据并放入Page
        accountMapper.selectAccountByStatus(status);
        PageInfo pageInfo = new PageInfo(accountPage);
        return pageInfo;
    }

    @Override
    public boolean updateStatus(int id, int status) {
        int state = accountMapper.updateStatus(id,status);
        if (state>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean updateAccount(Account account) {
        int state = accountMapper.updateAccount(account);
       return state>0;
    }

    @Override
    public boolean updatePassword(Account account) {
        int state = accountMapper.updatePassword(account);
        if (state>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean updatePointsById(int id, int points) {
        int success = accountMapper.updatePointsById(id,points);
        return success>0;
    }

    @Override
    public int selectCount() {
        return accountMapper.selectCount();
    }
}