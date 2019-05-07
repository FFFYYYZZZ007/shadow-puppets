package top.fuyuaaa.shadowpuppets.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.fuyuaaa.shadowpuppets.common.annotations.ValidateAdmin;
import top.fuyuaaa.shadowpuppets.common.Result;
import top.fuyuaaa.shadowpuppets.common.enums.CourseOrderStatusEnum;
import top.fuyuaaa.shadowpuppets.model.PageVO;
import top.fuyuaaa.shadowpuppets.model.bo.CourseBO;
import top.fuyuaaa.shadowpuppets.model.qo.CourseQO;
import top.fuyuaaa.shadowpuppets.model.vo.CourseVO;
import top.fuyuaaa.shadowpuppets.service.CourseOrderService;
import top.fuyuaaa.shadowpuppets.service.CourseService;
import top.fuyuaaa.shadowpuppets.common.utils.FileUtils;
import top.fuyuaaa.shadowpuppets.common.utils.UploadUtil;

import java.io.File;

/**
 * @author: fuyuaaa
 * @creat: 2019-05-01 15:43
 */
@RestController
@RequestMapping("/course/manager")
@Slf4j
public class CourseManagerController {

    @Autowired
    CourseService courseService;
    @Autowired
    CourseOrderService courseOrderService;

    @PostMapping("/list")
    @ValidateAdmin
    public Result<PageVO<CourseVO>> getList(@RequestBody CourseQO courseQO){
        PageVO<CourseVO> pageVO = courseService.getVOList(courseQO);
        return Result.success(pageVO);
    }

    @PostMapping("/add")
    @ValidateAdmin
    public Result<Boolean> add(@RequestBody CourseBO courseBO) {
        courseBO.setPaidNumber(0);
        courseService.addCourse(courseBO);
        return Result.success(true).setMsg("修改课程成功");
    }

    @PostMapping("/update")
    @ValidateAdmin
    public Result<Boolean> update(@RequestBody CourseBO courseBO) {
        courseService.update(courseBO);
        return Result.success(true).setMsg("修改课程成功");
    }

    @PostMapping("/delete")
    @ValidateAdmin
    public Result<Boolean> delete(@RequestParam Integer id) {
        courseService.delete(id);
        return Result.success(true).setMsg("删除课程成功");
    }

    @PostMapping("/image/main/upload")
    @ValidateAdmin
    public Result<String> uploadGoodsMainImage(@RequestParam("file") MultipartFile multipartFile) {
        File file = FileUtils.convertMultipartFile2File(multipartFile);
        String resultUrl = UploadUtil.uploadCourseMain2OSS(file);
        return Result.success(resultUrl);
    }

    //==============================  order related  ==============================

    @PostMapping("/order/endCourse")
    @ValidateAdmin
    public Result<Boolean> endCourse(@RequestParam String orderId){
        courseOrderService.updateStatus(orderId, CourseOrderStatusEnum.PENDING_CONFIRMED.code());
        return Result.success(true).setMsg("结课成功");
    }
}
