package top.fuyuaaa.shadowpuppets.common.aspects;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import top.fuyuaaa.shadowpuppets.common.enums.ExEnum;
import top.fuyuaaa.shadowpuppets.common.exceptions.UserException;
import top.fuyuaaa.shadowpuppets.common.holders.LoginUserHolder;
import top.fuyuaaa.shadowpuppets.model.LoginUserInfo;


/**
 * @author fuyuaaa
 * @creat: 2019-04-11 22:44
 */
@AllArgsConstructor
@Slf4j
@Aspect
@Component
public class LoginAspect {


    @Pointcut("@annotation(top.fuyuaaa.shadowpuppets.common.annotations.NeedLogin)")
    public void pointCut() {
    }

    @SuppressWarnings("all")
    @Before("pointCut()")
    public void checkIsLogin(JoinPoint joinPoint) {
        LoginUserInfo loginUserInfo = LoginUserHolder.instance().get();
        if (null == loginUserInfo) {
            throw new UserException(ExEnum.NON_LOGIN_USER.getMsg());
        }
    }

}
