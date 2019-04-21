package top.fuyuaaa.shadowpuppets.dao;

import org.apache.ibatis.annotations.*;
import top.fuyuaaa.shadowpuppets.model.po.ShoppingCartPO;

import java.util.List;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-11 22:19
 */
@Mapper
public interface ShoppingCartDao {

    @Insert("insert into shopping_cart (user_id,goods_id,num,date_create,date_update) " +
            "values (#{userId},#{goodsId},#{num},now(),now())")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    Integer insertShoppingCart(ShoppingCartPO shoppingCartPO);


    @Select("select * from shopping_cart where id = #{id} limit 1")
    ShoppingCartPO getById(Integer id);

    /**
     * 查询购物车列表 BY userId
     * @param userId 用户ID
     * @return 购物车列表
     */
    @Select("select * from shopping_cart where user_id = #{userId} and date_delete is null order by date_create desc")
    List<ShoppingCartPO> queryShoppingCartListByUserId(@Param("userId") Integer userId);

    @Update("update shopping_cart set date_delete = now() where id=#{id} limit 1")
    Integer deleteShoppingCart(Integer id);
}
