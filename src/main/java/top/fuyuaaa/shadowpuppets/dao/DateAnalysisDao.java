package top.fuyuaaa.shadowpuppets.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.fuyuaaa.shadowpuppets.model.analysis.XYData;

import java.util.List;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-27 23:45
 */
@Mapper
public interface DateAnalysisDao {
    @Select("select count(*) from user")
    Integer countUser();

    @Select("select count(*) from goods_order")
    Integer countGoodsOrder();

    @Select("select count(*) from goods_order where date_create between current_date() - 30  and  current_date()\n")
    Integer countOrderCreateMonth();

    /**
     * 0内每天的订单数量
     *
     * @return 30内每天的订单数量
     */
    List<XYData> orderDataMonth();

    @Select("select round(sum(deal_price)) from goods_order")
    Double sumAllOrderDealPrice();

    @Select("select round(sum(deal_price)) from goods_order where date_create between current_date() - 30  and  current_date()")
    Double sumMonthOrderDealPrice();

    /**
     * 0内每天的订单金额
     *
     * @return 0内每天的订单金额
     */
    List<XYData> sumDayOrderDealPrice();

    List<XYData> countGoodsGroupByCategory();
}
