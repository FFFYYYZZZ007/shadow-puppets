package top.fuyuaaa.shadowpuppets.service.impl;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.fuyuaaa.shadowpuppets.dao.GoodsDao;
import top.fuyuaaa.shadowpuppets.model.bo.GoodsBO;
import top.fuyuaaa.shadowpuppets.model.po.GoodsPO;
import top.fuyuaaa.shadowpuppets.service.GoodsService;
import top.fuyuaaa.shadowpuppets.util.BeanUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-08 22:25
 */
@Service("goodsService")
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    GoodsDao goodsDao;

    @Override
    public GoodsBO getGoodsDetailsById(Integer id) {
        GoodsPO goodsPO = goodsDao.findByGoodsId(id);
        if(null == goodsPO) {
            return null;
        }
        return BeanUtils.copyProperties(goodsPO, GoodsBO.class);
    }

    @Override
    public List<GoodsBO> getList() {
        List<GoodsPO> goodsPOList = goodsDao.findList();
        if (null == goodsPOList) {
            return Lists.newArrayList();
        }
        return goodsPOList.stream()
                .map(goodsPO -> BeanUtils.copyProperties(goodsPO, GoodsBO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<GoodsBO> getGoodsListByParam(Map<String, Object> param) {
        return null;
    }

    @Override
    public Boolean addManagerGoods(GoodsBO goodsBO) {
        return null;
    }

    @Override
    public Boolean updateManagerGoods(GoodsBO goodsBO) {
        return null;
    }

    @Override
    public Boolean removeManagerGoods(Integer goodsId) {
        return null;
    }
}
