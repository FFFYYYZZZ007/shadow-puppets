package top.fuyuaaa.shadowpuppets.common.enums;

/**
 * @author: fuyuaaa
 * @creat: 2019-05-02 20:50
 */
@SuppressWarnings("all")
public enum CourseOrderStatusEnum  {

    PENDING_PAY(0, "待付款"),
    PENDING_STUDY(1,"待学习"),
    PENDING_CONFIRMED(2, "待确认结课"),
    PENGDING_COMMENT(3,"待评价"),
    FINISHED(4,"已完成"),
    CLOSED(5,"已关闭");


    private int code;
    private String desc;

    CourseOrderStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int code() {
        return code;
    }

    public String desc() {
        return desc;
    }

    public static CourseOrderStatusEnum find(int code) {
        for (CourseOrderStatusEnum filterMode : CourseOrderStatusEnum.values()) {
            if (filterMode.code == code) {
                return filterMode;
            }
        }
        return null;
    }
}
