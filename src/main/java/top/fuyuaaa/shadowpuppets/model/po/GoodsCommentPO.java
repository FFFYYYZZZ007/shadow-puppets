package top.fuyuaaa.shadowpuppets.model.po;

import lombok.Data;
import top.fuyuaaa.shadowpuppets.model.BaseModel;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-26 18:24
 */
@Data
@SuppressWarnings("all")
public class GoodsCommentPO extends BaseModel {
    private static final long serialVersionUID = -1919032761984568226L;
    private Integer id;
    private String orderId;
    private Integer userId;
    private String userName;
    private Integer goodsId;
    private Integer starLevel;
    private String content;
}
