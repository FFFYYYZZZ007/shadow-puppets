package top.fuyuaaa.shadowpuppets.mapstruct;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import top.fuyuaaa.shadowpuppets.model.bo.GoodsBO;
import top.fuyuaaa.shadowpuppets.model.bo.ShoppingCartBO;
import top.fuyuaaa.shadowpuppets.model.po.ShoppingCartPO;
import top.fuyuaaa.shadowpuppets.model.vo.ShoppingCartVO;
import top.fuyuaaa.shadowpuppets.service.GoodsService;
import top.fuyuaaa.shadowpuppets.common.utils.SpringUtil;

import java.util.List;

/**
 * @author: fuyuaaa
 * @creat: 2019-05-05 09:34
 */
@Mapper(componentModel="spring")
@SuppressWarnings("all")
public abstract class ShoppingCartConverter {
    public final static ShoppingCartConverter INSTANCE = Mappers.getMapper(ShoppingCartConverter.class);

    public abstract ShoppingCartPO toShoppingCartPO (ShoppingCartBO ShoppingCartBO);

    public abstract ShoppingCartBO toShoppingCartBO (ShoppingCartPO shoppingCartPO);

    @IterableMapping(elementTargetType = ShoppingCartBO.class)
    public abstract List<ShoppingCartBO> toShoppingCartBOList(List<ShoppingCartPO> sourceList);

    //==============================  BO => VO  ==============================


    public abstract ShoppingCartVO toShoppingCartVO (ShoppingCartBO shoppingCartBO);

    @AfterMapping
    public void afterToShoppingCartVO(ShoppingCartBO shoppingCartBO, @MappingTarget ShoppingCartVO shoppingCartVO) {
        GoodsService goodsService = SpringUtil.getBean(GoodsService.class);
        GoodsBO goodsBO = goodsService.getGoodsDetailsById(shoppingCartBO.getGoodsId());
        shoppingCartVO.setKey(shoppingCartBO.getId());
        shoppingCartVO.setGoodsName(goodsBO.getGoodsName());
        shoppingCartVO.setPrice(goodsBO.getPrice());
    }

    @IterableMapping(elementTargetType = ShoppingCartVO.class)
    public abstract List<ShoppingCartVO> toShoppingCartVOList(List<ShoppingCartBO> sourceList);

}
