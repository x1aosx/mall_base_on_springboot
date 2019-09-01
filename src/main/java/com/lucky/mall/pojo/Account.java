/**
 * @Description 用户类
 * @Author shuxian.xiao@luckincoffee.com
 * @Date 2019/8/1 14:17
 */
package com.lucky.mall.pojo;

import lombok.Data;

import java.io.Serializable;
/**
 * @Description 用户类
 * @Author shuxian.xiao
 * @Date 2019/8/22 8:47
 */

@Data
public class Account implements Serializable {
    /**
     * 序列化id
     */
    private static final long serialVersionUID = -7898194272883238670L;
    /**
     * 用户id
     */
    private Integer id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
     private String password;
    /**
     * 邮箱
     */
     private String email;
    /**
     * 状态，是否已经激活
     */
     private Integer status;
    /**
     * 用户积分
     */
    private Integer points;
    /**
     * 用户头像
     */
    private String image;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 无参构造方法
     */
    public Account() {
    }

    /**
     * 有参构造方法
     * @param username 用户名
     * @param password 密码
     * @param email 邮箱
     */
    public Account(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }



}