package top.fuyuaaa.shadowpuppets.service;

import top.fuyuaaa.shadowpuppets.model.bo.UserBO;

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


}
