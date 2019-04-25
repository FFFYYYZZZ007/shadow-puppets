package top.fuyuaaa.shadowpuppets.model.vo;

import lombok.Data;
import top.fuyuaaa.shadowpuppets.model.BaseModel;

import java.util.List;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-25 21:48
 */
@Data
public class OrderDeliveryVO extends BaseModel {
    private static final long serialVersionUID = 275898570957372806L;
    private Integer orderId;
    private String deliveryStatus;
    private GoodsOrderVO goodsOrderVO;
    /**
     * 快递单号
     */
    private String expressCode;
    private Double expressPrice;
    private String userName;
}
