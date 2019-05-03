package top.fuyuaaa.shadowpuppets.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.fuyuaaa.shadowpuppets.common.enums.ExEnum;
import top.fuyuaaa.shadowpuppets.dao.UserDao;
import top.fuyuaaa.shadowpuppets.exceptions.ParamException;
import top.fuyuaaa.shadowpuppets.holder.LoginUserHolder;
import top.fuyuaaa.shadowpuppets.model.PageVO;
import top.fuyuaaa.shadowpuppets.model.bo.UserBO;
import top.fuyuaaa.shadowpuppets.model.bo.UserPasswordBO;
import top.fuyuaaa.shadowpuppets.model.po.UserPO;
import top.fuyuaaa.shadowpuppets.model.qo.UserListQO;
import top.fuyuaaa.shadowpuppets.model.vo.UserVO;
import top.fuyuaaa.shadowpuppets.service.UserService;
import top.fuyuaaa.shadowpuppets.util.BeanUtils;
import top.fuyuaaa.shadowpuppets.util.EncodeUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: fuyuaaa
 * @creat: 2019-03-28 23:18
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public UserBO getByUserName(String userName) {
        UserPO userPO = userDao.getByUserName(userName);
        if (null == userPO) {
            return null;
        }
        return BeanUtils.copyProperties(userPO, UserBO.class);
    }

    @Override
    public UserBO getByTel(String tel) {
        UserPO userPO = userDao.getByTel(tel);
        if (null == userPO) {
            return null;
        }
        return BeanUtils.copyProperties(userPO, UserBO.class);
    }

    @Override
    public Boolean addUser(UserBO userBO) {
        UserPO userPO = BeanUtils.copyProperties(userBO, UserPO.class);
        userPO.setBirthday(new Date(0));
        userPO.setSex(0);
        Integer res = userDao.insert(userPO);
        return res != null && res > 0;
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
    public Boolean updateUser(UserBO userBO) {
        if (userBO.getPassword() != null) {
            userBO.setPassword(EncodeUtils.encode(userBO.getPassword()));
        }
        return userDao.update(userBO) > 0;
    }

    @Override
    public UserBO getById(Integer id) {
        UserPO userPO = userDao.getById(id);
        UserBO userBO = BeanUtils.copyProperties(userPO, UserBO.class);
        return userBO;
    }

    @Override
    public UserVO getByVOId(Integer id) {
        UserPO userPO = userDao.getById(id);
        return convertUserPO2VO(userPO);
    }

    @Override
    public PageVO<UserVO> getUserManagerList(UserListQO userListQO) {
        PageHelper.startPage(userListQO.getPageNum(), userListQO.getPageSize());
        List<UserPO> userPOList = userDao.getUserListByQO(userListQO);
        PageInfo<UserPO> page = new PageInfo<>(userPOList);
        List<UserVO> userVOList = userPOList.stream().map(this::convertUserPO2VO).collect(Collectors.toList());
        Integer total = Integer.valueOf(String.valueOf(page.getTotal()));
        return new PageVO<>
                (page.getPageNum(), page.getPageSize(), total, userVOList);
    }

    @Override
    public Boolean removeUser(Integer id) {
        return userDao.delete(id) > 0;
    }

    private UserVO convertUserPO2VO(UserPO userPO) {
        if (null == userPO) {
            return null;
        }
        UserVO userVO = BeanUtils.copyProperties(userPO, UserVO.class);
        userVO.setSex(userPO.getBirthday() != null ? (1 == userPO.getSex() ? "男" : "女") : "");
        userVO.setBirthday(userPO.getBirthday() != null ? DateFormatUtils.format(userPO.getBirthday(), "yyyy-MM-dd") : "暂无");
        return userVO;
    }

    @Override
    public Boolean isAdmin(Integer userId) {
        return userDao.admin(userId) == 1;
    }
}
