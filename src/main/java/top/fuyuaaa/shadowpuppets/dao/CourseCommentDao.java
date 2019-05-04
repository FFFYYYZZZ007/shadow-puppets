package top.fuyuaaa.shadowpuppets.dao;

import org.apache.ibatis.annotations.*;
import top.fuyuaaa.shadowpuppets.model.po.CourseCommentPO;

import java.util.List;

/**
 * @author: fuyuaaa
 * @creat: 2019-05-04 22:37
 */
@Mapper
public interface CourseCommentDao {
    @Insert("insert into course_comment (user_id,course_id,star_level,content,date_create,date_update) " +
            "values (#{userId},#{courseId},#{starLevel},#{content},now(),now())")
    @Options(keyColumn = "id",keyProperty = "id",useGeneratedKeys = true)
    Integer insert(CourseCommentPO courseCommentPO);

    @Delete("update course_comment set date_delete = now() where id = #{commentId}")
    Integer delete(Integer commentId);

    @Select("select c.*,u.user_name from course_comment c left join user u on c.user_id = u.id " +
            "where c.course_id = #{courseId} order by c.date_create desc")
    List<CourseCommentPO> findListByCourseId(Integer courseId);
}
