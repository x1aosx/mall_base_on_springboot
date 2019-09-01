package com.lucky.mall.service;

import com.lucky.mall.pojo.GoodsType;
/**
 * @Description 商品类型业务
 * @Author shuxian.xiao
 * @Date 2019/8/22 9:18
 */

public interface GoodsTypeService {
    /**
     * 获取属于激活状态的商品类型
     * @return 商品类型数组
     */
    GoodsType [] getActivatedGoodsType();

    /**
     * 获取锁定状态的商品类型
     * @return 商品类型数组
     */
    GoodsType [] getLockedGoodsType();
}
