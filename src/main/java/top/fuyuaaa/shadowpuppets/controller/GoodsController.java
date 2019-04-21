package top.fuyuaaa.shadowpuppets.controller;

import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.fuyuaaa.shadowpuppets.common.Result;
import top.fuyuaaa.shadowpuppets.dao.GoodsDao;
import top.fuyuaaa.shadowpuppets.enums.ExEnum;
import top.fuyuaaa.shadowpuppets.exceptions.ParamException;
import top.fuyuaaa.shadowpuppets.exceptions.UploadException;
import top.fuyuaaa.shadowpuppets.model.PageVO;
import top.fuyuaaa.shadowpuppets.model.bo.GoodsBO;
import top.fuyuaaa.shadowpuppets.model.qo.GoodsListQO;
import top.fuyuaaa.shadowpuppets.model.vo.GoodsVO;
import top.fuyuaaa.shadowpuppets.service.CategoryService;
import top.fuyuaaa.shadowpuppets.service.GoodsService;
import top.fuyuaaa.shadowpuppets.util.FileUtils;
import top.fuyuaaa.shadowpuppets.util.UploadUtil;

import java.io.File;
import java.util.List;
import java.util.Map;

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
    @Autowired
    GoodsDao goodsDao;

    private final Integer NON_CATEGORY = 0;

    @PostMapping("/list")
    public Result<PageVO<GoodsVO>> getGoodsList(@RequestBody GoodsListQO goodsListQO) {
        //只能查看在售的
        goodsListQO.setOnlySeeOnSale(1);
        PageVO<GoodsVO> goodsPageVO = this.getGoodsPageVO(goodsListQO);
        return Result.success(goodsPageVO);
    }

    private PageVO<GoodsVO> getGoodsPageVO(GoodsListQO goodsListQO) {
        if (NON_CATEGORY.equals(goodsListQO.getCategory())) {
            goodsListQO.setCategory(null);
        }
        Integer pageNum = goodsListQO.getPageNum();
        Integer pageSize = goodsListQO.getPageSize();
        //此处分页信息由于在service中做了各种转换所以丢失了，但是数据是对的
        PageHelper.startPage(pageNum, pageSize);
        List<GoodsVO> voList = goodsService.getVOList(goodsListQO);
        Integer count = goodsDao.count(goodsListQO);
        PageVO<GoodsVO> pageVO =
                new PageVO<>(pageNum, pageSize, count, voList);
        return pageVO;
    }

    @GetMapping("/one")
    public Result<GoodsVO> getGoodsDetailsById(@RequestParam Integer goodsId) {
        if (null == goodsId) {
            return Result.fail("参数不能为空");
        }
        return Result.success(goodsService.getGoodsVOById(goodsId));
    }



    //==========================后台管理===========================

    @PostMapping("/manager/list")
    public Result<PageVO<GoodsVO>> getManagerGoodsList(@RequestBody GoodsListQO goodsListQO) {
        PageVO<GoodsVO> goodsPageVO = this.getGoodsPageVO(goodsListQO);
        return Result.success(goodsPageVO);
    }

    @PostMapping("/manager/add")
    public Result addManagerGoods(@RequestBody GoodsBO goodsBO) {
        if (null == goodsBO) {
            return Result.fail("参数不太对劲哦");
        }
        boolean result = goodsService.addManagerGoods(goodsBO);
        return result ? Result.success() : Result.fail("添加失败");
    }

    @PostMapping("/manager/update")
    public Result<Boolean> updateManagerGoods(@RequestBody GoodsBO goodsBO) {
        if (null == goodsBO || null == goodsBO.getId()) {
            throw new ParamException(ExEnum.PARAM_ERROR.getMsg());
        }
        boolean result = goodsService.updateManagerGoods(goodsBO);
        return result ? Result.success() : Result.fail("更新失败");
    }

    @PostMapping("/manager/remove")
    public Result<Boolean> updateManagerGoods(@RequestParam Integer goodsId) {
        if (null == goodsId) {
            throw new ParamException(ExEnum.PARAM_ERROR.getMsg());
        }
        boolean result = goodsService.removeManagerGoods(goodsId);
        return result ? Result.success() : Result.fail("删除失败");
    }

    @PostMapping("/manager/image/add")
    public Result<String> addGoodsDetailsImage(@RequestParam("file") MultipartFile multipartFile,
                                               @RequestParam("goodsId") Integer goodsId) {
        File file = FileUtils.convertMultipartFile2File(multipartFile);
        //上传图片到阿里云
        String resultUrl = UploadUtil.upload2OSSWithGoodsId(goodsId, file);
        //修改商品的图片列表
        if (!goodsService.addGoodsImage(goodsId, resultUrl)) {
            throw new UploadException(ExEnum.UPLOAD_ERROR.getMsg());
        }
        //删除临时文件
        FileUtils.deleteFile(file);
        return Result.success(resultUrl);
    }

    @PostMapping("/manager/image/remove")
    public Result removeGoodsDetailsImage(@RequestParam("imageUrl") String imageUrl,
                                          @RequestParam("goodsId") Integer goodsId) {
        goodsService.removeGoodsImage(goodsId, imageUrl);
        return Result.success();
    }

    @PostMapping("/manager/image/main/upload")
    public Result<String> uploadGoodsMainImage(@RequestParam("file") MultipartFile multipartFile) {
        File file = FileUtils.convertMultipartFile2File(multipartFile);
        String resultUrl = UploadUtil.uploadGoodsMain2OSS(file);
        return Result.success(resultUrl);
    }

    /**
     * @return 分类统计信息
     */
    @PostMapping("/manager/category/statistics")
    public Result<Map<String, Integer>> categoryStatisticsInfo() {
        Map<String, Integer> statisticsInfo = goodsService.categoryStatisticsInfo();
        return Result.success(statisticsInfo);
    }

}
