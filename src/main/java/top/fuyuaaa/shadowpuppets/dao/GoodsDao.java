package top.fuyuaaa.shadowpuppets.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.fuyuaaa.shadowpuppets.model.po.GoodsPO;
import top.fuyuaaa.shadowpuppets.model.qo.GoodsListQO;

import java.util.List;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-08 22:19
 */
@Mapper
public interface GoodsDao {

    @Select("select * from goods where id = #{id} limit 1")
    GoodsPO findByGoodsId(Integer id);

    List<GoodsPO> findList(GoodsListQO goodsListQO);

    Integer count(GoodsListQO goodsListQO);

    Integer insert(GoodsPO goodsPO);

    Integer update(GoodsPO goodsPO);

    @Update("update goods set quantity = quantity - 1, date_update = now() where id = #{goodsId} and quantity > 0")
    Integer reduceStock(@Param("goodsId")Integer goodsId);

    @Select("select count(*) from goods where category_id = #{categoryId} and date_delete is null")
    Integer countByCategoryId(Integer categoryId);

    @Update("update goods set date_delete = now() where id = #{goodsId}")
    Integer delete(Integer goodsId);

    @Update("update goods set date_update = now(),images_urls=#{imagesUrls} where id = #{id}")
    Integer updateImagesUrls(GoodsPO goodsPO);
}
