package top.fuyuaaa.shadowpuppets.mapstruct;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import top.fuyuaaa.shadowpuppets.model.po.ExpressDeliveryPO;
import top.fuyuaaa.shadowpuppets.model.vo.ExpressDeliveryVO;
import top.fuyuaaa.shadowpuppets.common.utils.DateUtils;

import java.util.List;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-27 22:23
 */
@Mapper
@SuppressWarnings("all")
public abstract class ExpressDeliveryConverter {
    public final static ExpressDeliveryConverter INSTANCE = Mappers.getMapper(ExpressDeliveryConverter.class);

    @Mapping(target = "deliveryStatus", ignore = true)
    @Mapping(target = "expressCarrier", ignore = true)
    @Mapping(target = "dateExpressStart", ignore = true)
    @Mapping(target = "dateExpressEnd", ignore = true)
    @Mapping(target = "dateUpdate", ignore = true)
    public abstract ExpressDeliveryVO toExpressDeliveryVO(ExpressDeliveryPO expressDeliveryPO);

    @AfterMapping
    protected void afterToExpressDeliveryVO(ExpressDeliveryPO source, @MappingTarget ExpressDeliveryVO target) {
        if (source.getDeliveryStatus() != null) {
            target.setDeliveryStatus(source.getDeliveryStatus().desc());
        }
        if (source.getExpressCarrier() != null) {
            target.setExpressCarrier(source.getExpressCarrier().desc());
        }
        target.setDateExpressStart(DateUtils.formatDate(source.getDateExpressStart()));
        target.setDateExpressEnd(DateUtils.formatDate(source.getDateExpressEnd()));
        target.setDateUpdate(DateUtils.formatDate(source.getDateUpdate()));
    }

    @IterableMapping(elementTargetType = ExpressDeliveryVO.class)
    public abstract List<ExpressDeliveryVO> toExpressDeliveryVOList(List<ExpressDeliveryPO> sourceList);

}
