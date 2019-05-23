package top.fuyuaaa.shadowpuppets.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import top.fuyuaaa.shadowpuppets.common.annotations.NeedLogin;
import top.fuyuaaa.shadowpuppets.common.annotations.ValidateCourseOrderOwner;
import top.fuyuaaa.shadowpuppets.common.Result;
import top.fuyuaaa.shadowpuppets.common.enums.CourseOrderStatusEnum;
import top.fuyuaaa.shadowpuppets.model.PageVO;
import top.fuyuaaa.shadowpuppets.model.po.CourseOrderPO;
import top.fuyuaaa.shadowpuppets.model.qo.CourseOrderQO;
import top.fuyuaaa.shadowpuppets.model.qo.CourseQO;
import top.fuyuaaa.shadowpuppets.model.vo.CourseOrderVO;
import top.fuyuaaa.shadowpuppets.model.vo.CourseSimpleVO;
import top.fuyuaaa.shadowpuppets.model.vo.CourseVO;
import top.fuyuaaa.shadowpuppets.service.CourseOrderService;
import top.fuyuaaa.shadowpuppets.service.CourseService;

import java.util.List;

/**
 * @author: fuyuaaa
 * @creat: 2019-05-01 15:43
 */
@RestController
@RequestMapping("/course")
@Slf4j
public class CourseController {
    @Autowired
    CourseService courseService;
    @Autowired
    CourseOrderService courseOrderService;

    @PostMapping("/list")
    public Result<PageVO<CourseSimpleVO>> getCourseSimpleVOPage(@RequestBody CourseQO courseQO){
        PageVO<CourseSimpleVO> courseSimpleVOPageVO = courseService.getSimpleVOList(courseQO);
        return Result.success(courseSimpleVOPageVO);
    }

    @GetMapping("/recommend")
    @Cacheable(value = "getRecommendCourseList",keyGenerator = "simpleKeyGenerator")
    public Result<List<CourseSimpleVO>> getRecommendCourseList(){
        List<CourseSimpleVO> recommendCourseList = courseService.getRecommendCourseList();
        return Result.success(recommendCourseList);
    }

    @GetMapping("/detail")
    public Result<CourseVO> getCourseVO(@RequestParam Integer id) {
        CourseVO courseVO = courseService.getCourseVOById(id);
        return Result.success(courseVO);
    }

    //==============================  order related  ==============================

    @PostMapping("/order/add")
    @NeedLogin
    public Result<String> addNewCourseOrder(@RequestBody CourseOrderPO courseOrderPO) {
        String orderId = courseOrderService.add(courseOrderPO);
        return Result.success(orderId).setMsg("生成订单成功");
    }

    @PostMapping("/order/info")
    @NeedLogin
    @ValidateCourseOrderOwner
    public Result<CourseOrderVO> getOrderInfo(@RequestParam String orderId) {
        CourseOrderVO courseOrderVO = courseOrderService.getOrderInfo(orderId);
        return Result.success(courseOrderVO);
    }

    @PostMapping("/order/payUrl")
    @NeedLogin
    @ValidateCourseOrderOwner
    public Result<String> getPayUrl(@RequestParam String orderId) {
        String payUrl = courseOrderService.getPayUrl(orderId);
        return Result.success(payUrl);
    }

    @PostMapping("/order/pay/check")
    @NeedLogin
    @ValidateCourseOrderOwner
    public Result<Boolean> checkTradeStatus(@RequestParam String orderId) {
        Boolean success = courseOrderService.checkTradeStatus(orderId);
        return success?Result.success(true).setMsg("支付成功"):Result.fail("支付未完成");
    }

    @PostMapping("/order/close")
    @NeedLogin
    @ValidateCourseOrderOwner
    public Result<Boolean> closeOrder(@RequestParam String orderId) {
        courseOrderService.closeOrder(orderId);
        return Result.success(true).setMsg("取消订单成功");
    }

    @PostMapping("/order/confirm")
    @NeedLogin
    @ValidateCourseOrderOwner
    public Result<Boolean> confirmStudied(@RequestParam String orderId) {
        //待结课 => 待评价
        courseOrderService.updateStatus(orderId, CourseOrderStatusEnum.PENGDING_COMMENT.code());
        return Result.success(true).setMsg("确认结课成功");
    }

    @PostMapping("/order/user/list")
    @NeedLogin
    public Result getUserOrderList(@RequestBody CourseOrderQO courseOrderQO){
        PageVO<CourseOrderVO> courseOrderPageVO = courseOrderService.getUserCourseOrderPageVO(courseOrderQO);
        return Result.success(courseOrderPageVO);
    }

    @PostMapping("/order/list")
    @NeedLogin
    public Result getOrderList(@RequestBody CourseOrderQO courseOrderQO){
        PageVO<CourseOrderVO> courseOrderPageVO = courseOrderService.getCourseOrderPageVO(courseOrderQO);
        return Result.success(courseOrderPageVO);
    }

}
