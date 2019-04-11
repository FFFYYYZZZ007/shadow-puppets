package top.fuyuaaa.shadowpuppets.model.bo;

import lombok.Data;
import lombok.NoArgsConstructor;
import top.fuyuaaa.shadowpuppets.model.BaseModel;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-11 00:07
 */
@Data
@NoArgsConstructor
public class ShoppingCartBO extends BaseModel {
    private static final long serialVersionUID = -7321776377190148481L;
    private Integer id;
    private Integer userId;
    private Integer goodsId;
    private Integer num;

    public ShoppingCartBO(Integer userId, Integer goodsId, Integer num) {
        this.userId = userId;
        this.goodsId = goodsId;
        this.num = num;
    }
}
