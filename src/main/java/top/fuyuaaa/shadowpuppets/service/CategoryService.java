package top.fuyuaaa.shadowpuppets.service;

import top.fuyuaaa.shadowpuppets.model.Category;
import top.fuyuaaa.shadowpuppets.model.vo.CategoryVO;

import java.util.List;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-08 22:54
 */
public interface CategoryService {

    /**
     * 根据类别id 获取 类别名
     *
     * @param id 类别id
     * @return top.fuyuaaa.shadowpuppets.model.Category
     */
    String getCategoryNameById(Integer id);

    /**
     * 获取类别列表
     *
     * @return 类别列表
     */
    List<Category> getCategoryList();


    /**
     * 获取类别列表
     *
     * @return 类别列表
     */
    List<CategoryVO> getCategoryVOList();

    /**
     * 添加类别
     *
     * @param category 类别
     * @return true/false
     */
    void addCategory(Category category);

    /**
     * 更新类别
     *
     * @param category 类别
     * @return true/false
     */
    void updateCategory(Category category);

    /**
     * 删除类别
     *
     * @param id 类别ID
     * @return true/false
     */
    void removeCategory(Integer id);

}
