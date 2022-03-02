package com.zml.demo.util;

import com.overzealous.remark.Options;
import com.overzealous.remark.convert.DocumentConverter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author: zhumlu@yonyou.com
 * @date: 2022/2/28 19:25
 * @description:
 */
public class Html2MDUtil {
    public static Logger log  = LoggerFactory.getLogger(Html2MDUtil.class);

    /**
     *
     * @param content
     * @param outputFile
     * @param cover 是否覆盖文件
     */
    public static void convert(String content,String outputFile,boolean cover){

        try{
            DocumentConverter converter = new DocumentConverter(Options.markdown());
//            Document doc = Jsoup.parse(new File("D:\\github_projects\\geektime_download\\output\\test.html"), "utf-8");
            Document doc = Jsoup.parse(content);

            File file = new File(outputFile);
            if(file.exists()&&!cover){
                log.info("文件{}已存在,跳过...",outputFile);
                return;
            }
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
            // creates a FileWriter Object
            FileWriter writer = new FileWriter(file);
            converter.convert(doc,writer);

//        writer.write(article_content);
            writer.flush();
            writer.close();
        }catch (Exception e){
            log.error("html转换markdown异常",e);
        }

    }
    public static void main(String[] args) throws IOException {

        DocumentConverter converter = new DocumentConverter(Options.markdown());
        Document doc = Jsoup.parse(new File("D:\\github_projects\\geektime_download\\output\\test.html"), "utf-8");
        File file = new File("output/test.md");
        file.createNewFile();
        // creates a FileWriter Object
        FileWriter writer = new FileWriter(file);
        converter.convert(doc,writer);

//        writer.write(article_content);
        writer.flush();
        writer.close();

    }
}
