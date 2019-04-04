package top.fuyuaaa.shadowpuppets.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.fuyuaaa.shadowpuppets.dao.UserDao;
import top.fuyuaaa.shadowpuppets.model.bo.UserBO;
import top.fuyuaaa.shadowpuppets.model.dbo.UserDO;
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
        UserDO userDO = userDao.getByUserName(userName);
        if (null == userDO) {
            return null;
        }
        return BeanUtils.copyProperties(userDO, UserBO.class);
    }

    @Override
    public UserBO getByTel(String tel) {
        UserDO userDO = userDao.getByTel(tel);
        if (null == userDO) {
            return null;
        }
        return BeanUtils.copyProperties(userDO, UserBO.class);
    }

    @Override
    public Boolean addUser(UserBO userBO) {
        UserDO userDO = BeanUtils.copyProperties(userBO, UserDO.class);
        Integer res = userDao.insert(userDO);
        return res != null && res > 0;
    }
}
