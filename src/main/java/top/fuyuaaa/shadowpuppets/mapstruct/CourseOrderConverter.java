package top.fuyuaaa.shadowpuppets.mapstruct;

import org.mapstruct.AfterMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import top.fuyuaaa.shadowpuppets.dao.UserDao;
import top.fuyuaaa.shadowpuppets.model.po.CourseOrderPO;
import top.fuyuaaa.shadowpuppets.model.vo.CourseOrderVO;
import top.fuyuaaa.shadowpuppets.model.vo.CourseVO;
import top.fuyuaaa.shadowpuppets.service.CourseService;
import top.fuyuaaa.shadowpuppets.common.utils.DateUtils;
import top.fuyuaaa.shadowpuppets.common.utils.SpringUtil;

import java.util.List;

/**
 * @author: fuyuaaa
 * @creat: 2019-05-02 21:08
 */
@Mapper(componentModel="spring")
@SuppressWarnings("all")
public abstract class CourseOrderConverter {

    @Autowired
    CourseService courseService;

    public final static CourseOrderConverter INSTANCE = Mappers.getMapper(CourseOrderConverter.class);

    public abstract CourseOrderVO toCourseOrderVO(CourseOrderPO courseOrderPO);

    @AfterMapping
    protected void afterToCourseOrderVO(CourseOrderPO source, @MappingTarget CourseOrderVO target){
        Integer courseId = source.getCourseId();
        target.setCourseOrderStatus(source.getCourseOrderStatus().desc());
        CourseService courseService = SpringUtil.getBean(CourseService.class);
        CourseVO courseVO = courseService.getCourseVOById(source.getCourseId());
        target.setCourseVO(courseVO);

        UserDao userDao = SpringUtil.getBean(UserDao.class);
        String userName = userDao.getById(source.getUserId()).getUserName();
        target.setUserName(userName);

        target.setDateCreate(DateUtils.formatDate(source.getDateCreate()));
        target.setDateUpdate(DateUtils.formatDate(source.getDateUpdate()));
    }

    @IterableMapping(elementTargetType = CourseOrderVO.class)
    public abstract List<CourseOrderVO> toCourseOrderVOList(List<CourseOrderPO> sourceList);


}
