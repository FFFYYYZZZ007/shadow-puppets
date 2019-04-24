package top.fuyuaaa.shadowpuppets.util;

import org.springframework.web.multipart.MultipartFile;
import top.fuyuaaa.shadowpuppets.common.enums.ExEnum;
import top.fuyuaaa.shadowpuppets.exceptions.UploadException;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-20 19:09
 */
public class FileUtils {
    public static File convertMultipartFile2File(MultipartFile multfile) {
        // 获取文件名
        String fileName = multfile.getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // 用uuid作为文件名，防止生成的临时文件重复
        File file = null;
        try {
            file = File.createTempFile(UUID.randomUUID().toString(), prefix);
            multfile.transferTo(file);
        } catch (IOException e) {
            throw new UploadException(ExEnum.UPLOAD_ERROR.getMsg());
        }
        return  file;
    }

    /**
     * 删除临时文件
     *
     * @param files 文件
     */
    public static void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
