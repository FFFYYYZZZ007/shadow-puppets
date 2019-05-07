package top.fuyuaaa.shadowpuppets.controller;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.fuyuaaa.shadowpuppets.common.annotations.ValidateAdmin;
import top.fuyuaaa.shadowpuppets.common.Result;
import top.fuyuaaa.shadowpuppets.model.Category;
import top.fuyuaaa.shadowpuppets.model.vo.CategoryVO;
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
    @Autowired
    RedissonClient redissonClient;

    @GetMapping("/test")
    public Long test(){
        RKeys keys = redissonClient.getKeys();
        RBucket<Long> bucket = redissonClient.getBucket("token:15967121280");
        return bucket.get();
    }

    @PostMapping("/list")
    public Result<List<CategoryVO>> getCategoryList() {
        List<CategoryVO> categoryVOList = categoryService.getCategoryVOList();
        return Result.success(categoryVOList);
    }

    @PostMapping("/add")
    @ValidateAdmin
    public Result addCateGory(@RequestBody Category category) {
        categoryService.addCategory(category);
        return Result.success().setMsg("添加类别成功");
    }

    @PostMapping("/update")
    @ValidateAdmin
    public Result updateCateGory(@RequestBody Category category) {
        categoryService.updateCategory(category);
        return Result.success().setMsg("修改类别成功");
    }

    @PostMapping("/remove")
    @ValidateAdmin
    public Result removeCateGory(@RequestParam Integer id) {
        categoryService.removeCategory(id);
        return Result.success().setMsg("删除类别成功");
    }
}
