package top.fuyuaaa.shadowpuppets.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import top.fuyuaaa.shadowpuppets.annotation.NeedLogin;
import top.fuyuaaa.shadowpuppets.common.Result;
import top.fuyuaaa.shadowpuppets.common.enums.ExEnum;
import top.fuyuaaa.shadowpuppets.exceptions.ParamException;
import top.fuyuaaa.shadowpuppets.holder.LoginUserHolder;
import top.fuyuaaa.shadowpuppets.model.bo.GoodsBO;
import top.fuyuaaa.shadowpuppets.model.bo.ShoppingCartBO;
import top.fuyuaaa.shadowpuppets.model.vo.ShoppingCartVO;
import top.fuyuaaa.shadowpuppets.service.GoodsService;
import top.fuyuaaa.shadowpuppets.service.ShoppingCartService;
import top.fuyuaaa.shadowpuppets.util.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

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
        Integer userId = LoginUserHolder.instance().get().getId();
        validateShoppingCartParams(userId, shoppingCartBO);
        shoppingCartBO.setUserId(userId);
        Integer addResult = shoppingCartService.addShoppingCart(shoppingCartBO);
        if (null == addResult || addResult <= 0) {
            return Result.fail("加入购物车失败，请重试！");
        }
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


    @PostMapping("/delete")
    @NeedLogin
    public Result<Boolean> deleteShoppingCartList(@RequestBody List<Integer> ids) {
        if (!shoppingCartService.isOwner(ids)) {
            return Result.fail("删除购物车失败");
        }
        if (CollectionUtils.isEmpty(ids) || shoppingCartService.deleteShoppingCartList(ids)) {
            return Result.success(true);
        }
        return Result.fail("删除失败");
    }

    /**
     * 校验参数
     *
     * @param userId         用户id
     * @param shoppingCartBO 商品
     */
    private void validateShoppingCartParams(Integer userId, ShoppingCartBO shoppingCartBO) {
        if (shoppingCartBO.getNum() == null || shoppingCartBO.getNum() < 1) {
            shoppingCartBO.setNum(1);
        }
        if (null == userId || null == shoppingCartBO.getGoodsId()) {
            throw new ParamException(ExEnum.PARAM_ERROR.getMsg());
        }
    }
}
