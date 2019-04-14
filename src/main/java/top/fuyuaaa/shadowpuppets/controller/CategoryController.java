package top.fuyuaaa.shadowpuppets.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.fuyuaaa.shadowpuppets.common.Result;
import top.fuyuaaa.shadowpuppets.model.Category;
import top.fuyuaaa.shadowpuppets.service.CategoryService;

import java.util.List;

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

    @PostMapping
    public Result<List<Category>> getCategoryList(){
        return Result.success(categoryService.getCategoryList());
    }


}
