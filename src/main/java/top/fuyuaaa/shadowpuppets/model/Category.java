package top.fuyuaaa.shadowpuppets.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-14 22:01
 */
@Data
public class Category extends BaseModel {
    private static final long serialVersionUID = 1733620545147135973L;
    private Integer id;
    private String categoryName;
}
