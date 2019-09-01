package com.lucky.mall.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description 订单类
 * @Author shuxian.xiao
 * @Date 2019/8/15 15:52
 */

@Data
public class Ticker implements Serializable {
    /**
     * 序列化id
     */
    private static final long serialVersionUID = -7898194202883238670L;
    /**
     * 订单id
     */

    private Long id;
    /**
     * 用户id
     */
    private Integer accountId;
    /**
     * 商品id
     */
    private Integer goodsId;
    /**
     * 商品单价
     */
    private Double price;
    /**
     * 商品数量
     */
    private Integer number;
    /**
     * 订单状态
     */
    private Integer status;
    /**
     * 产生积分
     */
    private Integer points;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 满意度
     */
    private Integer satisfaction;
    /**
     * 评价
     */
    private String evaluation;
    /**
     * 商品名字
     */
    private String goodsName;

    /**
     * 有参构造方法
     * @param userId 用户id
     * @param num 数量
     * @param goods 商品
     */
    public Ticker (int userId,int num,Goods goods){
        accountId = userId;
        this.goodsId = goods.getId();
        this.price = goods.getPrice()*goods.getDiscount();
        this.number = num;
        this.points = goods.getPointsNeed()*num;
        this.goodsName = goods.getName();
    }

    /**
     * 无参构造方法
     */
    public Ticker() {
    }

    /**
     * 完成订单 状态为1
     */
    public void finishTicker(){
        status = 1;
    }


}
