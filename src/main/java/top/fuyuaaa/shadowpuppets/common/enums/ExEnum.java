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

    ADMIN_AUTH_ERROR("00101", "需要管理员权限"),

    /**
     * COMMON ERROR ENUM
     */
    PARAM_ERROR("10101", "参数错误，请检查参数"),

    /**
     * 类别 相关
     */
    CATEGORY_ADD_ERROR("10201","添加类别失败"),
    CATEGORY_UPDATE_ERROR("10202","修改类别失败"),
    CATEGORY_REMOVE_ERROR("10203","删除类别失败"),
    CATEGORY_OPERATE_ERROR("10204","操作类别失败，请校验参数"),

    /**
     * USER 相关
     */
    IS_NOT_ORDER_OWNER("10301", "订单不属于你，无法操作"),
    NON_EXIST_USER("10302", "用户不存在"),
    NON_LOGIN_USER("10303", "请先登录"),
    REGISTER_USERNAME_EXIST("10304", "用户名已被注册"),
    REGISTER_TEL_EXIST("10305", "手机号已被注册"),
    REGISTER_ERROR("10306", "注册失败"),
    INVALID_USER("10307", "无效的用户"),
    USER_UPDATE_ERROR("10308", "修改用戶信息失败"),
    User_REMOVE_ERROR("10309", "删除用戶失败"),

    /**
     * goods 相关
     */
    GOODS_ADD_ERROR("10401", "添加商品失败"),
    GOODS_UPDATE_ERROR("10402", "修改商品失败"),
    GOODS_REMOVE_ERROR("10403", "删除商品失败"),

    ORDER_CREATE_PARAMS_ERROR("10405", "创建订单失败，参数错误"),
    UPDATE_DELIVERY_STATUS_ERROR("10501", "更新物流状态失败"),
    UPLOAD_ERROR("10600", "上传文件异常"),

    SHOPPING_CART_EXIST_GOODS("10650","购物车中已存在"),
    SHOPPING_CART_IS_NOT_BELONGS("10651","该购物车记录不属于你"),
    SHOPPING_ADD_ERROR("10652","该购物车记录不属于你"),

    COMMENT_ADD_ERROR("10701","添加评论失败"),
    COMMENT_REMOVE_ERROR("10702","删除评论失败"),
    COMMENT_HAVE_EXIST("10703","您已经评论过了"),

    COURSE_ADD_ERROR("10801","添加课程失败"),
    COURSE_UPDATE_ERROR("10802","添加课程失败"),
    COURSE_DELETE_ERROR("10803","删除课程失败"),

    COURSE_ORDER_ADD_ERROR("10811","生成订单失败"),
    COURSE_ORDER_CLOSE_ERROR("10812","取消订单失败"),

    PASSWORD_CHANGE_ERROR("10900","修改密码错误，原密码错误")
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
