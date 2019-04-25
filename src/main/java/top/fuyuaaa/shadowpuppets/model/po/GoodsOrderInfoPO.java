package top.fuyuaaa.shadowpuppets.model.po;

import lombok.Data;
import top.fuyuaaa.shadowpuppets.model.BaseModel;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-15 23:14
 */
@Data
public class GoodsOrderInfoPO extends BaseModel {
    private static final long serialVersionUID = 1543301968135170466L;
    private Integer id;
    private String goodsOrderId;
    private Integer goodsId;
    private Integer num;
}
