package top.fuyuaaa.shadowpuppets.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.fuyuaaa.shadowpuppets.dao.CategoryDao;
import top.fuyuaaa.shadowpuppets.service.CategoryService;

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
}
