import okhttp3.logging.HttpLoggingInterceptor;
import sun.rmi.runtime.Log;

/**
 * @author: zhumlu@yonyou.com
 * @date: 2022/2/26 15:55
 * @description:
 */
public class HttpLogger implements HttpLoggingInterceptor.Logger {
    @Override
    public void log(String message) {
//        Log.d("HttpLogInfo", message);//okHttp的详细日志会打印出来
        System.out.println(message);
    }
}
