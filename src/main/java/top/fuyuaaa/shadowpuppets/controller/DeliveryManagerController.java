package top.fuyuaaa.shadowpuppets.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.fuyuaaa.shadowpuppets.common.annotations.ValidateAdmin;
import top.fuyuaaa.shadowpuppets.common.Result;
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
    @ValidateAdmin
    public Result ship(@RequestBody ShipQO shipQO){
        expressDeliveryService.ship(shipQO);
        return Result.success().setMsg("发货成功");
    }

    @PostMapping("/status/update")
    @ValidateAdmin
    public Result updateStatus(@RequestParam String orderId, @RequestParam Integer deliveryStatus) {
        expressDeliveryService.updateExpressDeliveryStatus(orderId, deliveryStatus);
        return Result.success().setMsg("更新物流状态成功");
    }

    @PostMapping("/list")
    @ValidateAdmin
    public Result<PageVO<ExpressDeliveryVO>> getList(@RequestBody ExpressDeliveryQO expressDeliveryQO) {
        PageVO<ExpressDeliveryVO> page = expressDeliveryService.getList(expressDeliveryQO);
        return Result.success(page);
    }

    @PostMapping("/one")
    @ValidateAdmin
    public Result<ExpressDeliveryVO> getByOrderId(@RequestParam String orderId){
        ExpressDeliveryVO deliveryVO =  expressDeliveryService.getByOrderId(orderId);
        return Result.success(deliveryVO);
    }

    @PostMapping("/codeGenerate")
    public Result<String> codeGenerate(@RequestParam Integer expressCarrier){
        String expressCode = expressDeliveryService.codeGenerate(expressCarrier);
        return Result.success(expressCode);
    }
}
