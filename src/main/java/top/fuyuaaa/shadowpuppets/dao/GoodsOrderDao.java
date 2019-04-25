package top.fuyuaaa.shadowpuppets.dao;

import org.apache.ibatis.annotations.*;
import top.fuyuaaa.shadowpuppets.model.po.GoodsOrderInfoPO;
import top.fuyuaaa.shadowpuppets.model.po.GoodsOrderPO;
import top.fuyuaaa.shadowpuppets.model.qo.GoodsOrderQO;

import java.util.List;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-15 00:21
 */
@Mapper
public interface GoodsOrderDao {

    @Insert("INSERT INTO `goods_order`" +
            "(`id`,`user_id`, `express_fee`, `deal_price`, `status`,`delivery_status`, `date_create`, `date_update`) " +
            "VALUES ( #{id}, #{userId},#{expressFee},#{dealPrice}, 0,0, now(), now());")
    void insertNewGoodsOrder(GoodsOrderPO goodsOrderPO);


    @Select("select * from goods_order where id = #{orderId} limit 1")
    GoodsOrderPO getById(String orderId);

    @Update("update goods_order set status=3 ,date_update = now() where id = #{orderId}")
    Integer cancelGoodsOrder(String orderId);


    List<GoodsOrderPO> getOrderList(GoodsOrderQO goodsOrderQO);

    @Insert("insert into goods_order_info (goods_order_id,goods_id,num,date_create,date_update) " +
            "values (#{goodsOrderId},#{goodsId},#{num},now(),now())")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    Integer insertGoodsOrderInfo(GoodsOrderInfoPO goodsOrderInfoPO);

    @Select("select * from goods_order_info where goods_order_id = #{orderId} and date_delete is null")
    List<GoodsOrderInfoPO> getOrderInfoByOrderId(String orderId);

    @Update("update goods_order set status = #{status}, date_update= now() where id = #{orderId}")
    Integer updateOrderStatus(@Param("status") Integer status, @Param("orderId") String orderId);

    @Update("update goods_order set delivery_status = #{deliveryStatus}, date_update= now() where id = #{orderId}")
    Integer updateOrderDeliveryStatus(@Param("deliveryStatus") Integer deliveryStatus, @Param("orderId") String orderId);


    //==============================  订单管理  ==============================

    Integer count(GoodsOrderQO goodsOrderQO);
}
