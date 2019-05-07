package top.fuyuaaa.shadowpuppets.model;

import lombok.Data;

/**
 * @author: fuyuaaa
 * @creat: 2019-03-28 23:35
 */
@Data
public class SMS {

    private String sid;
    private String token;
    private String appid;
    private String templateid;
    private String uid;
    private String param;
    private String mobile;

    public SMS(String param, String mobile) {
        this.param = param;
        this.mobile = mobile;
        this.sid = "164d62fe65788be09197f0fe0de0f1a5";
        this.token = "eb15c15165c9526768c8e70cece4fa9e";
        this.appid = "d2e0b47771f5409bbbf8c1e418fb47c7";
        //448969皮影戏 448959自带
        this.templateid = "448969";
    }
}
