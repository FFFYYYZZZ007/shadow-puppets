package top.fuyuaaa.shadowpuppets.common.enums;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-16 20:47
 */
@SuppressWarnings("all")
public enum UserSexEnum {

    MONKEY_GIRL(0, "女"),
    MONKEY_BOY(1, "男");

    private int code;
    private String desc;

    UserSexEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public int code() {
        return code;
    }

    public String desc() {
        return desc;
    }

    public static UserSexEnum find(int code) {
        for (UserSexEnum filterMode : UserSexEnum.values()) {
            if (filterMode.code == code) {
                return filterMode;
            }
        }
        return null;
    }
}
