package com.lucky.mall.service;

import com.lucky.mall.pojo.Admin;

/**
 * @Description 管理员业务模块
 * @Author shuxian.xiao
 * @Date 2019/8/22 9:05
 */
public interface AdminService {
    /**
     * 查找是否有此管理员
     * @param username 用户名
     * @return 登陆成功或失败
     */
    Admin selectByUsername(String username);
}
