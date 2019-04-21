package top.fuyuaaa.shadowpuppets.model.vo;

import lombok.Data;
import top.fuyuaaa.shadowpuppets.model.BaseModel;

import java.io.Serializable;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-08 21:57
 */
@Data
public class GoodsVO implements Serializable {
    private static final long serialVersionUID = 6148835212385330758L;
    private Integer id;
    private String goodsName;
    private String categoryName;
    private Double price;
    private String introduction;
    private String mainImageUrl;
    private String imagesUrls;
    private String onSale;
    private Integer quantity;
}
