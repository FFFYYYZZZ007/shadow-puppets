package top.fuyuaaa.shadowpuppets.service;

import top.fuyuaaa.shadowpuppets.model.PageVO;
import top.fuyuaaa.shadowpuppets.model.bo.GoodsOrderBO;
import top.fuyuaaa.shadowpuppets.model.qo.GoodsOrderQO;
import top.fuyuaaa.shadowpuppets.model.vo.GoodsOrderVO;

import java.util.List;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-14 23:32
 */
public interface GoodsOrderService {

    GoodsOrderBO addNewGoodsOrder(GoodsOrderBO goodsOrderBO);

    GoodsOrderBO getById(Integer orderId);

    GoodsOrderVO getOrderVOById(Integer orderId);

    List<GoodsOrderBO> getOrderList(GoodsOrderQO goodsOrderQO);

    Integer count(GoodsOrderQO goodsOrderQO);

    PageVO<GoodsOrderVO> getOrderVOList(GoodsOrderQO goodsOrderQO);

    Boolean cancelGoodsOrderById(Integer orderId);

    String getAliPayUrl(Integer orderId);

    Boolean checkOrderPaidAndUpdateOrderStatus(Integer orderId);

}
