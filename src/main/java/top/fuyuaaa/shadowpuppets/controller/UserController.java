package top.fuyuaaa.shadowpuppets.controller;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import top.fuyuaaa.shadowpuppets.annotation.NeedLogin;
import top.fuyuaaa.shadowpuppets.common.Result;
import top.fuyuaaa.shadowpuppets.enums.ExEnum;
import top.fuyuaaa.shadowpuppets.exceptions.ParamException;
import top.fuyuaaa.shadowpuppets.model.PageVO;
import top.fuyuaaa.shadowpuppets.model.bo.UserBO;
import top.fuyuaaa.shadowpuppets.model.qo.UserListQO;
import top.fuyuaaa.shadowpuppets.model.qo.UserLoginQO;
import top.fuyuaaa.shadowpuppets.model.vo.UserVO;
import top.fuyuaaa.shadowpuppets.service.UserService;
import top.fuyuaaa.shadowpuppets.util.*;

import java.text.ParseException;
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

    private static ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("pyx-pool-%d").build();

    private static ThreadPoolExecutor threadPool =
            new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() + 1, 10,
                    0, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1024), namedThreadFactory);

    /**
     * 验证码KEY前缀
     */
    private static final String CODE_REDIS_PREFIX = "code:";

    /**
     * 验证码过期时间 单位/秒
     */
    private static final Integer CODE_EXPIRE_TIME = 300;

    /**
     * 是否发送过 KYE 前缀
     */
    private static final String IS_SEND_PREFIX = "isSend:";

    /**
     * 是否发送过 60s, 防止频繁发送
     */
    private static final Integer IS_SEND_EXPIRE_TIME = 60;

    /**
     * token KEY 前缀
     */
    private static final String TOKEN_REDIS_PREFIX = "token:";

    /**
     * token 过期时间 单位/天
     */
    private static final Integer TOKEN_EXPIRE_TIME = 7;

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
    public Result<String> sendVerificationCode(@RequestParam String tel) {
        //校验手机号格式
        if (!TelNumberUtils.isTel(tel)) {
            return Result.fail("手机号格式有误！");
        }
        //如果redis中已有(即近期已发送),防止频繁发送
        String isSendKey = IS_SEND_PREFIX + tel;
        if (null != redisTemplate.opsForValue().get(isSendKey)) {
            return Result.fail("请勿频繁请求发送！");
        }
        //生成验证码
        String code = RandomUtils.code6();
        log.info(code);
        //发送验证码
        sendCode(isOpenSend, code, tel);

        String key = CODE_REDIS_PREFIX + tel;
        redisTemplate.opsForValue().set(key, code, CODE_EXPIRE_TIME, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(isSendKey, code, IS_SEND_EXPIRE_TIME, TimeUnit.SECONDS);
        return Result.success("发送验证码成功");
    }

    @GetMapping("/name/check")
    public Result checkName(@RequestParam String userName) {
        UserBO userBO = userService.getByUserName(userName);
        boolean unExist = userBO == null;
        return unExist ? Result.success().setMsg("用户名可用") :
                Result.fail("用户名已被注册");
    }

    @GetMapping("/tel/check")
    public Result checkTel(@RequestParam String tel) {
        UserBO userBO = userService.getByTel(tel);
        boolean unExist = userBO == null;
        return unExist ? Result.success().setMsg("手机号可用") :
                Result.fail("手机号已被注册");
    }

    @GetMapping("/test")
    @NeedLogin
    public Result xx(@RequestParam String xxx) {
        if (xxx.equals("xxx")) {
            throw new ParamException(ExEnum.PARAM_ERROR.getMsg());
        }
        return null;
    }

    /**
     * 注册
     *
     * @param userBO 用户类
     * @param code   验证码
     * @return Result
     */
    @PostMapping("/register")
    public Result register(@RequestBody UserBO userBO,
                           @RequestParam String code) {
        if (!checkRegisterUserBO(userBO, code)) {
            return Result.fail("请输入完整信息！");
        }

        if (!codeCheck(userBO.getTel(), code)) {
            return Result.fail("验证码有误！请重试！");
        }

        userBO.setPassword(EncodeUtils.encode(userBO.getPassword()));
        if (!userService.addUser(userBO)) {
            return Result.fail("500", "注册失败！请重试！");
        }
        return Result.success();
    }

    /**
     * 登录
     *
     * @param userLoginQO 用户登录类
     * @return Result
     */
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

        String token = TOKEN_REDIS_PREFIX + userBO.getTel();
        setLoginStatus(token);
        boolean isAdmin = userService.isAdmin(userBO.getId());
        return Result.success(token, userBO.getUserName() + "," + isAdmin);
    }

    @PostMapping("/logout")
    public Result logout(@RequestParam String tel) {
        redisTemplate.delete(TOKEN_REDIS_PREFIX + tel);
        return Result.success().setMsg("退出登录成功");
    }

    @PostMapping("/update")
    public Result updateUserInfo(@RequestBody UserVO userVO) {
        if (null == userVO.getId() || null == userService.getById(userVO.getId())) {
            return Result.fail("无效的用户");
        }
        UserBO userBO = convertUserVO2BO(userVO);
        boolean success = userService.updateUser(userBO);
        return success ? Result.success() : Result.fail("修改用户信息失败");
    }

    @PostMapping("/info")
    public Result<UserVO> getUserInfo(@RequestParam Integer userId) {
        UserVO userVO = userService.getByVOId(userId);
        return Result.success(userVO);
    }

    //================================= 用户管理接口 ========================================

    @PostMapping("/manager/list")
    public Result<PageVO<UserVO>> getUserManagerList(@RequestBody UserListQO userListQO) {
        fillQueryParam(userListQO);
        PageVO<UserVO> userManagerList = userService.getUserManagerList(userListQO);
        return Result.success(userManagerList);
    }

    @PostMapping("/manager/remove")
    public Result removeUserByManager(@RequestParam Integer userId) {
        Boolean success = userService.removeUser(userId);
        return success? Result.success().setMsg("移除用户成功"):Result.fail("移除用户失败");
    }

    //============================== private help methods ==============================

    private UserBO convertUserVO2BO(UserVO userVO) {
        UserBO userBO = BeanUtils.copyProperties(userVO, UserBO.class);
        userBO.setSex("男".equals(userVO.getSex()) ? 1 : 0);
        try {
            userBO.setBirthday(DateUtils.parseDate(userVO.getBirthday(), "yyyy-MM-dd"));
        } catch (ParseException e) {
            throw new ParamException(ExEnum.PARAM_ERROR.getMsg());
        }
        return userBO;
    }

    /**
     * 发送短信验证码
     *
     * @param isOpenSend 是否开启发送短信
     * @param code       验证码
     * @param tel        手机号
     */
    private void sendCode(Boolean isOpenSend, String code, String tel) {
        if (isOpenSend) {
            SmsUtil.sendMsg(code, tel);
        }
    }

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
     * @param token 手机号
     */
    private void setLoginStatus(String token) {
        // 如果不存在，设置缓存
        // TODO（这样做存在的问题，get和set不是原子性操作，考虑改成将整个步骤改成lua脚本）
        if (StringUtils.isEmpty((redisTemplate.opsForValue().get(token)))) {
            String value = String.valueOf(System.currentTimeMillis());
            redisTemplate.opsForValue().set(token, value, TOKEN_EXPIRE_TIME, TimeUnit.DAYS);
            return;
        }
        redisTemplate.expire(token, TOKEN_EXPIRE_TIME, TimeUnit.DAYS);
    }

    /**
     * 填充分页参数(如果没有分页参数)
     *
     * @param userListQO 用户列表查询对象
     */
    private void fillQueryParam(UserListQO userListQO) {
        Integer pageNum = userListQO.getPageNum();
        Integer pageSize = userListQO.getPageSize();
        if (pageNum == null || pageNum <= 0) {
            userListQO.setPageNum(1);
        }
        if (pageSize == null || pageSize <= 0) {
            userListQO.setPageSize(10);
        }
    }

}
