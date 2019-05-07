package top.fuyuaaa.shadowpuppets.common;

/**
 * @author: fuyuaaa
 * @creat: 2019-05-07 15:45
 */
public interface Constant {
    interface User {
        /**
         * 是否发送过 KYE 前缀
         */
        String IS_SEND_PREFIX = "isSend:";
        /**
         * 验证码KEY前缀
         */
        String CODE_REDIS_PREFIX = "code:";
        /**
         * 验证码过期时间 单位/秒
         */
        Integer CODE_EXPIRE_TIME = 300;
        /**
         * 是否发送过 60s, 防止频繁发送
         */
        Integer IS_SEND_EXPIRE_TIME = 60;

        /**
         * token KEY 前缀
         */
        String TOKEN_REDIS_PREFIX = "token:";

        /**
         * token 过期时间 单位/天
         */
        Integer TOKEN_EXPIRE_TIME = 7;
    }
}
