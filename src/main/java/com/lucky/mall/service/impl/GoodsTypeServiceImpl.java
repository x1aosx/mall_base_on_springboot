package com.lucky.mall.service.impl;

import com.lucky.mall.mapper.GoodsTypeMapper;
import com.lucky.mall.pojo.GoodsType;
import com.lucky.mall.service.GoodsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 饰品服务实现
 * @Author shuxian.xiao
 * @Date 2019/8/5 20:06
 */
@Service
public class GoodsTypeServiceImpl implements GoodsTypeService {
    /**
     * 商品类型Mapper
     */
    @Autowired
    private GoodsTypeMapper goodsTypeMapper;

    @Override
    public GoodsType[] getActivatedGoodsType() {
        return goodsTypeMapper.selectByStatus(1);
    }

    @Override
    public GoodsType[] getLockedGoodsType() {
        return goodsTypeMapper.selectByStatus(0);
    }
}