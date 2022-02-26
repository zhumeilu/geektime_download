/**
 * @author: zhumlu@yonyou.com
 * @date: 2022/2/26 16:52
 * @description:
 */

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * http请求工具类
 */
public class HttpUtil {

    public static Logger log  = LoggerFactory.getLogger(HttpUtil.class);
    /**
     * 请求连接构造对象
     */
    private static final HttpClientBuilder httpClientBuilder = HttpClients.custom();

    /**
     * 连接池最大连接数
     */
    private static final int MAX_TOTAL = 8;

    /**
     * 每个路由最大默认连接数
     */
    private static final int DEFAULT_MAX_RER_ROUTE = 8;

    /**
     * 获取连接获取超时时间
     */
    private static final int CONNECTION_REQUEST_TIMEOUT = 2000;

    /**
     * 连接超时时间
     */
    private static final int CONNECTION_TIMEOUT = 2000;

    /**
     * 数据响应超时时间
     */
    private static final int SOCKET_TIMEOUT = 10000;



    static {
        /*
         1、绕开不安全的https请求的证书验证(不需要可以注释，然后使用空参数的PoolingHttpClientConnectionManager构造连接池管理对象)
         */
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", trustHttpsCertificates())
                .build();

        /*
         2、创建请求连接池管理
         */
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
        // 设置连接池最大连接数
        cm.setMaxTotal(MAX_TOTAL);
        // 设置每个路由最大默认连接数
        cm.setDefaultMaxPerRoute(DEFAULT_MAX_RER_ROUTE);
        httpClientBuilder.setConnectionManager(cm);

        /*
        3、设置默认请求配置
         */
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT) // 设置获取连接获取超时时间
                .setConnectTimeout(CONNECTION_TIMEOUT) // 设置连接超时时间
                .setSocketTimeout(SOCKET_TIMEOUT) // 设置数据响应超时时间
                .build();
        httpClientBuilder.setDefaultRequestConfig(requestConfig);
    }


    /**
     *  执行get请求（网页）
     * @param url 请求地址(含有特殊符号需要URLEncoder编码)
     * @param headers 请求头参数
     * @return 响应数据
     */
    public static String getPage(String url, Map<String, String> headers) {

        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        HttpGet httpGet = new HttpGet(url);

        // 请求头设置，如果常用的请求头设置，也可以写死，特殊的请求才传入
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36 Edg/94.0.992.38");
        if (headers != null) {
            for (String headerKey : headers.keySet()) {
                httpGet.setHeader(headerKey, headers.get(headerKey));
            }
        }

        CloseableHttpResponse response = null;
        try {
            response = closeableHttpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode== HttpStatus.SC_OK) { // 请求响应成功
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity, StandardCharsets.UTF_8);
            } else {
                log.error("请求地址({})失败:{}", url, statusCode);
            }
        } catch (Exception e) {
            log.error("请求地址({})失败", url, e);
            throw new RuntimeException("请求地址("+url+")失败");
        } finally {
            HttpClientUtils.closeQuietly(response);
        }
        return null;
    }

    /**
     *  执行post请求（form表单）
     * @param url 请求地址
     * @param headers 请求头参数
     * @return 响应数据
     */
    public static String postForm(String url, Map<String, String> headers, Map<String, String> params) {
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        HttpPost httpPost = new HttpPost(url);
        // 请求头设置，如果常用的请求头设置，也可以写死，特殊的请求才传入
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36 Edg/94.0.992.38");
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        if (headers != null) {
            for (String headerKey : headers.keySet()) {
                httpPost.setHeader(headerKey, headers.get(headerKey));
            }
        }

        // 设置请求参数
        if (params!=null) {
            List<NameValuePair> nvList = new ArrayList<>(params.size());
            for (String paramKey : params.keySet()) {
                NameValuePair nv = new BasicNameValuePair(paramKey, params.get(paramKey));
                nvList.add(nv);
            }
            HttpEntity paramsEntity = new UrlEncodedFormEntity(nvList, StandardCharsets.UTF_8);
            httpPost.setEntity(paramsEntity);
        }

        CloseableHttpResponse response = null;
        try {
            response = closeableHttpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode== HttpStatus.SC_OK) { // 请求响应成功
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity, StandardCharsets.UTF_8);
            } else {
                log.error("请求地址({})失败:{}", url, statusCode);
            }
        } catch (IOException e) {
            log.error("请求地址({})失败", url, e);
            throw new RuntimeException("请求地址("+url+")失败");
        } finally {
            HttpClientUtils.closeQuietly(response);
        }
        return null;
    }

    /**
     *  执行post请求（接口）
     * @param url 请求地址
     * @param headers 请求头参数
     * @return 响应数据
     */
    public static String getJson(String url, Map<String, String> headers) {
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        HttpGet httpGet = new HttpGet(url);
        // 请求头设置，如果常用的请求头设置，也可以写死，特殊的请求才传入
        if (headers != null) {
            for (String headerKey : headers.keySet()) {
                httpGet.setHeader(headerKey, headers.get(headerKey));
            }
        }
        CloseableHttpResponse response = null;
        try {
            response = closeableHttpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode== HttpStatus.SC_OK) { // 请求响应成功
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity, StandardCharsets.UTF_8);
            } else {
                log.error("请求地址({})失败:{}", url, statusCode);
            }
        } catch (IOException e) {
            log.error("请求地址({})失败", url, e);
            throw new RuntimeException("请求地址("+url+")失败");
        } finally {
            HttpClientUtils.closeQuietly(response);
        }
        return null;
    }

    /**
     *  执行post请求（接口）
     * @param url 请求地址
     * @param headers 请求头参数
     * @return 响应数据
     */
    public static String postJson(String url, Map<String, String> headers, Map<String, String> params) {
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        HttpPost httpPost = new HttpPost(url);
        // 请求头设置，如果常用的请求头设置，也可以写死，特殊的请求才传入
//        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
        if (headers != null) {
            for (String headerKey : headers.keySet()) {
                httpPost.setHeader(headerKey, headers.get(headerKey));
            }
        }
        if (params!=null) {
            HttpEntity paramEntity = new StringEntity(JSON.toJSONString(params), StandardCharsets.UTF_8);
            httpPost.setEntity(paramEntity);
        }

        CloseableHttpResponse response = null;
        try {
            response = closeableHttpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode== HttpStatus.SC_OK) { // 请求响应成功
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity, StandardCharsets.UTF_8);
            } else {
                log.error("请求地址({})失败:{}", url, statusCode);
            }
        } catch (IOException e) {
            log.error("请求地址({})失败", url, e);
            throw new RuntimeException("请求地址("+url+")失败");
        } finally {
            HttpClientUtils.closeQuietly(response);
        }
        return null;
    }

    /**
     * 构建https安全连接工厂
     * @return 安全连接工厂
     */
    private static ConnectionSocketFactory trustHttpsCertificates() {
        SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
        try {
            sslContextBuilder.loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            });
            SSLContext sslContext = sslContextBuilder.build();
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,
                    new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2"}, // 支持的https安全认证协议
                    null, NoopHostnameVerifier.INSTANCE);
            return sslConnectionSocketFactory;
        } catch (Exception e) {
            log.error("构建安全连接工厂失败", e);
            throw new RuntimeException("构建安全连接工厂失败");
        }
    }
}