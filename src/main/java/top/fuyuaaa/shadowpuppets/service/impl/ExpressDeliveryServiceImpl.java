package top.fuyuaaa.shadowpuppets.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.fuyuaaa.shadowpuppets.common.enums.ExpressDeliveryStatusEnum;
import top.fuyuaaa.shadowpuppets.dao.ExpressDeliveryDao;
import top.fuyuaaa.shadowpuppets.model.PageVO;
import top.fuyuaaa.shadowpuppets.model.po.ExpressDeliveryPO;
import top.fuyuaaa.shadowpuppets.model.qo.ExpressDeliveryQO;
import top.fuyuaaa.shadowpuppets.model.vo.ExpressDeliveryVO;
import top.fuyuaaa.shadowpuppets.service.ExpressDeliveryService;
import top.fuyuaaa.shadowpuppets.util.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-26 17:15
 */
@Service
public class ExpressDeliveryServiceImpl implements ExpressDeliveryService {

    @Autowired
    ExpressDeliveryDao expressDeliveryDao;

    @Override
    public Boolean updateExpressDeliveryStatus(String orderId, ExpressDeliveryStatusEnum expressDeliveryStatusEnum) {
        return expressDeliveryDao.updateDeliveryStatus(orderId, expressDeliveryStatusEnum.code()) == 1;
    }

    @Override
    @SuppressWarnings("all")
    public PageVO<ExpressDeliveryVO> getList(ExpressDeliveryQO expressDeliveryQO) {
        PageHelper.startPage(expressDeliveryQO.getPageNum(), expressDeliveryQO.getPageSize());
        List<ExpressDeliveryPO> expressDeliveryPOList = expressDeliveryDao.findList(expressDeliveryQO);
        PageInfo<ExpressDeliveryPO> pageInfo = new PageInfo<>(expressDeliveryPOList);
        // 这样的代码可忒丑了，可能有空就引入mapStruct
        List<ExpressDeliveryVO> expressDeliveryVOList = expressDeliveryPOList.stream().map(expressDeliveryPO -> {
            ExpressDeliveryVO expressDeliveryVO = BeanUtils.copyProperties(expressDeliveryPO, ExpressDeliveryVO.class);
            if (expressDeliveryPO.getDeliveryStatus() != null) {
                expressDeliveryVO.setDeliveryStatus(expressDeliveryPO.getDeliveryStatus().desc());
            }
            if (expressDeliveryPO.getExpressCarrier() != null) {
                expressDeliveryVO.setExpressCarrier(expressDeliveryPO.getExpressCarrier().desc());
            }
            expressDeliveryVO.setDateCreate(DateFormatUtils.format(expressDeliveryPO.getDateCreate(), "yyyy-MM-DD HH:mm:ss"));
            expressDeliveryVO.setDateUpdate(DateFormatUtils.format(expressDeliveryPO.getDateUpdate(), "yyyy-MM-DD HH:mm:ss"));
            return expressDeliveryVO;
        }).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(expressDeliveryPOList)) {
            expressDeliveryVOList = new ArrayList<>(0);
        }
        Integer total = Integer.valueOf(String.valueOf(pageInfo.getTotal()));
        PageVO<ExpressDeliveryVO> pageVO = new PageVO<>(pageInfo.getPageNum(), pageInfo.getPageSize(), total, expressDeliveryVOList);
        return pageVO;
    }
}
