package top.fuyuaaa.shadowpuppets.service;

import top.fuyuaaa.shadowpuppets.model.PageVO;
import top.fuyuaaa.shadowpuppets.model.qo.CommentQO;
import top.fuyuaaa.shadowpuppets.model.qo.OrderCommentQO;
import top.fuyuaaa.shadowpuppets.model.vo.CourseCommentVO;

/**
 * @author: fuyuaaa
 * @creat: 2019-05-04 22:44
 */
public interface CourseCommentService {
    /**
     * 添加评论
     */
    void addComment(OrderCommentQO orderCommentQO);


    /**
     * 删除评论
     * @param commentId 评论ID
     */
    void removeComment(Integer commentId);

    /**
     * 查询某个商品的评论
     * 这里复用CommentQO 虽然里面是goodsId
     * @param commentQO 评论查询参数
     * @return 评论分页列表
     */
    PageVO<CourseCommentVO> getListByCourseId(CommentQO commentQO);
}
