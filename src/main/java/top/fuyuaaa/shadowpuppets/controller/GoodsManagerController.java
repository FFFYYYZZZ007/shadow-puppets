package top.fuyuaaa.shadowpuppets.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.fuyuaaa.shadowpuppets.common.annotations.ValidateAdmin;
import top.fuyuaaa.shadowpuppets.common.Result;
import top.fuyuaaa.shadowpuppets.model.PageVO;
import top.fuyuaaa.shadowpuppets.model.bo.GoodsBO;
import top.fuyuaaa.shadowpuppets.model.qo.GoodsListQO;
import top.fuyuaaa.shadowpuppets.model.vo.GoodsVO;
import top.fuyuaaa.shadowpuppets.service.GoodsService;
import top.fuyuaaa.shadowpuppets.common.utils.FileUtils;
import top.fuyuaaa.shadowpuppets.common.utils.UploadUtil;

import java.io.File;
import java.util.Map;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-29 10:15
 */

@RestController
@RequestMapping("/goods/manager")
@Slf4j
public class GoodsManagerController {

    @Autowired
    GoodsService goodsService;

    @PostMapping("/list")
    @ValidateAdmin
    public Result<PageVO<GoodsVO>> getManagerGoodsList(@RequestBody GoodsListQO goodsListQO) {
        PageVO<GoodsVO> goodsPageVO = goodsService.getGoodsPageVO(goodsListQO);
        return Result.success(goodsPageVO);
    }


    @PostMapping("/add")
    @ValidateAdmin
    public Result addManagerGoods(@RequestBody GoodsBO goodsBO) {
         goodsService.addManagerGoods(goodsBO);
        return Result.success().setMsg("添加成功");
    }

    @PostMapping("/update")
    @ValidateAdmin
    public Result updateManagerGoods(@RequestBody GoodsBO goodsBO) {
        goodsService.updateManagerGoods(goodsBO);
        return Result.success().setMsg("更新成功");
    }

    @PostMapping("/remove")
    @ValidateAdmin
    public Result updateManagerGoods(@RequestParam Integer goodsId) {
        goodsService.removeManagerGoods(goodsId);
        return  Result.success().setMsg("删除成功");
    }

    @PostMapping("/image/add")
    @ValidateAdmin
    public Result<String> addGoodsDetailsImage(@RequestParam("file") MultipartFile multipartFile,
                                               @RequestParam("goodsId") Integer goodsId) {
        String resultUrl = goodsService.addGoodsImage(multipartFile, goodsId);
        return Result.success(resultUrl);
    }

    @PostMapping("/image/remove")
    @ValidateAdmin
    public Result removeGoodsDetailsImage(@RequestParam("imageUrl") String imageUrl,
                                          @RequestParam("goodsId") Integer goodsId) {
        goodsService.removeGoodsImage(goodsId, imageUrl);
        return Result.success();
    }

    @PostMapping("/image/main/upload")
    public Result<String> uploadGoodsMainImage(@RequestParam("file") MultipartFile multipartFile) {
        File file = FileUtils.convertMultipartFile2File(multipartFile);
        String resultUrl = UploadUtil.uploadGoodsMain2OSS(file);
        return Result.success(resultUrl);
    }

    /**
     * @return 分类统计信息
     */
    @PostMapping("/category/statistics")
    @ValidateAdmin
    public Result<Map<String, Integer>> categoryStatisticsInfo() {
        Map<String, Integer> statisticsInfo = goodsService.categoryStatisticsInfo();
        return Result.success(statisticsInfo);
    }
}
