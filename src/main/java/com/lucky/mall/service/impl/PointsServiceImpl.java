package com.lucky.mall.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lucky.mall.mapper.PointsMapper;
import com.lucky.mall.pojo.Points;
import com.lucky.mall.service.PointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;

/**
 * @Description 积分Service
 * @Author shuxian.xiao
 * @Date 2019/8/19 13:05
 */

@Service
public class PointsServiceImpl implements PointsService {
    /**
     * 积分Mapper
     */
    @Autowired
    private PointsMapper pointsMapper;

    /**
     * 返回积分明细
     * @param type 积分类型
     * @param pageNum 页码
     * @param pageSize 页数
     * @return
     */
    @Override
    public PageInfo<Points> selectByType(int type, int pageNum, int pageSize) {
        Page<Points> pointsPage = PageHelper.startPage(pageNum,pageSize);
        pointsMapper.selectByType(type);
        PageInfo<Points> pageInfo = new PageInfo<>(pointsPage);
        return pageInfo;
    }

    @Override
    public PageInfo<Points> selectByAccountId(int accountId,int pageNum,int  pageSize) {
        Page<Points> pointsPage = PageHelper.startPage(pageNum,pageSize);
        pointsMapper.selectByAccountId(accountId);
        PageInfo pageInfo = new PageInfo(pointsPage);
        return pageInfo;
    }

    @Override
    public int selectCount() {
        return pointsMapper.selectCount();
    }
}
