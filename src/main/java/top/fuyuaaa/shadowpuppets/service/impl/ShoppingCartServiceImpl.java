package top.fuyuaaa.shadowpuppets.service.impl;

import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.fuyuaaa.shadowpuppets.common.enums.ExEnum;
import top.fuyuaaa.shadowpuppets.dao.ShoppingCartDao;
import top.fuyuaaa.shadowpuppets.common.exceptions.ParamException;
import top.fuyuaaa.shadowpuppets.common.exceptions.ShoppingCartException;
import top.fuyuaaa.shadowpuppets.common.holders.LoginUserHolder;
import top.fuyuaaa.shadowpuppets.mapstruct.ShoppingCartConverter;
import top.fuyuaaa.shadowpuppets.model.bo.ShoppingCartBO;
import top.fuyuaaa.shadowpuppets.model.po.ShoppingCartPO;
import top.fuyuaaa.shadowpuppets.model.vo.ShoppingCartVO;
import top.fuyuaaa.shadowpuppets.service.GoodsService;
import top.fuyuaaa.shadowpuppets.service.ShoppingCartService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-11 22:18
 */
@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    ShoppingCartDao shoppingCartDao;

    @Autowired
    GoodsService goodsService;

    @Override
    public ShoppingCartBO getById(Integer id) {
        return null;
    }

    @Override
    public void addShoppingCart(ShoppingCartBO shoppingCartBO) {
        Integer userId = LoginUserHolder.instance().get().getId();
        validateShoppingCartParams(userId, shoppingCartBO);
        shoppingCartBO.setUserId(userId);

        ShoppingCartPO shoppingCartPO = ShoppingCartConverter.INSTANCE.toShoppingCartPO(shoppingCartBO);
        //购物车中已有该商品
        if (shoppingCartDao.getByUserIdAndGoodsId(shoppingCartPO) > 0) {
            throw new ShoppingCartException(ExEnum.SHOPPING_CART_EXIST_GOODS.getMsg());
        }
        Integer addResult = shoppingCartDao.insertShoppingCart(shoppingCartPO);
        if (addResult != 1) {
            throw new ShoppingCartException(ExEnum.SHOPPING_ADD_ERROR.getMsg());
        }
    }

    @Override
    public List<ShoppingCartBO> getShoppingCartList(Integer userId, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<ShoppingCartPO> shoppingCartPOList = shoppingCartDao.queryShoppingCartListByUserId(userId);
        if (CollectionUtils.isEmpty(shoppingCartPOList)) {
            return new ArrayList<>(0);
        }
        return ShoppingCartConverter.INSTANCE.toShoppingCartBOList(shoppingCartPOList);
    }

    @Override
    public List<ShoppingCartVO> getShoppingCartVOList(Integer userId, Integer page, Integer pageSize) {
        List<ShoppingCartBO> shoppingCartList = this.getShoppingCartList(userId, page, pageSize);
        return ShoppingCartConverter.INSTANCE.toShoppingCartVOList(shoppingCartList);
    }

    @Override
    public void deleteAllShoppingCart() {
        Integer userId = LoginUserHolder.instance().get().getId();
        shoppingCartDao.deleteAllShoppingCartByUserId(userId);
    }

    @Override
    public void deleteOneShoppingCart(Integer shoppingCartId) {
        Integer result = shoppingCartDao.deleteShoppingCart(shoppingCartId);
        if (result != 1) {
            throw new ShoppingCartException(ExEnum.SHOPPING_CART_IS_NOT_BELONGS.getMsg());
        }
    }

    //==============================  private help methods  ==============================

    /**
     * 校验参数
     *
     * @param userId         用户id
     * @param shoppingCartBO 商品
     */
    private void validateShoppingCartParams(Integer userId, ShoppingCartBO shoppingCartBO) {
        if (shoppingCartBO.getNum() == null || shoppingCartBO.getNum() < 1) {
            shoppingCartBO.setNum(1);
        }
        if (null == userId || null == shoppingCartBO.getGoodsId()) {
            throw new ParamException(ExEnum.PARAM_ERROR.getMsg());
        }
    }
}
