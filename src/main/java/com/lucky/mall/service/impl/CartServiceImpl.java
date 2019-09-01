package com.lucky.mall.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lucky.mall.mapper.CartMapper;
import com.lucky.mall.pojo.Cart;
import com.lucky.mall.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 实现CartService接口
 * @Author shuxian.xiao
 * @Date 2019/8/13 17:11
 */
@Service
public class CartServiceImpl implements CartService {
    /**
     * 购物车Mapper
     */
    @Autowired
    public CartMapper cartMapper;

    @Override
    public PageInfo<Cart> selectByAccountId(int accountId, int pageNum) {
        Page<Cart> cartPage = PageHelper.startPage(pageNum,4);
        cartMapper.selectByAccountId(accountId);
        PageInfo pageInfo = new PageInfo(cartPage);
        return pageInfo;
    }

    @Override
    public boolean insertCart(Cart cart) {
        return cartMapper.insertCart(cart)>0;
    }

    @Override
    public boolean updateNumber(int id, int number) {
        return cartMapper.updateNumber(id,number)>0;
    }

    @Override
    public boolean deleteCartById(int id) {
        return cartMapper.deleteCart(id)>0;
    }

    @Override
    public Cart selectByAccountIdAndGoodsId(int accountId, int goodsId) {
        return cartMapper.selectByAccountIdAndGoodsId(accountId,goodsId);
    }

    @Override
    public boolean deleteByAccountId(int accountId) {
        return cartMapper.deleteByAccountId(accountId)>0;
    }

    @Override
    public int selectCountByAccountId(int accountId) {
        return cartMapper.selectCountByAccountId(accountId);
    }
}