package top.fuyuaaa.shadowpuppets.model.po;

import lombok.Data;
import top.fuyuaaa.shadowpuppets.model.BaseModel;

/**
 * @author: fuyuaaa
 * @creat: 2019-05-04 22:35
 */
@Data
public class CourseCommentPO extends BaseModel {
    private static final long serialVersionUID = 5798783325891311093L;
    private Integer id;
    private Integer userId;
    private String orderId;
    private String userName;
    private Integer courseId;
    private Integer starLevel;
    private String content;
}
