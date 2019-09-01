package com.lucky.mall.mapper;

import com.lucky.mall.pojo.Account;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description 账号Mapper
 * @Author shuxian.xiao
 * @Date 2019/8/2 13:33
 */
@Repository
public interface AccountMapper {

    /**
     * 获取用户
     * @param username 用户名称
     * @return 用户
     */
    Account selectAccount(String username);

    /**
     * 通过邮箱查找用户
     * @param email 邮箱
     * @return 用户
     */
    Account selectAccountByEmail(String email);

    /**
     * 插入一条用户信息
     * @param account 用户
     * @return 插入数据的主键
     */
    Integer insertAccount(Account account);

    /**
     * 通过id查找用户
     * @param id 用户id
     * @return 用户
     */
    Account selectAccountById(int id);

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
     * 通过用户状态获取用户
     * @param status 状态
     * @return account数组
     */
    List<Account> selectAccountByStatus(int status);

    /**
     * 获取所有用户
     * @param map 集合
     * @return Account数组
     */
    Account [] selectAllAccount(Map<String,Integer> map);

    /**
     * 修改用户状态
     * @param id 用户id
     * @param status 设置的状态
     * @return >0 执行成功，否则失败
     */
    int updateStatus(int id,int status);

    /**
     * 修改用户基本信息，不包括密码
     * @param account 用户对象
     * @return >0 执行成功，否则失败
     */
    int updateAccount(Account account);

    /**
     * 更新密码
     * @param account 用户对象
     * @return 修改的行数
     */
    int updatePassword(Account account);

    /**
     * 获取所有用户
     * @return 用户集合
     */
    List<Account> selectAll();

    /**
     * 通过id获取用户积分
     * @param id 用户id
     * @return 积分
     */
    int selectPointsById(int id);

    /**
     * 更新用户积分
     * @param id 用户id
     * @param points 积分
     * @return 0更新失败，1更新成功
     */
    int updatePointsById(int id,int points);

    /**
     * 查询用户总数
     * @return 用户总数
     */
    int selectCount();



}
