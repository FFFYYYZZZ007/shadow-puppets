package top.fuyuaaa.shadowpuppets.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import top.fuyuaaa.shadowpuppets.common.Constant;
import top.fuyuaaa.shadowpuppets.common.enums.ExEnum;
import top.fuyuaaa.shadowpuppets.common.enums.UserSexEnum;
import top.fuyuaaa.shadowpuppets.common.utils.EncodeUtils;
import top.fuyuaaa.shadowpuppets.common.utils.RandomUtils;
import top.fuyuaaa.shadowpuppets.common.utils.SmsUtil;
import top.fuyuaaa.shadowpuppets.common.utils.TelNumberUtils;
import top.fuyuaaa.shadowpuppets.dao.UserDao;
import top.fuyuaaa.shadowpuppets.common.exceptions.ParamException;
import top.fuyuaaa.shadowpuppets.common.exceptions.UserException;
import top.fuyuaaa.shadowpuppets.common.holders.LoginUserHolder;
import top.fuyuaaa.shadowpuppets.mapstruct.UserConverter;
import top.fuyuaaa.shadowpuppets.model.PageVO;
import top.fuyuaaa.shadowpuppets.model.analysis.NameValue;
import top.fuyuaaa.shadowpuppets.model.bo.UserBO;
import top.fuyuaaa.shadowpuppets.model.bo.UserPasswordBO;
import top.fuyuaaa.shadowpuppets.model.po.UserPO;
import top.fuyuaaa.shadowpuppets.model.qo.UserListQO;
import top.fuyuaaa.shadowpuppets.model.vo.UserVO;
import top.fuyuaaa.shadowpuppets.service.UserService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: fuyuaaa
 * @creat: 2019-03-28 23:18
 */
@Service("userService")
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    UserDao userDao;
    @Value("${sms.send}")
    boolean isOpenSend;

    @Override
    public UserBO getByUserName(String userName) {
        UserPO userPO = userDao.getByUserName(userName);
        if (null == userPO) {
            return null;
        }
        return UserConverter.INSTANCE.toUserBO(userPO);
    }

    @Override
    public UserBO getByTel(String tel) {
        UserPO userPO = userDao.getByTel(tel);
        if (null == userPO) {
            return null;
        }
        return UserConverter.INSTANCE.toUserBO(userPO);
    }

    @Override
    public void addUser(UserBO userBO, String code) {
        validateUserBO(userBO, code);
        fillUserBO(userBO);
        UserPO userPO = UserConverter.INSTANCE.toUserPO(userBO);
        if (1 != userDao.insert(userPO)) {
            throw new UserException(ExEnum.REGISTER_ERROR.getMsg());
        }
    }

    @Override
    public void sendCode(String tel) {
        //校验手机号格式
        if (!TelNumberUtils.isTel(tel)) {
            throw new UserException("手机号格式有误！");
        }
        //如果redis中已有(即近期已发送),防止频繁发送
        String isSendKey = Constant.User.IS_SEND_PREFIX + tel;
        if (null != redisTemplate.opsForValue().get(isSendKey)) {
            throw new UserException("请勿频繁请求发送！");
        }
        //生成验证码
        String code = RandomUtils.code6();
        log.info(code);
        //发送验证码
        sendCode(isOpenSend, code, tel);

        String key = Constant.User.CODE_REDIS_PREFIX + tel;
        redisTemplate.opsForValue().set(key, code, Constant.User.CODE_EXPIRE_TIME, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(isSendKey, code, Constant.User.IS_SEND_EXPIRE_TIME, TimeUnit.SECONDS);
    }

    @Override
    public void changePassword(UserPasswordBO userPasswordBO) {
        Integer userId = LoginUserHolder.instance().get().getId();
        UserPO userPO = userDao.getById(userId);
        if (!userPO.getPassword().equals(EncodeUtils.encode(userPasswordBO.getOldPassword()))) {
            throw new RuntimeException(ExEnum.PASSWORD_CHANGE_ERROR.getMsg());
        }
        userPO.setPassword(EncodeUtils.encode(userPasswordBO.getNewPassword()));
        userDao.updatePassword(userPO);
    }

    @Override
    public void updateUser(UserVO userVO) {
        if (null == userVO.getId() || null == userDao.getById(userVO.getId())) {
            throw new UserException(ExEnum.INVALID_USER.getMsg());
        }
        UserBO userBO = UserConverter.INSTANCE.toUserBO(userVO);
        if (1!=userDao.update(userBO)) {
            throw new UserException(ExEnum.USER_UPDATE_ERROR.getMsg());
        }
    }

    @Override
    public UserVO getByVOId(Integer id) {
        UserPO userPO = userDao.getById(id);
        return UserConverter.INSTANCE.toUserVO(userPO);
    }

    @Override
    public PageVO<UserVO> getUserManagerList(UserListQO userListQO) {
        fillQueryParam(userListQO);
        PageHelper.startPage(userListQO.getPageNum(), userListQO.getPageSize());
        List<UserPO> userPOList = userDao.getUserListByQO(userListQO);
        PageInfo<UserPO> page = new PageInfo<>(userPOList);
        List<UserVO> userVOList = UserConverter.INSTANCE.toUserVOList(userPOList);
        Integer total = Integer.valueOf(String.valueOf(page.getTotal()));
        return new PageVO<>
                (page.getPageNum(), page.getPageSize(), total, userVOList);
    }

    @Override
    public void removeUser(Integer id) {
        if(1!=userDao.delete(id)){
            throw new UserException(ExEnum.User_REMOVE_ERROR.getMsg());
        }
    }

    @Override
    public Boolean isAdmin(Integer userId) {
        return userDao.admin(userId) == 1;
    }

    @Override
    public Map<String, Object> getUserManagerHeader() {
        Integer boysNumber = userDao.countBySex(UserSexEnum.MONKEY_BOY.code());
        Integer girlsNumber = userDao.countBySex(UserSexEnum.MONKEY_GIRL.code());
        Integer boysPercent = boysNumber*100 / (boysNumber + girlsNumber);
        Integer girlsPercent = 100 - boysPercent;
        Integer newUserWeek = userDao.countNewUserWeek();
        Integer newUserMonth = userDao.countNewUserMonth();
        List<NameValue> userTagList = userDao.getUserTagList();
        userTagList.forEach(user->{
            user.setValue(RandomUtils.code2());
        });
        Map<String, Object> result = new HashMap<>();
        result.put("boysPercent", boysPercent);
        result.put("girlsPercent", girlsPercent);
        result.put("newUserWeek", newUserWeek);
        result.put("newUserMonth", newUserMonth);
        result.put("userTagList", userTagList);
        return result;
    }

    //==============================  private help methods  ==============================

    private void sendCode(Boolean isOpenSend, String code, String tel) {
        if (isOpenSend) {
            SmsUtil.sendMsg(code, tel);
        }
    }

    private void validateUserBO(UserBO userBO, String code) {
        if (!checkRegisterUserBO(userBO, code)) {
            throw new ParamException("请输入完整信息！");
        }
        if (!codeCheck(userBO.getTel(), code)) {
            throw new ParamException("验证码有误！请重试！");
        }
        if (this.getByUserName(userBO.getUserName()) != null) {
            throw new UserException(ExEnum.REGISTER_USERNAME_EXIST.getMsg());
        }
        if (this.getByTel(userBO.getTel()) != null) {
            throw new UserException(ExEnum.REGISTER_TEL_EXIST.getMsg());
        }
    }


    private void fillUserBO(UserBO userBO) {
        userBO.setPassword(EncodeUtils.encode(userBO.getPassword()));
        userBO.setBirthday(new Date(0));
        userBO.setSex(0);
    }

    /**
     * 校验注册用户的字段和验证码
     *
     * @param userBO
     * @param code
     * @return
     */
    private boolean checkRegisterUserBO(UserBO userBO, String code) {
        return null != userBO && !StringUtils.isAnyEmpty(userBO.getUserName(), userBO.getPassword(), userBO.getTel(), code);
    }

    /**
     * 校验验证码
     *
     * @param tel  手机号
     * @param code 验证码
     * @return
     */
    private boolean codeCheck(String tel, String code) {
        String correctCode = redisTemplate.opsForValue().get(Constant.User.CODE_REDIS_PREFIX + tel);
        return null != correctCode && code.equals(correctCode);
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
