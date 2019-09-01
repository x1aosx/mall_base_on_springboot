package com.lucky.mall.service;


import com.github.pagehelper.PageInfo;
import com.lucky.mall.pojo.Account;


/**
 * @Description 用户业务
 * @Author shuxian.xiao@luckincoffee.com
 * @Date 2019/8/2 11:30
 */
public interface AccountService {
    /**
     * 验证登录
     * @param username 用户名
     * @return 0未激活,1已激活，2锁定,3密码错误,4用户不存在，
     */
    Account selectByUsername(String username);

    /**
     * 发送邮件
     * @param email 邮箱
     * @return true 发送成功，发送失败
     */
    boolean sendEmail(String email);
    /**
     * 通过邮箱获取用户
     * @param email 邮箱
     * @return 用户
     */
    Account selectByEmail(String email);

    /**
     * 验证登录
     * @param email 邮箱
     * @param code 验证码
     * @return true验证成功页面跳转，false返回错误信息
     */
    boolean checkEmailLogin(String email,String code);

    /**
     * 发送注册邮件
     * @param account 用户
     */
    void sendRegisterEmail(Account account);

    /**
     * 用户注册，插入用户到数据库
     * @param email 邮箱
     * @param code  验证码
     * @return 0 插入失败，1插入成功
     */
    int accountRegister(String email,String code);

    /**
     * 查找是否有此用户名
     * @param username 用户名
     * @return 用户名或空
     */
    String selectUsername(String username);

    /**
     * 查找是否有此邮箱
     * @param email 邮箱
     * @return 邮箱或空
     */
    String selectEmail(String email);
    /**
     * 获取不同状态的用户
     * @param status 用户状态
     * @param pageNum 用户页码
     * @param pageSize 页大小
     * @return 页面信息
     */
    PageInfo<Account> selectAccountByStatus(int status,int pageNum,int pageSize);

    /**
     * 改变用户状态
     * @param id 用户id
     * @param status 修改后用户状态
     * @return true修改成功，false修改失败
     */
    boolean updateStatus(int id,int status);

    /**
     * 修改用户的基本信息
     * @param account 用户
     * @return true 修改成功，false修改失败
     */
    boolean updateAccount(Account account);

    /**
     * 修改用户密码
     * @param account 用户
     * @return true 修改成功，false修改失败
     */
    boolean updatePassword(Account account);

    /**
     * 更新用户积分
     * @param id 用户id
     * @param points 积分
     * @return true 更新成功，false更新失败
     */
    boolean updatePointsById(int id,int points);

    /**
     * 用户数量
     * @return 用户数量
     */
    int selectCount();



}