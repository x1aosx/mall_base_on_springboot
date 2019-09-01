package com.lucky.mall.service.impl;

import com.lucky.mall.mapper.AdminMapper;
import com.lucky.mall.pojo.Admin;
import com.lucky.mall.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 管理员服务接口实现
 * @Author shuxian.xiao
 * @Date 2019/8/7 09:33
 */
@Service
public class AdminServiceImpl implements AdminService {
    /**
     * 管理员Mapper
     */
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin selectByUsername(String username) {
        Admin admin = adminMapper.selectByUsername(username);
        return admin;
    }
}