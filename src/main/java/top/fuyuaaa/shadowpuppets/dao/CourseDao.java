package top.fuyuaaa.shadowpuppets.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.fuyuaaa.shadowpuppets.model.po.CoursePO;
import top.fuyuaaa.shadowpuppets.model.qo.CourseQO;
import top.fuyuaaa.shadowpuppets.model.vo.CourseSimpleVO;

import java.util.List;

/**
 * @author: fuyuaaa
 * @creat: 2019-05-01 15:53
 */
@Mapper
@SuppressWarnings("all")
public interface CourseDao {
    Integer insert(CoursePO coursePO);

    List<CoursePO> findList(CourseQO courseQO);

    @Select("select * from course order by paid_number desc limit #{limit}")
    List<CourseSimpleVO> findRecommendList(Integer limit);

    @Select("select * from course where id = #{id} limit 1")
    CoursePO findById(Integer id);

    Integer update(CoursePO coursePO);


    @Update("update course set paid_number = paid_number + 1, date_update=now() where id =#{courseId}")
    Integer paidNumberAdd(Integer courseId);

    @Delete("update course set date_delete = now() where id = #{id}")
    Integer delete(Integer id);
}
