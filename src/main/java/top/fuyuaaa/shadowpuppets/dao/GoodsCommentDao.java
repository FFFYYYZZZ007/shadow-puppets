package top.fuyuaaa.shadowpuppets.dao;

import org.apache.ibatis.annotations.*;
import top.fuyuaaa.shadowpuppets.model.po.GoodsCommentPO;

import java.util.List;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-26 18:29
 */
@Mapper
@SuppressWarnings("all")
public interface GoodsCommentDao {
    @Insert("insert into comment (order_id,user_id,goods_id,star_level,content,date_create,date_update) " +
            "values (#{orderId},#{userId},#{goodsId},#{starLevel},#{content},now(),now())")
    @Options(keyColumn = "id",keyProperty = "id",useGeneratedKeys = true)
    Integer insert(GoodsCommentPO goodsCommentPO);

    @Delete("update comment set date_delete = now() where id = #{commentId}")
    Integer delete(Integer commentId);

    @Select("select count(*) from comment where order_id = #{orderId}")
    Integer findByOrderId(@Param("orderId") String orderId);

    @Select("select c.*,u.user_name from comment c left join user u on c.user_id = u.id " +
            "where c.goods_id = #{goods_id} order by c.date_create desc")
    List<GoodsCommentPO> findListByGoodsId(Integer goodsId);
}
