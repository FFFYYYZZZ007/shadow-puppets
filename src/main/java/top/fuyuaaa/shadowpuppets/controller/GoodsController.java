package top.fuyuaaa.shadowpuppets.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.fuyuaaa.shadowpuppets.common.Result;
import top.fuyuaaa.shadowpuppets.common.enums.ExEnum;
import top.fuyuaaa.shadowpuppets.exceptions.ParamException;
import top.fuyuaaa.shadowpuppets.model.PageVO;
import top.fuyuaaa.shadowpuppets.model.qo.GoodsListQO;
import top.fuyuaaa.shadowpuppets.model.vo.GoodsVO;
import top.fuyuaaa.shadowpuppets.service.CategoryService;
import top.fuyuaaa.shadowpuppets.service.GoodsService;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-08 22:44
 */
@RestController
@RequestMapping("/goods")
@Slf4j
public class GoodsController {

    @Autowired
    GoodsService goodsService;
    @Autowired
    CategoryService categoryService;

    @PostMapping("/list")
    public Result<PageVO<GoodsVO>> getGoodsList(@RequestBody GoodsListQO goodsListQO) {
        //只能查看在售的
        goodsListQO.setOnlySeeOnSale(1);
        PageVO<GoodsVO> goodsPageVO = goodsService.getGoodsPageVO(goodsListQO);
        return Result.success(goodsPageVO);
    }

    @GetMapping("/one")
    public Result<GoodsVO> getGoodsDetailsById(@RequestParam Integer goodsId) {
        if (null == goodsId) {
            throw new ParamException(ExEnum.PARAM_ERROR.getMsg());
        }
        return Result.success(goodsService.getGoodsVOById(goodsId));
    }

}
