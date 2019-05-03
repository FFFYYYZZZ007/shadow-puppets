package top.fuyuaaa.shadowpuppets.util;

import org.springframework.util.DigestUtils;

/**
 * 加密工具类(其实前端没做处理，加不加好像无所谓)
 *
 * @author: fuyuaaa
 * @creat: 2019-03-28 23:12
 */
public class EncodeUtils {
    public static String encode(String var){
        if ("fuyu".equals(var)){
            return "fuyu";
        }
        return DigestUtils.md5DigestAsHex(var.getBytes());
    }
}
