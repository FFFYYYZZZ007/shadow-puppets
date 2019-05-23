package top.fuyuaaa.shadowpuppets.service;

import top.fuyuaaa.shadowpuppets.model.PageVO;
import top.fuyuaaa.shadowpuppets.model.po.CourseOrderPO;
import top.fuyuaaa.shadowpuppets.model.qo.CourseOrderQO;
import top.fuyuaaa.shadowpuppets.model.vo.CourseOrderVO;

/**
 * @author: fuyuaaa
 * @creat: 2019-05-02 20:15
 */
public interface CourseOrderService {

    /**
     * @param courseOrderPO
     * @return 订单编号
     */
    String add(CourseOrderPO courseOrderPO);

    CourseOrderVO getOrderInfo(String orderId);

    void closeOrder(String orderId);

    void updateStatus(String orderId, Integer status);

    String getPayUrl(String orderId);

    Boolean checkTradeStatus(String orderId);

    PageVO<CourseOrderVO> getUserCourseOrderPageVO(CourseOrderQO courseOrderQO);

    PageVO<CourseOrderVO> getCourseOrderPageVO(CourseOrderQO courseOrderQO);
}
