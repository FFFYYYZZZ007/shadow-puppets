package top.fuyuaaa.shadowpuppets.dao;

import org.apache.ibatis.annotations.*;
import top.fuyuaaa.shadowpuppets.model.po.CourseOrderPO;
import top.fuyuaaa.shadowpuppets.model.po.CoursePO;
import top.fuyuaaa.shadowpuppets.model.qo.CourseOrderQO;
import top.fuyuaaa.shadowpuppets.model.qo.CourseQO;
import top.fuyuaaa.shadowpuppets.model.vo.CourseSimpleVO;

import java.util.List;

/**
 * @author: fuyuaaa
 * @creat: 2019-05-01 15:53
 */
@Mapper
@SuppressWarnings("all")
public interface CourseOrderDao {
    @Insert("insert into course_order (id, user_id, course_id, deal_price, course_order_status, date_create, date_update) " +
            "values (#{id}, #{userId}, #{courseId}, #{dealPrice}, #{courseOrderStatus}, now(), now())")
    Integer insert(CourseOrderPO courseOrderPO);

    @Select("select * from course_order where id = #{id}")
    CourseOrderPO getById(String id);

    @Update("update course_order set date_update=now(), course_order_status=#{courseOrderStatus} where id=#{id} ")
    Integer updateStatus(CourseOrderPO courseOrderPO);

    List<CourseOrderPO> findList(CourseOrderQO courseOrderQO);
}
