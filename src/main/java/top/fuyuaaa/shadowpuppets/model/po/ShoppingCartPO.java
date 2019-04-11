package top.fuyuaaa.shadowpuppets.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.fuyuaaa.shadowpuppets.model.BaseModel;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-11 00:04
 */
@Data
@NoArgsConstructor
public class ShoppingCartPO extends BaseModel {
    private static final long serialVersionUID = -383318293233492554L;
    private Integer id;
    private Integer userId;
    private Integer goodsId;
    private Integer num;

    public ShoppingCartPO(Integer userId, Integer goodsId, Integer num) {
        this.userId = userId;
        this.goodsId = goodsId;
        this.num = num;
    }
}
