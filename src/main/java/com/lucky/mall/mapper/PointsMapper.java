package com.lucky.mall.mapper;

import com.lucky.mall.pojo.Points;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description 积分Mapper
 * @Author shuxian.xiao
 * @Date 2019/8/16 10:16
 */
@Repository
public interface PointsMapper {
    /**
     * 插入一条积分记录
     * @param points 积分记录
     * @return 0插入失败，1插入成功
     */
    int insertPoints(Points points);

    /**
     * 查询不同状态的积分明细
     * @param type 状态
     * @return 积分集合
     */
    List<Points> selectByType(int type);

    /**
     * 获取某个用户的积分明细
     * @param accountId 用户id
     * @return 积分明细集合
     */
    List<Points> selectByAccountId(int accountId);

    /**
     * 积分数量
     * @return 数量
     */
    int selectCount();
}
