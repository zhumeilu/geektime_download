package com.zml.demo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zml.demo.constant.CommonConstant;
import com.zml.demo.util.FileUtil;
import com.zml.demo.util.Html2MDUtil;
import com.zml.demo.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author: zhumlu@yonyou.com
 * @date: 2022/2/28 15:04
 * @description:
 */
public class GeekTimeDownloader {
    public static Logger log = LoggerFactory.getLogger(GeekTimeDownloader.class);
    public static final String articleUrl = "https://time.geekbang.org/serv/v1/article";
    public static final String articlesUrl = "https://time.geekbang.org/serv/v1/column/articles";
    public static final String infoUrl = "https://time.geekbang.org/serv/v3/column/info";
    public static String cookies = CommonConstant.cookies;
    public static final String cid = "100006601";
    public static final String filePath = "D:\\github_projects\\geektime_download\\article";
    public static final Integer interval = 1000;

    public static void main(String[] args) {
        articles(cid);

    }


    public static void articles(String cid) {
        //获取专栏标题
        String info = info(Long.parseLong(cid));

        Map data = new HashMap();
        data.put("cid", cid);
        data.put("order", "earliest");
        data.put("prev", 0);
        data.put("sample", false);
        data.put("size", 500);
        String result = geekRequest(articlesUrl, data, cookies);
        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONObject dataJSON = jsonObject.getJSONObject("data");
        JSONArray list = dataJSON.getJSONArray("list");
        //文件目录
        String path = filePath + File.separator + info;
//        File file = new File(path+File.separator+getFileName("00  "+info));
        StringBuilder sb = new StringBuilder();
        sb.append("# ").append(info).append("\n");
        //制作目录
        list.forEach(obj -> {
            JSONObject jsonObject1 = (JSONObject) obj;
            String id = jsonObject1.getString("id");
            String article_title = jsonObject1.getString("article_title");
            String fileName = getFileName(article_title);
            sb.append("* ").append("[").append(fileName).append("]").append("(").append("./").append(fileName).append(")").append("\n");
        });
        String head = getFileName("00_" + info);
        FileUtil.write(sb.toString(), path, head);
        log.info("保存:{}成功!", head);

        //循环生成文章
        list.forEach(obj -> {
            JSONObject jsonObject1 = (JSONObject) obj;
            String id = jsonObject1.getString("id");
            String article_title = jsonObject1.getString("article_title");

            String audio_download_url = jsonObject1.getString("audio_download_url");
            String audio_title = jsonObject1.getString("audio_title");
            String article = article(id);
            //文件名
            String fileName = getFileName(article_title);
//            com.zml.demo.utils.FileUtils.write(article,path,fileName);
            String fileNamePath = path + File.separator + fileName;
            Html2MDUtil.convert(article, fileNamePath, true);
            log.info("保存:{}成功!", fileName);
        });
    }

    public static String getFileName(String title) {
        //去除关键字
        String fileName = FileUtil.replace(title) + ".md";
        return fileName;
    }

    public static String article(String id) {
        Map data = new HashMap();
        data.put("id", id);
        data.put("include_neighbors", true);
        data.put("is_freelyread", true);
        String result = geekRequest(articleUrl, data, cookies);
        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONObject dataJSON = jsonObject.getJSONObject("data");
        String article_content = dataJSON.getString("article_content");
        return article_content;
    }

    public static String info(Long id) {
        Map data = new HashMap();
        data.put("product_id", id);
        data.put("with_recommend_article", true);
        String result = geekRequest(infoUrl, data, cookies);
        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONObject dataJSON = jsonObject.getJSONObject("data");
        String title = dataJSON.getString("title");
        log.info("获取专栏名称:{}", title);
        return title;
    }

    public static String geekRequest(String url, Map data, String cookies) {
        Map header = new HashMap<>();
        header.put("Cookie", cookies);
        header.put("Origin", "https://time.geekbang.org");
        header.put("Host", "time.geekbang.org");
        header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36");
        header.put("Content-Type", "text/plain");
        header.put("Origin", "https://time.geekbang.org");
        String result = HttpUtil.postJson(url, header, data);
        log.info(result);
        sleep();
        return result;
    }

    public static void sleep() {
        try {
            Random random = new Random();
            //间隔时间1-2s
            Thread.sleep(random.nextInt(1000) + interval);
        } catch (InterruptedException e) {
            log.error("异常:", e);
        }
    }
}
