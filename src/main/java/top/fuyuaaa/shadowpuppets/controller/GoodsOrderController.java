package top.fuyuaaa.shadowpuppets.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.fuyuaaa.shadowpuppets.common.Result;
import top.fuyuaaa.shadowpuppets.holder.LoginUserHolder;
import top.fuyuaaa.shadowpuppets.model.bo.GoodsOrderBO;
import top.fuyuaaa.shadowpuppets.model.vo.GoodsOrderVO;
import top.fuyuaaa.shadowpuppets.service.GoodsOrderService;
import top.fuyuaaa.shadowpuppets.service.ShoppingCartService;
import top.fuyuaaa.shadowpuppets.util.BeanUtils;

import java.util.List;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-14 23:19
 */
@RestController
@RequestMapping("/goods/order")
@Slf4j
public class GoodsOrderController {

    @Autowired
    GoodsOrderService goodsOrderService;

    @Autowired
    ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public Result<Integer> addGoodsOrder(@RequestBody GoodsOrderBO goodsOrderBO) {
        if (!validateOrder(goodsOrderBO)) {
            return Result.fail("生成订单失败，请稍后重试");
        }
        Integer userId = LoginUserHolder.instance().get().getId();
        goodsOrderBO.setUserId(userId);
        goodsOrderBO = goodsOrderService.addNewGoodsOrder(goodsOrderBO);
        System.out.println(goodsOrderBO);
        log.info(goodsOrderBO.toString());
        if (goodsOrderBO != null) {
            return Result.success(goodsOrderBO.getId());
        }
        return Result.fail("生成订单失败，请稍后重试");
    }

    private Boolean validateOrder(GoodsOrderBO goodsOrderBO) {
        return true;
    }

    @PostMapping("/one")
    public Result<GoodsOrderVO> getGoodsOrder(@RequestParam Integer orderId) {
        GoodsOrderBO goodsOrderBO = goodsOrderService.getById(orderId);
        GoodsOrderVO goodsOrderVO = BeanUtils.copyProperties(goodsOrderBO, GoodsOrderVO.class);
        return Result.success(goodsOrderVO);
    }
}
