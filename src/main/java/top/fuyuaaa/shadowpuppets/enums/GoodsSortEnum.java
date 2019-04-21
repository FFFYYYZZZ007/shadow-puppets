package top.fuyuaaa.shadowpuppets.enums;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-16 20:47
 */
public enum GoodsSortEnum {
    /**
     * 创建时间正序
     */
    CREATE_TIME_ASC(1, "创建时间正序"),
    /**
     * 创建时间倒序
     */
    CREATE_TIME_DESC(2, "创建时间倒序");
    private int code;
    private String desc;

    GoodsSortEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public int code() {
        return code;
    }

    public String desc() {
        return desc;
    }

    public static GoodsSortEnum find(int code) {
        for (GoodsSortEnum filterMode : GoodsSortEnum.values()) {
            if (filterMode.code == code) {
                return filterMode;
            }
        }
        return null;
    }
}
