package top.fuyuaaa.shadowpuppets.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: fuyuaaa
 * @creat: 2019-05-04 22:40
 */
@Data
public class CourseCommentVO implements Serializable {
    private static final long serialVersionUID = -4104877190707099865L;
    private Integer id;
    private Integer userId;
    private String userName;
    private Integer courseId;
    private Integer starLevel;
    private String content;
    private String dateCreate;
}
