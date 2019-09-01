package com.lucky.mall.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lucky.mall.mapper.*;
import com.lucky.mall.pojo.Points;
import com.lucky.mall.pojo.PriceUser;
import com.lucky.mall.pojo.Ticker;
import com.lucky.mall.service.TickerService;
import com.lucky.mall.utils.snowflakeid.SnowflakeIdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 订单业务类
 * @Author shuxian.xiao
 * @Date 2019/8/15 19:54
 */

@Service
public class TickerServiceImpl implements TickerService {
    /**
     * 订单Mapper
     */
    @Autowired
    private TickerMapper tickerMapper;
    /**
     * 商品Mapper
     */
    @Autowired
    private GoodsMapper goodsMapper;
    /**
     * 账户Mapper
     */
    @Autowired
    private AccountMapper accountMapper;
    /**
     * 购物车Mapper
     */
    @Autowired
    private CartMapper cartMapper;
    /**
     * 积分Mapper
     */
    @Autowired
    private PointsMapper pointsMapper;
    /**
     * 日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TickerServiceImpl.class);

    /**
     * 雪花id生成
     */
    private SnowflakeIdUtils idWorker = new SnowflakeIdUtils(3, 1);

    /**
     * 插入一条订单，业务包括，添加一条订单，商品减少库存
     * 用户添加积分，购物车删除商品
     * @param ticker
     * @return
     */
    @Override
    @Transactional
    public boolean insertTicker(Ticker ticker) {
        int goodsId = ticker.getGoodsId();
        int stock = goodsMapper.selectStock(goodsId);
        int points = accountMapper.selectPointsById(ticker.getAccountId());
        Points pointsRecord = new Points(idWorker.nextId(),ticker.getId(),ticker.getAccountId(),System.currentTimeMillis(),ticker.getPoints());
        stock =stock - ticker.getNumber();
        points = points + ticker.getPoints();
        ticker.setCreateTime(System.currentTimeMillis());

        //status=1 表示积分类型为获得,status=3 代表积分类型为使用
        if (ticker.getStatus()==1){
            pointsRecord.getPoints();
        }else if (ticker.getStatus()==3){
            pointsRecord.costPoints();
        }else {

        }
        LOGGER.info("插入的订单为:{}",ticker);
        LOGGER.info("修改后的库存为:{}",stock);
        LOGGER.info("修改后的积分为:{}",points);
        LOGGER.info("积分记录为:{}",pointsRecord);
        int tickerSuccess =  tickerMapper.insertTicker(ticker);
        int goodsSuccess = goodsMapper.updateStock(goodsId,stock);
        int accountSuccess = accountMapper.updatePointsById(ticker.getAccountId(),points);
        int pointsRecordSuccess = pointsMapper.insertPoints(pointsRecord);
        return tickerSuccess>0&&goodsSuccess >0 &&accountSuccess >0&&pointsRecordSuccess>0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PageInfo<Ticker> selectByAccountIdAndStatus(int accountId, int status,int pageNum,int pageSize) {
        Page<Ticker> tickerPage = PageHelper.startPage(pageNum,pageSize);
        tickerMapper.selectByAccountIdAndStatus(accountId,status);
        PageInfo pageInfo = new PageInfo(tickerPage);
        return pageInfo;
    }

    @Override
    public PageInfo<Ticker> selectAllTicker(int pageNum,int pageSize) {
        Page<Ticker> tickerPage = PageHelper.startPage(pageNum,pageSize);
        tickerMapper.selectAllTickers();
        PageInfo pageInfo = new PageInfo(tickerPage);
        return pageInfo;
    }

    @Override
    public boolean updateEvaluation(Ticker ticker) {
        Map<String,Object> map = new HashMap<>();
        int success = tickerMapper.updateEvaluation(ticker);
        return success>0;
    }


    @Override
    public List<PriceUser> selectPriceUser(int status) {
        return tickerMapper.selectPriceUser(status);
    }

    @Override
    public PageInfo<Ticker> selectTickersByGoodsNameByLike(int accountId,String goodsName,int pageNum,int pageSize) {
        Map<String,Object> map = new HashMap<>();
        map.put("accountId",accountId);
        map.put("goodsName",goodsName);
        Page<Ticker> tickerPage = PageHelper.startPage(pageNum,pageSize);
        tickerMapper.selectTickersByGoodsNameByLike(map);
        PageInfo pageInfo = new PageInfo(tickerPage);
        return pageInfo;
    }

    @Override
    public int selectCount() {
        return tickerMapper.selectCount();
    }
}
