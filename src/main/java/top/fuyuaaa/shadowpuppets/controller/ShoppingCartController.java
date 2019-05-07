package top.fuyuaaa.shadowpuppets.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.fuyuaaa.shadowpuppets.common.annotations.NeedLogin;
import top.fuyuaaa.shadowpuppets.common.Result;
import top.fuyuaaa.shadowpuppets.common.holders.LoginUserHolder;
import top.fuyuaaa.shadowpuppets.model.bo.ShoppingCartBO;
import top.fuyuaaa.shadowpuppets.model.vo.ShoppingCartVO;
import top.fuyuaaa.shadowpuppets.service.GoodsService;
import top.fuyuaaa.shadowpuppets.service.ShoppingCartService;

import java.util.List;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-11 00:10
 */
@RestController
@RequestMapping("/cart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    ShoppingCartService shoppingCartService;

    @Autowired
    GoodsService goodsService;

    @PostMapping("/add")
    @NeedLogin
    public Result<String> addShoppingCart(@RequestBody ShoppingCartBO shoppingCartBO) {
        shoppingCartService.addShoppingCart(shoppingCartBO);
        return Result.success("success", "加入购物车成功！");
    }

    @GetMapping("/list")
    @NeedLogin
    public Result<List<ShoppingCartVO>> getShoppingCartList(@RequestParam(defaultValue = "1") Integer page,
                                                            @RequestParam(defaultValue = "10") Integer pageSize) {
        Integer userId = LoginUserHolder.instance().get().getId();
        List<ShoppingCartVO> shoppingCartVOList = shoppingCartService.getShoppingCartVOList(userId, page, pageSize);
        return Result.success(shoppingCartVOList);
    }


    @PostMapping("/delete/all")
    @NeedLogin
    public Result<Boolean> deleteAllShoppingCart() {
        shoppingCartService.deleteAllShoppingCart();
        return Result.success(true).setMsg("清空购物车成功");
    }

    @PostMapping("/delete/one")
    @NeedLogin
    public Result<Boolean> deleteOneShoppingCart(@RequestParam Integer shoppingCartId) {
        shoppingCartService.deleteOneShoppingCart(shoppingCartId);
        return Result.success(true).setMsg("从购物车中移除成功");
    }
}
