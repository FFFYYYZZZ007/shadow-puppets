package top.fuyuaaa.shadowpuppets.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: fuyuaaa
 * @creat: 2019-03-29 09:53
 */
public class TelNumberUtils {

    private final static Integer TEL_LENGTH = 11;

    private final static String REGEX = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";

    /**
     * @param tel 正则校验手机号格式
     * @return true/false
     */
    public static boolean isTel(String tel) {
        if (tel.length() != TEL_LENGTH) {
            return false;
        } else {
            Pattern p = Pattern.compile(REGEX);
            Matcher m = p.matcher(tel);
            return m.matches();
        }
    }

}
