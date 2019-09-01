package com.lucky.mall;

import com.lucky.mall.mapper.AccountMapper;
import com.lucky.mall.pojo.Goods;
import com.lucky.mall.service.GoodsService;
import com.lucky.mall.utils.email.SendEmailUtil;
import com.lucky.mall.utils.redis.RedisUtil;
import com.lucky.mall.utils.snowflakeid.SnowflakeIdUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MallApplicationTests {

    /**
     * 日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MallApplicationTests.class);


    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private AccountMapper accountMapper;

    /**
     * 商品业务
     */
    @Autowired
    private GoodsService goodsService;

    /**
     * 测试redis工具类
     */
    @Test
    public void redisTest() {
        redisUtil.set("string", "string");
        System.out.println(redisUtil.get("string"));
    }

    /**
     * 测试邮件发送工具类
     */
    @Test
    public void emailTest() {
        SendEmailUtil sendEmailUtil = new SendEmailUtil("xsx1075312923@163.com");
    }

    /**
     * 测试雪花id
     */
    @Test
    public void testSnowFlake() {
        SnowflakeIdUtils idWorker = new SnowflakeIdUtils(3, 1);
        for (int i = 0; i < 1000; i++) {
            System.out.println(idWorker.nextId());
        }
    }

    @Test
    public void randomGoods() {
        Goods emptyGoods = new Goods();
        emptyGoods.setImage("/static/images/roll/thanks.png");
        emptyGoods.setId(0);
        Goods[] goodsArray = new Goods[12];
        int[] empty = new int[]{(int) (Math.random() * 6), (int) (Math.random() * 6 + 6)};
        //存放商品id
        int[] randomArray = new int[100];
        for (int i = 0; i < 100; i++) {
            randomArray[i] = 0;
        }
        List<Goods> goodsLevel1 = goodsService.randomGoodsByLevel(1, 5);
        LOGGER.info("level_1:{}",goodsLevel1);
        List<Goods> goodsLevel2 = goodsService.randomGoodsByLevel(2, 4);
        LOGGER.info("level_2:{}",goodsLevel2);
        List<Goods> goodsLevel3 = goodsService.randomGoodsByLevel(3, 2);
        LOGGER.info("level_3:{}",goodsLevel3);
        List<Goods> goodsLevel4 = goodsService.randomGoodsByLevel(4, 1);
        LOGGER.info("level_4:{}",goodsLevel4);
        List<Goods> goodsList = new ArrayList<>();
        goodsList.addAll(goodsLevel1);
        goodsList.addAll(goodsLevel2);
        goodsList.addAll(goodsLevel3);
        goodsList.addAll(goodsLevel4);
        goodsList.set(empty[0],emptyGoods);
        goodsList.set(empty[1],emptyGoods);
        LOGGER.info("goodsList:{}",goodsList);
        LOGGER.info("goodsList的长度为:{}",goodsList.size());
    }

}
