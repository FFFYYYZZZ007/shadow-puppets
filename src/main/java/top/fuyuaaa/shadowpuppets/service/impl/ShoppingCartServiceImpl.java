package top.fuyuaaa.shadowpuppets.service.impl;

import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.fuyuaaa.shadowpuppets.dao.ShoppingCartDao;
import top.fuyuaaa.shadowpuppets.holder.LoginUserHolder;
import top.fuyuaaa.shadowpuppets.model.bo.ShoppingCartBO;
import top.fuyuaaa.shadowpuppets.model.po.ShoppingCartPO;
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
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    ShoppingCartDao shoppingCartDao;

    @Override
    public Integer addShoppingCart(ShoppingCartBO shoppingCartBO) {
        return null;
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
    public Boolean deleteShoppingCartList(List<Integer> ids) {
        return null;
    }

    @Override
    public Boolean isOwner(List<Integer> shoppingCartIdList) {
        Integer userId = LoginUserHolder.instance().get().getId();
        List<ShoppingCartPO> shoppingCartPOList =
                shoppingCartDao.queryShoppingCartListByUserId(userId);
        Map<Integer, ShoppingCartPO> map =
                shoppingCartPOList.stream().collect(Collectors.toMap(ShoppingCartPO::getId, Function.identity()));
        shoppingCartIdList = shoppingCartIdList.stream().filter(shoppingCartId -> !map.containsKey(shoppingCartId)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(shoppingCartIdList)) {
            return true;
        }
        return false;
    }
}
