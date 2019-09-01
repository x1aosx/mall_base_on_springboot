package com.lucky.mall.controller;

import com.github.pagehelper.PageInfo;
import com.lucky.mall.interceptor.AccountRequired;
import com.lucky.mall.interceptor.AdminRequired;
import com.lucky.mall.pojo.*;
import com.lucky.mall.service.GoodsService;
import com.lucky.mall.service.GoodsTypeService;
import com.lucky.mall.service.TickerService;
import com.lucky.mall.utils.file.FileUtils;
import com.lucky.mall.utils.redis.RedisUtil;
import com.lucky.mall.utils.restful.Result;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.slf4j.Logger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description 商品控制类
 * @Author shuxian.xiao
 * @Date 2019/8/5 20:07
 */
@Controller
public class GoodsController {
    /**
     * 商品业务
     */
    @Autowired
    private GoodsService goodsService;
    /**
     * 等级名字
     */
    private static final String [] LEVEL_NAME = {" ","工业","军规","受限","保密","隐秘","传说"};
    /**
     * 下面的数组长度
     */
    private static final Integer LENGTH = 50;

    /**
     * 类型名字
     */
    private static final String [] TYPE_NAME= new String[LENGTH];

    /**
     * 商品种类业务
     */
    @Autowired
    public GoodsTypeService goodsTypeService;

    /**
     * 日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsController.class);
    /**
     * 图片存储路径
     */
    private static final String IMAGE_FILE_PATH = "/static/images/product/";
    /**
     * redis工具类对象
     */
    @Autowired
    private RedisUtil redisUtil;
    /**
     * 抽奖商品key
     */
    private static final String RANDOM_GOODS = "randomGoods";
    /**
     * 订单Service
     */
    @Autowired
    private TickerService tickerService;

    /**
     * 分类显示商品
     * @param type 类型
     * @param page 页码
     * @return 页面信息
     */
    @GetMapping(value = "/showGoodsByType")
    @ResponseBody
    public ModelAndView showGoodsByType(int type,int page){
        int status = 1;
        ModelAndView modelAndView = new ModelAndView("user/category");
        PageInfo<Goods> pageInfo = goodsService.selectGoodsByType(type,page,status);
        modelAndView.addObject("pageInfo",pageInfo);
        modelAndView.addObject("levelName",LEVEL_NAME);
        return modelAndView;
    }

    /**
     * 展示商品给管理员
     * @param type 类别
     * @param page 页数
     * @return 商品集合
     */
    @AdminRequired
    @GetMapping(value = "/showGoodsByTypeToAdmin")
    @ResponseBody
    public ModelAndView showGoodsByTypeToAdmin(int type,int page){
        ModelAndView modelAndView = new ModelAndView("admin/category");
        int status = 1;
        PageInfo<Goods> pageInfo = goodsService.selectGoodsByType(type,page,status);
        GoodsType [] goodsTypes = goodsTypeService.getActivatedGoodsType();
        for (GoodsType goodsType:goodsTypes){
            TYPE_NAME[goodsType.getId()]=goodsType.getName();
        }
        modelAndView.addObject("types",TYPE_NAME);
        modelAndView.addObject("pageInfo",pageInfo);
        modelAndView.addObject("levelName",LEVEL_NAME);
        return modelAndView;
    }

    /**
     * 显示商品详细信息
     * @param id 商品id
     * @return 商品对象
     */
    @PostMapping(value = "/productDetail")
    @ResponseBody
    public Result<Goods> showGoodsDetailById(int id){
        Goods goods = goodsService.selectGoodsDetailById(id);
        LOGGER.debug("商品信息为:{}",goods);
        if (goods!=null){
            return Result.success(goods);
        }
        return Result.fail("获取失败");
    }

    /**
     * 添加商品页面
     * @return 添加商品页面
     */
    @AdminRequired
    @GetMapping(value = "/addProduct")
    public String addProduct(){
        return "admin/addProduct";
    }

    /**
     * 添加商品
     * @param goods 商品
     * @param productImage 图片
     * @return 添加成功或失败的信息
     */
    @AdminRequired
    @PostMapping(value = "/addProduct")
    @ResponseBody
    public Result<String> addProduct(Goods goods,MultipartFile productImage){
        if (goods==null||productImage==null){
            return Result.fail("信息为空");
        }
        LOGGER.debug("商品信息为:{}",goods);
        LOGGER.debug("图片信息为:{}",productImage);
        boolean success = false;
        if (goods.goodsExist()){
            String time = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
            String imageName = "product_" + goods.getType() + "_" + goods.getLevel() + "_" + time;
            goods.setImage(FileUtils.saveFile(productImage, IMAGE_FILE_PATH,imageName));
            int status = 1;
            goods.setStatus(status);
            LOGGER.debug("商品的完整信息为:{}",goods);
            success = goodsService.insertGoods(goods);
        }
        return success?Result.success("添加成功"):Result.fail("商品信息不完成");
    }

    /**
     * 修改商品信息页面
     * @param id 商品id
     * @return 商品信息和页面
     */
    @AdminRequired
    @GetMapping(value = "/modifyProduct")
    public ModelAndView modifyProduct(int id){
        ModelAndView modelAndView = new ModelAndView("admin/updateProduct");
        Goods goods = goodsService.selectGoodsDetailById(id);
        modelAndView.addObject("goods",goods);
        return modelAndView;
    }

    /**
     * 修改商品
     * @param goods 商品
     * @param productImage 图片
     * @param page 页码
     * @param type 类型
     * @return 修改成功或失败信息
     */
    @AdminRequired
    @PostMapping(value = "/modifyProduct")
    @ResponseBody
    public Result<String> modifyProduct(Goods goods,MultipartFile productImage,int page,int type){
        LOGGER.info("商品信息:{}",goods);
        LOGGER.info("图片信息:{}",productImage);
        LOGGER.info("页面:{}",page);
        LOGGER.info("类型:{}",type);
        boolean success = false;
        if (goods.goodsExist()){
            if (productImage!=null){
                String time = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
                String imageName = "product_" + goods.getType() + "_" + goods.getLevel() + "_" + time;
                goods.setImage(FileUtils.saveFile(productImage, IMAGE_FILE_PATH,imageName));
                LOGGER.info("商品的完整信息为:{}",goods);
            }else {
                String image = goodsService.selectImageById(goods.getId());
                LOGGER.info("图片:{}",image);
                goods.setImage(image);
            }
            success = goodsService.updateGoods(goods);
        }
        return success?Result.success("修改成功").redirect("/showGoodsByTypeToAdmin?page=" + page + "&type=" + type):Result.fail("商品信息不完整");
    }

    /**
     * 展示商品详情给用户
     * @param id 商品id
     * @param page 页码
     * @return 商品详情，和详情页面
     */
    @GetMapping(value = "/showGoodsToUser")
    public ModelAndView showProduct(int id,@RequestParam(defaultValue = "1") int page){
        ModelAndView modelAndView = new ModelAndView("user/product");
        Goods goods = goodsService.selectGoodsDetailById(id);
        List<Goods> goodsList = goodsService.randomGoods(4);
        int pageSize = 5;
        PageInfo<UserComment> pageInfo = goodsService.selectUserComment(id,page,pageSize);
        LOGGER.info("用户评论:{}",pageInfo.getList());
        modelAndView.addObject("pageInfo",pageInfo);
        modelAndView.addObject("goodsList",goodsList);
        modelAndView.addObject("goods",goods);
        modelAndView.addObject("levelName",LEVEL_NAME);
        return modelAndView;
    }

    /**
     * 删除商品
     * @param id 商品id
     * @return 删除成功或失败信息
     */
    @PostMapping(value = "/deleteGoods")
    @ResponseBody
    @AdminRequired
    public Result<String> deleteGoods(Integer id){
        if (id==null){
            return Result.fail("删除失败");
        }
        int status = 2;
        boolean success = goodsService.updateStatus(id,status);
        return success?Result.success("修改成功"):Result.fail("修改失败");
    }

    /**
     * roll奖品页面
     * @return 商品信息
     */
    @AccountRequired
    @GetMapping(value = "/roll")
    public ModelAndView rollPrice(){
        ModelAndView modelAndView = new ModelAndView("user/roll");
        List<Goods> goodsInRedis = (List<Goods>)redisUtil.get(RANDOM_GOODS);
        int status = 3;
        List<PriceUser> priceUserList = tickerService.selectPriceUser(status);
        LOGGER.info("已中奖的用户:{}",priceUserList);
        int number = 0;
        if (goodsInRedis==null){
            goodsInRedis = randomGoods();
            redisUtil.set(RANDOM_GOODS,goodsInRedis,1800L);
        }
        LOGGER.info("随机生成的商品为:{}",goodsInRedis);
        for (Goods goods:goodsInRedis){
            modelAndView.addObject("goods_" + number++,goods);
        }
        modelAndView.addObject("priceUserList",priceUserList);
        LOGGER.info("redis中用于抽奖的商品为:{}",goodsInRedis);
        LOGGER.info("redis中用于抽奖的商品数量为:{}",goodsInRedis.size());

        return modelAndView;
    }

    /**
     * 随机生成12个用于抽奖的饰品
     * @return 商品集合
     */
    public List<Goods> randomGoods() {
        Goods emptyGoods = new Goods();
        emptyGoods.setImage("/static/images/roll/thanks.png");
        emptyGoods.setId(0);
        int[] empty = new int[]{(int) (Math.random() * 6), (int) (Math.random() * 6 + 6)};
        //存放商品id
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
        return goodsList;
    }










}