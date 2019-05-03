package top.fuyuaaa.shadowpuppets.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: fuyuaaa
 * @creat: 2019-05-01 15:44
 */
@Data
public class CourseSimpleVO implements Serializable {
    private static final long serialVersionUID = 853893204589797102L;
    private Integer id;
    private String courseName;
    private String mainImageUrl;
    private BigDecimal courseDiscountPrice;
    private Integer paidNumber;
}
