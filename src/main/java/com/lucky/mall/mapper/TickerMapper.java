package com.lucky.mall.mapper;

import com.lucky.mall.pojo.PriceUser;
import com.lucky.mall.pojo.Ticker;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
/**
 * @Description 订单Mapper
 * @Author shuxian.xiao
 * @Date 2019/8/22 8:46
 */

@Repository
public interface TickerMapper {
    /**
     * 插入订单
     * @param ticker 订单
     * @return 0插入失败，>1插入成功
     */
    int insertTicker(Ticker ticker);

    /**
     * 获取用户不同状态的订单
     * @param accountId 用户id
     * @param status 订单状态
     * @return 订单集合
     */
    List<Ticker> selectByAccountIdAndStatus(@Param("accountId") int accountId,@Param("status") int status);

    /**
     * 获取所有订单
     * @return 订单集合
     */
    List<Ticker> selectAllTickers();

    /**
     * 更新评价
     * @param ticker 包括订单id，评价内容
     * @return 0 更新失败，1更新成功
     */
    int updateEvaluation(Ticker ticker);

    /**
     * 查询中奖的用户
     * @param status 状态为中奖的订单
     * @return 中奖的用户
     */
    List<PriceUser> selectPriceUser(int status);

    /**
     * 通过商品名模糊查询订单
     * @param map 包括用户id，和商品名
     * @return 订单集合
     */
    List<Ticker> selectTickersByGoodsNameByLike(Map<String,Object> map);

    /**
     * 订单数量
     * @return 订单数量
     */
    int selectCount();


}
