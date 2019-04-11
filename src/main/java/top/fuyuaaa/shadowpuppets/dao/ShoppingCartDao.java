package top.fuyuaaa.shadowpuppets.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.fuyuaaa.shadowpuppets.model.po.ShoppingCartPO;

import java.util.List;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-11 22:19
 */
@Mapper
public interface ShoppingCartDao {

    @Insert("insert shopping_cart into (user_id,goods_id,num,date_create,date_update) " +
            "values (#{userId},#{goodsId},#{num},now(),now())")
    Integer insertShoppingCart(ShoppingCartPO shoppingCartPO);

    /**
     * 查询购物车列表 BY userId
     * @param userId 用户ID
     * @return 购物车列表
     */
    @Select("select * from shopping_cart where user_id = #{userId} order by date_create desc")
    List<ShoppingCartPO> queryShoppingCartListByUserId(@Param("userId") Integer userId);
}
