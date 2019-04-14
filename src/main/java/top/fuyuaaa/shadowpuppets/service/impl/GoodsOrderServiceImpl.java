package top.fuyuaaa.shadowpuppets.service.impl;

import com.google.common.base.Joiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.fuyuaaa.shadowpuppets.dao.GoodsDao;
import top.fuyuaaa.shadowpuppets.dao.GoodsOrderDao;
import top.fuyuaaa.shadowpuppets.dao.ShoppingCartDao;
import top.fuyuaaa.shadowpuppets.model.bo.GoodsOrderBO;
import top.fuyuaaa.shadowpuppets.model.po.GoodsOrderPO;
import top.fuyuaaa.shadowpuppets.model.po.ShoppingCartPO;
import top.fuyuaaa.shadowpuppets.service.GoodsOrderService;
import top.fuyuaaa.shadowpuppets.service.GoodsService;
import top.fuyuaaa.shadowpuppets.util.BeanUtils;

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

    @Override
    public GoodsOrderBO addNewGoodsOrder(GoodsOrderBO goodsOrderBO) {
        goodsOrderBO.setExpressFee(15.0);
        double price = calculateTotalPrice(goodsOrderBO.getShoppingCartIdList());
        goodsOrderBO.setDealPrice(price);
        GoodsOrderPO goodsOrderPO = BeanUtils.copyProperties(goodsOrderBO, GoodsOrderPO.class);
        goodsOrderPO.setGoodsIds(Joiner.on(",").join(goodsOrderBO.getShoppingCartIdList()));
        goodsOrderDao.insertNewGoodsOrder(goodsOrderPO);
        return BeanUtils.copyProperties(goodsOrderPO, GoodsOrderBO.class);
    }

    private double calculateTotalPrice(List<Integer> shoppingCartIdList) {
        double price = 0;
        for (Integer id : shoppingCartIdList) {
            ShoppingCartPO shoppingCartPO = shoppingCartDao.getById(id);
            Integer goodsId = shoppingCartPO.getGoodsId();
            price += goodsDao.findByGoodsId(goodsId).getPrice();
        }
        return price;
    }

    @Override
    public GoodsOrderBO getById(Integer orderId) {
        GoodsOrderPO goodsOrderPO = goodsOrderDao.getById(orderId);
        return BeanUtils.copyProperties(goodsOrderPO, GoodsOrderBO.class);
    }
}
