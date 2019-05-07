package top.fuyuaaa.shadowpuppets.service.impl;

import com.github.pagehelper.PageHelper;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.fuyuaaa.shadowpuppets.common.enums.ExEnum;
import top.fuyuaaa.shadowpuppets.dao.GoodsDao;
import top.fuyuaaa.shadowpuppets.common.exceptions.GoodsException;
import top.fuyuaaa.shadowpuppets.common.exceptions.ParamException;
import top.fuyuaaa.shadowpuppets.mapstruct.GoodsConverter;
import top.fuyuaaa.shadowpuppets.model.Category;
import top.fuyuaaa.shadowpuppets.model.PageVO;
import top.fuyuaaa.shadowpuppets.model.bo.GoodsBO;
import top.fuyuaaa.shadowpuppets.model.po.GoodsPO;
import top.fuyuaaa.shadowpuppets.model.qo.GoodsListQO;
import top.fuyuaaa.shadowpuppets.model.vo.GoodsVO;
import top.fuyuaaa.shadowpuppets.service.CategoryService;
import top.fuyuaaa.shadowpuppets.service.GoodsService;
import top.fuyuaaa.shadowpuppets.common.utils.FileUtils;
import top.fuyuaaa.shadowpuppets.common.utils.UploadUtil;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-08 22:25
 */
@Service("goodsService")
public class GoodsServiceImpl implements GoodsService {

    private final static String SEPARATOR = ",";

    private final static Integer NON_CATEGORY = 0;

    @Autowired
    GoodsDao goodsDao;

    @Autowired
    CategoryService categoryService;

    @Override
    public GoodsBO getGoodsDetailsById(Integer id) {
        GoodsPO goodsPO = goodsDao.findByGoodsId(id);
        if (null == goodsPO) {
            return null;
        }
        return GoodsConverter.INSTANCE.toGoodsBO(goodsPO);
    }

    @Override
    public GoodsVO getGoodsVOById(Integer id) {
        GoodsBO goodsDetailsById = this.getGoodsDetailsById(id);
        return GoodsConverter.INSTANCE.toGoodsVO(goodsDetailsById);
    }

    @Override
    public List<GoodsBO> getList(GoodsListQO goodsListQO) {
        List<GoodsPO> goodsPOList = goodsDao.findList(goodsListQO);
        if (null == goodsPOList) {
            return Lists.newArrayList();
        }
        return GoodsConverter.INSTANCE.toGoodsBOList(goodsPOList);
    }

    @Override
    public List<GoodsVO> getVOList(GoodsListQO goodsListQO) {
        List<GoodsBO> goodsBOList = this.getList(goodsListQO);
        return GoodsConverter.INSTANCE.toGoodsVOList(goodsBOList);
    }

    @Override
    public PageVO<GoodsVO> getGoodsPageVO(GoodsListQO goodsListQO) {
        if (NON_CATEGORY.equals(goodsListQO.getCategory())) {
            goodsListQO.setCategory(null);
        }
        Integer pageNum = goodsListQO.getPageNum();
        Integer pageSize = goodsListQO.getPageSize();
        //此处分页信息由于在service中做了各种转换所以丢失了，但是list数据是对的
        PageHelper.startPage(pageNum, pageSize);
        List<GoodsVO> voList = this.getVOList(goodsListQO);
        Integer count = goodsDao.count(goodsListQO);
        return new PageVO<>(pageNum, pageSize, count, voList);
    }

    //==============================  manager methods  ==============================

    @Override
    public void addManagerGoods(GoodsBO goodsBO) {
        this.validateAddGoodsParam(goodsBO);
        GoodsPO goodsPO = GoodsConverter.INSTANCE.toGoodsPO(goodsBO);
        Integer insert = goodsDao.insert(goodsPO);
        if (1 != insert) {
            throw new GoodsException(ExEnum.GOODS_ADD_ERROR.getMsg());
        }
    }


    @Override
    public void updateManagerGoods(GoodsBO goodsBO) {
        if (null == goodsBO || null == goodsBO.getId()) {
            throw new ParamException(ExEnum.PARAM_ERROR.getMsg());
        }
        GoodsPO goodsPO = GoodsConverter.INSTANCE.toGoodsPO(goodsBO);
        if (1 != goodsDao.update(goodsPO)) {
            throw new GoodsException(ExEnum.GOODS_UPDATE_ERROR.getMsg());
        }
    }

    @Override
    public void removeManagerGoods(Integer goodsId) {
        if (null == goodsId) {
            throw new ParamException(ExEnum.PARAM_ERROR.getMsg());
        }
        if (1!=goodsDao.delete(goodsId)){
            throw new GoodsException(ExEnum.GOODS_REMOVE_ERROR.getMsg());
        }
    }

    @Override
    public Boolean updateImagesUrls(Integer goodsId, String imagesUrls) {
        GoodsPO goodsPO = new GoodsPO();
        goodsPO.setId(goodsId);
        goodsPO.setImagesUrls(imagesUrls);
        return goodsDao.updateImagesUrls(goodsPO) > 0;
    }

    @Override
    public String addGoodsImage(MultipartFile multipartFile, Integer goodsId) {
        File file = FileUtils.convertMultipartFile2File(multipartFile);
        //上传图片到阿里云
        String resultUrl = UploadUtil.upload2OSSWithGoodsId(goodsId, file);

        //更新数据库中urls
        GoodsBO goodsBO = this.getGoodsDetailsById(goodsId);
        String imagesUrls = goodsBO.getImagesUrls();
        String end = ",";
        imagesUrls += imagesUrls.endsWith(end) ? resultUrl : end + resultUrl;
        this.updateImagesUrls(goodsId, imagesUrls);

        //删除临时文件
        FileUtils.deleteFile(file);
        return resultUrl;
    }

    @Override
    public void removeGoodsImage(Integer goodsId, String imageUrl) {
        GoodsBO goodsBO = this.getGoodsDetailsById(goodsId);
        String imagesUrls = goodsBO.getImagesUrls();
        String[] urlArray = imagesUrls.split(SEPARATOR);
        List<String> urlList = Arrays.asList(urlArray);
        urlList = urlList.stream().filter(url -> !url.equals(imageUrl)).collect(Collectors.toList());
        imagesUrls = Joiner.on(SEPARATOR).join(urlList);
        this.updateImagesUrls(goodsId, imagesUrls);
    }

    @Override
    public Integer countByCategoryId(Integer categoryId) {
        Integer countByCategoryId = goodsDao.countByCategoryId(categoryId);
        return countByCategoryId == null ? 0 : countByCategoryId;
    }

    @Override
    public Map<String, Integer> categoryStatisticsInfo() {
        List<Category> categoryList = categoryService.getCategoryList();
        Map<String, Integer> info = new HashMap<>(8);
        categoryList.forEach(category -> info.put(category.getCategoryName(), this.countByCategoryId(category.getId())));
        return info;
    }


    //==============================  private help method  ==============================

    private void validateAddGoodsParam(GoodsBO goodsBO) {
        if (null == goodsBO) {
            throw new ParamException(ExEnum.PARAM_ERROR.getMsg());
        }
        String error = ExEnum.GOODS_ADD_ERROR.getMsg() + "，";
        if (StringUtils.isEmpty(goodsBO.getGoodsName())) {
            throw new ParamException(error + "请填写商品名");
        }
        if (StringUtils.isEmpty(goodsBO.getMainImageUrl())) {
            throw new ParamException(error + "请上传主图");
        }
        if (null == goodsBO.getOnSale()) {
            throw new ParamException(error + "请选择商品上架状态");
        }
        if (null == goodsBO.getPrice()) {
            throw new ParamException(error + "请输入商品价格");
        }
        if (null == goodsBO.getCategoryId()) {
            throw new ParamException(error + "请选择商品类别");
        }
        if (null == goodsBO.getQuantity()) {
            throw new ParamException(error + "请输入商品库存数量");
        }

    }
}
