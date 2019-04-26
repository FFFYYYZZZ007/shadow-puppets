package top.fuyuaaa.shadowpuppets.service;

import top.fuyuaaa.shadowpuppets.model.PageVO;
import top.fuyuaaa.shadowpuppets.model.po.GoodsCommentPO;
import top.fuyuaaa.shadowpuppets.model.qo.CommentQO;
import top.fuyuaaa.shadowpuppets.model.qo.OrderCommentQO;
import top.fuyuaaa.shadowpuppets.model.vo.GoodsCommentVO;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-26 22:51
 */
public interface GoodsCommentService {

    /**
     * 添加评论(for order)
     *
     * @param orderCommentQO 评论
     */
    void addComment(OrderCommentQO orderCommentQO);


    /**
     * 删除评论
     * @param commentId 评论ID
     */
    void removeComment(Integer commentId);

    /**
     * 点赞评论
     * @param commentId 评论ID
     */
    void updateLikeCount(Integer commentId);


    /**
     * 查询某个商品的评论
     * @param commentQO 评论查询参数
     * @return 评论分页列表
     */
    PageVO<GoodsCommentVO> getListByGoodsId(CommentQO commentQO);
}
