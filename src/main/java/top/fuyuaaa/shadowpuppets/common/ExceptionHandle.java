package top.fuyuaaa.shadowpuppets.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.fuyuaaa.shadowpuppets.common.Result;
import top.fuyuaaa.shadowpuppets.common.exceptions.AuthException;
import top.fuyuaaa.shadowpuppets.common.exceptions.ParamException;

/**
 * 统一异常处理类
 *
 * @author: fuyuaaa
 * @creat: 2019-04-18 16:36
 */
@RestControllerAdvice
@Slf4j
public class ExceptionHandle {
    @ResponseBody
    @ExceptionHandler(value = RuntimeException.class)
    public Result javaExceptionHandler(Exception ex) {
        String message = ex.getMessage();
        if (ex instanceof ParamException) {
            log.error("参数异常: message: {}", message);
        } else {
            log.error("运行异常: message: {}", message);
        }

        if (ex instanceof AuthException) {
            return Result.fail(ex.getMessage()).setCode("403");
        }
        return Result.fail(message);
    }
}
