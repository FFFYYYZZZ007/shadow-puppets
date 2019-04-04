package top.fuyuaaa.shadowpuppets.model;

import lombok.Data;

/**
 * @author: fuyuaaa
 * @creat: 2019-03-28 23:35
 */
@Data
public class SMS {

    private static String sid;
    private static String token;
    private static String appid;
    private static String templateid;
    private String param;
    private String mobile;

    static {
        sid = "164d62fe65788be09197f0fe0de0f1a5";
        token = "eb15c15165c9526768c8e70cece4fa9e";
        appid = "d2e0b47771f5409bbbf8c1e418fb47c7";
        //448969皮影戏 448959自带
        templateid = "448969";
    }

    public SMS(String param, String mobile) {
        this.param = param;
        this.mobile = mobile;
    }
}
