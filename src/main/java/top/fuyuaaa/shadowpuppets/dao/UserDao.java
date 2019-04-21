package top.fuyuaaa.shadowpuppets.dao;

import org.apache.ibatis.annotations.*;
import top.fuyuaaa.shadowpuppets.model.bo.UserBO;
import top.fuyuaaa.shadowpuppets.model.po.UserPO;
import top.fuyuaaa.shadowpuppets.model.qo.UserListQO;

import java.util.List;

/**
 * @author: fuyuaaa
 * @creat: 2019-03-27 21:50
 */
@Mapper
public interface UserDao {

    @Insert("insert into user (user_name, password, sex, birthday, tel, date_create, date_update) " +
            "values(#{userName}, #{password}, #{sex}, #{birthday} ,#{tel}, now(), now())")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    Integer insert(UserPO userPO);

    Integer update(UserBO userBO);

    @Delete("delete from user where id = #{id} limit 1")
    Integer delete(Integer id);

    @Select("select * from user where id = #{userId} limit 1")
    UserPO getById(Integer userId);

    @Select("select * from user where user_name = #{userName} limit 1")
    UserPO getByUserName(String userName);

    @Select("select * from user where tel = #{tel}")
    UserPO getByTel(String tel);

    @Insert("<script> INSERT INTO test\n" +
            "        (\n" +
            "            text\n" +
            "        )\n" +
            "        VALUES\n" +
            "        <foreach collection=\"list\"  item='item'  separator=\",\">\n" +
            "        (\n" +
            "           #{item}\n" +
            "        )\n" +
            "        </foreach></script>")
    void insertBatch(@Param(value = "list")List<String> list);

    List<UserPO> getUserListByQO(UserListQO userListQO);

    @Select("select admin from user where id = #{userId} limit 1")
    Integer admin(Integer userId);
}
