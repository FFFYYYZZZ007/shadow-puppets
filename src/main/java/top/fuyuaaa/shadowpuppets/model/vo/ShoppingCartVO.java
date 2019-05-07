package top.fuyuaaa.shadowpuppets.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import top.fuyuaaa.shadowpuppets.model.bo.GoodsBO;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-11 00:08
 */
@Data
public class ShoppingCartVO implements Serializable {
    private static final long serialVersionUID = 54524992817888014L;
    private Integer key;
    private Integer goodsId;
    private String goodsName;
    private Double price;
    private Integer num;
    private Date dateCreate;
}
