package top.fuyuaaa.shadowpuppets.controller;

import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.fuyuaaa.shadowpuppets.common.Result;
import top.fuyuaaa.shadowpuppets.enums.DeliveryOrderStatusEnum;
import top.fuyuaaa.shadowpuppets.enums.OrderStatusEnum;
import top.fuyuaaa.shadowpuppets.holder.LoginUserHolder;
import top.fuyuaaa.shadowpuppets.model.PageVO;
import top.fuyuaaa.shadowpuppets.model.bo.GoodsOrderBO;
import top.fuyuaaa.shadowpuppets.model.qo.GoodsOrderQO;
import top.fuyuaaa.shadowpuppets.model.vo.GoodsOrderVO;
import top.fuyuaaa.shadowpuppets.service.GoodsOrderService;
import top.fuyuaaa.shadowpuppets.service.GoodsService;
import top.fuyuaaa.shadowpuppets.service.ShoppingCartService;


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

    @Autowired
    GoodsService goodsService;

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

    @PostMapping("/pay")
    public Result<String> payGoodsOrder(@RequestParam Integer orderId){
        //TODO校验参数
        goodsOrderService.payGoodsOrder(orderId);
        return Result.success("").setMsg("支付成功");
    }

    /**
     * TODO 校验参数
     */
    private Boolean validateOrder(GoodsOrderBO goodsOrderBO) {
        return true;
    }

    @PostMapping("/one")
    public Result<GoodsOrderVO> getGoodsOrder(@RequestParam Integer orderId) {
        GoodsOrderVO goodsOrderVO = goodsOrderService.getOrderVOById(orderId);
        return Result.success(goodsOrderVO);
    }

    @GetMapping("/user/list")
    public Result<PageVO<GoodsOrderVO>> getGoodsOrderListByUser(@RequestParam(defaultValue = "1") Integer page,
                                                              @RequestParam(defaultValue = "5") Integer pageSize,
                                                              @RequestParam(defaultValue = "-1") Integer orderStatus) {

        Integer userId = LoginUserHolder.instance().get().getId();
        GoodsOrderQO goodsOrderQO = new GoodsOrderQO();
        goodsOrderQO.setUserId(userId);
        goodsOrderQO.setPageNum(page);
        goodsOrderQO.setPageSize(pageSize);
        goodsOrderQO.setStatus(orderStatus);
        PageHelper.startPage(page, pageSize);
        PageVO<GoodsOrderVO> pageVO = goodsOrderService.getOrderVOList(goodsOrderQO);
        return Result.success(pageVO);
    }

    @PostMapping("/user/delete")
    public Result<String> deleteGoodsOrderById(@RequestParam Integer orderId) {
        if (null == orderId || 0 >= orderId || !goodsOrderService.deleteGoodsOrderById(orderId)) {
            return Result.fail("取消订单失败！");
        }
        return Result.success("取消订单成功！", "取消订单成功！");
    }

    //==============================  订单管理  ==============================


    @PostMapping("/manager/list")
    public Result<PageVO<GoodsOrderVO>> getGoodsOrderList(@RequestBody GoodsOrderQO goodsOrderQO) {
        fillGoodsOrderQO(goodsOrderQO);
        PageHelper.startPage(goodsOrderQO.getPageNum(), goodsOrderQO.getPageSize());
        PageVO<GoodsOrderVO> pageVO = goodsOrderService.getOrderVOList(goodsOrderQO);
        return Result.success(pageVO);
    }


    private void fillGoodsOrderQO(GoodsOrderQO goodsOrderQO) {
        if (null == goodsOrderQO.getPageNum()) {
            goodsOrderQO.setPageNum(1);
        }
        if (null == goodsOrderQO.getPageSize()) {
            goodsOrderQO.setPageSize(10);
        }
    }
}
