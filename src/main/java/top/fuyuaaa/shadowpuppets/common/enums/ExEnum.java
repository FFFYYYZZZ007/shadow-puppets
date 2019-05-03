package top.fuyuaaa.shadowpuppets.common.enums;

import lombok.Getter;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-18 16:59
 */
public enum ExEnum {

    /**
     * 异常code 和 msg，这里的code其实随便定的，反正也没用
     */
    PARAM_ERROR("401", "参数错误，请检查参数"),
    NON_EXIST_USER("402", "用户不存在"),
    NON_LOGIN_USER("403", "请先登录"),
    IS_NOT_ORDER_OWNER("404", "订单不属于你，无法操作"),
    ORDER_CREATE_PARAMS_ERROR("405", "创建订单失败，参数错误"),
    ADMIN_AUTH_ERROR("501", "需要管理员权限"),
    UPDATE_DELIVERY_STATUS_ERROR("501", "更新物流状态失败"),
    UPLOAD_ERROR("600", "上传文件异常"),

    SHOPPING_CART_EXIST_GOODS("650","购物车中已存在"),
    SHOPPING_CART_IS_NOT_BELONGS("651","该购物车记录不属于你"),

    COMMENT_ADD_ERROR("700","添加评论失败"),
    COMMENT_LIKE_ERROR("701","点赞评论失败"),
    COMMENT_REMOVE_ERROR("702","删除评论失败"),
    COMMENT_HAVE_EXIST("703","您已经评论过了"),

    COURSE_ADD_ERROR("801","添加课程失败"),
    COURSE_UPDATE_ERROR("802","添加课程失败"),
    COURSE_DELETE_ERROR("803","删除课程失败"),

    COURSE_ORDER_ADD_ERROR("811","生成订单失败"),
    COURSE_ORDER_CLOSE_ERROR("812","取消订单失败"),

    PASSWORD_CHANGE_ERROR("900","修改密码错误，原密码错误")
    ;

    /**
     * code
     */
    @Getter
    private String code;
    /**
     * msg
     */
    @Getter
    private String msg;

    ExEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String code() {
        return code;
    }

    public String msg() {
        return msg;
    }

    public static ExEnum find(String code) {
        for (ExEnum filterMode : ExEnum.values()) {
            if (filterMode.code.equals(code)) {
                return filterMode;
            }
        }
        return null;
    }
}
