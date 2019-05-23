package top.fuyuaaa.shadowpuppets.mapstruct;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.BeanUtils;
import top.fuyuaaa.shadowpuppets.common.enums.OrderStatusEnum;
import top.fuyuaaa.shadowpuppets.dao.GoodsOrderDao;
import top.fuyuaaa.shadowpuppets.dao.UserDao;
import top.fuyuaaa.shadowpuppets.model.bo.GoodsOrderBO;
import top.fuyuaaa.shadowpuppets.model.po.GoodsOrderInfoPO;
import top.fuyuaaa.shadowpuppets.model.po.GoodsOrderPO;
import top.fuyuaaa.shadowpuppets.model.vo.GoodsOrderVO;
import top.fuyuaaa.shadowpuppets.model.vo.GoodsVO;
import top.fuyuaaa.shadowpuppets.model.vo.GoodsVOWithNum;
import top.fuyuaaa.shadowpuppets.service.GoodsService;
import top.fuyuaaa.shadowpuppets.common.utils.SpringUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: fuyuaaa
 * @creat: 2019-05-05 11:13
 */
@Mapper(componentModel = "spring")
@SuppressWarnings("all")
public abstract class GoodsOrderConverter {
    public final static GoodsOrderConverter INSTANCE = Mappers.getMapper(GoodsOrderConverter.class);

    //==============================  PO => BO  ==============================

    @Mapping(target = "status", ignore = true)
    public abstract GoodsOrderBO toGoodsOrderBO(GoodsOrderPO goodsOrderPO);

    @AfterMapping
    public void afterToGoodsOrderBO(GoodsOrderPO source, @MappingTarget GoodsOrderBO target) {
        if (source.getStatus() != null) {
            target.setStatus(OrderStatusEnum.find(source.getStatus()));
        }
    }

    @IterableMapping(elementTargetType = GoodsOrderBO.class)
    public abstract List<GoodsOrderBO> toGoodsOrderBOList(List<GoodsOrderPO> sourceList);


    //==============================  BO => PO  ==============================

    @Mapping(target = "status", ignore = true)
    public abstract GoodsOrderPO toGoodsOrderPO(GoodsOrderBO goodsOrderBO);

    @AfterMapping
    public void afterToGoodsOrderPO(GoodsOrderBO source, @MappingTarget GoodsOrderPO target) {
        if (source.getStatus() != null) {
            target.setStatus(source.getStatus().code());
        }
    }

    //==============================  BO => VO  ==============================

    @Mapping(target = "goodsVOList", ignore = true)
    @Mapping(target = "userName", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "dateCreate", ignore = true)
    @Mapping(target = "dateUpdate", ignore = true)
    public abstract GoodsOrderVO toGoodsOrderVO(GoodsOrderBO goodsOrderBO);

    @AfterMapping
    public void afterToGoodsOrderVO(GoodsOrderBO goodsOrderBO, @MappingTarget GoodsOrderVO goodsOrderVO) {
        GoodsOrderDao goodsOrderDao = SpringUtil.getBean(GoodsOrderDao.class);
        GoodsService goodsService = SpringUtil.getBean(GoodsService.class);
        UserDao userDao = SpringUtil.getBean(UserDao.class);

        List<GoodsOrderInfoPO> goodsOrderInfoPOList = goodsOrderDao.getOrderInfoByOrderId(goodsOrderBO.getId());

        List<GoodsVOWithNum> goodsVOWithNumList = goodsOrderInfoPOList.stream()
                .map(goodsOrderInfoPO -> {
                    GoodsVO goodsVOById = goodsService.getGoodsVOById(goodsOrderInfoPO.getGoodsId());
                    GoodsVOWithNum goodsVOWithNum = new GoodsVOWithNum();
                    BeanUtils.copyProperties(goodsVOById, goodsVOWithNum);
                    goodsVOWithNum.setNum(goodsOrderInfoPO.getNum());
                    return goodsVOWithNum;
                })
                .collect(Collectors.toList());
        goodsOrderVO.setGoodsVOList(goodsVOWithNumList);

        //设置用户名
        goodsOrderVO.setUserName(userDao.getById(goodsOrderBO.getUserId()).getUserName());

        //枚举类转成String
        goodsOrderVO.setStatus(goodsOrderBO.getStatus().desc());

        //设置时间
        goodsOrderVO.setDateCreate(DateFormatUtils.format(goodsOrderBO.getDateCreate(), "yyyy-MM-dd HH:mm:ss"));
        goodsOrderVO.setDateUpdate(DateFormatUtils.format(goodsOrderBO.getDateUpdate(), "yyyy-MM-dd HH:mm:ss"));
    }

    @IterableMapping(elementTargetType = GoodsOrderVO.class)
    public abstract List<GoodsOrderVO> toGoodsOrderVOList(List<GoodsOrderBO> sourceList);

}
