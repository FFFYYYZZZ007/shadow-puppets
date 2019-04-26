package top.fuyuaaa.shadowpuppets.dao;

import org.apache.ibatis.annotations.*;
import top.fuyuaaa.shadowpuppets.common.enums.ExpressDeliveryStatusEnum;
import top.fuyuaaa.shadowpuppets.model.po.ExpressDeliveryPO;
import top.fuyuaaa.shadowpuppets.model.qo.ExpressDeliveryQO;

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

    @Update("update express_delivery_info set delivery_status = #{deliveryStatusCode}, date_update = now() where order_id = #{orderId}")
    Integer updateDeliveryStatus(@Param("orderId") String orderId, @Param("deliveryStatusCode")Integer deliveryStatusCode);

    @Select("select * from express_delivery_info where order_id = #{orderId}")
    ExpressDeliveryPO findByOrderId(String orderId);

    List<ExpressDeliveryPO> findList(ExpressDeliveryQO expressDeliveryQO);
}
