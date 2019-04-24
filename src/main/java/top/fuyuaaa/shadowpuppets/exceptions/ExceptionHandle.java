package top.fuyuaaa.shadowpuppets.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.fuyuaaa.shadowpuppets.common.Result;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-18 16:36
 */
@RestControllerAdvice
@Slf4j
public class ExceptionHandle {
    @ResponseBody
    @ExceptionHandler(value = RuntimeException.class)
    public Result javaExceptionHandler(Exception ex) {
        log.error("捕获到RuntimeException异常: message: {}", ex.getMessage());
        return Result.fail(ex.getMessage());
    }
}
