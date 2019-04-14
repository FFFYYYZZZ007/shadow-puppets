package top.fuyuaaa.shadowpuppets.model.vo;

import lombok.Data;
import top.fuyuaaa.shadowpuppets.model.BaseModel;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-14 23:23
 */
@Data
public class GoodsOrderVO extends BaseModel {
    private static final long serialVersionUID = -1661403354547186166L;
    private Integer id;
    private Integer userId;
    private String goodsIds;
    /**
     * 快递费
     */
    private Double expressFee;
    /**
     * 成交价
     */
    private Double dealPrice;
    /**
     * 订单状态（0未付款/1已付款）
     */
    private Integer status;
    /**
     * 物流状态（0未发货/1已发货/2配送中/3已送达）
     */
    private Integer deliveryStatus;
}
