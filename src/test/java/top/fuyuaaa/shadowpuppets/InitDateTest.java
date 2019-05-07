package top.fuyuaaa.shadowpuppets;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import top.fuyuaaa.shadowpuppets.common.Constant;
import top.fuyuaaa.shadowpuppets.common.enums.CourseOrderStatusEnum;
import top.fuyuaaa.shadowpuppets.common.enums.ExpressDeliveryStatusEnum;
import top.fuyuaaa.shadowpuppets.common.utils.DateUtils;
import top.fuyuaaa.shadowpuppets.common.utils.EncodeUtils;
import top.fuyuaaa.shadowpuppets.dao.*;
import top.fuyuaaa.shadowpuppets.common.holders.LoginUserHolder;
import top.fuyuaaa.shadowpuppets.mapstruct.UserConverter;
import top.fuyuaaa.shadowpuppets.model.LoginUserInfo;
import top.fuyuaaa.shadowpuppets.model.bo.GoodsOrderBO;
import top.fuyuaaa.shadowpuppets.model.bo.GoodsOrderSimpleBO;
import top.fuyuaaa.shadowpuppets.model.po.*;
import top.fuyuaaa.shadowpuppets.model.qo.*;
import top.fuyuaaa.shadowpuppets.service.*;
import top.fuyuaaa.shadowpuppets.common.utils.init.CommentGenerateUtils;
import top.fuyuaaa.shadowpuppets.common.utils.init.Random7788Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-16 20:42
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class InitDateTest {

    @Autowired
    CategoryDao categoryDao;
    @Autowired
    UserDao userDao;
    @Autowired
    GoodsOrderDao goodsOrderDao;
    @Autowired
    GoodsDao goodsDao;
    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     * 初始化数据，生成100个用户
     */
    @Test
    public void insertUser() {
        for (int i = 0; i < 30; i++) {
            UserPO userPO = new UserPO();
            userPO.setUserName(Random7788Utils.randomName(true, 2));
            userPO.setPassword(EncodeUtils.encode("00000000"));
            userPO.setBirthday(new Date());
            userPO.setTel(Random7788Utils.getTel());
            userPO.setSex(i % 2 == 0 ? 1 : 0);
            userDao.insert(userPO);
        }
        for (int i = 0; i < 30; i++) {
            UserPO userPO = new UserPO();
            userPO.setUserName(Random7788Utils.randomName(true, 3));
            userPO.setPassword(EncodeUtils.encode("00000000"));
            userPO.setBirthday(new Date());
            userPO.setTel(Random7788Utils.getTel());
            userPO.setSex(i % 2 == 0 ? 1 : 0);
            userDao.insert(userPO);
        }
        for (int i = 0; i < 20; i++) {
            UserPO userPO = new UserPO();
            userPO.setUserName(Random7788Utils.randomName(false, 4));
            userPO.setPassword(EncodeUtils.encode("00000000"));
            userPO.setBirthday(new Date());
            userPO.setTel(Random7788Utils.getTel());
            userPO.setSex(i % 2 == 0 ? 1 : 0);
            userDao.insert(userPO);
        }
        for (int i = 0; i < 20; i++) {
            UserPO userPO = new UserPO();
            userPO.setUserName(Random7788Utils.randomName(false, 3));
            userPO.setPassword(EncodeUtils.encode("00000000"));
            userPO.setBirthday(new Date());
            userPO.setTel(Random7788Utils.getTel());
            userPO.setSex(i % 2 == 0 ? 1 : 0);
            userDao.insert(userPO);
        }
    }

    @Test
    public void buildUserLoginStatus() {
        List<UserPO> userPOList = userDao.getUserListByQO(new UserListQO());
        userPOList.forEach(userPO -> {
            int num = Random7788Utils.getNum(1, 100);
            if (num % 2 == 0) {
                String token = Constant.User.TOKEN_REDIS_PREFIX + userPO.getTel();
                if (StringUtils.isEmpty((redisTemplate.opsForValue().get(token)))) {
                    redisTemplate.opsForValue().set(token, String.valueOf(System.currentTimeMillis()), Constant.User.TOKEN_EXPIRE_TIME, TimeUnit.DAYS);
                } else{
                    redisTemplate.expire(token, Constant.User.TOKEN_EXPIRE_TIME, TimeUnit.DAYS);
                }
            }
        });
    }

    @Autowired
    GoodsOrderService goodsOrderService;
    @Autowired
    ExpressDeliveryService expressDeliveryService;
    @Autowired
    GoodsCommentService goodsCommentService;

    /**
     * 初始化100个订单
     */
    @Test
    public void insertGoodsOrder() {
        List<UserPO> userPOList = userDao.getUserListByQO(new UserListQO());
        List<GoodsPO> goodsPOList = goodsDao.findList(new GoodsListQO());
        for (int i = 0; i < 100; i++) {
            GoodsOrderBO goodsOrderBO = new GoodsOrderBO();

            Integer userId = userPOList.get(Random7788Utils.getNum(userPOList.size() - 1)).getId();
            UserPO userPO = userDao.getById(userId);
            LoginUserInfo loginUserInfo = UserConverter.INSTANCE.toLoginUserInfo(userPO);
            LoginUserHolder.instance().put(loginUserInfo);
            goodsOrderBO.setUserId(userId);

            GoodsOrderSimpleBO goodsOrderSimpleBO = new GoodsOrderSimpleBO();
            goodsOrderSimpleBO.setNum(1);
            goodsOrderSimpleBO.setGoodsId(goodsPOList.get(Random7788Utils.getNum(goodsPOList.size() - 1)).getId());
            List<GoodsOrderSimpleBO> simpleBOList = new ArrayList<>();
            simpleBOList.add(goodsOrderSimpleBO);
            goodsOrderBO.setGoodsOrderSimpleBOList(simpleBOList);
            String orderId = goodsOrderService.addNewGoodsOrder(goodsOrderBO).getId();
            //关闭订单
            if (i % 9 == 0) {
                goodsOrderService.cancelGoodsOrderById(orderId);
                continue;
            }
            if (i % 3 != 0) {
                //订单发货，模拟付款成功
                goodsOrderService.ship(orderId, 15.0);
                if (i % 4 != 0) {
                    int carrier = Random7788Utils.getNum(1, 4);
                    String expressCode = expressDeliveryService.codeGenerate(carrier);
                    ShipQO shipQO = new ShipQO();
                    shipQO.setOrderId(orderId);
                    shipQO.setExpressCarrier(carrier);
                    shipQO.setExpressCode(expressCode);
                    //物流发货
                    expressDeliveryService.ship(shipQO);
                    if (i % 5 != 0) {
                        //物流送达
                        expressDeliveryService.updateExpressDeliveryStatus(orderId, ExpressDeliveryStatusEnum.IS_ARRIVED.code());
                        if (i % 6 != 0) {
                            //确认送达
                            goodsOrderService.confirmReceipt(orderId);
                            if (i % 7 != 0) {
                                //添加评论
                                OrderCommentQO orderCommentQO = new OrderCommentQO();
                                orderCommentQO.setOrderId(orderId);
                                orderCommentQO.setContent(CommentGenerateUtils.generateComment());
                                orderCommentQO.setStarLevel(Random7788Utils.getNum(4, 5));
                                goodsCommentService.addComment(orderCommentQO);
                            }
                        }
                    }
                }
            }
            LoginUserHolder.clear();
        }
    }


    @Autowired
    CourseDao courseDao;
    @Autowired
    CourseOrderService courseOrderService;
    @Autowired
    CourseOrderDao courseOrderDao;
    @Autowired
    CourseCommentService courseCommentService;

    /**
     * 初始化100个课程订单
     */
    @Test
    public void insertCourseOrder() {
        List<UserPO> userPOList = userDao.getUserListByQO(new UserListQO());
        List<CoursePO> coursePOList = courseDao.findList(new CourseQO());
        for (int i = 0; i < 100; i++) {
            Integer userId = userPOList.get(Random7788Utils.getNum(userPOList.size() - 1)).getId();
            UserPO userPO = userDao.getById(userId);
            LoginUserInfo loginUserInfo = UserConverter.INSTANCE.toLoginUserInfo(userPO);
            LoginUserHolder.instance().put(loginUserInfo);

            CourseOrderPO courseOrderPO = new CourseOrderPO();
            courseOrderPO.setCourseId(coursePOList.get(Random7788Utils.getNum(coursePOList.size() - 1)).getId());
            courseOrderPO.setDealPrice(courseDao.findById(courseOrderPO.getCourseId()).getCourseDiscountPrice());

            String courseOrderId = courseOrderService.add(courseOrderPO);

            if (i % 9 == 0) {
                courseOrderService.updateStatus(courseOrderId, CourseOrderStatusEnum.CLOSED.code());
                continue;
            }

            if (i % 3 != 0) {
                //变更为支付状态，付款人数+1
                courseOrderPO.setId(courseOrderId);
                courseOrderPO.setCourseOrderStatus(CourseOrderStatusEnum.PENDING_STUDY);
                courseOrderDao.updateStatus(courseOrderPO);
                courseDao.paidNumberAdd(courseOrderPO.getCourseId());

                if (i % 4 != 0) {
                    //结课, 确认结课
                    courseOrderService.updateStatus(courseOrderId, CourseOrderStatusEnum.PENDING_CONFIRMED.code());
                    courseOrderService.updateStatus(courseOrderId, CourseOrderStatusEnum.PENGDING_COMMENT.code());
                    if (i % 5 != 0) {
                        //评论
                        OrderCommentQO orderCommentQO = new OrderCommentQO();
                        orderCommentQO.setOrderId(courseOrderId);
                        orderCommentQO.setContent(CommentGenerateUtils.generateComment());
                        orderCommentQO.setStarLevel(Random7788Utils.getNum(4, 5));
                        courseCommentService.addComment(orderCommentQO);
                    }
                }
            }
        }
    }

    /**
     * reset order create DATE
     */
    @Test
    public void resetOrderDateCreate() {
        List<GoodsOrderPO> goodsOrderPOList = goodsOrderDao.getOrderList(new GoodsOrderQO());
        GoodsOrderPO goodsOrderPO = null;
        for (int i = 0; i < goodsOrderPOList.size(); i++) {
            Date date = DateUtils.get(-(Random7788Utils.getNum(1, 30)));
            goodsOrderPO = goodsOrderPOList.get(i);
            String goodsOrderPOId = goodsOrderPO.getId();
            goodsOrderDao.updateDateCreate(goodsOrderPOId, date);
        }
    }
}
