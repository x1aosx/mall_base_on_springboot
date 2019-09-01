package com.lucky.mall.utils.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Description 文件操作
 * @Author shuxian.xiao
 * @Date 2019/8/12 10:05
 */
public class FileUtils {
    /**
     * 日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);
    /**
     * 保存图片
     * @param imageFile 图片
     * @param filePath 保存路径
     * @param fileName 文件名字
     * @return 文件存储路径
     */
    public final static String saveFile(MultipartFile imageFile,String filePath,String fileName){
        LOGGER.info("文件保存路径:{}",filePath);
        if (imageFile != null) {
            String[] fileTypes = imageFile.getOriginalFilename().split("\\.");
            String fileType = fileTypes[fileTypes.length - 1];
            LOGGER.debug("文件类型位：{}",fileType);
            try {
                //相对路径
                String savePath = filePath + fileName +"." + fileType;
                File file = new File(ResourceUtils.getURL("classpath:").getPath() + savePath );
//                File file = new File("/home/ubuntu/mall/" + savePath );
                LOGGER.debug("保存路径：{}",file.getAbsolutePath());
                FileOutputStream out = new FileOutputStream(file);
                out.write(imageFile.getBytes());
                out.close();
                LOGGER.info("上传的图片未:{}",file.getAbsolutePath());
                return savePath;
                //return file.getAbsolutePath();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "error";
    }
}