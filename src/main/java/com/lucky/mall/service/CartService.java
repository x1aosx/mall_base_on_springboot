package com.lucky.mall.service;

import com.github.pagehelper.PageInfo;
import com.lucky.mall.pojo.Cart;
/**
 * @Description 购物车业务
 * @Author shuxian.xiao
 * @Date 2019/8/22 9:06
 */

public interface CartService {
    /**
     * 查询某个用户所有购物车信息
     * @param accountId 用户id
     * @param pageNum 页码
     * @return 购物车信息
     */
    PageInfo<Cart> selectByAccountId(int accountId, int pageNum);

    /**
     * 添加购物车信息
     * @param cart 购物车数据
     * @return true添加成功，false添加失败
     */
    boolean insertCart(Cart cart);

    /**
     * 修改单间商品的数量
     * @param id 购物车id
     * @param number 数量
     * @return true修改成功，false修改失败
     */
    boolean updateNumber(int id,int number);

    /**
     * 删除购物车的某条信息
     * @param id 购物车id
     * @return ture删除成功，false删除失败
     */
    boolean deleteCartById(int id);

    /**
     * 查询购物车里是否有某件商品
     * @param accountId 用户id
     * @param goodsId 商品id
     * @return 购物出数据，包括购物车id和购物车内商品数量
     */
    Cart selectByAccountIdAndGoodsId(int accountId,int goodsId);

    /**
     * 删除某个用户购物车所有信息
     * @param accountId 用户id
     * @return true 删除成功，false删除失败
     */
    boolean deleteByAccountId(int accountId);

    /**
     * 查询某个用户购物车条数
     * @param accountId 用户id
     * @return 购物车条数
     */
    int selectCountByAccountId(int accountId);


}
