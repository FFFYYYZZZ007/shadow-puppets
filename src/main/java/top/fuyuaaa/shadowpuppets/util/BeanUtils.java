package top.fuyuaaa.shadowpuppets.util;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-04 23:51
 */
public class BeanUtils {

    /**
     * 用过反射拷贝对象
     * @param source 源对象
     * @param clazz  目标对象
     * @param <T>
     * @return
     */
    public static <T> T copyProperties(Object source, Class<T> clazz) {
        T target = null;
        try {
            target = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("通过反射创建对象失败");
        }
        org.springframework.beans.BeanUtils.copyProperties(source, target);
        return target;
    }
}
