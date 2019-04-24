package top.fuyuaaa.shadowpuppets.common.enums;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-15 10:08
 */
public enum OrderStatusEnum {
    /**
     * 未支付
     */
    UN_PAID(0, "未支付"),
    PAID(1,"已支付"),
    CLOSED(2,"已完成"),
    FINISHED(3,"已关闭");


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
