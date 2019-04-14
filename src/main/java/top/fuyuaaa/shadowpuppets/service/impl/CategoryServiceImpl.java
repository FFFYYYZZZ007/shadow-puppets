package top.fuyuaaa.shadowpuppets.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.fuyuaaa.shadowpuppets.dao.CategoryDao;
import top.fuyuaaa.shadowpuppets.model.Category;
import top.fuyuaaa.shadowpuppets.service.CategoryService;

import java.util.List;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-08 22:54
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryDao categoryDao;

    @Override
    public String getCategoryNameById(Integer id) {
        return categoryDao.findCategoryNameById(id);
    }

    @Override
    public Category getCategoryByName(String categoryName) {
        return categoryDao.getCategoryByName(categoryName);
    }

    @Override
    public List<Category> getCategoryList() {
        return categoryDao.getCategoryList();
    }

    @Override
    public Boolean addCategory(Category category) {
        return categoryDao.insertCategory(category) > 0;
    }

    @Override
    public Boolean updateCategory(Category category) {
        return categoryDao.updateCategory(category) > 0;
    }

    @Override
    public Boolean removeCategory(Integer id) {
        return categoryDao.deleteCategory(id) > 0;
    }
}
