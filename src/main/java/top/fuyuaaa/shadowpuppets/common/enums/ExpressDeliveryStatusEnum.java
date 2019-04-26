package top.fuyuaaa.shadowpuppets.common.enums;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-15 10:08
 */
public enum ExpressDeliveryStatusEnum {

    /**
     * 未发货
     */
    UN_DELIVERY(0, "待发货"),
    /**
     * 已发货
     */
    IS_DELIVERY(1,"已发货"),
    /**
     * 配送中
     */
    IN_DISTRIBUTION(2,"配送中"),
    /**
     * 已送达
     */
    IS_ARRIVED(3,"已送达");


    private int code;
    private String desc;

    ExpressDeliveryStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int code() {
        return code;
    }

    public String desc() {
        return desc;
    }

    public static ExpressDeliveryStatusEnum find(int code) {
        for (ExpressDeliveryStatusEnum filterMode : ExpressDeliveryStatusEnum.values()) {
            if (filterMode.code == code) {
                return filterMode;
            }
        }
        return null;
    }
}
