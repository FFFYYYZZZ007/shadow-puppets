package top.fuyuaaa.shadowpuppets.common.enums;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-15 10:08
 */
@SuppressWarnings("all")
public enum OrderStatusEnum {

    PENDING_PAY(0, "待付款"),
    PENDING_DELIVERY(1,"待发货"),
    PENDING_RECEIVE(2,"待收货"),
    PENDING_COMMENT(3,"待评价"),
    FINISHED(4,"已完成"),
    CLOSED(5,"已关闭");


    private int code;
    private String desc;

    OrderStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int code() {
        return code;
    }

    public String desc() {
        return desc;
    }

    public static OrderStatusEnum find(int code) {
        for (OrderStatusEnum filterMode : OrderStatusEnum.values()) {
            if (filterMode.code == code) {
                return filterMode;
            }
        }
        return null;
    }
}
