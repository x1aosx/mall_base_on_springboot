package com.lucky.mall.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description 饰品类
 * @Author shuxian.xiao
 * @Date 2019/8/5 20:04
 */
@Data
public class Goods implements Serializable {
    /**
     * 商品id
     */
    private Integer id;
    /**
     * 商品名字
     */
    private String name;
    /**
     * 商品价格
     */
    private Double price;
    /**
     * 商品类型
     */
    private Integer type;
    /**
     * 折扣所需积分
     */
    private Integer pointsNeed;
    /**
     * 商品描述
     */
    private String description;
    /**
     * 商品图片
     */
    private String image;
    /**
     * 商品库存
     */
    private Integer stock;
    /**
     * 商品状态
     */
    private Integer status;
    /**
     * 商品等级
     */
    private Integer level;
    /**
     * 折扣力度
     */
    private Double discount;

    /**
     * 判断商品是否存在
     * @return true 存在，false 不存在
     */
    public boolean goodsExist(){
        return name.length()>0&&price>0&&type>0&&pointsNeed>0&&stock>0&&level>0&&description.length()>0&&discount<1;
    }

    /**
     * 无参构造方法
     */
    public Goods() {
    }


}