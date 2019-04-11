package top.fuyuaaa.shadowpuppets.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.fuyuaaa.shadowpuppets.dao.UserDao;
import top.fuyuaaa.shadowpuppets.model.bo.UserBO;
import top.fuyuaaa.shadowpuppets.model.po.UserPO;
import top.fuyuaaa.shadowpuppets.service.UserService;
import top.fuyuaaa.shadowpuppets.util.BeanUtils;

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
        Integer res = userDao.insert(userPO);
        return res != null && res > 0;
    }
}
