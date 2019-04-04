package top.fuyuaaa.shadowpuppets.model.dbo;

import lombok.Data;
import top.fuyuaaa.shadowpuppets.model.BaseModel;

import java.util.Date;

/**
 * @author: fuyuaaa
 * @creat: 2019-03-27 21:34
 */
@Data
public class UserDO extends BaseModel {
    private static final long serialVersionUID = 9020317726771799362L;
    private Integer id;
    private String userName;
    private String password;
    private Integer sex;
    private Date birthday;
    private String tel;
}
