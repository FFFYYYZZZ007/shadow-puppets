package top.fuyuaaa.shadowpuppets.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.fuyuaaa.shadowpuppets.annotation.ValidateAdmin;
import top.fuyuaaa.shadowpuppets.common.Result;
import top.fuyuaaa.shadowpuppets.common.enums.ExEnum;
import top.fuyuaaa.shadowpuppets.common.enums.ExpressDeliveryStatusEnum;
import top.fuyuaaa.shadowpuppets.exceptions.DeliveryStatusException;
import top.fuyuaaa.shadowpuppets.exceptions.ExpressDeliveryException;
import top.fuyuaaa.shadowpuppets.exceptions.ParamException;
import top.fuyuaaa.shadowpuppets.model.PageVO;
import top.fuyuaaa.shadowpuppets.model.qo.ExpressDeliveryQO;
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
public class DeliveryController {

    @Autowired
    GoodsOrderService goodsOrderService;
    @Autowired
    ExpressDeliveryService expressDeliveryService;

    @PostMapping("/status/update")
    public Result updateStatus(@RequestParam String orderId, @RequestParam Integer deliveryStatus) {
        this.validateUpdateParams(orderId, deliveryStatus);
        Boolean success = expressDeliveryService.updateExpressDeliveryStatus(orderId, ExpressDeliveryStatusEnum.find(deliveryStatus));
        if (!success){
            throw new ExpressDeliveryException(ExEnum.UPDATE_DELIVERY_STATUS_ERROR.getMsg());
        }
        return Result.success().setMsg("更新物流状态成功");
    }

    @PostMapping("/list")
    public Result<PageVO<ExpressDeliveryVO>> getList(@RequestBody ExpressDeliveryQO expressDeliveryQO) {
        this.validateExpressDeliveryQO(expressDeliveryQO);
        PageVO<ExpressDeliveryVO> page = expressDeliveryService.getList(expressDeliveryQO);
        return Result.success(page);
    }

    private void validateExpressDeliveryQO(ExpressDeliveryQO expressDeliveryQO) {
        if (expressDeliveryQO == null) {
            expressDeliveryQO = new ExpressDeliveryQO();
        }
        if (expressDeliveryQO.getPageNum() == null) {
            expressDeliveryQO.setPageNum(1);
        }
        if (expressDeliveryQO.getPageSize() == null){
            expressDeliveryQO.setPageSize(10);
        }
    }

    private void validateUpdateParams(String orderId, Integer deliveryStatus){
        if (StringUtils.isEmpty(orderId) || null == deliveryStatus || 0 > deliveryStatus) {
            throw new ParamException(ExEnum.PARAM_ERROR.getMsg());
        }
    }
}
