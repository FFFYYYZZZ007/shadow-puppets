package top.fuyuaaa.shadowpuppets.service;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-08 22:54
 */
public interface CategoryService {

    /**
     * 根据类别id 获取 类别名
     *
     * @param id 类别id
     * @return
     */
    String getCategoryNameById(Integer id);
}
