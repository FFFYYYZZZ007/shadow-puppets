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
            "(`user_id`, `express_fee`, `deal_price`, `status`,`delivery_status`, `date_create`, `date_update`) " +
            "VALUES ( #{userId},#{expressFee},#{dealPrice}, 0,0, now(), now());")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insertNewGoodsOrder(GoodsOrderPO goodsOrderPO);


    @Select("select * from goods_order where id = #{orderId} limit 1")
    GoodsOrderPO getById(Integer orderId);

    @Update("update goods_order set status=3 ,date_update = now() where id = #{orderId}")
    Integer cancelGoodsOrder(Integer orderId);


    List<GoodsOrderPO> getOrderList(GoodsOrderQO goodsOrderQO);

    @Insert("insert into goods_order_info (goods_order_id,goods_id,num,date_create,date_update) " +
            "values (#{goodsOrderId},#{goodsId},#{num},now(),now())")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    Integer insertGoodsOrderInfo(GoodsOrderInfoPO goodsOrderInfoPO);

    @Select("select * from goods_order_info where goods_order_id = #{orderId} and date_delete is null")
    List<GoodsOrderInfoPO> getOrderInfoByOrderId(Integer orderId);

    @Update("update goods_order set status = #{status}, date_update= now() where id = #{orderId}")
    Integer updateOrderStatus(@Param("status") Integer status, @Param("orderId") Integer orderId);

    @Update("update goods_order set delivery_status = #{deliveryStatus}, date_update= now() where id = #{orderId}")
    Integer updateOrderDeliveryStatus(@Param("deliveryStatus") Integer deliveryStatus, @Param("orderId") Integer orderId);


    //==============================  订单管理  ==============================

    Integer count(GoodsOrderQO goodsOrderQO);
}
