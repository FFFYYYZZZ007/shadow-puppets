package top.fuyuaaa.shadowpuppets.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.fuyuaaa.shadowpuppets.common.Result;
import top.fuyuaaa.shadowpuppets.model.PageVO;
import top.fuyuaaa.shadowpuppets.model.qo.CommentQO;
import top.fuyuaaa.shadowpuppets.model.qo.OrderCommentQO;
import top.fuyuaaa.shadowpuppets.model.vo.CourseCommentVO;
import top.fuyuaaa.shadowpuppets.service.CourseCommentService;

/**
 * @author: fuyuaaa
 * @creat: 2019-05-04 23:07
 */
@RestController
@RequestMapping("/course/comment")
@Slf4j
public class CourseCommentController {

    @Autowired
    CourseCommentService courseCommentService;

    @PostMapping("/list")
    public Result<PageVO<CourseCommentVO>> getCommentList(@RequestBody CommentQO commentQO) {
        PageVO<CourseCommentVO> pageVO = courseCommentService.getListByCourseId(commentQO);
        return Result.success(pageVO);
    }

    @PostMapping("/add")
    public Result addComment(@RequestBody OrderCommentQO orderCommentQO) {
        courseCommentService.addComment(orderCommentQO);
        return Result.success().setMsg("评论成功");
    }

    @PostMapping("/remove")
    public Result removeComment(@RequestParam Integer commentId) {
        courseCommentService.removeComment(commentId);
        return Result.success();
    }
}
