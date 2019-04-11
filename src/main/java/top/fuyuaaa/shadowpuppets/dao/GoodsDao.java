package top.fuyuaaa.shadowpuppets.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.fuyuaaa.shadowpuppets.model.po.GoodsPO;

import java.util.List;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-08 22:19
 */
@Mapper
public interface GoodsDao {

    @Select("select * from goods where id = #{id} limit 1")
    GoodsPO findByGoodsId(Integer id);

    @Select("select * from goods where date_delete is null order by date_update")
    List<GoodsPO> findList();

}
