package top.fuyuaaa.shadowpuppets.annotation;

import java.lang.annotation.*;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-24 23:31
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidateAdmin {
}
