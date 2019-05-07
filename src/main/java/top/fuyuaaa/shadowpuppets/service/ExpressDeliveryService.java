package top.fuyuaaa.shadowpuppets.service;

import top.fuyuaaa.shadowpuppets.model.PageVO;
import top.fuyuaaa.shadowpuppets.model.qo.ExpressDeliveryQO;
import top.fuyuaaa.shadowpuppets.model.qo.ShipQO;
import top.fuyuaaa.shadowpuppets.model.vo.ExpressDeliveryVO;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-26 16:10
 */
public interface ExpressDeliveryService {

    void updateExpressDeliveryStatus(String orderId, Integer deliveryStatus);

    PageVO<ExpressDeliveryVO> getList(ExpressDeliveryQO expressDeliveryQO);

    void ship(ShipQO shipQO);

    ExpressDeliveryVO getByOrderId(String orderId);

    String codeGenerate(Integer expressCarrier);
}
