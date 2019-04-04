package top.fuyuaaa.shadowpuppets.util;

/**
 * @author: fuyuaaa
 * @creat: 2019-03-28 23:59
 */
public class RandomUtils {

    /**
     * 随机生成6位数验证码
     * @return 6位随机数
     */
    public static String code6() {
        return String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
    }

}
