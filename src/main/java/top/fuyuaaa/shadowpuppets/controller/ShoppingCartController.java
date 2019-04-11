package top.fuyuaaa.shadowpuppets.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import top.fuyuaaa.shadowpuppets.common.Result;
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
    public Result<String> addShoppingCart(@RequestParam Integer goodsId,
                                          @RequestParam Integer num) {
        Integer userId = LoginUserHolder.instance().get().getId();
        if (validateShoppingCartParams(userId, goodsId)) {
            return Result.fail("参数缺失");
        }
        num = null == num || num <= 0 ? 1 : num;

        ShoppingCartBO shoppingCartBO = new ShoppingCartBO(userId, goodsId, num);
        Integer addResult = shoppingCartService.addShoppingCart(shoppingCartBO);
        if (null == addResult || addResult <= 0) {
            return Result.fail("加入购物车失败，请重试！");
        }
        return Result.success("加入购物车成功！");
    }

    @GetMapping("/list")
    public Result<List<ShoppingCartVO>> getShoppingCartList(@RequestParam(defaultValue = "1") Integer page,
                                                            @RequestParam(defaultValue = "10") Integer pageSize) {
        Integer userId = LoginUserHolder.instance().get().getId();
        List<ShoppingCartBO> shoppingCartBOList = shoppingCartService.getShoppingCartList(userId, page, pageSize);
        List<ShoppingCartVO> shoppingCartVOList = convertShoppingCartBO2VO(shoppingCartBOList);
        return Result.success(shoppingCartVOList);
    }

    /**
     * 将BO转成VO
     * @param shoppingCartBOList 购物车列表
     * @return
     */
    private List<ShoppingCartVO> convertShoppingCartBO2VO(List<ShoppingCartBO> shoppingCartBOList) {
        return shoppingCartBOList.stream()
                .map(shoppingCartBO -> {
                    ShoppingCartVO shoppingCartVO = BeanUtils.copyProperties(shoppingCartBO, ShoppingCartVO.class);
                    GoodsBO goodsBO = goodsService.getGoodsDetailsById(shoppingCartBO.getGoodsId());
                    shoppingCartVO.setKey(shoppingCartBO.getId());
                    shoppingCartVO.setGoodsName(goodsBO.getGoodsName());
                    shoppingCartVO.setPrice(goodsBO.getPrice());
                    return shoppingCartVO;
                }).collect(Collectors.toList());
    }

    @PostMapping("/delete")
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
     * @param userId  用户id
     * @param goodsId 商品id
     * @return 参数是否符合
     */
    private boolean validateShoppingCartParams(Integer userId, Integer goodsId) {
        return null != userId && null != goodsId;
    }
}
