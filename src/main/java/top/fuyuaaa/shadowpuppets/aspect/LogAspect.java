package top.fuyuaaa.shadowpuppets.aspect;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 一个日志切面
 * @author: fuyuaaa
 * @creat: 2019-04-19 10:21
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    @Pointcut("execution(public * top.fuyuaaa.shadowpuppets.controller.*.*(..))")
    public void log() {
    }

    @Before("log()")
    public void deBefore(JoinPoint joinPoint) {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 记录下请求内容
        log.info("==请求信息=================>>>>>>>>>>>>>>>>>>>>请求信息");
        log.info("URL: {}, TYPE: {}, ACCESS_TOKEN: {}, METHOD: {}, PARAMETERS: {}",
                request.getRequestURI(),
                request.getMethod(),
                request.getHeader("ACCESS_TOKEN"),
                joinPoint.getSignature().getName(),
                JSONObject.toJSONString(request.getParameterMap()));
    }

    @AfterReturning(pointcut = "log()", returning = "ret")
    public void doAfterReturning(JoinPoint point, Object ret) {
        log.info("RETURN: " + JSONObject.toJSONString(ret));
    }

    @AfterThrowing(pointcut = "log()", throwing = "e")
    public void logException(JoinPoint point, Throwable e) {
        log.info("日志切面捕获到异常，MESSAGE: {}", e.getMessage());
    }

}

