package top.fuyuaaa.shadowpuppets.annotation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import top.fuyuaaa.shadowpuppets.enums.ExEnum;
import top.fuyuaaa.shadowpuppets.exceptions.UnLoginException;
import top.fuyuaaa.shadowpuppets.holder.LoginUserHolder;
import top.fuyuaaa.shadowpuppets.model.LoginUserInfo;

import java.lang.reflect.Method;


/**
 * @author fuyuaaa
 * @creat: 2019-04-11 22:44
 */
@AllArgsConstructor
@Slf4j
@Aspect
@Component
public class LoginAspect {


    @Pointcut("@annotation(top.fuyuaaa.shadowpuppets.annotation.NeedLogin)")
    public void pointCut() {
    }

    @SuppressWarnings("all")
    @Before("pointCut()")
    public void checkIsLogin(JoinPoint joinPoint) {
        LoginUserInfo loginUserInfo = LoginUserHolder.instance().get();
        if (null == loginUserInfo) {
            throw new UnLoginException(ExEnum.NON_LOGIN_USER.getMsg());
        }
    }

}
