package top.fuyuaaa.shadowpuppets.model.bo;

import lombok.Data;
import top.fuyuaaa.shadowpuppets.model.BaseModel;
import top.fuyuaaa.shadowpuppets.model.vo.GoodsOrderVO;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-25 21:48
 */
@Data
public class ExpressDeliveryBO extends BaseModel {
    private static final long serialVersionUID = 275898570957372806L;
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
    private Double expressPrice;
}
