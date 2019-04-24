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
import top.fuyuaaa.shadowpuppets.exceptions.AuthException;
import top.fuyuaaa.shadowpuppets.exceptions.OrderOwnerException;
import top.fuyuaaa.shadowpuppets.exceptions.ParamException;
import top.fuyuaaa.shadowpuppets.holder.LoginUserHolder;
import top.fuyuaaa.shadowpuppets.model.LoginUserInfo;
import top.fuyuaaa.shadowpuppets.model.bo.GoodsOrderBO;
import top.fuyuaaa.shadowpuppets.service.GoodsOrderService;
import top.fuyuaaa.shadowpuppets.service.UserService;

import javax.servlet.http.HttpServletRequest;


/**
 * 校验用户是否是管理员
 *
 * @author fuyuaaa
 * @creat: 2019-04-11 22:44
 */
@AllArgsConstructor
@Slf4j
@Aspect
@Component
public class ValidateAdminAspect {


    @Autowired
    UserService userService;

    @Pointcut("@annotation(top.fuyuaaa.shadowpuppets.annotation.ValidateAdmin)")
    public void pointCut() {
    }

    @SuppressWarnings("all")
    @Before("pointCut()")
    public void checkUserIsAdmin(JoinPoint joinPoint) {
        LoginUserInfo userInfo = LoginUserHolder.instance().get();
        Boolean isAdmin = userService.isAdmin(userInfo.getId());
        if (!isAdmin) {
            throw new AuthException(ExEnum.ADMIN_AUTH_ERROR.getMsg());
        }
    }

}
