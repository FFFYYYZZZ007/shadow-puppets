package top.fuyuaaa.shadowpuppets.annotation;

import java.lang.annotation.*;

/**
 * @author: fuyuaaa
 * @creat: 2019-03-29 11:27
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreSecurity {
}
