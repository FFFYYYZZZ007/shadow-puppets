package top.fuyuaaa.shadowpuppets.service.impl;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.fuyuaaa.shadowpuppets.dao.GoodsDao;
import top.fuyuaaa.shadowpuppets.dao.GoodsOrderDao;
import top.fuyuaaa.shadowpuppets.dao.ShoppingCartDao;
import top.fuyuaaa.shadowpuppets.dao.UserDao;
import top.fuyuaaa.shadowpuppets.common.enums.DeliveryOrderStatusEnum;
import top.fuyuaaa.shadowpuppets.common.enums.OrderStatusEnum;
import top.fuyuaaa.shadowpuppets.exceptions.AlipayException;
import top.fuyuaaa.shadowpuppets.model.PageVO;
import top.fuyuaaa.shadowpuppets.model.bo.GoodsOrderBO;
import top.fuyuaaa.shadowpuppets.model.bo.GoodsOrderSimpleBO;
import top.fuyuaaa.shadowpuppets.model.po.GoodsOrderInfoPO;
import top.fuyuaaa.shadowpuppets.model.po.GoodsOrderPO;
import top.fuyuaaa.shadowpuppets.model.po.ShoppingCartPO;
import top.fuyuaaa.shadowpuppets.model.qo.GoodsOrderQO;
import top.fuyuaaa.shadowpuppets.model.vo.GoodsOrderVO;
import top.fuyuaaa.shadowpuppets.model.vo.GoodsVO;
import top.fuyuaaa.shadowpuppets.model.vo.GoodsVOWithNum;
import top.fuyuaaa.shadowpuppets.service.GoodsOrderService;
import top.fuyuaaa.shadowpuppets.service.GoodsService;
import top.fuyuaaa.shadowpuppets.util.AlipayUtil;
import top.fuyuaaa.shadowpuppets.util.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-14 23:52
 */
@Service
public class GoodsOrderServiceImpl implements GoodsOrderService {

    @Autowired
    GoodsDao goodsDao;
    @Autowired
    ShoppingCartDao shoppingCartDao;
    @Autowired
    GoodsOrderDao goodsOrderDao;
    @Autowired
    UserDao userDao;
    @Autowired
    GoodsService goodsService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GoodsOrderBO addNewGoodsOrder(GoodsOrderBO goodsOrderBO) {
        GoodsOrderPO goodsOrderPO = BeanUtils.copyProperties(goodsOrderBO, GoodsOrderPO.class);
        goodsOrderPO.setExpressFee(15.0);
        double price = 0;
        //购物车生成的订单
        if (CollectionUtils.isEmpty(goodsOrderBO.getGoodsOrderSimpleBOList())) {
            //购物车
            List<Integer> shoppingCartIdList = goodsOrderBO.getShoppingCartIdList();
            //计算商品总价
            price = calculateTotalPrice4Cart(shoppingCartIdList);
            goodsOrderPO.setDealPrice(price);
            //插入订单
            goodsOrderDao.insertNewGoodsOrder(goodsOrderPO);
            //插入订单信息
            insertOrderInfo4Cart(goodsOrderPO.getId(), shoppingCartIdList);
            //删除购物车
            shoppingCartIdList.forEach(id -> shoppingCartDao.deleteShoppingCart(id));
        } else {
            //直接购买
            List<GoodsOrderSimpleBO> goodsOrderSimpleBOList = goodsOrderBO.getGoodsOrderSimpleBOList();
            price = calculateTotalPrice(goodsOrderSimpleBOList);
            goodsOrderPO.setDealPrice(price);
            //插入订单
            goodsOrderDao.insertNewGoodsOrder(goodsOrderPO);
            //插入订单信息
            insertOrderInfo(goodsOrderPO.getId(), goodsOrderSimpleBOList);
        }
        //TODO 减库存
        return BeanUtils.copyProperties(goodsOrderPO, GoodsOrderBO.class);
    }

    @Override
    public GoodsOrderBO getById(Integer orderId) {
        GoodsOrderPO goodsOrderPO = goodsOrderDao.getById(orderId);
        GoodsOrderBO goodsOrderBO = BeanUtils.copyProperties(goodsOrderPO, GoodsOrderBO.class);
        setGoodsOrderBO(goodsOrderBO, goodsOrderPO);
        return goodsOrderBO;
    }

    @Override
    public GoodsOrderVO getOrderVOById(Integer orderId) {
        GoodsOrderBO goodsOrderBO = this.getById(orderId);
        return convertOrderBO2VO(goodsOrderBO);
    }

    @Override
    public Integer count(GoodsOrderQO goodsOrderQO) {
        return goodsOrderDao.count(goodsOrderQO);
    }

    @Override
    public List<GoodsOrderBO> getOrderList(GoodsOrderQO goodsOrderQO) {
        List<GoodsOrderPO> goodsOrderPOList = goodsOrderDao.getOrderList(goodsOrderQO);
        return goodsOrderPOList.stream()
                .map(goodsOrderPO -> {
                    GoodsOrderBO goodsOrderBO = BeanUtils.copyProperties(goodsOrderPO, GoodsOrderBO.class);
                    setGoodsOrderBO(goodsOrderBO, goodsOrderPO);
                    return goodsOrderBO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public PageVO<GoodsOrderVO> getOrderVOList(GoodsOrderQO goodsOrderQO) {
        List<GoodsOrderBO> goodsOrderBOList = this.getOrderList(goodsOrderQO);
        List<GoodsOrderVO> goodsOrderVOList =
                goodsOrderBOList.stream()
                        .map(this::convertOrderBO2VO)
                        .collect(Collectors.toList());
        return new PageVO<>(
                goodsOrderQO.getPageNum(),
                goodsOrderQO.getPageSize(),
                this.count(goodsOrderQO),
                goodsOrderVOList);
    }

    @Override
    public Boolean cancelGoodsOrderById(Integer orderId) {
        return goodsOrderDao.cancelGoodsOrder(orderId) > 0 ;
    }

    @Override
    public String getAliPayUrl(Integer orderId) {
        GoodsOrderVO orderVO = this.getOrderVOById(orderId);
        String aliPayUrl = AlipayUtil.getAliPayUrl(orderVO, "http://fuyuaaa.nat300.top/orderInfo");
        return aliPayUrl;
    }

    @Override
    public Boolean checkOrderPaidAndUpdateOrderStatus(Integer orderId) {
        GoodsOrderVO goodsOrderVO = this.getOrderVOById(orderId);
        //去查找是否已支付
        if (!AlipayUtil.checkTradeStatus(String.valueOf(orderId))) {
            throw new AlipayException("支付失败");
        }
        //如果已支付，更改状态为已支付
        goodsOrderDao.updateOrderStatus(OrderStatusEnum.PAID.code(), goodsOrderVO.getId());
        //更改状态为未发货
        goodsOrderDao.updateOrderDeliveryStatus(DeliveryOrderStatusEnum.UN_DELIVERY.code(), goodsOrderVO.getId());
        return true;
    }

    //==============================  private help methods  ==============================

    /**
     * 根据购物车id列表去查询价格并统计出总价
     *
     * @param shoppingCartIdList 购物车id列表
     * @return 总价格
     */
    private double calculateTotalPrice4Cart(List<Integer> shoppingCartIdList) {
        double price = 0;
        ShoppingCartPO shoppingCartPO = null;
        for (Integer id : shoppingCartIdList) {
            shoppingCartPO = shoppingCartDao.getById(id);
            Integer goodsId = shoppingCartPO.getGoodsId();
            price += (goodsDao.findByGoodsId(goodsId).getPrice()) * (shoppingCartPO.getNum());
        }
        return price;
    }

    private double calculateTotalPrice(List<GoodsOrderSimpleBO> goodsOrderSimpleBOList) {
        double price = 0;
        for (GoodsOrderSimpleBO goodsOrderSimpleBO : goodsOrderSimpleBOList) {
            price += goodsDao.findByGoodsId(goodsOrderSimpleBO.getGoodsId()).getPrice()
                    * goodsOrderSimpleBO.getNum();
        }
        return price;
    }

    private void insertOrderInfo(Integer orderId, List<GoodsOrderSimpleBO> goodsOrderSimpleBOList) {
        GoodsOrderInfoPO goodsOrderInfoPO;
        for (GoodsOrderSimpleBO goodsOrderSimpleBO : goodsOrderSimpleBOList) {
            goodsOrderInfoPO = new GoodsOrderInfoPO();
            goodsOrderInfoPO.setGoodsOrderId(orderId);
            goodsOrderInfoPO.setGoodsId(goodsOrderSimpleBO.getGoodsId());
            goodsOrderInfoPO.setNum(goodsOrderSimpleBO.getNum());
            goodsOrderDao.insertGoodsOrderInfo(goodsOrderInfoPO);
        }
    }

    private void insertOrderInfo4Cart(Integer orderId, List<Integer> shoppingCartIdList) {
        ShoppingCartPO shoppingCartPO;
        GoodsOrderInfoPO goodsOrderInfoPO;
        for (Integer id : shoppingCartIdList) {
            goodsOrderInfoPO = new GoodsOrderInfoPO();
            goodsOrderInfoPO.setGoodsOrderId(orderId);
            shoppingCartPO = shoppingCartDao.getById(id);
            goodsOrderInfoPO.setGoodsId(shoppingCartPO.getGoodsId());
            goodsOrderInfoPO.setNum(shoppingCartPO.getNum());
            goodsOrderDao.insertGoodsOrderInfo(goodsOrderInfoPO);
        }
    }

    private void setGoodsOrderBO(GoodsOrderBO goodsOrderBO, GoodsOrderPO goodsOrderPO) {
        //设置枚举类
        goodsOrderBO.setStatus(OrderStatusEnum.find(goodsOrderPO.getStatus()));
        goodsOrderBO.setDeliveryStatus(DeliveryOrderStatusEnum.find(goodsOrderPO.getDeliveryStatus()));
    }

    private GoodsOrderVO convertOrderBO2VO(GoodsOrderBO goodsOrderBO) {
        GoodsOrderVO goodsOrderVO = BeanUtils.copyProperties(goodsOrderBO, GoodsOrderVO.class);
        List<GoodsOrderInfoPO> goodsOrderInfoPOList = goodsOrderDao.getOrderInfoByOrderId(goodsOrderBO.getId());

        List<GoodsVOWithNum> goodsVOWithNumList = goodsOrderInfoPOList.stream()
                .map(goodsOrderInfoPO -> {
                    GoodsVO goodsVOById = goodsService.getGoodsVOById(goodsOrderInfoPO.getGoodsId());
                    GoodsVOWithNum goodsVOWithNum = BeanUtils.copyProperties(goodsVOById, GoodsVOWithNum.class);
                    goodsVOWithNum.setNum(goodsOrderInfoPO.getNum());
                    return goodsVOWithNum;
                })
                .collect(Collectors.toList());
        goodsOrderVO.setGoodsVOList(goodsVOWithNumList);

        //设置用户名
        goodsOrderVO.setUserName(userDao.getById(goodsOrderBO.getUserId()).getUserName());

        //枚举类转成String
        goodsOrderVO.setStatus(goodsOrderBO.getStatus().desc());
        goodsOrderVO.setDeliveryStatus(goodsOrderBO.getDeliveryStatus().desc());

        //设置时间
        goodsOrderVO.setDateCreate(DateFormatUtils.format(goodsOrderBO.getDateCreate(), "yyyy-MM-dd HH:mm:ss"));
        goodsOrderVO.setDateUpdate(DateFormatUtils.format(goodsOrderBO.getDateUpdate(), "yyyy-MM-dd HH:mm:ss"));
        return goodsOrderVO;
    }

}
