package top.fuyuaaa.shadowpuppets.model.vo;

import lombok.Data;
import top.fuyuaaa.shadowpuppets.model.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-14 23:23
 */
@Data
public class GoodsOrderVO implements Serializable {
    private static final long serialVersionUID = -1661403354547186166L;
    private String id;
    private String userName;
    private List<GoodsVOWithNum> goodsVOList;
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
    private String status;

    private String dateCreate;
    private String dateUpdate;
    private String dateDelete;
}
