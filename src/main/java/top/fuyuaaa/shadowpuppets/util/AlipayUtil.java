package top.fuyuaaa.shadowpuppets.util;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import lombok.extern.slf4j.Slf4j;
import top.fuyuaaa.shadowpuppets.exceptions.AlipayException;
import top.fuyuaaa.shadowpuppets.model.bo.AlipayBO;
import top.fuyuaaa.shadowpuppets.model.vo.GoodsOrderVO;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-23 22:45
 */
@SuppressWarnings("all")
@Slf4j
public class AlipayUtil {
    private static final String SERVER_URL = "https://openapi.alipaydev.com/gateway.do";
    private static final String APP_ID = "2016093000635581";
    private static final String PRIVATE_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCGeH40wcaxg9Lx1Fq0F9aicaasJQDDhGg3bpjHlfbMik48dqsn6gR42RWytQjw052pzX7HIpiC2MvHx1crxeCvmlHjyP4Nr+tIpp5UL8oWvIuvxuJ20OYIPnSXA0EjaQNSKcjEiHLLndwxfO/g+mq0LpPo4cjcn/f1J8ecHvCWR5v29EWR+zAGA3ek0GsJ2pEqbTSCz1aP8XnXbPi5iE+2BBv5h4cD2W+PiqWtAGCf0GFvVPI9Qdb8zya0h57Ds6otZAlWuKrnmnAhu4UjOG1wCAlFnHprqmjg61hCcMGWw1+4K0GJjANzEeBzlukxbFaRN5n/EteNOvYoLiSMjHPzAgMBAAECggEAXGsfwj6TwpkpN4+NL01a9JZLyPA9kCLGRaK7WiDVWEqN7rxYcbxCipQYd5Q4qtul9ngHc/FxBCzgEVQNs0XyYH4dXgJbqSiI+ouZMZ8Os4WuM4brSmHUV0Ile0x/Tkj0QVn6ZCirTQoXteN6MhOplmCUw2+6TveawWEvrTsOexmDXXi8zXkIKkuH4+4zZii4+moQjwuUDpfzqPDDfRirUFhzo6oZVgMUEde4dHDKEm7Emn8GreM8LS6x/kI9e9WIZy3xMyPX1uvj6hsz8EjKqGEe6mbCIfsVgoNjupERxQzB2gnD9vOXQgiPUYv20lsYWeGRDBrS6tSVpA3H5xKisQKBgQDllrRKgVsIMkuWHYnFhxfM50N6+WVS5N1XvvuYot/lPmXlNTd6Gh7Ij1lKGbg1KZOG4T50Ct6ILDixxKDqjp2MZok6uFr3I417dE/l6q9diePkM35XEfNSlrmM/jINCAJolq/MxYZeygYw9T/xDk2YTuu5GyE2dsaSeeFnTP6pdwKBgQCV8Jnb/02QVEheNOLejJe8RrE3Q0FTMSeDRLFevJJ5lG9khN+/nZnP5+zSyCqRFrnaQ3+k2fOrqNIGgtINHvXJZ9Wn1sxPgMf/bkfiIYNCbkaRj85tsAH/5hcRhBQTliOyCV/p1+2w/u/fradpovSrVqTjthnObu2bLuCJEW8oZQKBgAzLLRjJxIzcYKHo23iKYqUkbfo4U5Ee98af+d0zlk5r/7maEZFYS0bZ1hvu+vjbbEEj5BsLUAM+k5o0XtMzR0w/mn4PL+J9tLCgdqNVRJhtyqk4xV2MvoZnp8dXkupu+9NgMkOy7h3yHA42XG3OYjyOiUwzWtJyI/adnBXpkHdZAoGASM6G94OV10KFzXDixOwB9gNhpBL9Unco153wfq8UdQL1wBqhRzsw5/qXTZayzLb7Uhe6kILgxnb4XP8DHintcXoBDjnIBcbj1o1nTE+3m/dOPvpUY4C2x9qjUvcOhKA//wB8WWFnYq24LqX1B99/7P7qnEPdbhcBO3xcvVIA5t0CgYAae7wstV04m0Fm3L81Y5/eL/NKo12cumDQRb3y9pD14a0B5bnaLXCCfR0qxUbvpy+gZfDEFeRRJqe+HU/PTiB/WD49D8eL8gLINTZhI3SBZOoUti/LQzw3Esbqgb7yUpT0a2d3mL8bOhe0shgBQvSyaSroa/uSdjXMt3j3Vw7wtQ==";
    private static final String FORMAT = "json";
    private static final String CHARSET = "utf-8";
    private static final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmYaiGW8WHxlpLzOQJSTHQgmR6UhOLSJkriJg2lWWl8kQSuNOE4I0Iis4kARuhwyFbBlpmlfEj0htMoSs3bZHFbaC6xIOr0gelWasw2FUn+sUyV+0cBZnjZWNtkimEp2QUPL7FubQl1jL2Y67NAirVy8bAw899XeFpsrYks+1xmdz8R2jXcZEmwSw9aq5fL8Dt6+qUlid5fYJs6fS/KyjcP3BgG/y7oe2BdJ27/bYp0UNAEUFP86/uLH+4v6JEuf4CZo33tkR7QmYBlDKaMwPvQl/KblqlicY7jrPMAPy83ARKf7HTYluCQb8yBxTD8dH2FN7j+I02sD3Z+xYe6A7ewIDAQAB";
    private static final String SIGN_TYPE = "RSA2";

    private static final String TRADE_SUCCESS = "TRADE_SUCCESS";

    public static String getAliPayUrl(GoodsOrderVO goodsOrderVO) {
        AlipayClient alipayClient = new DefaultAlipayClient(SERVER_URL, APP_ID, PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE); //获得初始化的AlipayClient
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();//创建API对应的request
        AlipayBO alipayBO = new AlipayBO();
        alipayBO.setOut_trade_no(String.valueOf(goodsOrderVO.getId()));
        alipayBO.setProduct_code("FAST_INSTANT_TRADE_PAY");
        alipayBO.setTotal_amount(goodsOrderVO.getDealPrice());
        String goodsName = goodsOrderVO.getGoodsVOList().get(0).getGoodsName();
        if (goodsOrderVO.getGoodsVOList().size() > 1) {
            goodsName += "...等";
        }
        alipayBO.setSubject(goodsName);
        goodsOrderVO.setGoodsVOList(null);
        alipayBO.setBody(JSON.toJSONString(goodsOrderVO));
        System.out.println(JSON.toJSONString(alipayBO));
        request.setBizContent(JSON.toJSONString(alipayBO));
        AlipayTradePagePayResponse response = null;
        try {
            response = alipayClient.pageExecute(request, "get");
        } catch (AlipayApiException e) {
            throw new AlipayException("调用支付宝接口出错");
        }
        String body = response.getBody();
        log.info("支付宝返回信息: body: {}", body);
        return body;
    }

    /**
     * 通过订单id去支付宝查询订单状态
     *
     * @param orderId 订单id
     * @return
     */
    public static Boolean checkTradeStatus(String orderId) {
        // TODO 这个client多出用到，考虑注入到spring中
        AlipayClient alipayClient = new DefaultAlipayClient(SERVER_URL, APP_ID, PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE); //获得初始化的AlipayClient
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizContent("{" +
                "\"out_trade_no\":\"" + orderId + "\"" +
                "  }");
        AlipayTradeQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
            return response.getTradeStatus().equals(TRADE_SUCCESS);
        } catch (Exception e) {
            log.error("调用支付宝查询状态失败，message: {}", e.getMessage());
        }
        return false;
    }

    public static void main(String[] args) {
//        getAliPayUrl(null, null);
        checkTradeStatus("24");
    }
}
