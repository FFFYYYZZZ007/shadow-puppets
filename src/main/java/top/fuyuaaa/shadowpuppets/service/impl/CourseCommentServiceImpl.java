package top.fuyuaaa.shadowpuppets.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.fuyuaaa.shadowpuppets.common.enums.CourseOrderStatusEnum;
import top.fuyuaaa.shadowpuppets.common.enums.ExEnum;
import top.fuyuaaa.shadowpuppets.dao.CourseCommentDao;
import top.fuyuaaa.shadowpuppets.dao.CourseOrderDao;
import top.fuyuaaa.shadowpuppets.exceptions.CommentException;
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
}
