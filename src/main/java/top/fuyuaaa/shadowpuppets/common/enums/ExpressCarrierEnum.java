package top.fuyuaaa.shadowpuppets.common.enums;

import lombok.Getter;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-26 11:26
 */
@SuppressWarnings("all")
public enum  ExpressCarrierEnum {

    SF_EXPRESS(1,"顺丰"),
    YT_EXPRESS(2,"圆通"),
    ST_EXPRESS(3,"申通"),
    EMS_EXPRESS(4,"EMS");

    @Getter
    private int code;
    @Getter
    private String desc;

    ExpressCarrierEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer code() {
        return code;
    }

    public String desc() {
        return desc;
    }

    public static ExpressCarrierEnum find(int code) {
        for (ExpressCarrierEnum filterMode : ExpressCarrierEnum.values()) {
            if (filterMode.code == code) {
                return filterMode;
            }
        }
        return null;
    }
}
