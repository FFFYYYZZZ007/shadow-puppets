package top.fuyuaaa.shadowpuppets.mapstruct;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import top.fuyuaaa.shadowpuppets.dao.CourseOrderDao;
import top.fuyuaaa.shadowpuppets.holder.LoginUserHolder;
import top.fuyuaaa.shadowpuppets.model.po.CourseCommentPO;
import top.fuyuaaa.shadowpuppets.model.po.CourseOrderPO;
import top.fuyuaaa.shadowpuppets.model.qo.OrderCommentQO;
import top.fuyuaaa.shadowpuppets.model.vo.CourseCommentVO;
import top.fuyuaaa.shadowpuppets.util.DateUtils;
import top.fuyuaaa.shadowpuppets.util.SpringUtil;

import java.util.List;

/**
 * @author: fuyuaaa
 * @creat: 2019-05-04 22:51
 */
@Mapper(componentModel="spring")
public abstract class CourseCommentConverter {
    public final static CourseCommentConverter INSTANCE = Mappers.getMapper(CourseCommentConverter.class);

    public abstract CourseCommentPO toCourseCommentPO(OrderCommentQO orderCommentQO);

    @AfterMapping
    protected void afterToCourseCommentPO(OrderCommentQO source, @MappingTarget CourseCommentPO target){
        //userId
        Integer userId = LoginUserHolder.instance().get().getId();
        target.setUserId(userId);

        //courseId
        CourseOrderDao courseOrderDao = SpringUtil.getBean(CourseOrderDao.class);
        CourseOrderPO courseOrderPO = courseOrderDao.getById(source.getOrderId());
        target.setCourseId(courseOrderPO.getCourseId());
    }

    //==============================  PO => VO  ==============================

    @Mapping(target = "dateCreate",ignore = true)
    public abstract CourseCommentVO toCourseCommentVO(CourseCommentPO courseCommentPO);

    @AfterMapping
    protected void afterToCourseCommentVO(CourseCommentPO source, @MappingTarget CourseCommentVO target){
        target.setDateCreate(DateUtils.formatDate(source.getDateCreate()));
    }

    @IterableMapping(elementTargetType = CourseCommentVO.class)
    public abstract List<CourseCommentVO> toCourseCommentVOList(List<CourseCommentPO> sourceList);

}
