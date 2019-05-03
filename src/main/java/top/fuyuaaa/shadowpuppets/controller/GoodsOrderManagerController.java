package top.fuyuaaa.shadowpuppets.controller;

import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.fuyuaaa.shadowpuppets.annotation.ValidateAdmin;
import top.fuyuaaa.shadowpuppets.common.Result;
import top.fuyuaaa.shadowpuppets.model.PageVO;
import top.fuyuaaa.shadowpuppets.model.qo.GoodsOrderQO;
import top.fuyuaaa.shadowpuppets.model.vo.GoodsOrderVO;
import top.fuyuaaa.shadowpuppets.service.GoodsOrderService;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-29 10:22
 */
@RestController
@RequestMapping("/goods/order/manager")
@Slf4j
public class GoodsOrderManagerController {


    @Autowired
    GoodsOrderService goodsOrderService;

    @PostMapping("/list")
    @ValidateAdmin
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
