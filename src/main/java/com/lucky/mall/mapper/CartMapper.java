package com.lucky.mall.mapper;

import com.lucky.mall.pojo.Cart;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * @Description 购物车Mapper
 * @Author shuxian.xiao
 * @Date 2019/8/21 17:28
 */

@Repository
public interface CartMapper {
    /**
     * 通过用户查询购物车
     * @param accountId 用户id
     * @return 购物车数据
     */
    List<Cart> selectByAccountId(int accountId);

    /**
     * 添加商品到购物车
     * @param cart 购物车
     * @return 0添加失败，1添加成功
     */
    int insertCart(Cart cart);

    /**
     * 更新数量
     * @param id 商品id
     * @param number 商品数量
     * @return 0更新失败，1更新成功
     */
    int updateNumber(int id,int number);

    /**
     * 删除某一条商品
     * @param id 购物车id
     * @return 0删除失败，1删除成功
     */
    int deleteCart(int id);

    /**
     * 查询购物车里是否有某件商品
     * @param accountId 用户id
     * @param goodsId 商品id
     * @return 购物出数据，包括购物车id和购物车内商品数量
     */
    Cart selectByAccountIdAndGoodsId(@Param("accountId")int accountId, @Param("goodsId") int goodsId);

    /**
     * 删除某个用户的所有购物车信息
     * @param accountId 用户id
     * @return 0 删除失败。>1修改成功
     */
    int deleteByAccountId(int accountId);

    /**
     * 查询用户购物车商品数量，商品不重复
     * @param accountId 用户id
     * @return 商品数量
     */
    int selectCountByAccountId(int accountId);

    /**
     * 通过用户id和商品id删除
     * @param accountId 用户id
     * @param goodsId 商品id
     * @return 0 删除失败，1删除成功
     */
    int deleteByAccountIdAndGoodsId(int accountId,int goodsId);

}
