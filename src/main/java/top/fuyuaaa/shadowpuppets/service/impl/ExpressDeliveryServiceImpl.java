package top.fuyuaaa.shadowpuppets.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.fuyuaaa.shadowpuppets.common.enums.ExEnum;
import top.fuyuaaa.shadowpuppets.common.enums.ExpressCarrierEnum;
import top.fuyuaaa.shadowpuppets.common.enums.ExpressDeliveryStatusEnum;
import top.fuyuaaa.shadowpuppets.common.enums.OrderStatusEnum;
import top.fuyuaaa.shadowpuppets.dao.ExpressDeliveryDao;
import top.fuyuaaa.shadowpuppets.dao.GoodsOrderDao;
import top.fuyuaaa.shadowpuppets.common.exceptions.ExpressDeliveryException;
import top.fuyuaaa.shadowpuppets.common.exceptions.ParamException;
import top.fuyuaaa.shadowpuppets.mapstruct.ExpressDeliveryConverter;
import top.fuyuaaa.shadowpuppets.model.PageVO;
import top.fuyuaaa.shadowpuppets.model.po.ExpressDeliveryPO;
import top.fuyuaaa.shadowpuppets.model.qo.ExpressDeliveryQO;
import top.fuyuaaa.shadowpuppets.model.qo.ShipQO;
import top.fuyuaaa.shadowpuppets.model.vo.ExpressDeliveryVO;
import top.fuyuaaa.shadowpuppets.service.ExpressDeliveryService;
import top.fuyuaaa.shadowpuppets.common.utils.UUIDUtils;

import java.util.ArrayList;
import java.util.List;

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
    public void updateExpressDeliveryStatus(String orderId, Integer deliveryStatus) {
        this.validateUpdateParams(orderId, deliveryStatus);
        ExpressDeliveryStatusEnum expressDeliveryStatusEnum = ExpressDeliveryStatusEnum.find(deliveryStatus);
        if (expressDeliveryDao.updateDeliveryStatus(orderId, expressDeliveryStatusEnum.code()) != 1) {
            throw new ExpressDeliveryException(ExEnum.UPDATE_DELIVERY_STATUS_ERROR.getMsg());
        }
        //如果是已送达，修改到货时间为现在
        if (expressDeliveryStatusEnum.code() == ExpressDeliveryStatusEnum.IS_ARRIVED.code()) {
            expressDeliveryDao.updateDateExpressEnd(orderId);
        }
    }

    @Override
    @SuppressWarnings("all")
    public PageVO<ExpressDeliveryVO> getList(ExpressDeliveryQO expressDeliveryQO) {
        this.validateExpressDeliveryQO(expressDeliveryQO);
        PageHelper.startPage(expressDeliveryQO.getPageNum(), expressDeliveryQO.getPageSize());
        List<ExpressDeliveryPO> expressDeliveryPOList = expressDeliveryDao.findList(expressDeliveryQO);
        PageInfo<ExpressDeliveryPO> pageInfo = new PageInfo<>(expressDeliveryPOList);
        // 这样的代码可忒丑了，可能有空就引入mapStruct
        List<ExpressDeliveryVO> expressDeliveryVOList = ExpressDeliveryConverter.INSTANCE.toExpressDeliveryVOList(expressDeliveryPOList);
        if (CollectionUtils.isEmpty(expressDeliveryPOList)) {
            expressDeliveryVOList = new ArrayList<>(0);
        }
        Integer total = Integer.valueOf(String.valueOf(pageInfo.getTotal()));
        PageVO<ExpressDeliveryVO> pageVO = new PageVO<>(pageInfo.getPageNum(), pageInfo.getPageSize(), total, expressDeliveryVOList);
        return pageVO;
    }

    @Override
    public void ship(ShipQO shipQO) {
        this.validateShipQO(shipQO);
        expressDeliveryDao.ship(shipQO);
        goodsOrderDao.updateOrderStatus(OrderStatusEnum.PENDING_RECEIVE.code(), shipQO.getOrderId());
    }

    @Override
    public ExpressDeliveryVO getByOrderId(String orderId) {
        ExpressDeliveryPO expressDeliveryPO = expressDeliveryDao.findByOrderId(orderId);
        return ExpressDeliveryConverter.INSTANCE.toExpressDeliveryVO(expressDeliveryPO);
    }

    @Override
    public String codeGenerate(Integer expressCarrier) {
        ExpressCarrierEnum expressCarrierEnum = ExpressCarrierEnum.find(expressCarrier);
        if (expressCarrierEnum == null) {
            return "";
        }
        return this.codeGenerate(expressCarrierEnum);
    }

    //==============================  private help methods  ==============================

    private void validateShipQO(ShipQO shipQO) {
        if (null == shipQO || StringUtils.isAnyEmpty(shipQO.getOrderId(),shipQO.getExpressCode())||
                null == shipQO.getExpressCarrier()){
            throw new ExpressDeliveryException(ExEnum.PARAM_ERROR.getMsg());
        }
    }


    private void validateUpdateParams(String orderId, Integer deliveryStatus) {
        if (StringUtils.isEmpty(orderId) || null == deliveryStatus || 0 > deliveryStatus) {
            throw new ParamException(ExEnum.PARAM_ERROR.getMsg());
        }
    }


    private void validateExpressDeliveryQO(ExpressDeliveryQO expressDeliveryQO) {
        if (expressDeliveryQO == null) {
            expressDeliveryQO = new ExpressDeliveryQO();
        }
        if (expressDeliveryQO.getPageNum() == null) {
            expressDeliveryQO.setPageNum(1);
        }
        if (expressDeliveryQO.getPageSize() == null) {
            expressDeliveryQO.setPageSize(10);
        }
    }

    private String codeGenerate(ExpressCarrierEnum expressCarrierEnum) {
        if (expressCarrierEnum.getCode() == ExpressCarrierEnum.SF_EXPRESS.getCode()) {
            return "SF" + UUIDUtils.getExpressCode();
        }
        if (expressCarrierEnum.getCode() == ExpressCarrierEnum.YT_EXPRESS.getCode()) {
            return "YT" + UUIDUtils.getExpressCode();
        }
        if (expressCarrierEnum.getCode() == ExpressCarrierEnum.ST_EXPRESS.getCode()) {
            return "ST" + UUIDUtils.getExpressCode();
        }
        if (expressCarrierEnum.getCode() == ExpressCarrierEnum.EMS_EXPRESS.getCode()) {
            return "EMS" + UUIDUtils.getExpressCode();
        }
        return "YZ" + UUIDUtils.getExpressCode();
    }
}
