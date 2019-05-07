package top.fuyuaaa.shadowpuppets.mapstruct;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import top.fuyuaaa.shadowpuppets.model.bo.GoodsBO;
import top.fuyuaaa.shadowpuppets.model.po.GoodsPO;
import top.fuyuaaa.shadowpuppets.model.vo.GoodsVO;
import top.fuyuaaa.shadowpuppets.service.CategoryService;
import top.fuyuaaa.shadowpuppets.common.utils.SpringUtil;

import java.util.List;

/**
 * @author: fuyuaaa
 * @creat: 2019-05-05 11:13
 */
@Mapper(componentModel="spring")
@SuppressWarnings("all")
public abstract class GoodsConverter {
    public final static GoodsConverter INSTANCE = Mappers.getMapper(GoodsConverter.class);

    //==============================  BO => PO  ==============================

    public abstract GoodsPO toGoodsPO (GoodsBO GoodsBO);

    //==============================  PO => BO  ==============================

    public abstract GoodsBO toGoodsBO (GoodsPO goodsPO);

    @AfterMapping
    public void agterToGoodsBO (GoodsPO source, @MappingTarget GoodsBO target){
        if (source.getImagesUrls() == null) {
            target.setImagesUrls("");
        }
    }

    @IterableMapping(elementTargetType = GoodsBO.class)
    public abstract List<GoodsBO> toGoodsBOList(List<GoodsPO> sourceList);

    //==============================  BO => VO  ==============================

    @Mapping(target = "categoryName",ignore = true)
    @Mapping(target = "onSale",ignore = true)
    public abstract GoodsVO toGoodsVO (GoodsBO goodsBO);

    @AfterMapping
    public void afterGoodsVO(GoodsBO source, @MappingTarget GoodsVO target){
        CategoryService categoryService = SpringUtil.getBean(CategoryService.class);
        target.setCategoryName(categoryService.getCategoryNameById(source.getCategoryId()));
        target.setOnSale(1 == source.getOnSale() ? "在售" : "未上架");
        if (null == source.getImagesUrls()) {
            target.setImagesUrls("");
        }
    }

    @IterableMapping(elementTargetType = GoodsVO.class)
    public abstract List<GoodsVO> toGoodsVOList(List<GoodsBO> sourceList);
}
