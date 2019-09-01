package com.lucky.mall.pojo;

import lombok.Data;
/**
 * @Description 中奖用户类
 * @Author shuxian.xiao
 * @Date 2019/8/21 10:12
 */

@Data
public class PriceUser {
    /**
     * 用户名
     */
    private String username;
    /**
     * 时间
     */
    private Long createTime;
    /**
     * 商品名
     */
    private String goodsName;
    /**
     * 昵称
     */
    private String nickName;

    /**
     * 无参构造方法
     */
    public PriceUser() {
    }
}
