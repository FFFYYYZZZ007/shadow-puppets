package top.fuyuaaa.shadowpuppets.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.fuyuaaa.shadowpuppets.annotation.ValidateAdmin;
import top.fuyuaaa.shadowpuppets.common.Result;
import top.fuyuaaa.shadowpuppets.dao.DateAnalysisDao;
import top.fuyuaaa.shadowpuppets.model.analysis.XYData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-27 23:42
 */
@RestController
@RequestMapping("/analysis")
@Slf4j
@SuppressWarnings("all")
public class DateAnalysisController {

    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    DateAnalysisDao dateAnalysisDao;

    @GetMapping("/user")
    @ValidateAdmin
    @Cacheable(value = "userDateAnalysis", keyGenerator = "simpleKeyGenerator")
    public Result<Map<String, Object>> userDateAnalysis() {
        int countUser = dateAnalysisDao.countUser();
        //key 最好别用, 我滴乖乖
        int countOnlineWeek = Objects.requireNonNull(redisTemplate.keys("token:*")).size();
        int onlinePercent = countOnlineWeek * 100 / countUser;
        Map<String, Object> result = new HashMap<>(2);
        result.put("countUser", countUser);
        result.put("countOnlineWeek", countOnlineWeek);
        result.put("onlinePercent", onlinePercent);
        return Result.success(result);
    }

    @GetMapping("/order")
    @ValidateAdmin
    @Cacheable(value = "orderDateAnalysis", keyGenerator = "simpleKeyGenerator")
    public Result<Map<String, Object>> orderDateAnalysis() {
        int countGoodsOrder = dateAnalysisDao.countGoodsOrder();
        int countOrderCreateMonth = dateAnalysisDao.countOrderCreateMonth();
        List<XYData> xyDataList = dateAnalysisDao.orderDataMonth();
        Map<String, Object> result = new HashMap<>(2);
        result.put("countGoodsOrder", countGoodsOrder);
        result.put("countOrderCreateMonth", countOrderCreateMonth);
        result.put("xyDataList", xyDataList);
        return Result.success(result);
    }

    @GetMapping("/price")
    @ValidateAdmin
    @Cacheable(value = "priceDateAnalysis", keyGenerator = "simpleKeyGenerator")
    public Result<Map<String, Object>> priceDateAnalysis() {
        Double totalSellPrice = dateAnalysisDao.sumAllOrderDealPrice();
        Double monthSellPrice = dateAnalysisDao.sumMonthOrderDealPrice();
        List<XYData> xyDataList = dateAnalysisDao.sumDayOrderDealPrice();
        Map<String, Object> result = new HashMap<>(2);
        result.put("totalSellPrice", totalSellPrice);
        result.put("monthSellPrice", monthSellPrice);
        result.put("xyDataList", xyDataList);
        return Result.success(result);
    }

    @GetMapping("/goods/category")
    @ValidateAdmin
    @Cacheable(value = "goodsCategoryAnalysis", keyGenerator = "simpleKeyGenerator")
    public Result<Map<String, Object>> goodsCategoryAnalysis() {
        List<XYData> xyDataList = dateAnalysisDao.countGoodsGroupByCategory();
        Map<String, Object> result = new HashMap<>(2);
        result.put("xyDataList", xyDataList);
        return Result.success(result);
    }
}
