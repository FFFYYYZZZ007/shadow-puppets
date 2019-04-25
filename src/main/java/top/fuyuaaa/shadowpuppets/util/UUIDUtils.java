package top.fuyuaaa.shadowpuppets.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;
import java.util.UUID;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-25 22:03
 */
@SuppressWarnings("all")
public class UUIDUtils {

    /**
     * 获取订单编号
     */
    public static String getOrderCode(){
        return DateFormatUtils.format(new Date(), "yyyyMMddHHmmSSs") + RandomUtils.code6();
    }

    /**
     * 获取32位UUID
     */
    public static String getUUID(){
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid;
    }
}
