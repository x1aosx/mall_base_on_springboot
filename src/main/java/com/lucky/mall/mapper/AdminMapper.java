package com.lucky.mall.mapper;

import com.lucky.mall.pojo.Admin;
import org.springframework.stereotype.Repository;
/**
 * @Description 管理员Mapper
 * @Author shuxian.xiao
 * @Date 2019/8/21 17:28
 */

@Repository
public interface AdminMapper {
    /**
     * 查找管理员
     * @param username 用户名
     * @return 管理员对象
     */
    Admin selectByUsername(String username);
}
