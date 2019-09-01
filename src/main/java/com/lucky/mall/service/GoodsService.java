package com.lucky.mall.service;

import com.github.pagehelper.PageInfo;
import com.lucky.mall.pojo.Goods;
import com.lucky.mall.pojo.UserComment;

import java.util.List;


/**
 * @Description 商品业务模块
 * @Author shuxian.xiao
 * @Date 2019/8/22 9:06
 */

public interface GoodsService {
    /**
     * 查询指定类型的饰品
     *
     * @param type    类别id
     * @param pageNum 当前页数
     * @param status 状态
     * @return 商品信息
     */
    PageInfo<Goods> selectGoodsByType(int type, int pageNum, int status);

    /**
     * 通过id查询商品详情
     * @param id 商品id
     * @return 商品类
     */
    Goods selectGoodsDetailById(int id);

    /**
     * 插入商品
     * @param goods 商品
     * @return true插入成功，false插入失败
     */
    boolean insertGoods(Goods goods);

    /**
     * 修改商品
     * @param goods 商品
     * @return true 修改成功，false修改失败
     */
    boolean updateGoods(Goods goods);

    /**
     * 获取商品图片
     * @param id 商品id
     * @return 商品图片
     */
    String selectImageById(int id);

    /**
     * 获取商品价格
     * @param id 商品id
     * @return 价格
     */
    Goods selectPriceAndDiscountById(int id);

    /**
     * 随机生成n条记录
     * @param randNum 随机生成的条数
     * @return 商品集合
     */
    List<Goods> randomGoods(int randNum);

    /**
     * 更新状态
     * @param id     商品
     * @param status 订单状态
     * @return true 修改成功，false修改失败
     */
    boolean updateStatus(int id, int status);

    /**
     * 根据级别随机生成
     * @param level   级别
     * @param randNum 数量
     * @return 商品集合
     */
    List<Goods> randomGoodsByLevel(int level, int randNum);

    /**
     * 通过商品id获取这个商品的评价以及评价的用户的昵称
     * @param id       商品id
     * @param pageNum  页码
     * @param pageSize 页大小
     * @return 用户评论
     */
    PageInfo<UserComment> selectUserComment(int id, int pageNum, int pageSize);

    /**
     * 通过商品名模糊查询
     * @param goodsName 商品名
     * @param pageNum 页码
     * @param pageSize 也大小
     * @return 商品信息
     */
    PageInfo<Goods> selectGoodsByNameByLIke(String goodsName, int pageNum, int pageSize);

    /**
     * 商品数量
     * @return 商品数量
     */
    int selectCount();


}