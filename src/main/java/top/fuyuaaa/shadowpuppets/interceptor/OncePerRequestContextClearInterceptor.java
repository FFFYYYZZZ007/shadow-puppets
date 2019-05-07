package top.fuyuaaa.shadowpuppets.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import top.fuyuaaa.shadowpuppets.common.holders.AbstractOncePerRequestContextHolder;

/**
 * @author fuyuaaa
 * @date 2019/4/6 Wed 23:59:00
 */
@Slf4j
@Component
public class OncePerRequestContextClearInterceptor extends HandlerInterceptorAdapter {

    /**
     *清空ThreadLocal
     */
    @Override
    public void afterCompletion(javax.servlet.http.HttpServletRequest request,
        javax.servlet.http.HttpServletResponse response, Object handler, Exception ex) {
        AbstractOncePerRequestContextHolder.clear();
        boolean debug = log.isDebugEnabled();
        if (debug) {
            log.debug("一次请求完成, AbstractOncePerRequestContextHolder清空了当前上下文数据");
        }
    }
}
