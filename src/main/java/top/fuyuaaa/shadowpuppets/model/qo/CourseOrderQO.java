package top.fuyuaaa.shadowpuppets.model.qo;

import lombok.Data;

/**
 * @author: fuyuaaa
 * @creat: 2019-05-02 20:49
 */
@Data
public class CourseOrderQO extends BaseQO {
    private static final long serialVersionUID = 4073892651373011225L;
    private Integer courseOrderStatus;
    private String keyword;
    private Integer userId;
}
