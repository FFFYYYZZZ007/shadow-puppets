package top.fuyuaaa.shadowpuppets.mapstruct;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import top.fuyuaaa.shadowpuppets.model.bo.CourseBO;
import top.fuyuaaa.shadowpuppets.model.po.CoursePO;
import top.fuyuaaa.shadowpuppets.model.vo.CourseSimpleVO;
import top.fuyuaaa.shadowpuppets.model.vo.CourseVO;

import java.util.List;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-26 21:59
 */
@Mapper(componentModel="spring")
@SuppressWarnings("all")
public abstract class CourseConverter {
    public final static CourseConverter INSTANCE = Mappers.getMapper(CourseConverter.class);

    //==============================  PO => VO  ==============================

    public abstract CourseVO toCourseVO(CoursePO coursePO);

    @AfterMapping
    protected void afterToCourseVO(CoursePO source, @MappingTarget CourseVO target){
    }

    @IterableMapping(elementTargetType = CourseVO.class)
    public abstract List<CourseVO> toCourseVOList(List<CoursePO> sourceList);

    //==============================  BO => PO  ==============================

    public abstract CoursePO toCoursePO(CourseBO courseBO);

    //==============================  PO => SimpleVO  ==============================

    public abstract CourseSimpleVO toCourseSimpleVO(CoursePO coursePO);

    @IterableMapping(elementTargetType = CourseSimpleVO.class)
    public abstract List<CourseSimpleVO> toCourseSimpleVOList(List<CoursePO> sourceList);


}
