package top.fuyuaaa.shadowpuppets.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import top.fuyuaaa.shadowpuppets.model.po.GoodsOrderPO;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-15 00:21
 */
@Mapper
public interface GoodsOrderDao {

    @Insert("INSERT INTO `goods_order`" +
            "(`user_id`, `goods_ids`, `express_fee`, `deal_price`, `status`,`delivery_status`, `date_create`, `date_update`) " +
            "VALUES ( #{userId},#{goodsIds},#{expressFee},#{dealPrice}, 0,0, now(), now());")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insertNewGoodsOrder(GoodsOrderPO goodsOrderPO);


    @Select("select * from goods_order where id = #{orderId} limit 1")
    GoodsOrderPO getById(Integer orderId);
}
