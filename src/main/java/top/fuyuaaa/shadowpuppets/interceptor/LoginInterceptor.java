package top.fuyuaaa.shadowpuppets.interceptor;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import top.fuyuaaa.shadowpuppets.annotation.IgnoreSecurity;
import top.fuyuaaa.shadowpuppets.common.Result;
import top.fuyuaaa.shadowpuppets.holder.LoginUserHolder;
import top.fuyuaaa.shadowpuppets.model.LoginUserInfo;
import top.fuyuaaa.shadowpuppets.model.bo.UserBO;
import top.fuyuaaa.shadowpuppets.service.UserService;
import top.fuyuaaa.shadowpuppets.util.BeanUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * @author: fuyuaaa
 * @creat: 2019-03-29 10:21
 */
@Slf4j
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        String requestPath = request.getRequestURI();
        log.info("Method: " + method.getName() + ", IgnoreSecurity: " + method.isAnnotationPresent(IgnoreSecurity.class));
        log.info("requestPath: " + requestPath);
        //忽略
        if (method.isAnnotationPresent(IgnoreSecurity.class)) {
            return true;
        }

        String token = request.getHeader("ACCESS_TOKEN");
        log.info("token: " + token);

        if (isInvalidToken(token) || !getUser(token)) {
            writeResponse(response, requestPath);
            return false;
        }

        return true;
    }

    private void writeResponse(HttpServletResponse response, String requestPath) {
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            String result = JSON.toJSONString(Result.fail("请先登录！"));
            writer.print(result);
        } catch (Exception e) {
            log.error("LoginInterceptor_preHandle error: requestPath:{}, e:{}", requestPath, e.getMessage());
        } finally {
            if (null != writer) {
                writer.close();
            }
        }
    }

    private boolean isInvalidToken(String token) {
        return StringUtils.isEmpty(token) || null == redisTemplate.hasKey(token) || !redisTemplate.hasKey(token);
    }

    private boolean getUser(String token) {
        String tel = token.split(":")[1];
        UserBO userBO = userService.getByTel(tel);
        if (null == userBO || null == userBO.getId()) {
            return false;
        }
        LoginUserInfo loginUserInfo = BeanUtils.copyProperties(userBO, LoginUserInfo.class);
        LoginUserHolder.instance().put(loginUserInfo);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
