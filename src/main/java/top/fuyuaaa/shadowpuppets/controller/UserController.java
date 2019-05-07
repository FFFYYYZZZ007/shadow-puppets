package top.fuyuaaa.shadowpuppets.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import top.fuyuaaa.shadowpuppets.common.annotations.NeedLogin;
import top.fuyuaaa.shadowpuppets.common.Constant;
import top.fuyuaaa.shadowpuppets.common.Result;
import top.fuyuaaa.shadowpuppets.common.holders.LoginUserHolder;
import top.fuyuaaa.shadowpuppets.common.utils.EncodeUtils;
import top.fuyuaaa.shadowpuppets.model.bo.UserBO;
import top.fuyuaaa.shadowpuppets.model.bo.UserPasswordBO;
import top.fuyuaaa.shadowpuppets.model.qo.UserLoginQO;
import top.fuyuaaa.shadowpuppets.model.vo.UserVO;
import top.fuyuaaa.shadowpuppets.service.UserService;

import java.util.concurrent.TimeUnit;

/**
 * @author: fuyuaaa
 * @creat: 2019-03-28 22:41
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Value("${sms.send}")
    boolean isOpenSend;

    @GetMapping("/code")
    public Result<String> sendVerificationCode(@RequestParam String tel) {
        userService.sendCode(tel);
        return Result.success("发送验证码成功");
    }

    @PostMapping("/register")
    public Result register(@RequestBody UserBO userBO,
                           @RequestParam String code) {
        userService.addUser(userBO, code);
        return Result.success().setMsg("注册成功，即将跳转到登录页面");
    }

    @PostMapping("/login")
    public Result login(@RequestBody UserLoginQO userLoginQO) {

        if (!checkLoginUser(userLoginQO)) {
            return Result.fail("500", "用户名或密码为空");
        }
        String password = userLoginQO.getPassword();
        String userName = userLoginQO.getUserName();
        password = EncodeUtils.encode(password);
        UserBO userBO = userService.getByUserName(userName);
        if (null == userBO) {
            return Result.fail("用户不存在！");
        }
        if (!userBO.getPassword().equals(password)) {
            return Result.fail("密码错误！");
        }

        String token = Constant.User.TOKEN_REDIS_PREFIX + userBO.getTel();
        setLoginStatus(token);
        boolean isAdmin = userService.isAdmin(userBO.getId());
        return Result.success(token, userBO.getUserName() + "," + isAdmin);
    }

    @PostMapping("/logout")
    public Result logout(@RequestParam String tel) {
        redisTemplate.delete(Constant.User.TOKEN_REDIS_PREFIX + tel);
        return Result.success().setMsg("退出登录成功");
    }

    @PostMapping("/update")
    public Result updateUserInfo(@RequestBody UserVO userVO) {
        userService.updateUser(userVO);
        return Result.success().setMsg("修改成功");
    }

    @PostMapping("/info")
    @NeedLogin
    public Result<UserVO> getUserInfo() {
        Integer userId = LoginUserHolder.instance().get().getId();
        UserVO userVO = userService.getByVOId(userId);
        return Result.success(userVO);
    }

    @PostMapping("/changePassword")
    @NeedLogin
    public Result<Boolean> changePassword(@RequestBody UserPasswordBO userPasswordBO) {
        userService.changePassword(userPasswordBO);
        return Result.success(true).setMsg("修改密码成功");
    }


    //============================== private help methods ==============================

    /**
     * 校验登录用户的字段
     *
     * @param user
     * @return
     */
    private boolean checkLoginUser(UserLoginQO user) {
        if (null == user || StringUtils.isAnyEmpty(user.getUserName(), user.getPassword())) {
            return false;
        }
        return true;
    }
    /**
     * 将token保存至缓存中
     *
     * @param token 手机号
     */
    private void setLoginStatus(String token) {
        // 如果不存在，设置缓存
        // （这样做存在的问题，get和set不是原子性操作，考虑改成将整个步骤改成lua脚本）
        if (StringUtils.isEmpty((redisTemplate.opsForValue().get(token)))) {
            String value = String.valueOf(System.currentTimeMillis());
            redisTemplate.opsForValue().set(token, value, Constant.User.TOKEN_EXPIRE_TIME, TimeUnit.DAYS);
            return;
        }
        redisTemplate.expire(token, Constant.User.TOKEN_EXPIRE_TIME, TimeUnit.DAYS);
    }
}
