package com.lucky.mall.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lucky.mall.mapper.GoodsMapper;
import com.lucky.mall.pojo.Goods;
import com.lucky.mall.pojo.UserComment;
import com.lucky.mall.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @Description 商品服务实现
 * @Author shuxian.xiao
 * @Date 2019/8/5 20:06
 */
@Service
public class GoodsServiceImpl implements GoodsService {
    /**
     * 饰品Mapper
     */
    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public PageInfo<Goods> selectGoodsByType(int type, int pageNum,int status) {
        Page<Goods> goodsPage = PageHelper.startPage(pageNum,8);
        goodsMapper.selectByType(type,status);
        PageInfo pageInfo= new PageInfo(goodsPage);
        return pageInfo;
    }

    @Override
    public Goods selectGoodsDetailById(int id) {
        return goodsMapper.selectById(id);
    }

    @Override
    public boolean insertGoods(Goods goods) {
        return goodsMapper.insertGoods(goods)>0;
    }

    @Override
    public boolean updateGoods(Goods goods) {
        return goodsMapper.updateGoods(goods)>0;
    }

    @Override
    public String selectImageById(int id) {
        return goodsMapper.selectImageById(id);
    }

    @Override
    public Goods selectPriceAndDiscountById(int id) {
        return goodsMapper.selectPriceAndDiscountById(id);
    }

    @Override
    public List<Goods> randomGoods(int randNum) {
        return goodsMapper.randomGoods(randNum);
    }

    @Override
    public boolean updateStatus(int id, int status) {
        int success = goodsMapper.updateStatus(id,status);
        return success>0;
    }

    @Override
    public List<Goods> randomGoodsByLevel(int level, int randNum) {
        return goodsMapper.randomGoodsByLevel(level,randNum);
    }

    @Override
    public PageInfo<UserComment> selectUserComment(int id,int pageNum,int pageSize) {
        Page<UserComment> userCommentPage = PageHelper.startPage(pageNum,pageSize);
        goodsMapper.selectGoodsEvaluationByGoodsId(id);
        PageInfo pageInfo = new PageInfo(userCommentPage);
        return pageInfo;
    }

    @Override
    public PageInfo<Goods> selectGoodsByNameByLIke(String goodsName, int pageNum, int pageSize) {
        Page<Goods> goodsPage = PageHelper.startPage(pageNum,pageSize);
        goodsMapper.selectGoodsByNameByLIke(goodsName);
        PageInfo pageInfo = new PageInfo(goodsPage);
        return pageInfo;
    }

    @Override
    public int selectCount() {
        return goodsMapper.selectCount();
    }
}