package top.fuyuaaa.shadowpuppets.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.fuyuaaa.shadowpuppets.common.enums.ExEnum;
import top.fuyuaaa.shadowpuppets.common.enums.ExpressDeliveryStatusEnum;
import top.fuyuaaa.shadowpuppets.dao.*;
import top.fuyuaaa.shadowpuppets.common.enums.OrderStatusEnum;
import top.fuyuaaa.shadowpuppets.common.exceptions.ParamException;
import top.fuyuaaa.shadowpuppets.common.holders.LoginUserHolder;
import top.fuyuaaa.shadowpuppets.mapstruct.GoodsOrderConverter;
import top.fuyuaaa.shadowpuppets.model.PageVO;
import top.fuyuaaa.shadowpuppets.model.bo.GoodsOrderBO;
import top.fuyuaaa.shadowpuppets.model.bo.GoodsOrderSimpleBO;
import top.fuyuaaa.shadowpuppets.model.po.ExpressDeliveryPO;
import top.fuyuaaa.shadowpuppets.model.po.GoodsOrderInfoPO;
import top.fuyuaaa.shadowpuppets.model.po.GoodsOrderPO;
import top.fuyuaaa.shadowpuppets.model.po.ShoppingCartPO;
import top.fuyuaaa.shadowpuppets.model.qo.GoodsOrderQO;
import top.fuyuaaa.shadowpuppets.model.vo.GoodsOrderVO;
import top.fuyuaaa.shadowpuppets.service.GoodsOrderService;
import top.fuyuaaa.shadowpuppets.service.GoodsService;
import top.fuyuaaa.shadowpuppets.common.utils.AlipayUtil;
import top.fuyuaaa.shadowpuppets.common.utils.UUIDUtils;

import java.text.DecimalFormat;
import java.util.List;

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

        validateOrder(goodsOrderBO);
        Integer userId = LoginUserHolder.instance().get().getId();
        goodsOrderBO.setUserId(userId);

        GoodsOrderPO goodsOrderPO = GoodsOrderConverter.INSTANCE.toGoodsOrderPO(goodsOrderBO);
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
        return GoodsOrderConverter.INSTANCE.toGoodsOrderBO(goodsOrderPO);
    }

    @Override
    public GoodsOrderBO getById(String orderId) {
        GoodsOrderPO goodsOrderPO = goodsOrderDao.getById(orderId);
        return GoodsOrderConverter.INSTANCE.toGoodsOrderBO(goodsOrderPO);
    }

    @Override
    public GoodsOrderVO getOrderVOById(String orderId) {
        GoodsOrderBO goodsOrderBO = this.getById(orderId);
        return GoodsOrderConverter.INSTANCE.toGoodsOrderVO(goodsOrderBO);
    }

    @Override
    public Integer count(GoodsOrderQO goodsOrderQO) {
        return goodsOrderDao.count(goodsOrderQO);
    }

    @Override
    public List<GoodsOrderBO> getOrderList(GoodsOrderQO goodsOrderQO) {
        List<GoodsOrderPO> goodsOrderPOList = goodsOrderDao.getOrderList(goodsOrderQO);
        return GoodsOrderConverter.INSTANCE.toGoodsOrderBOList(goodsOrderPOList);
    }

    @Override
    public PageVO<GoodsOrderVO> getOrderVOList(GoodsOrderQO goodsOrderQO) {
        List<GoodsOrderBO> goodsOrderBOList = this.getOrderList(goodsOrderQO);
        List<GoodsOrderVO> goodsOrderVOList = GoodsOrderConverter.INSTANCE.toGoodsOrderVOList(goodsOrderBOList);
        return new PageVO<>(
                goodsOrderQO.getPageNum(),
                goodsOrderQO.getPageSize(),
                this.count(goodsOrderQO),
                goodsOrderVOList);
    }

    @Override
    public Boolean cancelGoodsOrderById(String orderId) {
        return goodsOrderDao.cancelGoodsOrder(orderId,OrderStatusEnum.CLOSED.code()) > 0;
    }

    @Override
    public Boolean confirmReceipt(String orderId) {
        return goodsOrderDao.updateOrderStatus(OrderStatusEnum.PENDING_COMMENT.code(), orderId)==1;
    }

    @Override
    public String getAliPayUrl(String orderId) {
        GoodsOrderVO orderVO = this.getOrderVOById(orderId);
        return AlipayUtil.getAliPayUrl(orderVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean checkOrderPaidAndUpdateOrderStatus(String orderId) {
        GoodsOrderVO goodsOrderVO = this.getOrderVOById(orderId);
        //去查找是否已支付
        if (!AlipayUtil.checkTradeStatus(String.valueOf(orderId))) {
            return false;
        }
        //修改订单=>待发货
        this.ship(orderId, goodsOrderVO.getExpressFee());
        return true;
    }

    @Override
    public void ship(String orderId, Double expressFee) {
        //如果已支付，更改状态为待发货
        goodsOrderDao.updateOrderStatus(OrderStatusEnum.PENDING_DELIVERY.code(), orderId);
        //添加物流信息，单号为空，状态为待发货
        ExpressDeliveryPO po = new ExpressDeliveryPO();
        po.setOrderId(orderId);
        po.setDeliveryStatus(ExpressDeliveryStatusEnum.UN_DELIVERY);
        po.setExpressPrice(expressFee);
        expressDeliveryDao.insert(po);
    }

    //==============================  private help methods  ==============================

    /**
     * 校验参数订单参数
     */
    private void validateOrder(GoodsOrderBO goodsOrderBO) {
        if (CollectionUtils.isEmpty(goodsOrderBO.getGoodsOrderSimpleBOList()) && CollectionUtils.isEmpty(goodsOrderBO.getShoppingCartIdList())) {
            throw new ParamException(ExEnum.ORDER_CREATE_PARAMS_ERROR.getMsg());
        }
    }

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
}
