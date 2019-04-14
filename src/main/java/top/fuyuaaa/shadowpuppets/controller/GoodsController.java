package top.fuyuaaa.shadowpuppets.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.fuyuaaa.shadowpuppets.annotation.IgnoreSecurity;
import top.fuyuaaa.shadowpuppets.common.Result;
import top.fuyuaaa.shadowpuppets.model.bo.GoodsBO;
import top.fuyuaaa.shadowpuppets.model.vo.GoodsVO;
import top.fuyuaaa.shadowpuppets.service.CategoryService;
import top.fuyuaaa.shadowpuppets.service.GoodsService;
import top.fuyuaaa.shadowpuppets.util.BeanUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @GetMapping("/recommend/list")
    @IgnoreSecurity
    public Result<List<GoodsVO>> getRecommendList(){
        List<GoodsBO> goodsBOList = goodsService.getList();
        List<GoodsVO> goodsVOList = goodsBOList.stream()
                .map(goodsBO -> {
                    GoodsVO goodsVO = BeanUtils.copyProperties(goodsBO, GoodsVO.class);
                    goodsVO.setCategoryName(categoryService.getCategoryNameById(goodsBO.getId()));
                    return goodsVO;})
                .collect(Collectors.toList());
        return Result.success(goodsVOList);
    }

    @GetMapping("/one")
    @IgnoreSecurity
    public Result<GoodsVO> getGoodsDetailsById(@RequestParam Integer goodsId){
        if (null == goodsId) {
            return Result.fail("参数不能为空");
        }
        GoodsBO goodsBO = goodsService.getGoodsDetailsById(goodsId);
        GoodsVO goodsVO = BeanUtils.copyProperties(goodsBO, GoodsVO.class);
        goodsVO.setCategoryName(categoryService.getCategoryNameById(goodsBO.getCategoryId()));
        return Result.success(goodsVO);
    }


    //==========================后台管理===========================


    @GetMapping("/manager/list")
    @IgnoreSecurity
    public Result<List<GoodsVO>> getManagerGoodsList(@RequestParam Integer page,
                                                     @RequestParam Integer pageSize,
                                                     @RequestParam Integer brand,
                                                     @RequestParam String keyword){
        Map<String, Object> param = new HashMap<>(8);
        param.put("brand", brand);
        param.put("keyword", keyword);
        List<GoodsBO> goodsBOList = goodsService.getGoodsListByParam(param);
        List<GoodsVO> goodsVOList = goodsBOList.stream().map(goodsBO -> {
            GoodsVO goodsVO = BeanUtils.copyProperties(goodsBO, GoodsVO.class);
            goodsVO.setCategoryName(categoryService.getCategoryNameById(goodsBO.getCategoryId()));
            return goodsVO;
        }).collect(Collectors.toList());
        return Result.success(goodsVOList);
    }

    @PostMapping("/manager/add")
    public Result<Boolean> addManagerGoods(@RequestBody GoodsBO goodsBO) {
        if (null == goodsBO) {
            return Result.fail("参数不太对劲哦");
        }
        boolean result = goodsService.addManagerGoods(goodsBO);
        return Result.success(result);
    }

    @PostMapping("/manager/update")
    public Result<Boolean> updateManagerGoods(@RequestBody GoodsBO goodsBO) {
        if (null == goodsBO || null == goodsBO.getId()) {
            return Result.fail("参数不太对劲哦");
        }
        boolean result = goodsService.updateManagerGoods(goodsBO);

        return Result.success(result);
    }

    @PostMapping("/manager/remove")
    public Result<Boolean> updateManagerGoods(@RequestParam Integer goodsId) {
        if (null == goodsId ) {
            return Result.fail("参数不太对劲哦");
        }
        boolean result = goodsService.removeManagerGoods(goodsId);
        return Result.success(result);
    }
}
