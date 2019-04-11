package top.fuyuaaa.shadowpuppets.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-08 22:50
 */
@Mapper
public interface CategoryDao {

    @Select("select category_name from category where id =#{id} limit 1")
    String findCategoryNameById(Integer id);
}
