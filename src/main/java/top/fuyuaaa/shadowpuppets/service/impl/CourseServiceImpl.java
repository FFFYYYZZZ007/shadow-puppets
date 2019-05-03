package top.fuyuaaa.shadowpuppets.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.fuyuaaa.shadowpuppets.common.enums.ExEnum;
import top.fuyuaaa.shadowpuppets.dao.CourseDao;
import top.fuyuaaa.shadowpuppets.exceptions.CourseException;
import top.fuyuaaa.shadowpuppets.mapstruct.CourseConverter;
import top.fuyuaaa.shadowpuppets.model.PageVO;
import top.fuyuaaa.shadowpuppets.model.bo.CourseBO;
import top.fuyuaaa.shadowpuppets.model.po.CoursePO;
import top.fuyuaaa.shadowpuppets.model.qo.CourseQO;
import top.fuyuaaa.shadowpuppets.model.vo.CourseSimpleVO;
import top.fuyuaaa.shadowpuppets.model.vo.CourseVO;
import top.fuyuaaa.shadowpuppets.service.CourseService;

import java.util.List;

/**
 * @author: fuyuaaa
 * @creat: 2019-05-01 16:19
 */
@Service
@SuppressWarnings("all")
public class CourseServiceImpl implements CourseService {

    @Autowired
    CourseDao courseDao;

    @Override
    public void addCourse(CourseBO courseBO) {
        CoursePO coursePO = CourseConverter.INSTANCE.toCoursePO(courseBO);
        Integer result = courseDao.insert(coursePO);
        if (1 != result) {
            throw new CourseException(ExEnum.COURSE_ADD_ERROR.getMsg());
        }
    }

    @Override
    public PageVO<CourseSimpleVO> getSimpleVOList(CourseQO courseQO) {
        PageInfo<CoursePO> pageInfo = getList(courseQO);
        Integer total = Integer.valueOf(String.valueOf(pageInfo.getTotal()));
        List<CourseSimpleVO> courseVOSimpleList = CourseConverter.INSTANCE.toCourseSimpleVOList(pageInfo.getList());
        PageVO<CourseSimpleVO> pageVO = new PageVO<>(pageInfo.getPageNum(), pageInfo.getPageSize(), total, courseVOSimpleList);
        return pageVO;
    }

    @Override
    public List<CourseSimpleVO> getRecommendCourseList() {
        List<CourseSimpleVO> recommendList = courseDao.findRecommendList(3);
        return recommendList;
    }

    @Override
    public CourseVO getCourseVOById(Integer id) {
        CoursePO coursePO = courseDao.findById(id);
        return CourseConverter.INSTANCE.toCourseVO(coursePO);
    }

    //==============================  manager  ==============================

    @Override
    public PageVO<CourseVO> getVOList(CourseQO courseQO) {
        PageInfo<CoursePO> pageInfo = getList(courseQO);
        Integer total = Integer.valueOf(String.valueOf(pageInfo.getTotal()));
        List<CourseVO> courseVOSimpleList = CourseConverter.INSTANCE.toCourseVOList(pageInfo.getList());
        PageVO<CourseVO> pageVO = new PageVO<>(pageInfo.getPageNum(), pageInfo.getPageSize(), total, courseVOSimpleList);
        return pageVO;
    }

    @Override
    public void update(CourseBO courseBO) {
        CoursePO coursePO = CourseConverter.INSTANCE.toCoursePO(courseBO);
        Integer result = courseDao.update(coursePO);
        if (result != 1) {
            throw new CourseException(ExEnum.COURSE_UPDATE_ERROR.getMsg());
        }
    }

    @Override
    public void delete(Integer id) {
        Integer result = courseDao.delete(id);
        if (result != 1) {
            throw new CourseException(ExEnum.COURSE_DELETE_ERROR.getMsg());
        }
    }

    //==============================  private help methods  ==============================

    private PageInfo<CoursePO> getList(CourseQO courseQO){
        PageHelper.startPage(courseQO.getPageNum(), courseQO.getPageSize());
        List<CoursePO> coursePOList = courseDao.findList(courseQO);
        PageInfo<CoursePO> pageInfo = new PageInfo<>(coursePOList);
        return pageInfo;
    }
}
