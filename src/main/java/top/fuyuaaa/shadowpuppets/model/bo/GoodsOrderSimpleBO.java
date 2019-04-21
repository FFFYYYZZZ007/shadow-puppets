package top.fuyuaaa.shadowpuppets.model.bo;

import lombok.Data;
import top.fuyuaaa.shadowpuppets.model.BaseModel;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-15 23:01
 */
@Data
public class GoodsOrderSimpleBO extends BaseModel {
    private static final long serialVersionUID = -7660396147174634522L;
    private Integer goodsId;
    private Integer num;
}
