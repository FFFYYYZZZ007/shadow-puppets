package top.fuyuaaa.shadowpuppets.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.fuyuaaa.shadowpuppets.annotation.NeedLogin;
import top.fuyuaaa.shadowpuppets.common.enums.ExEnum;
import top.fuyuaaa.shadowpuppets.common.enums.OrderStatusEnum;
import top.fuyuaaa.shadowpuppets.dao.GoodsCommentDao;
import top.fuyuaaa.shadowpuppets.dao.GoodsOrderDao;
import top.fuyuaaa.shadowpuppets.exceptions.CommentException;
import top.fuyuaaa.shadowpuppets.exceptions.OrderOwnerException;
import top.fuyuaaa.shadowpuppets.holder.LoginUserHolder;
import top.fuyuaaa.shadowpuppets.mapstruct.CommentConverter;
import top.fuyuaaa.shadowpuppets.model.LoginUserInfo;
import top.fuyuaaa.shadowpuppets.model.PageVO;
import top.fuyuaaa.shadowpuppets.model.bo.GoodsOrderBO;
import top.fuyuaaa.shadowpuppets.model.po.GoodsCommentPO;
import top.fuyuaaa.shadowpuppets.model.po.GoodsOrderInfoPO;
import top.fuyuaaa.shadowpuppets.model.po.GoodsOrderPO;
import top.fuyuaaa.shadowpuppets.model.qo.CommentQO;
import top.fuyuaaa.shadowpuppets.model.qo.OrderCommentQO;
import top.fuyuaaa.shadowpuppets.model.vo.GoodsCommentVO;
import top.fuyuaaa.shadowpuppets.service.GoodsCommentService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-26 22:54
 */
@Service
@SuppressWarnings("all")
public class GoodsCommentServiceImpl implements GoodsCommentService {

    @Autowired
    GoodsCommentDao goodsCommentDao;

    @Autowired
    GoodsOrderDao goodsOrderDao;

    @Override
    @NeedLogin
    @Transactional(rollbackFor = Exception.class)
    public void addComment(OrderCommentQO orderCommentQO) {
        this.validateOrderOwner(orderCommentQO);
        List<GoodsOrderInfoPO> goodsOrderInfoPOList = goodsOrderDao.getOrderInfoByOrderId(orderCommentQO.getOrderId());
        List<Integer> goodsIdList = goodsOrderInfoPOList.stream()
                .map(goodsOrderInfoPO -> goodsOrderInfoPO.getGoodsId())
                .collect(Collectors.toList());
        goodsIdList.forEach(goodsId -> {
            this.checkHaveCommented(orderCommentQO.getOrderId());
            GoodsCommentPO goodsCommentPO = new GoodsCommentPO();
            goodsCommentPO.setOrderId(orderCommentQO.getOrderId());
            goodsCommentPO.setUserId(LoginUserHolder.instance().get().getId());
            goodsCommentPO.setStarLevel(orderCommentQO.getStarLevel());
            goodsCommentPO.setGoodsId(goodsId);
            goodsCommentPO.setContent(orderCommentQO.getContent());
            //循环Insert，虽然很弱智，但我还是写了，嘤嘤嘤
            if (goodsCommentDao.insert(goodsCommentPO) != 1) {
                throw new CommentException(ExEnum.COMMENT_ADD_ERROR.getMsg());
            }
        });
        goodsOrderDao.updateOrderStatus(OrderStatusEnum.FINISHED.code(), orderCommentQO.getOrderId());
    }

    @Override
    public void removeComment(Integer commentId) {
        if (goodsCommentDao.delete(commentId) != 1) {
            throw new CommentException(ExEnum.COMMENT_REMOVE_ERROR.getMsg());
        }
    }

    @Override
    public PageVO<GoodsCommentVO> getListByGoodsId(CommentQO commentQO) {
        PageHelper.startPage(commentQO.getPageNum(), commentQO.getPageSize());
        List<GoodsCommentPO> listByGoodsId = goodsCommentDao.findListByGoodsId(commentQO.getGoodsId());
        PageInfo<GoodsCommentPO> pageInfo = new PageInfo<>(listByGoodsId);
        PageVO<GoodsCommentVO> pageVO = new PageVO<>(pageInfo);
        List<GoodsCommentVO> commentVOList = CommentConverter.INSTANCE.toGoodsCommentVOList(listByGoodsId);
        pageVO.setList(commentVOList);
        return pageVO;
    }

    //==============================  private help methods  ==============================

    /**
     * 检验是否已经评论过
     *
     * @param goodsId 商品id
     */
    private void checkHaveCommented(String orderId) {
        if (goodsCommentDao.findByOrderId(orderId) > 0) {
            throw new CommentException(ExEnum.COMMENT_HAVE_EXIST.getMsg());
        }
    }

    /**
     * 校验订单是否属于用户，是否已经评论
     *
     * @param orderId 订单ID
     */
    private void validateOrderOwner(OrderCommentQO orderCommentQO) {
        GoodsOrderPO goodsOrderPO = goodsOrderDao.getById(orderCommentQO.getOrderId());
        LoginUserInfo userInfo = LoginUserHolder.instance().get();
        if (!userInfo.getId().equals(goodsOrderPO.getUserId())) {
            throw new OrderOwnerException(ExEnum.IS_NOT_ORDER_OWNER.getMsg());
        }
    }
}
