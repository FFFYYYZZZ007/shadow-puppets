package top.fuyuaaa.shadowpuppets.model.qo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-18 12:58
 */
@Data
public class UserLoginQO implements Serializable {
    private static final long serialVersionUID = -3754154964596030077L;
    private String userName;
    private String password;
    private String tel;
    private String code;
}
