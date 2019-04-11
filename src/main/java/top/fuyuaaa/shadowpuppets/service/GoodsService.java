package top.fuyuaaa.shadowpuppets.service;

import top.fuyuaaa.shadowpuppets.model.bo.GoodsBO;

import java.util.List;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-08 22:21
 */
public interface GoodsService {

    /**
     * 根据id查询商品
     *
     * @param id 商品id
     * @return
     */
    GoodsBO getGoodsDetailsById(Integer id);

    List<GoodsBO> getList();
}
