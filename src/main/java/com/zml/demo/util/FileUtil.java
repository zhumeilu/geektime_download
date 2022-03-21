package com.zml.demo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.util.regex.Pattern;

/**
 * @author: zhumlu@yonyou.com
 * @date: 2022/2/28 14:57
 * @description:
 */
public class FileUtil {
    public static Logger log = LoggerFactory.getLogger(FileUtil.class);
    private static Pattern FilePattern = Pattern.compile("[\\\\/:*?\"<> |]");

    public static void write(String content, String path, String fileName) {
        fileName = replace(fileName);
        String pathName = path + File.separator + fileName;
        try {
            File file = new File(pathName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            // 创建文件
            file.createNewFile();
            // creates a FileWriter Object
            FileWriter writer = new FileWriter(file);
            // 向文件写入内容
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            log.error("写入文件异常:", e);
        }

    }

    public static boolean isExist(String path, String fileName) {
        fileName = replace(fileName);
        String pathName = path + File.separator + fileName;
        try {
            File file = new File(pathName);
            if (file.exists()) {
                return true;
            }
        } catch (Exception e) {
            log.error("读取文件异常:", e);
        }
        return false;
    }

    public static String filter(String str) {
        return str == null ? null : FilePattern.matcher(str).replaceAll("");
    }

    public static String replace(String str) {
        return str.replace("|", "_").replace("｜", "").replace(" ", "").replace("/", "");
    }
}
