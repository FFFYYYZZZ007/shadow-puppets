package top.fuyuaaa.shadowpuppets.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: fuyuaaa
 * @creat: 2019-05-02 19:06
 */
@Data
public class CourseOrderVO implements Serializable {
    private static final long serialVersionUID = 7905768380000142239L;
    private String id;
    private String userName;
    private CourseVO courseVO;
    private BigDecimal dealPrice;
    private String courseOrderStatus;
    private String dateCreate;
    private String dateUpdate;
}
