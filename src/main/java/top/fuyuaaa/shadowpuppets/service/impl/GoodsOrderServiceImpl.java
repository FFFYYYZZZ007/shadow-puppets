package top.fuyuaaa.shadowpuppets.service.impl;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.fuyuaaa.shadowpuppets.common.enums.ExpressDeliveryStatusEnum;
import top.fuyuaaa.shadowpuppets.dao.*;
import top.fuyuaaa.shadowpuppets.common.enums.OrderStatusEnum;
import top.fuyuaaa.shadowpuppets.model.PageVO;
import top.fuyuaaa.shadowpuppets.model.bo.GoodsOrderBO;
import top.fuyuaaa.shadowpuppets.model.bo.GoodsOrderSimpleBO;
import top.fuyuaaa.shadowpuppets.model.po.ExpressDeliveryPO;
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
import top.fuyuaaa.shadowpuppets.util.UUIDUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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
    ExpressDeliveryDao expressDeliveryDao;
    @Autowired
    GoodsService goodsService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GoodsOrderBO addNewGoodsOrder(GoodsOrderBO goodsOrderBO) {
        GoodsOrderPO goodsOrderPO = BeanUtils.copyProperties(goodsOrderBO, GoodsOrderPO.class);
        goodsOrderPO.setId(UUIDUtils.getOrderCode());
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
        return BeanUtils.copyProperties(goodsOrderPO, GoodsOrderBO.class);
    }

    @Override
    public GoodsOrderBO getById(String orderId) {
        GoodsOrderPO goodsOrderPO = goodsOrderDao.getById(orderId);
        GoodsOrderBO goodsOrderBO = BeanUtils.copyProperties(goodsOrderPO, GoodsOrderBO.class);
        setGoodsOrderBO(goodsOrderBO, goodsOrderPO);
        return goodsOrderBO;
    }

    @Override
    public GoodsOrderVO getOrderVOById(String orderId) {
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
    public Boolean cancelGoodsOrderById(String orderId) {
        return goodsOrderDao.cancelGoodsOrder(orderId) > 0;
    }

    @Override
    public Boolean confirmReceipt(String orderId) {
        return goodsOrderDao.updateOrderStatus(OrderStatusEnum.PENDING_COMMENT.code(), orderId)==1;
    }

    @Override
    public String getAliPayUrl(String orderId) {
        GoodsOrderVO orderVO = this.getOrderVOById(orderId);
        String aliPayUrl = AlipayUtil.getAliPayUrl(orderVO);
        return aliPayUrl;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean checkOrderPaidAndUpdateOrderStatus(String orderId) {
        GoodsOrderVO goodsOrderVO = this.getOrderVOById(orderId);
        //去查找是否已支付
        if (!AlipayUtil.checkTradeStatus(String.valueOf(orderId))) {
            return false;
        }
        //如果已支付，更改状态为待发货
        goodsOrderDao.updateOrderStatus(OrderStatusEnum.PENDING_DELIVERY.code(), goodsOrderVO.getId());
        //添加物流信息，单号为空，状态为待发货
        ExpressDeliveryPO po = new ExpressDeliveryPO();
        po.setOrderId(orderId);
        po.setDeliveryStatus(ExpressDeliveryStatusEnum.UN_DELIVERY);
        po.setExpressPrice(goodsOrderVO.getExpressFee());
        expressDeliveryDao.insert(po);
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
        price = Double.valueOf(new DecimalFormat(".##").format(price));
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

    private void insertOrderInfo(String orderId, List<GoodsOrderSimpleBO> goodsOrderSimpleBOList) {
        GoodsOrderInfoPO goodsOrderInfoPO;
        for (GoodsOrderSimpleBO goodsOrderSimpleBO : goodsOrderSimpleBOList) {
            goodsOrderInfoPO = new GoodsOrderInfoPO();
            goodsOrderInfoPO.setGoodsOrderId(orderId);
            goodsOrderInfoPO.setGoodsId(goodsOrderSimpleBO.getGoodsId());
            goodsOrderInfoPO.setNum(goodsOrderSimpleBO.getNum());
            goodsOrderDao.insertGoodsOrderInfo(goodsOrderInfoPO);
            //减库存
            goodsDao.reduceStock(goodsOrderSimpleBO.getGoodsId());
        }
    }

    private void insertOrderInfo4Cart(String orderId, List<Integer> shoppingCartIdList) {
        ShoppingCartPO shoppingCartPO;
        GoodsOrderInfoPO goodsOrderInfoPO;
        for (Integer id : shoppingCartIdList) {
            goodsOrderInfoPO = new GoodsOrderInfoPO();
            goodsOrderInfoPO.setGoodsOrderId(orderId);
            shoppingCartPO = shoppingCartDao.getById(id);
            goodsOrderInfoPO.setGoodsId(shoppingCartPO.getGoodsId());
            goodsOrderInfoPO.setNum(shoppingCartPO.getNum());
            goodsOrderDao.insertGoodsOrderInfo(goodsOrderInfoPO);
            goodsDao.reduceStock(shoppingCartPO.getGoodsId());
        }
    }

    private void setGoodsOrderBO(GoodsOrderBO goodsOrderBO, GoodsOrderPO goodsOrderPO) {
        //设置枚举类
        goodsOrderBO.setStatus(OrderStatusEnum.find(goodsOrderPO.getStatus()));
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

        //设置时间
        goodsOrderVO.setDateCreate(DateFormatUtils.format(goodsOrderBO.getDateCreate(), "yyyy-MM-dd HH:mm:ss"));
        goodsOrderVO.setDateUpdate(DateFormatUtils.format(goodsOrderBO.getDateUpdate(), "yyyy-MM-dd HH:mm:ss"));
        return goodsOrderVO;
    }

}
