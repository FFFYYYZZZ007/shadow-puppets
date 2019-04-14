package top.fuyuaaa.shadowpuppets.service;

import top.fuyuaaa.shadowpuppets.model.bo.GoodsBO;

import java.util.List;
import java.util.Map;

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

    /**
     * 根据查询参数查询列表
     * @param param 参数
     * @return 商品列表
     */
    List<GoodsBO> getGoodsListByParam(Map<String, Object> param);

    //===========后台管理

    /**
     * 管理员添加商品
     * @param goodsBO 商品
     * @return true/false
     */
    Boolean addManagerGoods(GoodsBO goodsBO);

    /**
     * 管理员修改商品
     * @param goodsBO 商品
     * @return true/false
     */
    Boolean updateManagerGoods(GoodsBO goodsBO);

    /**
     * 管理员删除商品
     * @param goodsId 商品ID
     * @return true/false
     */
    Boolean removeManagerGoods(Integer goodsId);
}
