package top.fuyuaaa.shadowpuppets.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.fuyuaaa.shadowpuppets.common.annotations.NeedLogin;
import top.fuyuaaa.shadowpuppets.common.Result;
import top.fuyuaaa.shadowpuppets.model.PageVO;
import top.fuyuaaa.shadowpuppets.model.qo.CommentQO;
import top.fuyuaaa.shadowpuppets.model.qo.OrderCommentQO;
import top.fuyuaaa.shadowpuppets.model.vo.GoodsCommentVO;
import top.fuyuaaa.shadowpuppets.service.GoodsCommentService;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-26 23:04
 */
@RestController
@RequestMapping("/comment")
@Slf4j
public class CommentController {
    @Autowired
    GoodsCommentService commentService;

    @PostMapping("/list")
    public Result<PageVO<GoodsCommentVO>> getCommentList(@RequestBody CommentQO commentQO) {
        PageVO<GoodsCommentVO> pageVO = commentService.getListByGoodsId(commentQO);
        return Result.success(pageVO);
    }

    @PostMapping("/add")
    @NeedLogin
    public Result addComment(@RequestBody OrderCommentQO orderCommentQO) {
        commentService.addComment(orderCommentQO);
        return Result.success().setMsg("评论成功");
    }

    @PostMapping("/remove")
    @NeedLogin
    public Result removeComment(@RequestParam Integer commentId) {
        commentService.removeComment(commentId);
        return Result.success();
    }
}
