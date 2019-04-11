package top.fuyuaaa.shadowpuppets.model.po;

import lombok.Data;
import top.fuyuaaa.shadowpuppets.model.BaseModel;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-08 21:57
 */
@Data
public class GoodsPO extends BaseModel {
    private static final long serialVersionUID = 4079499934851096768L;
    private Integer id;
    private String goodsName;
    private Integer categoryId;
    private Double price;
    private String introduction;
    private String imagesUrls;
    private Integer onSale;
    private Integer quantity;
}
