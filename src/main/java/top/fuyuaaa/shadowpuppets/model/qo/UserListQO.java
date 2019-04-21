package top.fuyuaaa.shadowpuppets.model.qo;

import lombok.Data;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-18 00:59
 */
@Data
public class UserListQO extends BaseQO {
    private static final long serialVersionUID = -2992637483723862038L;
    private String tel;
    private String keyword;
    private Integer sex;
}
