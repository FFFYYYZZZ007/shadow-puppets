package top.fuyuaaa.shadowpuppets.service;

import org.springframework.web.multipart.MultipartFile;
import top.fuyuaaa.shadowpuppets.model.PageVO;
import top.fuyuaaa.shadowpuppets.model.bo.GoodsBO;
import top.fuyuaaa.shadowpuppets.model.qo.GoodsListQO;
import top.fuyuaaa.shadowpuppets.model.vo.GoodsVO;

import java.util.List;
import java.util.Map;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-08 22:21
 */
@SuppressWarnings("all")
public interface GoodsService {

    /**
     * 根据id查询商品
     *
     * @param id 商品id
     * @return
     */
    GoodsBO getGoodsDetailsById(Integer id);

    /**
     * 根据id获取商品VO
     *
     * @param id 商品id
     * @return 商品VO
     */
    GoodsVO getGoodsVOById(Integer id);

    /**
     * 获取商品列表
     *
     * @param goodsListQO 商品查询类
     * @return 商品列表
     */
    List<GoodsBO> getList(GoodsListQO goodsListQO);

    /**
     * 获取商品列表
     *
     * @param goodsListQO 商品查询类
     * @return 商品列表
     */
    List<GoodsVO> getVOList(GoodsListQO goodsListQO);

    /**
     * 获取商品列表
     *
     * @param goodsListQO 商品查询类
     * @return 商品列表
     */
    PageVO<GoodsVO> getGoodsPageVO(GoodsListQO goodsListQO);

    //==============================  后台管理  ==============================

    /**
     * 管理员添加商品
     *
     * @param goodsBO 商品
     * @return true/false
     */
    void addManagerGoods(GoodsBO goodsBO);

    /**
     * 管理员修改商品
     *
     * @param goodsBO 商品
     * @return true/false
     */
    void updateManagerGoods(GoodsBO goodsBO);

    /**
     * 管理员删除商品
     *
     * @param goodsId 商品ID
     * @return true/false
     */
    void removeManagerGoods(Integer goodsId);

    /**
     * 更新商品的图片地址
     *
     * @param goodsId    商品id
     * @param imagesUrls 图片地址
     * @return
     */
    Boolean updateImagesUrls(Integer goodsId, String imagesUrls);

    String addGoodsImage(MultipartFile multipartFile, Integer goodsId);

    void removeGoodsImage(Integer goodsId, String imageUrl);

    /**
     * 根据类别ID获取商品数量
     *
     * @param categoryId 类别ID
     * @return 商品数量
     */
    Integer countByCategoryId(Integer categoryId);

    Map<String, Integer> categoryStatisticsInfo();
}
