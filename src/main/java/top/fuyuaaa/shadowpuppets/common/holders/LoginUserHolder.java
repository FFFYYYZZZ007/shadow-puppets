package top.fuyuaaa.shadowpuppets.common.holders;

import top.fuyuaaa.shadowpuppets.model.LoginUserInfo;

/**
 *<p>
 *登录用户信息上下文
 *</p>
 *
 * @author fuyuaaa
 * @date 2019/4/8 Fri 11:43:00
 */
public class LoginUserHolder extends AbstractOncePerRequestContextHolder<LoginUserInfo> {

    private LoginUserHolder() {
    }

    private static final LoginUserHolder INSTANCE = new LoginUserHolder();

    public static LoginUserHolder instance() {
        return INSTANCE;
    }

    @Override
    public String key() {
        return this.getClass().getName()+"#UserInfo";
    }
}
