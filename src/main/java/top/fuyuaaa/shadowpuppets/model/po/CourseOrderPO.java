package top.fuyuaaa.shadowpuppets.model.po;

import lombok.Data;
import top.fuyuaaa.shadowpuppets.common.enums.CourseOrderStatusEnum;
import top.fuyuaaa.shadowpuppets.model.BaseModel;

import java.math.BigDecimal;

/**
 * @author: fuyuaaa
 * @creat: 2019-05-02 18:53
 */
@Data
public class CourseOrderPO extends BaseModel {
    private static final long serialVersionUID = 2234397891090088443L;
    private String id;
    private Integer userId;
    private Integer courseId;
    private BigDecimal dealPrice;
    private CourseOrderStatusEnum courseOrderStatus;
}
