package top.fuyuaaa.shadowpuppets.model.bo;

import lombok.Data;
import top.fuyuaaa.shadowpuppets.model.BaseModel;

import java.math.BigDecimal;

/**
 * @author: fuyuaaa
 * @creat: 2019-05-01 15:44
 */
@Data
public class CourseBO extends BaseModel {
    private static final long serialVersionUID = 34634280326264384L;
    private Integer id;
    private String courseName;
    private String courseIntroduction;
    private String mainImageUrl;
    private BigDecimal courseOriginPrice;
    private BigDecimal courseDiscountPrice;
    private String teacherName;
    private Integer courseHours;
    private Integer paidNumber;
    private String coursePlace;
    private String courseContent;
}
