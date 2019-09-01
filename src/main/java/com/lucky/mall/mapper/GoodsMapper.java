package com.lucky.mall.mapper;

import com.lucky.mall.pojo.Goods;
import com.lucky.mall.pojo.UserComment;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * @Description 商品mapper
 * @Author shuxian.xiao
 * @Date 2019/8/5 20:05
 */
@Repository
public interface GoodsMapper {
    /**
     * 获取不同类型不同状态的所有商品
     * @param type 类型
     * @param status 状态
     * @return 商品list
     */
    List<Goods> selectByType(int type,int status);

    /**
     * 通过id查询商品
     * @param id 商品id
     * @return 商品
     */
    Goods selectById(int id);

    /**
     * 添加饰品
     * @param goods 饰品
     * @return 0 添加失败，1添加成功
     */
    int insertGoods(Goods goods);

    /**
     * 更新商品
     * @param goods 商品信息
     * @return 0修改失败，1修改成功
     */
    int updateGoods(Goods goods);

    /**
     * 通过id查询图片
     * @param id 商品id
     * @return 图片路径
     */
    String selectImageById(int id);

    /**
     * 通过商品id获取价格
     * @param id 商品id
     * @return 价格
     */
    Goods selectPriceAndDiscountById(int id);

    /**
     * 随机生成n调商品信息
     * @param randNum 随机生成的条数
     * @return 商品集合
     */
    List<Goods> randomGoods(int randNum);

    /**
     * 获取商品的库存
     * @param id 商品id
     * @return 库存
     */
    int selectStock(int id);

    /**
     * 更新库存
     * @param id 商品id
     * @param stock 库存
     * @return 0失败 ，1 成功
     */
    int updateStock(int id,int stock);

    /**
     * 更新商品状态
     * @param id 商品id
     * @param status 商品状态
     * @return 0失败 ，1 成功
     */
    int updateStatus(int id,int status);

    /**
     * 根据级别随机生成n个商品
     * @param level 级别
     * @param randNum 个数
     * @return 商品集合
     */
    List<Goods> randomGoodsByLevel(int level,int randNum);

    /**
     * 通过商品id获取商品评价
     * @param id 商品id
     * @return 用户评论
     */
    List<UserComment> selectGoodsEvaluationByGoodsId(int id);

    /**
     * 通过商品模糊查询商品
     * @param goodsName 商品名
     * @return 商品集合
     */
    List<Goods> selectGoodsByNameByLIke(String goodsName);

    /**
     * 商品数量
     * @return 商品数量
     */
    int selectCount();
}