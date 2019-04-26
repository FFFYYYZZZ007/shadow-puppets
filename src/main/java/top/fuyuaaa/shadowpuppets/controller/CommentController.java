package top.fuyuaaa.shadowpuppets.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.fuyuaaa.shadowpuppets.common.Result;
import top.fuyuaaa.shadowpuppets.exceptions.ParamException;
import top.fuyuaaa.shadowpuppets.model.PageVO;
import top.fuyuaaa.shadowpuppets.model.po.GoodsCommentPO;
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
    public Result<PageVO<GoodsCommentVO>> getCommentList(@RequestBody CommentQO commentQO){
        this.fillCommentQO(commentQO);
        PageVO<GoodsCommentVO> pageVO = commentService.getListByGoodsId(commentQO);
        return Result.success(pageVO);
    }

    @PostMapping("/add")
    public Result addComment(@RequestBody OrderCommentQO orderCommentQO){
        this.validateCommentParam(orderCommentQO);
        commentService.addComment(orderCommentQO);
        return Result.success().setMsg("评论成功");
    }


    @PostMapping("/add/like")
    public Result addLikeCount(@RequestParam Integer commentId){
        commentService.updateLikeCount(commentId);
        return Result.success();
    }

    @PostMapping("/remove")
    public Result removeComment(@RequestParam Integer commentId){
        commentService.removeComment(commentId);
        return Result.success();
    }

    //==============================  private help method  ==============================


    private void validateCommentParam(OrderCommentQO orderCommentQO) {
        if (StringUtils.isEmpty(orderCommentQO.getContent())) {
            throw new ParamException("请输入评价内容");
        }
        if (null == orderCommentQO.getStarLevel() || 0 == orderCommentQO.getStarLevel()) {
            throw new ParamException("请选择满意程度");
        }
    }

    private void fillCommentQO(CommentQO commentQO) {
        if (null == commentQO) {
            commentQO = new CommentQO();
        }
        if (null == commentQO.getPageNum() || 0 >= commentQO.getPageNum()) {
            commentQO.setPageNum(1);
        }
        if (null == commentQO.getPageSize() || 0 >= commentQO.getPageSize()) {
            commentQO.setPageSize(10);
        }
    }
}
