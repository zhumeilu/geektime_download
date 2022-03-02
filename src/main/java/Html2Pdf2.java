import com.spire.pdf.graphics.PdfMargins;
import com.spire.pdf.htmlconverter.LoadHtmlType;
import com.spire.pdf.htmlconverter.qt.HtmlConverter;
import com.spire.pdf.htmlconverter.qt.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author: zhumlu@yonyou.com
 * @date: 2022/2/26 14:50
 * @description:
 */
public class Html2Pdf2 {
    public static Logger log  = LoggerFactory.getLogger(Html2Pdf2.class);
    public static void convert(String htmlString){
        //调用自定义方法HtmlToString()将HTML文件转换为字符串
//        String htmlString = HtmlToString("C: \\Users\\Administrator\\Desktop\\Sample.html");

        log.info("pdf内容:{}",htmlString);
        //指定输出文档路径
        String outputFile = "output/HtmlToPdf.pdf";

        //指定插件路径
        String pluginPath = "D:\\Program Files\\plugins-windows-x64\\plugins";

        //设置插件路径
        HtmlConverter.setPluginPath(pluginPath);

        //将HTML字符串转换为PDF
        HtmlConverter.convert(htmlString, outputFile, true, 100000, new Size(700, 900), new PdfMargins(0), LoadHtmlType.Source_Code);
    }

    //将HTML文件转换为字符串
    public static String HtmlToString(String filePath) throws IOException {
        String path = filePath;
        File file = new File(path);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder stringBuilder = new StringBuilder();
        String temp = "";
        while ((temp = bufferedReader.readLine()) != null) {
            stringBuilder.append(temp + "\n");
        }
        bufferedReader.close();
        String str = stringBuilder.toString();
        return str;
    }

    public static void main(String[] args) throws IOException {
        String htmlString = HtmlToString("D:/github_projects/geektime_download/output/test.html");
//        System.out.println("pdf内容");
        log.info("pdf内容:{}",htmlString);
        //指定输出文档路径
        String outputFile = "output/HtmlToPdf.pdf";

        //指定插件路径
        String pluginPath = "D:\\Program Files\\plugins-windows-x64\\plugins";

        //设置插件路径
        HtmlConverter.setPluginPath(pluginPath);
        //将HTML字符串转换为PDF
        HtmlConverter.convert(htmlString, outputFile, true, 100000, new Size(700, 900), new PdfMargins(0), LoadHtmlType.Source_Code);
    }
}
