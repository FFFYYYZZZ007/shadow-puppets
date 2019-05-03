package top.fuyuaaa.shadowpuppets.service;

import top.fuyuaaa.shadowpuppets.model.PageVO;
import top.fuyuaaa.shadowpuppets.model.bo.UserBO;
import top.fuyuaaa.shadowpuppets.model.bo.UserPasswordBO;
import top.fuyuaaa.shadowpuppets.model.qo.UserListQO;
import top.fuyuaaa.shadowpuppets.model.vo.UserVO;

/**
 * @author: fuyuaaa
 * @creat: 2019-03-28 22:41
 */
public interface UserService {

    /**
     * 通过用户名查询User
     * @param userName 用户名
     * @return UserPO.class
     */
    UserBO getByUserName(String userName);

    /**
     * 通过手机号查询User
     * @param tel 手机号
     * @return UserPO.class
     */
    UserBO getByTel(String tel);

    /**
     * 添加用户
     * @param userBO 用户
     * @return 添加结果
     */
    Boolean addUser(UserBO userBO);

    void changePassword(UserPasswordBO userPasswordBO);

    UserBO getById(Integer id);

    Boolean updateUser(UserBO userBO);

    UserVO getByVOId(Integer id);

    PageVO<UserVO> getUserManagerList(UserListQO userListQO);

    Boolean removeUser(Integer id);

    Boolean isAdmin(Integer userId);
}
