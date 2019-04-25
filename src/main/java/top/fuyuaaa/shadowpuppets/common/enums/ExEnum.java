package top.fuyuaaa.shadowpuppets.common.enums;

import lombok.Getter;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-18 16:59
 */
public enum ExEnum {

    /**
     * 异常code 和 msg
     */
    PARAM_ERROR("401", "参数错误，请检查参数"),
    NON_EXIST_USER("402", "用户不存在"),
    NON_LOGIN_USER("403", "请先登录"),
    IS_NOT_ORDER_OWNER("404", "订单不属于你，无法操作"),
    ORDER_CREATE_PARAMS_ERROR("405", "创建订单失败，参数错误"),
    ADMIN_AUTH_ERROR("501", "需要管理员权限"),
    UPDATE_DELIVERY_STATUS_ERROR("501", "修改物流状态失败"),
    UPLOAD_ERROR("600", "上传文件异常"),
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
