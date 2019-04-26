package top.fuyuaaa.shadowpuppets.service;

import top.fuyuaaa.shadowpuppets.common.enums.ExpressDeliveryStatusEnum;
import top.fuyuaaa.shadowpuppets.model.PageVO;
import top.fuyuaaa.shadowpuppets.model.qo.ExpressDeliveryQO;
import top.fuyuaaa.shadowpuppets.model.vo.ExpressDeliveryVO;

import java.util.List;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-26 16:10
 */
public interface ExpressDeliveryService {

    Boolean updateExpressDeliveryStatus(String orderId, ExpressDeliveryStatusEnum expressDeliveryStatusEnum);

    PageVO<ExpressDeliveryVO> getList(ExpressDeliveryQO expressDeliveryQO);
}
