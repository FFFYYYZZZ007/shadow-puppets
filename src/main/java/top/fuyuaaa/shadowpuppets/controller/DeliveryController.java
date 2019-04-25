package top.fuyuaaa.shadowpuppets.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.fuyuaaa.shadowpuppets.annotation.ValidateAdmin;
import top.fuyuaaa.shadowpuppets.common.Result;
import top.fuyuaaa.shadowpuppets.common.enums.ExEnum;
import top.fuyuaaa.shadowpuppets.exceptions.DeliveryStatusException;
import top.fuyuaaa.shadowpuppets.service.GoodsOrderService;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-25 20:01
 */
@RestController
@RequestMapping("/delivery")
@Slf4j
public class DeliveryController {

    @Autowired
    GoodsOrderService goodsOrderService;

    @PostMapping("/update")
    @ValidateAdmin
    public Result<Boolean> updateDeliveryStatus(@RequestParam String orderId,@RequestParam Integer deliveryStatus) {
        Boolean result = goodsOrderService.updateDeliveryStatus(orderId, deliveryStatus);
        if (!result) {
            throw new DeliveryStatusException(ExEnum.UPDATE_DELIVERY_STATUS_ERROR.getMsg());
        }
        return Result.success(true).setMsg("修改物流状态成功");
    }

}
