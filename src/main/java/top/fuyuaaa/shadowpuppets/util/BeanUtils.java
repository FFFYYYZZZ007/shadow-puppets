package top.fuyuaaa.shadowpuppets.util;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-04 23:51
 */
public class BeanUtils {

    /**
     * 用过反射拷贝对象
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

    public static <T> List<T> copyListProperties(List<Object> sourceList, Class<T> clazz) {
        List<T> result = new ArrayList<>(sourceList.size());
        T target = null;
        for (Object source : sourceList) {
            try {
                target = clazz.newInstance();
                org.springframework.beans.BeanUtils.copyProperties(source, target);
                result.add(target);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
