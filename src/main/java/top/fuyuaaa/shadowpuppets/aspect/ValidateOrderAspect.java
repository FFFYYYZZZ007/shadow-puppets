package top.fuyuaaa.shadowpuppets.aspect;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.fuyuaaa.shadowpuppets.common.enums.ExEnum;
import top.fuyuaaa.shadowpuppets.exceptions.OrderOwnerException;
import top.fuyuaaa.shadowpuppets.exceptions.ParamException;
import top.fuyuaaa.shadowpuppets.exceptions.UnLoginException;
import top.fuyuaaa.shadowpuppets.holder.LoginUserHolder;
import top.fuyuaaa.shadowpuppets.model.LoginUserInfo;
import top.fuyuaaa.shadowpuppets.model.bo.GoodsOrderBO;
import top.fuyuaaa.shadowpuppets.service.GoodsOrderService;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;


/**
 * 校验订单是否属于用户本人
 *
 * @author fuyuaaa
 * @creat: 2019-04-11 22:44
 */
@AllArgsConstructor
@Slf4j
@Aspect
@Component
public class ValidateOrderAspect {


    @Autowired
    GoodsOrderService goodsOrderService;

    @Pointcut("@annotation(top.fuyuaaa.shadowpuppets.annotation.ValidateOrderOwner)")
    public void pointCut() {
    }

    @SuppressWarnings("all")
    @Before("pointCut()")
    public void checkOrderBelongUser(JoinPoint joinPoint) {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String orderId = request.getParameter("orderId");
        if (StringUtils.isEmpty(orderId)) {
            throw new ParamException(ExEnum.PARAM_ERROR.getMsg());
        }
        Integer orderIdInteger = Integer.valueOf(orderId);
        GoodsOrderBO goodsOrderBO = goodsOrderService.getById(orderIdInteger);
        LoginUserInfo loginUserInfo = LoginUserHolder.instance().get();
        if (!goodsOrderBO.getUserId().equals(loginUserInfo.getId())) {
            throw new OrderOwnerException(ExEnum.IS_NOT_ORDER_OWNER.getMsg());
        }
    }

}
