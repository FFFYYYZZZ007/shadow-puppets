package top.fuyuaaa.shadowpuppets.dao;

import org.apache.ibatis.annotations.*;
import top.fuyuaaa.shadowpuppets.model.Category;

import java.util.List;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-08 22:50
 */
@Mapper
public interface CategoryDao {

    @Select("select category_name from category where id =#{id} limit 1")
    String findCategoryNameById(Integer id);



    /**
     * 根据类别名 获取 类别
     *
     * @param categoryName 类别名
     * @return top.fuyuaaa.shadowpuppets.model.Category
     */
    @Select("select * from category where category_name = #{categoryName} limit 1")
    Category getCategoryByName(String categoryName);

    /**
     * 获取类别列表
     * @return 类别列表
     */
    @Select("select * from category order where date_delete is null by date_update desc")
    List<Category> getCategoryList();

    /**
     * 添加类别
     * @param category 类别
     * @return true/false
     */
    @Insert("insert into category (category_name, date_create, date_update) values (#{categoryName}, now(), now())")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    Integer insertCategory(Category category);

    /**
     * 更新类别
     * @param category 类别
     * @return true/false
     */
    @Update("update category set category_name = {categoryName}, date_update = now() where id = #{id}")
    Integer updateCategory(Category category);

    /**
     * 删除类别
     * @param id 类别ID
     * @return true/false
     */
    @Delete("update category set date_delete = now() where id = #{id} limit 1")
    Integer deleteCategory(Integer id);
}
