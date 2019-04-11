package top.fuyuaaa.shadowpuppets.holder;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fuyuaaa
 * @date 2019/4/6 Wed 23:45:00
 */
@Slf4j
@SuppressWarnings("all")
public abstract class AbstractOncePerRequestContextHolder<T extends Object> {

    private static final ThreadLocal<Map<String, Object>> CONTEXT = new ThreadLocal<>();

    public T get() {
        return (T) this.getElement(key());
    }

    public void put(T t) {
        this.addElement(key(), t);
    }

    protected Object getElement(String key) {
        return getElements().get(key);
    }

    protected void addElement(String key, Object object) {
        Map<String, Object> elements = getElements();
        elements.put(key, object);
    }

    private Map<String, Object> getElements() {
        Map<String, Object> elements = CONTEXT.get();
        if (elements == null) {
            elements = new HashMap<>(16);
            CONTEXT.set(elements);
        }
        return elements;
    }

    public static void clear() {
        Map<String, Object> elements = CONTEXT.get();
        if (elements != null) {
            //清空Map
            elements.clear();
        }
        //清空ThreadLocal
        CONTEXT.remove();
    }

    /**
     * Map中存放唯一对象对应的KEY，最好类名全路径名加一个后缀避免重复
     * @return
     */
    public abstract String key();
}
