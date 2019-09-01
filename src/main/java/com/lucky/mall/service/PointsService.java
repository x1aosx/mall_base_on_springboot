package com.lucky.mall.service;

import com.github.pagehelper.PageInfo;
import com.lucky.mall.pojo.Points;

/**
 * @Description 积分Service
 * @Author shuxian.xiao
 * @Date 2019/8/19 11:40
 */

public interface PointsService {
    /**
     * 搜索某类型的积分
     * @param type 类型
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return 积分信息
     */
    PageInfo<Points> selectByType(int type, int pageNum, int pageSize);

    /**
     * 查询某个用户积分明细
     * @param accountId 用户id
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return 积分明细集合
     */
    PageInfo<Points> selectByAccountId(int accountId,int pageNum,int pageSize);

    /**
     * 积分记录
     * @return 积分记录
     */
    int selectCount();
}
