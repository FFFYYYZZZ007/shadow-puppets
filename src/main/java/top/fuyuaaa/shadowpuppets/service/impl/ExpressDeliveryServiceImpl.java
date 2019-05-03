package top.fuyuaaa.shadowpuppets.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.fuyuaaa.shadowpuppets.common.enums.ExEnum;
import top.fuyuaaa.shadowpuppets.common.enums.ExpressDeliveryStatusEnum;
import top.fuyuaaa.shadowpuppets.common.enums.OrderStatusEnum;
import top.fuyuaaa.shadowpuppets.dao.ExpressDeliveryDao;
import top.fuyuaaa.shadowpuppets.dao.GoodsOrderDao;
import top.fuyuaaa.shadowpuppets.exceptions.ExpressDeliveryException;
import top.fuyuaaa.shadowpuppets.mapstruct.ExpressDeliveryConverter;
import top.fuyuaaa.shadowpuppets.model.PageVO;
import top.fuyuaaa.shadowpuppets.model.po.ExpressDeliveryPO;
import top.fuyuaaa.shadowpuppets.model.qo.ExpressDeliveryQO;
import top.fuyuaaa.shadowpuppets.model.qo.ShipQO;
import top.fuyuaaa.shadowpuppets.model.vo.ExpressDeliveryVO;
import top.fuyuaaa.shadowpuppets.service.ExpressDeliveryService;
import top.fuyuaaa.shadowpuppets.util.BeanUtils;
import top.fuyuaaa.shadowpuppets.util.DateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-26 17:15
 */
@Service
public class ExpressDeliveryServiceImpl implements ExpressDeliveryService {

    @Autowired
    ExpressDeliveryDao expressDeliveryDao;

    @Autowired
    GoodsOrderDao goodsOrderDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateExpressDeliveryStatus(String orderId, ExpressDeliveryStatusEnum expressDeliveryStatusEnum) {
        if (expressDeliveryDao.updateDeliveryStatus(orderId, expressDeliveryStatusEnum.code()) != 1) {
            throw new ExpressDeliveryException(ExEnum.UPDATE_DELIVERY_STATUS_ERROR.getMsg());
        }
        //如果是已送达，修改到货时间为现在
        if (expressDeliveryStatusEnum.code() == ExpressDeliveryStatusEnum.IS_ARRIVED.code()) {
            expressDeliveryDao.updateDateExpressEnd(orderId);
        }
        return true;
    }

    @Override
    @SuppressWarnings("all")
    public PageVO<ExpressDeliveryVO> getList(ExpressDeliveryQO expressDeliveryQO) {
        PageHelper.startPage(expressDeliveryQO.getPageNum(), expressDeliveryQO.getPageSize());
        List<ExpressDeliveryPO> expressDeliveryPOList = expressDeliveryDao.findList(expressDeliveryQO);
        PageInfo<ExpressDeliveryPO> pageInfo = new PageInfo<>(expressDeliveryPOList);
        // 这样的代码可忒丑了，可能有空就引入mapStruct
        List<ExpressDeliveryVO> expressDeliveryVOList = expressDeliveryPOList.stream().map(expressDeliveryPO -> {
            ExpressDeliveryVO expressDeliveryVO = BeanUtils.copyProperties(expressDeliveryPO, ExpressDeliveryVO.class);
            if (expressDeliveryPO.getDeliveryStatus() != null) {
                expressDeliveryVO.setDeliveryStatus(expressDeliveryPO.getDeliveryStatus().desc());
            }
            if (expressDeliveryPO.getExpressCarrier() != null) {
                expressDeliveryVO.setExpressCarrier(expressDeliveryPO.getExpressCarrier().desc());
            }
            expressDeliveryVO.setDateExpressStart(DateUtils.formatDate(expressDeliveryPO.getDateExpressStart()));
            expressDeliveryVO.setDateExpressEnd(DateUtils.formatDate(expressDeliveryPO.getDateExpressEnd()));
            expressDeliveryVO.setDateUpdate(DateUtils.formatDate(expressDeliveryPO.getDateUpdate()));
            return expressDeliveryVO;
        }).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(expressDeliveryPOList)) {
            expressDeliveryVOList = new ArrayList<>(0);
        }
        Integer total = Integer.valueOf(String.valueOf(pageInfo.getTotal()));
        PageVO<ExpressDeliveryVO> pageVO = new PageVO<>(pageInfo.getPageNum(), pageInfo.getPageSize(), total, expressDeliveryVOList);
        return pageVO;
    }

    @Override
    public void ship(ShipQO shipQO) {
        expressDeliveryDao.ship(shipQO);
        goodsOrderDao.updateOrderStatus(OrderStatusEnum.PENDING_RECEIVE.code(), shipQO.getOrderId());
    }

    @Override
    public ExpressDeliveryVO getByOrderId(String orderId) {
        ExpressDeliveryPO expressDeliveryPO = expressDeliveryDao.findByOrderId(orderId);
        return ExpressDeliveryConverter.INSTANCE.toExpressDeliveryVO(expressDeliveryPO);
    }
}
