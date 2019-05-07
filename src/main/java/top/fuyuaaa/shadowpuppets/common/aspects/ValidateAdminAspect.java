package top.fuyuaaa.shadowpuppets.common.aspects;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.fuyuaaa.shadowpuppets.common.enums.ExEnum;
import top.fuyuaaa.shadowpuppets.common.exceptions.AuthException;
import top.fuyuaaa.shadowpuppets.common.exceptions.UserException;
import top.fuyuaaa.shadowpuppets.common.holders.LoginUserHolder;
import top.fuyuaaa.shadowpuppets.model.LoginUserInfo;
import top.fuyuaaa.shadowpuppets.service.UserService;


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

    @Pointcut("@annotation(top.fuyuaaa.shadowpuppets.common.annotations.ValidateAdmin)")
    public void pointCut() {
    }

    @SuppressWarnings("all")
    @Before("pointCut()")
    public void checkUserIsAdmin(JoinPoint joinPoint) {
        LoginUserInfo userInfo = LoginUserHolder.instance().get();
        if (null == userInfo) {
            throw new UserException(ExEnum.NON_LOGIN_USER.getMsg());
        }
        Boolean isAdmin = userService.isAdmin(userInfo.getId());
        if (!isAdmin) {
            throw new AuthException(ExEnum.ADMIN_AUTH_ERROR.getMsg());
        }
    }

}
