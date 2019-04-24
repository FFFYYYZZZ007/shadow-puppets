package top.fuyuaaa.shadowpuppets.model.bo;

import lombok.Data;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-24 11:35
 */
@SuppressWarnings("all")
@Data
public class AlipayBO {
    private String out_trade_no;
    private String product_code;
    private Double total_amount;
    private String subject;
    private String body;
}
