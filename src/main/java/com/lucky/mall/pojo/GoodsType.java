package com.lucky.mall.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description 商品类型
 * @Author shuxian.xiao
 * @Date 2019/8/6 09:05
 */
@Data
public class GoodsType implements Serializable {
    /**
     * 商品类型id
     */
    private Integer id;
    /**
     * 类型名字
     */
    private String name;
    /**
     * 类型状态
     */
    private Integer status;
}
