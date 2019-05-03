package top.fuyuaaa.shadowpuppets.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.fuyuaaa.shadowpuppets.common.Result;
import top.fuyuaaa.shadowpuppets.common.enums.ExEnum;
import top.fuyuaaa.shadowpuppets.common.enums.ExpressDeliveryStatusEnum;
import top.fuyuaaa.shadowpuppets.exceptions.ExpressDeliveryException;
import top.fuyuaaa.shadowpuppets.exceptions.ParamException;
import top.fuyuaaa.shadowpuppets.model.PageVO;
import top.fuyuaaa.shadowpuppets.model.qo.ExpressDeliveryQO;
import top.fuyuaaa.shadowpuppets.model.qo.ShipQO;
import top.fuyuaaa.shadowpuppets.model.vo.ExpressDeliveryVO;
import top.fuyuaaa.shadowpuppets.service.ExpressDeliveryService;
import top.fuyuaaa.shadowpuppets.service.GoodsOrderService;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-25 20:01
 */
@RestController
@RequestMapping("/delivery/manager")
@Slf4j
public class DeliveryManagerController {

    @Autowired
    GoodsOrderService goodsOrderService;
    @Autowired
    ExpressDeliveryService expressDeliveryService;

    @PostMapping("/ship")
    public Result ship(@RequestBody ShipQO shipQO){
        this.validateShipQO(shipQO);
        expressDeliveryService.ship(shipQO);
        return Result.success().setMsg("发货成功");
    }

    @PostMapping("/status/update")
    public Result updateStatus(@RequestParam String orderId, @RequestParam Integer deliveryStatus) {
        this.validateUpdateParams(orderId, deliveryStatus);
        expressDeliveryService.updateExpressDeliveryStatus(orderId, ExpressDeliveryStatusEnum.find(deliveryStatus));
        return Result.success().setMsg("更新物流状态成功");
    }

    @PostMapping("/list")
    public Result<PageVO<ExpressDeliveryVO>> getList(@RequestBody ExpressDeliveryQO expressDeliveryQO) {
        this.validateExpressDeliveryQO(expressDeliveryQO);
        PageVO<ExpressDeliveryVO> page = expressDeliveryService.getList(expressDeliveryQO);
        return Result.success(page);
    }

    @PostMapping("/one")
    public Result<ExpressDeliveryVO> getByOrderId(@RequestParam String orderId){
        ExpressDeliveryVO deliveryVO =  expressDeliveryService.getByOrderId(orderId);
        return Result.success(deliveryVO);
    }

    private void validateShipQO(ShipQO shipQO) {
        if (null == shipQO || StringUtils.isAnyEmpty(shipQO.getOrderId(),shipQO.getExpressCode())||
                null == shipQO.getExpressCarrier()){
            throw new ExpressDeliveryException(ExEnum.PARAM_ERROR.getMsg());
        }
    }

    private void validateExpressDeliveryQO(ExpressDeliveryQO expressDeliveryQO) {
        if (expressDeliveryQO == null) {
            expressDeliveryQO = new ExpressDeliveryQO();
        }
        if (expressDeliveryQO.getPageNum() == null) {
            expressDeliveryQO.setPageNum(1);
        }
        if (expressDeliveryQO.getPageSize() == null) {
            expressDeliveryQO.setPageSize(10);
        }
    }

    private void validateUpdateParams(String orderId, Integer deliveryStatus) {
        if (StringUtils.isEmpty(orderId) || null == deliveryStatus || 0 > deliveryStatus) {
            throw new ParamException(ExEnum.PARAM_ERROR.getMsg());
        }

    }
}
