package top.fuyuaaa.shadowpuppets.service.impl;

import com.github.pagehelper.PageHelper;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.fuyuaaa.shadowpuppets.dao.GoodsDao;
import top.fuyuaaa.shadowpuppets.model.Category;
import top.fuyuaaa.shadowpuppets.model.PageVO;
import top.fuyuaaa.shadowpuppets.model.bo.GoodsBO;
import top.fuyuaaa.shadowpuppets.model.po.GoodsOrderPO;
import top.fuyuaaa.shadowpuppets.model.po.GoodsPO;
import top.fuyuaaa.shadowpuppets.model.qo.GoodsListQO;
import top.fuyuaaa.shadowpuppets.model.vo.GoodsVO;
import top.fuyuaaa.shadowpuppets.service.CategoryService;
import top.fuyuaaa.shadowpuppets.service.GoodsService;
import top.fuyuaaa.shadowpuppets.util.BeanUtils;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-08 22:25
 */
@Service("goodsService")
public class GoodsServiceImpl implements GoodsService {

    private final static String SEPARATOR = ",";

    private final Integer NON_CATEGORY = 0;

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
        return BeanUtils.copyProperties(goodsPO, GoodsBO.class);
    }

    @Override
    public GoodsVO getGoodsVOById(Integer id) {
        GoodsBO goodsDetailsById = this.getGoodsDetailsById(id);
        GoodsVO goodsVO = BeanUtils.copyProperties(goodsDetailsById, GoodsVO.class);
        goodsVO.setCategoryName(categoryService.getCategoryNameById(goodsDetailsById.getCategoryId()));
        return goodsVO;
    }

    @Override
    public List<GoodsBO> getList(GoodsListQO goodsListQO) {
        List<GoodsPO> goodsPOList = goodsDao.findList(goodsListQO);
        if (null == goodsPOList) {
            return Lists.newArrayList();
        }
        return goodsPOList.stream()
                .map(goodsPO -> BeanUtils.copyProperties(goodsPO, GoodsBO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<GoodsVO> getVOList(GoodsListQO goodsListQO) {
        List<GoodsBO> goodsBOList = this.getList(goodsListQO);
        List<GoodsVO> goodsVOList = goodsBOList.stream()
                .map(goodsBO -> {
                    GoodsVO goodsVO = BeanUtils.copyProperties(goodsBO, GoodsVO.class);
                    goodsVO.setCategoryName(categoryService.getCategoryNameById(goodsBO.getCategoryId()));
                    goodsVO.setOnSale(1 == goodsBO.getOnSale() ? "在售" : "已下架");
                    return goodsVO;
                })
                .collect(Collectors.toList());
        return goodsVOList;
    }

    @Override
    public PageVO<GoodsVO> getGoodsPageVO(GoodsListQO goodsListQO) {
        if (NON_CATEGORY.equals(goodsListQO.getCategory())) {
            goodsListQO.setCategory(null);
        }
        Integer pageNum = goodsListQO.getPageNum();
        Integer pageSize = goodsListQO.getPageSize();
        //此处分页信息由于在service中做了各种转换所以丢失了，但是数据是对的
        PageHelper.startPage(pageNum, pageSize);
        List<GoodsVO> voList = this.getVOList(goodsListQO);
        Integer count = goodsDao.count(goodsListQO);
        PageVO<GoodsVO> pageVO =
                new PageVO<>(pageNum, pageSize, count, voList);
        return pageVO;
    }

    @Override
    public Boolean addManagerGoods(GoodsBO goodsBO) {
        GoodsPO goodsPO = BeanUtils.copyProperties(goodsBO, GoodsPO.class);
        return goodsDao.insert(goodsPO) > 0;
    }

    @Override
    public Boolean updateManagerGoods(GoodsBO goodsBO) {
        GoodsPO goodsPO = BeanUtils.copyProperties(goodsBO, GoodsPO.class);
        return goodsDao.update(goodsPO) > 0;
    }

    @Override
    public Boolean removeManagerGoods(Integer goodsId) {
        return goodsDao.delete(goodsId) > 0;
    }

    @Override
    public Boolean updateImagesUrls(Integer goodsId, String imagesUrls) {
        GoodsPO goodsPO = new GoodsPO();
        goodsPO.setId(goodsId);
        goodsPO.setImagesUrls(imagesUrls);
        return goodsDao.updateImagesUrls(goodsPO) > 0;
    }

    @Override
    public Boolean addGoodsImage(Integer goodsId, String resultUrl) {
        GoodsBO goodsBO = this.getGoodsDetailsById(goodsId);
        String imagesUrls = goodsBO.getImagesUrls();
        String end = ",";
        imagesUrls += imagesUrls.endsWith(end) ? resultUrl : end + resultUrl;
        return this.updateImagesUrls(goodsId, imagesUrls);
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
}
