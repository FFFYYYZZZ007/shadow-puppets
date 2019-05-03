package top.fuyuaaa.shadowpuppets.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.fuyuaaa.shadowpuppets.model.BaseModel;

import java.math.BigDecimal;

/**
 * @author: fuyuaaa
 * @creat: 2019-05-01 15:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoursePO extends BaseModel {
    private static final long serialVersionUID = 4730894659424795259L;
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

    public CoursePO(String courseName, String courseIntroduction, String mainImageUrl, BigDecimal courseOriginPrice, BigDecimal courseDiscountPrice, String teacherName, Integer courseHours, Integer paidNumber, String coursePlace, String courseContent) {
        this.courseName = courseName;
        this.courseIntroduction = courseIntroduction;
        this.mainImageUrl = mainImageUrl;
        this.courseOriginPrice = courseOriginPrice;
        this.courseDiscountPrice = courseDiscountPrice;
        this.teacherName = teacherName;
        this.courseHours = courseHours;
        this.paidNumber = paidNumber;
        this.coursePlace = coursePlace;
        this.courseContent = courseContent;
    }
}
