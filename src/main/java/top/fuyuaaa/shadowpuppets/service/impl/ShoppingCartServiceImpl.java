package top.fuyuaaa.shadowpuppets.service.impl;

import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.fuyuaaa.shadowpuppets.common.enums.ExEnum;
import top.fuyuaaa.shadowpuppets.dao.ShoppingCartDao;
import top.fuyuaaa.shadowpuppets.exceptions.ShoppingCartException;
import top.fuyuaaa.shadowpuppets.holder.LoginUserHolder;
import top.fuyuaaa.shadowpuppets.model.bo.GoodsBO;
import top.fuyuaaa.shadowpuppets.model.bo.ShoppingCartBO;
import top.fuyuaaa.shadowpuppets.model.po.ShoppingCartPO;
import top.fuyuaaa.shadowpuppets.model.vo.ShoppingCartVO;
import top.fuyuaaa.shadowpuppets.service.GoodsService;
import top.fuyuaaa.shadowpuppets.service.ShoppingCartService;
import top.fuyuaaa.shadowpuppets.util.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-11 22:18
 */
@Service("shoppingCartService")
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
    public Integer addShoppingCart(ShoppingCartBO shoppingCartBO) {
        ShoppingCartPO shoppingCartPO = BeanUtils.copyProperties(shoppingCartBO, ShoppingCartPO.class);
        //购物车中已有该商品
        if (shoppingCartDao.getByUserIdAndGoodsId(shoppingCartPO) > 0) {
            throw new ShoppingCartException(ExEnum.SHOPPING_CART_EXIST_GOODS.getMsg());
        }
        return shoppingCartDao.insertShoppingCart(shoppingCartPO);
    }

    @Override
    public List<ShoppingCartBO> getShoppingCartList(Integer userId, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<ShoppingCartPO> shoppingCartPOList = shoppingCartDao.queryShoppingCartListByUserId(userId);
        if (CollectionUtils.isEmpty(shoppingCartPOList)) {
            return new ArrayList<>(0);
        }
        return shoppingCartPOList.stream()
                .map(shoppingCartPO -> BeanUtils.copyProperties(shoppingCartPO, ShoppingCartBO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ShoppingCartVO> getShoppingCartVOList(Integer userId, Integer page, Integer pageSize) {
        List<ShoppingCartBO> shoppingCartList = this.getShoppingCartList(userId, page, pageSize);
        return convertShoppingCartBO2VO(shoppingCartList);
    }

    /**
     * 将BO转成VO
     *
     * @param shoppingCartBOList 购物车列表
     * @return
     */
    private List<ShoppingCartVO> convertShoppingCartBO2VO(List<ShoppingCartBO> shoppingCartBOList) {
        return shoppingCartBOList.stream()
                .map(shoppingCartBO -> {
                    ShoppingCartVO shoppingCartVO = BeanUtils.copyProperties(shoppingCartBO, ShoppingCartVO.class);
                    GoodsBO goodsBO = goodsService.getGoodsDetailsById(shoppingCartBO.getGoodsId());
                    shoppingCartVO.setKey(shoppingCartBO.getId());
                    shoppingCartVO.setGoodsName(goodsBO.getGoodsName());
                    shoppingCartVO.setPrice(goodsBO.getPrice());
                    return shoppingCartVO;
                }).collect(Collectors.toList());
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
}
