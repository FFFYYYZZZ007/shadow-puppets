package top.fuyuaaa.shadowpuppets.model.bo;

import lombok.Data;
import top.fuyuaaa.shadowpuppets.model.BaseModel;

import java.util.Date;

/**
 * @author: fuyuaaa
 * @creat: 2019-03-27 21:34
 */
@Data
public class UserBO extends BaseModel {
    private static final long serialVersionUID = 6095746942854078159L;
    private Integer id;
    private String userName;
    private String password;
    private Integer sex;
    private Date birthday;
    private String tel;
}
