package com.lucky.mall.pojo;

import lombok.Data;
/**
 * @Description 用户评价
 * @Author shuxian.xiao
 * @Date 2019/8/21 10:13
 */

@Data
public class UserComment {
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 评价内容
     */
    private String evaluation;
    /**
     * 下单时间
     */
    private Long createTime;

}
