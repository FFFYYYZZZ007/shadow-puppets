package top.fuyuaaa.shadowpuppets.service;

import top.fuyuaaa.shadowpuppets.model.bo.GoodsOrderBO;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-14 23:32
 */
public interface GoodsOrderService {
    GoodsOrderBO addNewGoodsOrder(GoodsOrderBO goodsOrderBO);

    GoodsOrderBO getById(Integer orderId);
}
