package top.fuyuaaa.shadowpuppets.model.qo;

import lombok.Data;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-27 00:57
 */
@Data
public class OrderCommentQO {
    private String orderId;
    private Integer starLevel;
    private String content;
}
