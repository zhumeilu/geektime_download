import com.spire.pdf.graphics.PdfMargins;
import com.spire.pdf.htmlconverter.qt.HtmlConverter;
import com.spire.pdf.htmlconverter.qt.Size;

/**
 * @author: zhumlu@yonyou.com
 * @date: 2022/2/26 14:50
 * @description:
 */
public class Html2Pdf {
    public static void main(String[] args) {
        //定义需要转换的HTML
        String url = "https://www.baidu.com/";

        //转换后的结果文档（结果文档保存在Java项目程序文件下）
        String fileName = "HtmlToPDF.pdf";

        //解压后的插件本地地址（这里是把插件包放在了Java项目文件夹下，也可以自定义其他本地路径）
        String pluginPath = "D:\\Program Files\\plugins-windows-x64\\plugins";
        HtmlConverter.setPluginPath(pluginPath);

        //调用方法转换到PDF并设置PDF尺寸
        HtmlConverter.convert(url, fileName, true, 1000, new Size(700f, 800f), new PdfMargins(0));
    }
}
