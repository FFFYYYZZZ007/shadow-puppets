package top.fuyuaaa.shadowpuppets.model.vo;

import lombok.Data;
import top.fuyuaaa.shadowpuppets.model.BaseModel;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: fuyuaaa
 * @creat: 2019-03-27 21:34
 */
@Data
public class UserVO implements Serializable {
    private static final long serialVersionUID = 6095746942854078159L;
    private Integer id;
    private String userName;
    private String sex;
    private String birthday;
    private String tel;
}
