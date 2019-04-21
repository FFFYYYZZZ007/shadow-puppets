package top.fuyuaaa.shadowpuppets.model.qo;

import lombok.Data;
import top.fuyuaaa.shadowpuppets.model.BaseModel;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-16 20:58
 */
@Data
public class GoodsListQO extends BaseQO {
    private static final long serialVersionUID = 7382759861517138742L;
    private Integer sort;
    private Integer category;
    private String keyword;
    private Integer onSale;
    /**
     * 只能看到在售的商品
     * 1-是
     */
    private Integer onlySeeOnSale;
}
