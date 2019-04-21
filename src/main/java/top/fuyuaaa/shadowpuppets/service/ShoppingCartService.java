package top.fuyuaaa.shadowpuppets.service;

import top.fuyuaaa.shadowpuppets.model.bo.ShoppingCartBO;
import top.fuyuaaa.shadowpuppets.model.vo.ShoppingCartVO;

import java.util.List;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-11 00:20
 */
public interface ShoppingCartService {

    /**
     * 查询购物车
     * @param id 购物车id
     * @return shoppingCartBO
     */
    ShoppingCartBO getById(Integer id);

    /**
     * 新建一条购物车记录
     * @param shoppingCartBO 购物车业务类
     * @return 插入结果
     */
    Integer addShoppingCart(ShoppingCartBO shoppingCartBO);

    /**
     * 查询购物车列表
     * @param userId 用户id
     * @param page 页
     * @param pageSize 页数量
     * @return 购物车列表
     */
    List<ShoppingCartBO> getShoppingCartList(Integer userId, Integer page, Integer pageSize);

    List<ShoppingCartVO> getShoppingCartVOList(Integer userId, Integer page, Integer pageSize);

    /**
     * 根据列表删除购物车(软删除)
     * @param ids 要删除的id列表
     * @return isSuccess
     */
    Boolean deleteShoppingCartList(List<Integer> ids);

    Boolean isOwner(List<Integer> shoppingCartIdList);
}
