package top.fuyuaaa.shadowpuppets.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import top.fuyuaaa.shadowpuppets.annotation.IgnoreSecurity;
import top.fuyuaaa.shadowpuppets.common.Result;
import top.fuyuaaa.shadowpuppets.model.SMS;
import top.fuyuaaa.shadowpuppets.model.bo.UserBO;
import top.fuyuaaa.shadowpuppets.model.vo.UserVO;
import top.fuyuaaa.shadowpuppets.service.UserService;
import top.fuyuaaa.shadowpuppets.util.*;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: fuyuaaa
 * @creat: 2019-03-28 22:41
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    /**
     * 短信验证码调用API地址
     */
    private final static String SMS_URL = "https://open.ucpaas.com/ol/sms/sendsms";

    private static ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("pyx-pool-%d").build();

    private ThreadPoolExecutor threadPool =
            new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() + 1, 10,
                    0, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1024), namedThreadFactory);

    /**
     * 验证码KEY前缀
     */
    private static final String CODE_REDIS_PREFIX = "code:";

    /**
     * 验证码过期时间 单位/秒
     */
    private static final Integer CODE_EXPIRE_TIME = 60;

    /**
     * token KEY 前缀
     */
    private static final String TOKEN_REDIS_PREFIX = "token:";

    /**
     * token 过期时间 单位/小时
     */
    private static final Integer TOKEN_EXPIRE_TIME = 1;

    @Autowired
    UserService userService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Value("${sms.send}")
    boolean isOpenSend;

    /**
     * 发送验证码
     *
     * @param tel 手机号
     * @return
     */
    @GetMapping("/code")
    @IgnoreSecurity
    public Result<String> sendVerificationCode(@RequestParam String tel) {
        //校验手机号格式
        if (!TelNumberUtils.isTel(tel)) {
            return Result.fail("手机号格式有误！");
        }
        //如果redis中已有(即近期已发送),防止频繁发送
        String key = CODE_REDIS_PREFIX + tel;
        if (null != redisTemplate.opsForValue().get(key)) {
            return Result.fail("请勿频繁请求！");
        }
        //生成验证码
        String code = RandomUtils.code6();
        log.info(code);
        //发送验证码
        threadPool.submit(() -> {
            if (isOpenSend) {
                SMS sms = new SMS(code, tel);
                String result = HttpUtils.post(SMS_URL, JSON.toJSONString(sms));
                if (StringUtils.isNotEmpty(result)) {
                    JSONObject json = JSONObject.parseObject(result);
                    log.info("发送验证码{}, tel: {}", "OK".equals(json.get("msg")) ? "成功" : "失败", tel);
                }
            }
        });
        redisTemplate.opsForValue().set(key, code, 60, TimeUnit.SECONDS);
        return Result.success("发送验证码成功");
    }

    @GetMapping("/name/check")
    public Result<Boolean> checkName(@RequestParam String userName) {
        UserBO userBO = userService.getByUserName(userName);
        boolean unExist = userBO == null;
        return Result.success(unExist);
    }

    @GetMapping("/tel/check")
    public Result<Boolean> checkTel(@RequestParam String tel) {
        UserBO userBO = userService.getByTel(tel);
        boolean unExist = userBO == null;
        return Result.success(unExist);
    }

    /**
     * 注册
     *
     * @param userBO 用户类
     * @param code   验证码
     * @return
     */
    @PostMapping("/register")
    @IgnoreSecurity
    public Result register(@RequestBody UserBO userBO,
                           @RequestParam String code) {

        if (!checkRegisterUserBO(userBO, code)) {
            return Result.fail("500", "请输入完整信息！");
        }

        String key = CODE_REDIS_PREFIX + userBO.getTel();
        if (!codeCheck(key, code)) {
            return Result.fail("验证码有误！");
        }

        userBO.setPassword(EncodeUtils.encode(userBO.getPassword()));

        if (!userService.addUser(userBO)) {
            return Result.fail("500", "注册失败！");
        }

        return Result.success();
    }

    /**
     * 登录
     *
     * @param userBO 用户类
     * @return
     */
    @PostMapping("/login")
    @IgnoreSecurity
    public Result<String> login(@RequestBody UserBO userBO) {
        if (!checkLoginUserBO(userBO)) {
            return Result.fail("500", "用户名或密码为空");
        }
        String password = userBO.getPassword();
        String userName = userBO.getUserName();
        password = EncodeUtils.encode(password);
        userBO = userService.getByUserName(userName);

        if (null == userBO) {
            return Result.fail("用户不存在！");
        }
        if (!userBO.getPassword().equals(password)) {
            return Result.fail("密码错误！");
        }
        //TODO 登陆状态
        String token = TOKEN_REDIS_PREFIX + userBO.getTel();
        setLoginStatus(token);

        return Result.success(token);
    }

    @PostMapping("/logout")
    public Result logout(@RequestParam String tel) {
        return Result.success();
    }

    @PostMapping("/update")
    public Result<String> updateUserInfo() {
        return Result.success();
    }

    @PostMapping("/info")
    public Result<UserVO> getUserInfo(@RequestParam String userId) {
        return Result.success();
    }

    //=========================================private help methods =========================================

    /**
     * 校验登录用户的字段
     *
     * @param userBO
     * @return
     */
    private boolean checkLoginUserBO(UserBO userBO) {
        if (null == userBO || StringUtils.isAnyEmpty(userBO.getUserName(), userBO.getPassword())) {
            return false;
        }
        return true;
    }

    /**
     * 校验注册用户的字段和验证码
     *
     * @param userBO
     * @param code
     * @return
     */
    private boolean checkRegisterUserBO(UserBO userBO, String code) {
        if (null == userBO || StringUtils.isAnyEmpty(userBO.getUserName(), userBO.getPassword(), userBO.getTel(), code)) {
            return false;
        }
        return true;
    }

    /**
     * 校验验证码
     *
     * @param tel  手机号
     * @param code 验证码
     * @return
     */
    private boolean codeCheck(String tel, String code) {
        String correctCode = redisTemplate.opsForValue().get(CODE_REDIS_PREFIX + tel);
        return null != correctCode && code.equals(correctCode);
    }

    /**
     * 将token保存至缓存中
     *
     * @param tel 手机号
     */
    private void setLoginStatus(String token) {

        // 如果不存在，设置缓存
        // TODO（这样做存在的问题，get和set不是原子性操作，考虑改成将整个步骤改成lua脚本）
        if (StringUtils.isEmpty((redisTemplate.opsForValue().get(token)))) {
            String value = String.valueOf(System.currentTimeMillis());
            redisTemplate.opsForValue().set(token, value, TOKEN_EXPIRE_TIME, TimeUnit.HOURS);
            return;
        }
        redisTemplate.expire(token, TOKEN_EXPIRE_TIME, TimeUnit.HOURS);

    }

    @GetMapping("/get")
    private A get() {
        String str = "{\n" +
                "      setup: 'What do you call a factory that sells passable products?',\n" +
                "      punchline: 'A satisfactory',\n" +
                "    }";
        A a = new A();
        a.setup = "What do you call a factory that sells passable products?";
        a.punchline = "A satisfactory";
        System.out.println(a);
        return a;
    }

    class A {
        public String setup;
        public String punchline;
    }
}
