package top.fuyuaaa.shadowpuppets.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.fuyuaaa.shadowpuppets.common.enums.CourseOrderStatusEnum;
import top.fuyuaaa.shadowpuppets.common.enums.ExEnum;
import top.fuyuaaa.shadowpuppets.dao.CourseDao;
import top.fuyuaaa.shadowpuppets.dao.CourseOrderDao;
import top.fuyuaaa.shadowpuppets.exceptions.CourseException;
import top.fuyuaaa.shadowpuppets.holder.LoginUserHolder;
import top.fuyuaaa.shadowpuppets.mapstruct.CourseOrderConverter;
import top.fuyuaaa.shadowpuppets.model.LoginUserInfo;
import top.fuyuaaa.shadowpuppets.model.PageVO;
import top.fuyuaaa.shadowpuppets.model.po.CourseOrderPO;
import top.fuyuaaa.shadowpuppets.model.po.CoursePO;
import top.fuyuaaa.shadowpuppets.model.qo.CourseOrderQO;
import top.fuyuaaa.shadowpuppets.model.vo.CourseOrderVO;
import top.fuyuaaa.shadowpuppets.service.CourseOrderService;
import top.fuyuaaa.shadowpuppets.service.CourseService;
import top.fuyuaaa.shadowpuppets.util.AlipayUtil;
import top.fuyuaaa.shadowpuppets.util.UUIDUtils;

import java.util.List;


/**
 * @author: fuyuaaa
 * @creat: 2019-05-02 20:16
 */
@Service
public class CourseOrderServiceImpl implements CourseOrderService {
    @Autowired
    CourseOrderDao courseOrderDao;
    @Autowired
    CourseDao courseDao;
    @Autowired
    CourseService courseService;
    @Override
    public String add(CourseOrderPO courseOrderPO) {
        this.validateCourseOrder(courseOrderPO);
        this.fillCourseOrder(courseOrderPO);
        Integer result = courseOrderDao.insert(courseOrderPO);
        if (result != 1) {
            throw new CourseException(ExEnum.COURSE_ORDER_ADD_ERROR.getMsg());
        }
        return courseOrderPO.getId();
    }

    @Override
    public CourseOrderVO getOrderInfo(String orderId) {
        CourseOrderPO orderPO = courseOrderDao.getById(orderId);
        CourseOrderVO courseOrderVO = CourseOrderConverter.INSTANCE.toCourseOrderVO(orderPO);
        return courseOrderVO;
    }

    @Override
    public void closeOrder(String orderId) {
        CourseOrderPO courseOrderPO = new CourseOrderPO();
        courseOrderPO.setId(orderId);
        courseOrderPO.setCourseOrderStatus(CourseOrderStatusEnum.CLOSED);
        Integer result = courseOrderDao.updateStatus(courseOrderPO);
        if (result != 1) {
            throw new CourseException(ExEnum.COURSE_ORDER_CLOSE_ERROR.getMsg());
        }
    }

    @Override
    public void updateStatus(String orderId, Integer status) {
        CourseOrderPO courseOrderPO = new CourseOrderPO();
        courseOrderPO.setId(orderId);
        courseOrderPO.setCourseOrderStatus(CourseOrderStatusEnum.find(status));
        courseOrderDao.updateStatus(courseOrderPO);
    }

    @Override
    public String getPayUrl(String orderId) {
        CourseOrderVO orderVO = this.getOrderInfo(orderId);
        String aliPayUrl = AlipayUtil.getAliPayUrl(orderVO);
        return aliPayUrl;
    }

    @Override
    public Boolean checkTradeStatus(String orderId) {
        //去查找是否已支付
        if (!AlipayUtil.checkTradeStatus(orderId)) {
            return false;
        }
        //如果已支付，更改状态为已支付
        CourseOrderPO courseOrderPO = new CourseOrderPO();
        courseOrderPO.setId(orderId);
        courseOrderPO.setCourseOrderStatus(CourseOrderStatusEnum.PENDING_STUDY);
        courseOrderDao.updateStatus(courseOrderPO);
        return true;
    }

    @Override
    public PageVO<CourseOrderVO> getCourseOrderPageVO(CourseOrderQO courseOrderQO) {
        PageHelper.startPage(courseOrderQO.getPageNum(), courseOrderQO.getPageSize());
        List<CourseOrderPO> courseOrderPOList = courseOrderDao.findList(courseOrderQO);
        PageInfo<CourseOrderPO> pageInfo = new PageInfo<>(courseOrderPOList);
        Integer total = Integer.valueOf(String.valueOf(pageInfo.getTotal()));
        List<CourseOrderVO> courseOrderVOList = CourseOrderConverter.INSTANCE.toCourseOrderVOList(pageInfo.getList());
        PageVO<CourseOrderVO> pageVO = new PageVO<>(pageInfo.getPageNum(), pageInfo.getPageSize(), total, courseOrderVOList);
        return pageVO;
    }

    //==============================  private help methods  ==============================

    private void validateCourseOrder(CourseOrderPO courseOrderPO) {
        if (null == courseOrderPO || null == courseOrderPO.getCourseId() || null == courseOrderPO.getDealPrice()) {
            throw new CourseException(ExEnum.ORDER_CREATE_PARAMS_ERROR.getMsg());
        }
    }

    private void fillCourseOrder(CourseOrderPO courseOrderPO) {
        LoginUserInfo userInfo = LoginUserHolder.instance().get();
        courseOrderPO.setId(UUIDUtils.getOrderCode());
        courseOrderPO.setUserId(userInfo.getId());
        courseOrderPO.setCourseOrderStatus(CourseOrderStatusEnum.PENDING_PAY);
    }
}
