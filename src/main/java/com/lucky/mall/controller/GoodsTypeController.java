package com.lucky.mall.controller;


import com.lucky.mall.pojo.GoodsType;
import com.lucky.mall.service.GoodsTypeService;
import com.lucky.mall.utils.restful.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 商品类别控制类
 * @Author shuxian.xiao
 * @Date 2019/8/5 20:07
 */
@Controller
public class GoodsTypeController {
    /**
     * 商品类型Service
     */
    @Autowired
    private GoodsTypeService goodsTypeService;

    /**
     * 获取所有状态为激活状态的商品种类
     *
     * @return Account[]
     */
    @PostMapping(value = "/getActivatedType")
    @ResponseBody
    public Result<Map> getActivatedType() {
        GoodsType[] types = goodsTypeService.getActivatedGoodsType();
        Map<String, GoodsType[]> typeMap = new HashMap<>();
        typeMap.put("types", types);
        return Result.success(typeMap);
    }

}