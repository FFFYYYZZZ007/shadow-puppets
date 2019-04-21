package top.fuyuaaa.shadowpuppets.model.bo;

import lombok.Data;
import top.fuyuaaa.shadowpuppets.model.BaseModel;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-08 21:57
 */
@Data
public class GoodsBO extends BaseModel {
    private static final long serialVersionUID = 2782145751386026327L;
    private Integer id;
    private String goodsName;
    private Integer categoryId;
    private Double price;
    private String introduction;
    private String mainImageUrl;
    private String imagesUrls;
    private Integer onSale;
    private Integer quantity;
}
