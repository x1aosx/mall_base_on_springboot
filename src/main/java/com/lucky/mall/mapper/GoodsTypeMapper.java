package com.lucky.mall.mapper;

import com.lucky.mall.pojo.GoodsType;
import org.springframework.stereotype.Repository;
/**
 * @Description 商品类型Mapper
 * @Author shuxian.xiao
 * @Date 2019/8/22 8:44
 */

@Repository
public interface GoodsTypeMapper {
    /**
     * 获取所有商品种类
     * @param status 状态
     * @return 商品种类
     */
    GoodsType [] selectByStatus(int status);
}
