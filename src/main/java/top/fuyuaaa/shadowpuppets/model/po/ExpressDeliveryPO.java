package top.fuyuaaa.shadowpuppets.model.po;

import lombok.Data;
import top.fuyuaaa.shadowpuppets.common.enums.ExpressCarrierEnum;
import top.fuyuaaa.shadowpuppets.common.enums.ExpressDeliveryStatusEnum;
import top.fuyuaaa.shadowpuppets.model.BaseModel;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-25 21:48
 */
@Data
public class ExpressDeliveryPO extends BaseModel {
    private static final long serialVersionUID = 275898570957372806L;
    private String orderId;
    private ExpressDeliveryStatusEnum deliveryStatus;
    /**
     * 快递单号
     */
    private String expressCode;
    /**
     * 快递承运商
     */
    private ExpressCarrierEnum expressCarrier;
    private Double expressPrice;
    private String userName;
}
