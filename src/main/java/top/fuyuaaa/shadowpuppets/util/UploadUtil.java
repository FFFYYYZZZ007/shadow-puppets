package top.fuyuaaa.shadowpuppets.util;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import top.fuyuaaa.shadowpuppets.common.enums.ExEnum;
import top.fuyuaaa.shadowpuppets.exceptions.UploadException;

import java.io.File;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-20 18:36
 */
public class UploadUtil {

    /**
     * 这里的配置应该写在yml里的 (懒的搞 -_- )
     */
    private final  static String END_POINT = "http://oss-cn-hangzhou.aliyuncs.com";
    private final  static String ACCESS_KEY_ID = "LTAIUl9E7ProEzcl";
    private final  static String ACCESS_KEY_SECRET = "i1OsmJzhPSOUQm1PAvBddOPu6QFRi1";
    /**
     * 上传商品详情图
     *
     * @param goodsId 商品id
     * @param file    图片
     */
    public static String upload2OSSWithGoodsId(Integer goodsId, File file) {
        try {
            OSSClient ossClient = new OSSClient(END_POINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
            ossClient.putObject("fuyuaaa-bucket", "pics/goods/" + goodsId + "/" + file.getName(), file);
            ossClient.shutdown();
        } catch (OSSException | ClientException e) {
            throw new UploadException(ExEnum.UPLOAD_ERROR.getMsg());
        }
        String url = "https://fuyuaaa-bucket.oss-cn-hangzhou.aliyuncs.com/pics/goods/" + goodsId + "/" + file.getName();
        return url;
    }

    public static String uploadGoodsMain2OSS(File file) {
        try {
            OSSClient ossClient = new OSSClient(END_POINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
            ossClient.putObject("fuyuaaa-bucket", "pics/goods/" + file.getName(), file);
            ossClient.shutdown();
        } catch (OSSException | ClientException e) {
            throw new UploadException(ExEnum.UPLOAD_ERROR.getMsg());
        }
        String url = "https://fuyuaaa-bucket.oss-cn-hangzhou.aliyuncs.com/pics/goods/" + file.getName();
        return url;
    }
}
