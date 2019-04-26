package top.fuyuaaa.shadowpuppets.model.qo;

import lombok.Data;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-26 17:04
 */
@Data
@SuppressWarnings("all")
public class ExpressDeliveryQO extends BaseQO {
    private String orderId;
    private Integer deliveryStatus;
    /**
     * 快递单号
     */
    private String expressCode;
    /**
     * 快递承运商
     */
    private Integer expressCarrier;
}
