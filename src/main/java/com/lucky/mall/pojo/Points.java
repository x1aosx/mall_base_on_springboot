package com.lucky.mall.pojo;

import lombok.Data;
import java.io.Serializable;
/**
 * @Description 积分类
 * @Author shuxian.xiao
 * @Date 2019/8/16 9:59
 */
@Data
public class Points implements Serializable {
    /**
     * 积分
     */
    private Integer number;
    /**
     * 积分id
     */
    private Long id;
    /**
     * 订单id
     */
    private Long tickerId;
    /**
     * 积分类型
     */
    private Integer type;
    /**
     * 用户id
     */
    private Integer accountId;
    /**
     * 获取或使用时间
     */
    private Long createTime;

    /**
     * 获取积分，类型为1
     */
    public void getPoints(){
        type = 1;
    }

    /**
     * 使用积分，类型为2
     */
    public void costPoints(){
        type = 2;
    }

    /**
     * 有参构造方法
     * @param id 积分id
     * @param tickerId 订单id
     * @param accountId 用户id
     * @param createTime 创建时间
     * @param number 数量
     */
    public Points(long id, long tickerId,  int accountId, long createTime,int number) {
        this.id = id;
        this.tickerId = tickerId;
        this.accountId = accountId;
        this.createTime = createTime;
        this.number = Math.abs(number);
    }

    /**
     * 无参构造方法
     */
    public Points() {
    }
}
