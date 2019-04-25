package top.fuyuaaa.shadowpuppets.model.qo;

import lombok.Data;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-18 14:19
 */
@Data
@SuppressWarnings("all")
public class GoodsOrderQO extends BaseQO {
    private static final long serialVersionUID = -3163889531664735638L;
    private Integer userId;
    private Integer status;
    private Integer deliveryStatus;
}
