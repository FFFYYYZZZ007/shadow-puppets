package top.fuyuaaa.shadowpuppets.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.fuyuaaa.shadowpuppets.common.enums.ExEnum;
import top.fuyuaaa.shadowpuppets.dao.CategoryDao;
import top.fuyuaaa.shadowpuppets.common.exceptions.CategoryException;
import top.fuyuaaa.shadowpuppets.mapstruct.CategoryConverter;
import top.fuyuaaa.shadowpuppets.model.Category;
import top.fuyuaaa.shadowpuppets.model.vo.CategoryVO;
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
    public List<Category> getCategoryList() {
        return categoryDao.getCategoryList();
    }

    @Override
    public List<CategoryVO> getCategoryVOList() {
        List<Category> categoryList = this.getCategoryList();
        return CategoryConverter.INSTANCE.toCategoryVOList(categoryList);
    }

    @Override
    public void addCategory(Category category) {
        if (null == category || StringUtils.isEmpty(category.getCategoryName())) {
            throw new CategoryException(ExEnum.CATEGORY_OPERATE_ERROR.getMsg());
        }
        //校验是否存在
        Category categoryByName = categoryDao.getCategoryByName(category.getCategoryName());
        String errorMessage = ExEnum.CATEGORY_ADD_ERROR.getMsg() + "，该类别已存在";
        if (null != categoryByName) {
            throw new CategoryException(errorMessage);
        }
        if (categoryDao.insertCategory(category) != 1) {
            throw new CategoryException(ExEnum.CATEGORY_ADD_ERROR.getMsg());
        }
    }

    @Override
    public void updateCategory(Category category) {
        if (null == category || null == category.getId() || StringUtils.isEmpty(category.getCategoryName())) {
            throw new CategoryException(ExEnum.CATEGORY_OPERATE_ERROR.getMsg());
        }
        if (categoryDao.updateCategory(category) != 1) {
            throw new CategoryException(ExEnum.CATEGORY_UPDATE_ERROR.getMsg());
        }
    }

    @Override
    public void removeCategory(Integer id) {
        if (null == id) {
            throw new CategoryException(ExEnum.CATEGORY_OPERATE_ERROR.getMsg());
        }
        if (categoryDao.deleteCategory(id) != 1) {
            throw new CategoryException(ExEnum.CATEGORY_REMOVE_ERROR.getMsg());
        }
    }
}
