package top.fuyuaaa.shadowpuppets.model.vo;

import lombok.Data;
import top.fuyuaaa.shadowpuppets.model.BaseModel;

import java.io.Serializable;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-26 18:24
 */
@Data
@SuppressWarnings("all")
public class GoodsCommentVO implements Serializable {
    private static final long serialVersionUID = 7913501837943590586L;
    private Integer id;
    private Integer userId;
    private String userName;
    private Integer goodsId;
    private Integer starLevel;
    private String content;
    private String dateCreate;
}
