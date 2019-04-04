package top.fuyuaaa.shadowpuppets.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import top.fuyuaaa.shadowpuppets.model.dbo.UserDO;

/**
 * @author: fuyuaaa
 * @creat: 2019-03-27 21:50
 */
@Mapper
public interface UserDao {

    @Insert("insert into user (user_name, password, sex, birthday, tel, date_create) " +
            "values(#{userName}, #{password}, #{sex}, #{birthday} ,#{tel}, now())")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    Integer insert(UserDO userDO);

    @Select("select * from user where id = #{userId} limit 1")
    UserDO getById(Integer userId);

    @Select("select * from user where user_name = #{userName} limit 1")
    UserDO getByUserName(String userName);

    @Select("select * from user where tel = #{tel}")
    UserDO getByTel(String tel);
}
