package top.fuyuaaa.shadowpuppets.model.vo;

import lombok.Data;
import top.fuyuaaa.shadowpuppets.model.BaseModel;

import java.io.Serializable;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-20 00:15
 */
@Data
public class CategoryVO implements Serializable {
    private static final long serialVersionUID = 6700010148522337672L;
    private Integer id;
    private String categoryName;
    private String dateUpdate;
}
