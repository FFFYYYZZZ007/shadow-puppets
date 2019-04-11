package top.fuyuaaa.shadowpuppets.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-12 00:03
 */
@Data
public class LoginUserInfo implements Serializable {
    private static final long serialVersionUID = -5384923922562793410L;
    private Integer id;
    private String userName;
    private String sex;
    private String birthday;
    private String tel;
}
