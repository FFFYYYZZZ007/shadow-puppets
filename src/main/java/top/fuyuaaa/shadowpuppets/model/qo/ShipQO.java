package top.fuyuaaa.shadowpuppets.model.qo;

import lombok.Data;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-27 17:14
 */
@Data
@SuppressWarnings("all")
public class ShipQO {
    /**
     * 订单Id
     */
    private String orderId;
    /**
     * 承运商
     */
    private Integer expressCarrier;
    /**
     * 快递单号
     */
    private String expressCode;
}
