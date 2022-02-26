import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhumlu@yonyou.com
 * @date: 2022/2/26 15:36
 * @description:
 */
public class GetHtml3 {
    public static Logger log  = LoggerFactory.getLogger(GetHtml3.class);

    public static final String cookies = "LF_ID=1642990619629-1904085-5943900; _ga=GA1.2.269747210.1642990620; gksskpitn=f14f8845-e2a2-484b-a926-21ef8fac8954; _gid=GA1.2.914330557.1645703940; GCID=96c355b-c2fec80-b288f6f-4a59645; GRID=96c355b-c2fec80-b288f6f-4a59645; gk_exp_uid=NTViODliMzFlNzdhZGFhYTAwNzc1YzNlNmQ5MTYxN2U=|1645705563028260415|1e7256a5a06515364e48d74b40ebd4105bdccd21f9f51be212afcf4b4f3a9862; GCESS=BgkBAQsCBgAMAQECBMrGGWIBCLDKEAAAAAAABQQAAAAABAQALw0ACAEDAwTKxhliBwRTO3n1BgQ10bA8DQEBCgQAAAAA; Hm_lvt_59c4ff31a9ee6263811b23eb921a5083=1645153217,1645174157,1645703948,1645856459; Hm_lvt_022f847c4e3acd44d4a2481d9187f1e6=1645153217,1645174157,1645703948,1645856459; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%221100464%22%2C%22first_id%22%3A%2217e89df4c1a68a-0559793e0591be-f791b31-2359296-17e89df4c1b1231%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%2C%22%24latest_landing_page%22%3A%22https%3A%2F%2Ftime.geekbang.org%2Fsearch%3Fq%3D%25E4%25BB%258E0%25E5%25BC%2580%25E5%25A7%258B%25E5%25AD%25A6%25E6%259E%25B6%25E6%259E%2584%22%7D%2C%22%24device_id%22%3A%2217e89df4c1a68a-0559793e0591be-f791b31-2359296-17e89df4c1b1231%22%7D; SERVERID=3431a294a18c59fc8f5805662e2bd51e|1645856951|1645855985; _gat=1; Hm_lpvt_59c4ff31a9ee6263811b23eb921a5083=1645856951; Hm_lpvt_022f847c4e3acd44d4a2481d9187f1e6=1645856951; gk_process_ev={%22count%22:10%2C%22utime%22:1645856454984%2C%22referrer%22:%22https://time.geekbang.org/column/intro/100047701?tab=catalog%22%2C%22target%22:%22%22%2C%22referrerTarget%22:%22page_geektime_login%22}";
    public static final String jsonStr = "{\"id\":\"6472\",\"include_neighbors\":true,\"is_freelyread\":true}";
    public static void main(String[] args) {



        String url = "https://time.geekbang.org/serv/v1/article";
        Map header = new HashMap<>();
        header.put("Cookie", cookies);
        header.put("Origin", "https://time.geekbang.org");
        header.put("Host", "time.geekbang.org");
        header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36");
        header.put("Content-Type", "text/plain");
//        header.put("Content-Length", "59");
        header.put("Origin", "https://time.geekbang.org");
        Map jsonObject = new HashMap();
        jsonObject.put("id","6472");
        jsonObject.put("include_neighbors",true);
        jsonObject.put("is_freelyread",true);

        String s = HttpUtil.postJson(url,header,jsonObject);
        log.info(s);

    }
}
