package top.fuyuaaa.shadowpuppets.service;

import top.fuyuaaa.shadowpuppets.model.PageVO;
import top.fuyuaaa.shadowpuppets.model.bo.CourseBO;
import top.fuyuaaa.shadowpuppets.model.qo.CourseQO;
import top.fuyuaaa.shadowpuppets.model.vo.CourseSimpleVO;
import top.fuyuaaa.shadowpuppets.model.vo.CourseVO;

import java.util.List;


/**
 * @author: fuyuaaa
 * @creat: 2019-05-01 16:19
 */
public interface CourseService {
    void addCourse(CourseBO courseBO);

    PageVO<CourseSimpleVO> getSimpleVOList(CourseQO courseQO);

    List<CourseSimpleVO> getRecommendCourseList();

    CourseVO getCourseVOById(Integer id);

    //==============================  manager  ==============================

    PageVO<CourseVO> getVOList(CourseQO courseQO);

    void update(CourseBO courseBO);

    void delete(Integer id);
}
