package top.fuyuaaa.shadowpuppets.util;

/**
 * 弱智写法，如果有空会把项目的Bean拷贝全换成mapStrut
 *
 * @author: fuyuaaa
 * @creat: 2019-04-04 23:51
 */
public class BeanUtils {

    /**
     * 用过反射拷贝对象（很弱智，考虑全部换成mapStruct）
     *
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
