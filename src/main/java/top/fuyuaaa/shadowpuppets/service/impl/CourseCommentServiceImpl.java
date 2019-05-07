package top.fuyuaaa.shadowpuppets.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.fuyuaaa.shadowpuppets.common.enums.CourseOrderStatusEnum;
import top.fuyuaaa.shadowpuppets.common.enums.ExEnum;
import top.fuyuaaa.shadowpuppets.dao.CourseCommentDao;
import top.fuyuaaa.shadowpuppets.dao.CourseOrderDao;
import top.fuyuaaa.shadowpuppets.common.exceptions.CommentException;
import top.fuyuaaa.shadowpuppets.common.exceptions.OrderOwnerException;
import top.fuyuaaa.shadowpuppets.common.exceptions.ParamException;
import top.fuyuaaa.shadowpuppets.common.holders.LoginUserHolder;
import top.fuyuaaa.shadowpuppets.mapstruct.CourseCommentConverter;
import top.fuyuaaa.shadowpuppets.model.PageVO;
import top.fuyuaaa.shadowpuppets.model.po.CourseCommentPO;
import top.fuyuaaa.shadowpuppets.model.po.CourseOrderPO;
import top.fuyuaaa.shadowpuppets.model.qo.CommentQO;
import top.fuyuaaa.shadowpuppets.model.qo.OrderCommentQO;
import top.fuyuaaa.shadowpuppets.model.vo.CourseCommentVO;
import top.fuyuaaa.shadowpuppets.service.CourseCommentService;

import java.util.List;

/**
 * @author: fuyuaaa
 * @creat: 2019-05-04 22:50
 */
@Service
public class CourseCommentServiceImpl implements CourseCommentService {

    @Autowired
    CourseCommentDao courseCommentDao;
    @Autowired
    CourseOrderDao courseOrderDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addComment(OrderCommentQO orderCommentQO) {
        this.validateOrderCommentQO(orderCommentQO);
        this.validateOrderOwner(orderCommentQO.getOrderId());
        this.validateExistComment(orderCommentQO.getOrderId());
        CourseCommentPO courseCommentPO = CourseCommentConverter.INSTANCE.toCourseCommentPO(orderCommentQO);
        Integer insert = courseCommentDao.insert(courseCommentPO);
        if (1 != insert) {
            throw new CommentException(ExEnum.COMMENT_ADD_ERROR.getMsg());
        }
        CourseOrderPO courseOrderPO = new CourseOrderPO();
        courseOrderPO.setId(orderCommentQO.getOrderId());
        courseOrderPO.setCourseOrderStatus(CourseOrderStatusEnum.FINISHED);
        courseOrderDao.updateStatus(courseOrderPO);
    }

    @Override
    public void removeComment(Integer commentId) {
        if (courseCommentDao.delete(commentId) != 1) {
            throw new CommentException(ExEnum.COMMENT_REMOVE_ERROR.getMsg());
        }
    }

    @Override
    public PageVO<CourseCommentVO> getListByCourseId(CommentQO commentQO) {
        PageHelper.startPage(commentQO.getPageNum(), commentQO.getPageSize());
        List<CourseCommentPO> listByGoodsId = courseCommentDao.findListByCourseId(commentQO.getGoodsId());
        PageInfo<CourseCommentPO> pageInfo = new PageInfo<>(listByGoodsId);
        PageVO<CourseCommentVO> pageVO = new PageVO<>(pageInfo);
        List<CourseCommentVO> commentVOList = CourseCommentConverter.INSTANCE.toCourseCommentVOList(listByGoodsId);
        pageVO.setList(commentVOList);
        return pageVO;
    }

    //==============================  private help method  ==============================

    private void validateOrderCommentQO(OrderCommentQO orderCommentQO) {
        if (null == orderCommentQO || StringUtils.isEmpty(orderCommentQO.getOrderId())) {
            throw new ParamException(ExEnum.PARAM_ERROR.getMsg());
        }
        if (StringUtils.isEmpty(orderCommentQO.getContent())) {
            throw new ParamException("请输入评论内容");
        }
        if (null == orderCommentQO.getStarLevel() || 0 >= orderCommentQO.getStarLevel()) {
            throw new ParamException("请选择评论星级");
        }
    }

    /**
     * 校验是否是订单Owner
     *
     * @param orderId 订单ID
     */
    private void validateOrderOwner(String orderId) {
        CourseOrderPO courseOrderPO = courseOrderDao.getById(orderId);
        Integer userId = LoginUserHolder.instance().get().getId();
        if (null == courseOrderPO || !userId.equals(courseOrderPO.getUserId())) {
            throw new OrderOwnerException(ExEnum.IS_NOT_ORDER_OWNER.getMsg());
        }
    }


    /**
     * 检查是否评论过了
     *
     * @param orderId 订单ID
     */
    private void validateExistComment(String orderId) {
        Integer countByOderId = courseCommentDao.getByOrderId(orderId);
        if (0 < countByOderId) {
            throw new CommentException(ExEnum.COMMENT_HAVE_EXIST.getMsg());
        }
    }
}
