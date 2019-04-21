package top.fuyuaaa.shadowpuppets.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.fuyuaaa.shadowpuppets.common.Result;
import top.fuyuaaa.shadowpuppets.model.Category;
import top.fuyuaaa.shadowpuppets.model.vo.CategoryVO;
import top.fuyuaaa.shadowpuppets.service.CategoryService;
import top.fuyuaaa.shadowpuppets.util.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-14 22:00
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping("/list")
    public Result<List<CategoryVO>> getCategoryList() {
        List<Category> categoryList = categoryService.getCategoryList();
        List<CategoryVO> categoryVOList = categoryList.stream().map(category -> {
            CategoryVO categoryVO = BeanUtils.copyProperties(category, CategoryVO.class);
            categoryVO.setDateUpdate(DateFormatUtils.format(category.getDateUpdate(), "yyyy-MM-dd HH:mm:ss"));
            return categoryVO;
        }).collect(Collectors.toList());
        return Result.success(categoryVOList);
    }

    @PostMapping("/add")
    public Result addCateGory(@RequestBody Category category) {
        if (null == category ){
            return Result.fail("添加类别失败");
        }
        Boolean success = categoryService.addCategory(category);
        return success ? Result.success() : Result.fail("添加类别失败");
    }

    @PostMapping("/update")
    public Result updateCateGory(@RequestBody Category category) {
        if (null == category || null == category.getId()) {
            return Result.fail("更新类别失败");
        }
        Boolean success = categoryService.updateCategory(category);
        return success ? Result.success() : Result.fail("更新类别失败");
    }

    @PostMapping("/remove")
    public Result removeCateGory(@RequestParam Integer id) {
        if (null == id) {
            return Result.fail("删除类别失败");
        }
        Boolean success = categoryService.removeCategory(id);
        return success ? Result.success() : Result.fail("删除类别失败");
    }
}
