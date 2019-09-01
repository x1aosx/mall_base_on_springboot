package com.lucky.mall.service;

import com.github.pagehelper.PageInfo;
import com.lucky.mall.pojo.PriceUser;
import com.lucky.mall.pojo.Ticker;

import java.util.List;
/**
 * @Description 订单业务
 * @Author shuxian.xiao
 * @Date 2019/8/22 9:07
 */

public interface TickerService {
    /**
     * 插入订单
     * @param ticker 订单
     * @return true插入成功，false插入失败
     */
    boolean insertTicker(Ticker ticker);

    /**
     * 根据用户id和订单状态查询订单
     * @param accountId 用户id
     * @param status 订单状态
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return 订单分页对象
     */
    PageInfo<Ticker> selectByAccountIdAndStatus(int accountId, int status,int pageNum,int pageSize);

    /**
     * 获取所有订单
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return 订单集合
     */
    PageInfo<Ticker> selectAllTicker(int pageNum,int pageSize);

    /**
     * 更新评价
     * @param ticker 包括订单id，评价，以及状态
     * @return true 更新成功，false更新失败
     */
    boolean updateEvaluation(Ticker ticker);

    /**
     * 查询状态为中奖的用户信息
     * @param status 订单状态
     * @return 中奖的用户
     */
    List<PriceUser> selectPriceUser (int status);
    /**
     * 按商品名查询订单
     * @param accountId 用户id
     * @param goodsName 商品名
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return 订单的集合和分页信息
     */
    PageInfo<Ticker> selectTickersByGoodsNameByLike(int accountId,String goodsName,int pageNum,int pageSize);

    /**
     * 订单数量
     * @return 订单数量
     */
    int selectCount();
}
