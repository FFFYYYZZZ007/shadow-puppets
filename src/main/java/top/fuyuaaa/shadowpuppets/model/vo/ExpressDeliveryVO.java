package top.fuyuaaa.shadowpuppets.model.vo;

import lombok.Data;
import top.fuyuaaa.shadowpuppets.model.BaseModel;

import java.io.Serializable;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-25 21:48
 */
@Data
public class ExpressDeliveryVO implements Serializable {
    private static final long serialVersionUID = 275898570957372806L;
    private String orderId;
    private String deliveryStatus;
    private GoodsOrderVO goodsOrderVO;
    /**
     * 快递单号
     */
    private String expressCode;
    /**
     * 快递承运商
     */
    private String expressCarrier;
    private Double expressPrice;
    private String userName;

    private String dateCreate;
    private String dateUpdate;
}
