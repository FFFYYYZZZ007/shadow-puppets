package top.fuyuaaa.shadowpuppets.dao;

import org.apache.ibatis.annotations.*;
import top.fuyuaaa.shadowpuppets.common.enums.ExpressDeliveryStatusEnum;
import top.fuyuaaa.shadowpuppets.model.po.ExpressDeliveryPO;
import top.fuyuaaa.shadowpuppets.model.qo.ExpressDeliveryQO;
import top.fuyuaaa.shadowpuppets.model.qo.ShipQO;

import java.util.List;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-25 23:27
 */
@Mapper
@SuppressWarnings("unused")
public interface ExpressDeliveryDao {
    @Insert("insert into express_delivery_info (order_id, delivery_status, express_code,express_carrier, express_price, date_create, date_update) " +
            "values (#{orderId},#{deliveryStatus},#{expressCode},#{expressCarrier},#{expressPrice},now(),now())")
    Integer insert(ExpressDeliveryPO expressDeliveryPO);

    @Update("update express_delivery_info set delivery_status = 1,express_carrier = #{expressCarrier},express_code = #{expressCode},date_express_start=now() " +
            "where order_id = #{orderId}")
    Integer ship(ShipQO shipQO);

    @Update("update express_delivery_info set delivery_status = #{deliveryStatusCode}, date_update = now() " +
            "where order_id = #{orderId} and delivery_status = #{deliveryStatusCode} - 1")
    Integer updateDeliveryStatus(@Param("orderId") String orderId, @Param("deliveryStatusCode")Integer deliveryStatusCode);

    @Update("update express_delivery_info set date_express_end = now(), date_update = now() " +
            "where order_id = #{orderId}")
    Integer updateDateExpressEnd(@Param("orderId") String orderId);

    ExpressDeliveryPO findByOrderId(String orderId);

    List<ExpressDeliveryPO> findList(ExpressDeliveryQO expressDeliveryQO);

}
