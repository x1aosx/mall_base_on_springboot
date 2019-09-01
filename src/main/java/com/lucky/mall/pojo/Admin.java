package com.lucky.mall.pojo;

import lombok.Data;

/**
 * @Description 管理员类
 * @Author shuxian.xiao
 * @Date 2019/8/7 09:28
 */
@Data
public class Admin {
    /**
     * 管理员id
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
     * 管理员等级
     */
    private Integer level;
}