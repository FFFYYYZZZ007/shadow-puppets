package top.fuyuaaa.shadowpuppets.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import top.fuyuaaa.shadowpuppets.model.SMS;


/**
 * @author: fuyuaaa
 * @creat: 2019-04-18 11:43
 */
@Slf4j
public class SmsUtil {
    /**
     * 短信验证码调用API地址
     */
    private final static String SMS_URL = "https://open.ucpaas.com/ol/sms/sendsms";

    public static void sendMsg(String code, String tel) {
        SMS sms = new SMS(code, tel);
        String result = HttpUtils.post(SMS_URL, JSON.toJSONString(sms));
        if (StringUtils.isNotEmpty(result)) {
            JSONObject json = null;
            try {
                json = JSONObject.parseObject(result);
            } catch (Exception e) {
                log.error("发送验证法失败:{}", e.getMessage());
            }
            boolean success = null != json && "OK".equals(json.get("msg"));
            log.info("发送验证码{}, tel: {}", success ? "成功" : "失败", tel);
        }
    }
}
