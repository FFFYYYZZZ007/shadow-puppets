package top.fuyuaaa.shadowpuppets.model.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: fuyuaaa
 * @creat: 2019-05-03 18:20
 */
@Data
public class UserPasswordBO implements Serializable {
    private static final long serialVersionUID = -8955742875707657864L;
    private String oldPassword;
    private String newPassword;
    private String repeatNewPassword;
}
