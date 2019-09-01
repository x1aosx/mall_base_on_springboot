package com.lucky.mall.pojo;

import lombok.Data;

/**
 * @Description 购物车
 * @Author shuxian.xiao
 * @Date 2019/8/13 16:24
 */
@Data
public class Cart {
    /**
     * 购物车每一条数据id
     */
    private Integer id;

    /**
     * 商品id
     */
    private Integer goodsId;
    /**
     * 数量
     */
    private Integer number;
    /**
     * 商品名字
     */
    private String goodsName;
    /**
     * 添加时间
     */
    private Long createTime;
    /**
     * 用户id
     */
    private Integer accountId;
    /**
     * 商品图片
     */
    private String image;
    /**
     * 商品价格
     */
    private Double price;

    /**
     * 判断购物车是否存在
     * @return true 存在，false 不存在
     */
    public boolean cartExist(){
        return goodsId>0&&goodsName.length()>0&&number>0;
    }
}