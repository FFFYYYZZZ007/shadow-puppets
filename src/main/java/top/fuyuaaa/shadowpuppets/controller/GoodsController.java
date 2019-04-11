package top.fuyuaaa.shadowpuppets.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.fuyuaaa.shadowpuppets.annotation.IgnoreSecurity;
import top.fuyuaaa.shadowpuppets.common.Result;
import top.fuyuaaa.shadowpuppets.model.bo.GoodsBO;
import top.fuyuaaa.shadowpuppets.model.vo.GoodsVO;
import top.fuyuaaa.shadowpuppets.service.CategoryService;
import top.fuyuaaa.shadowpuppets.service.GoodsService;
import top.fuyuaaa.shadowpuppets.util.BeanUtils;

import java.util.List;
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
}
