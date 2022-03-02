import com.spire.doc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author: zhumlu@yonyou.com
 * @date: 2022/2/26 14:50
 * @description:
 */
public class Html2Pdf3 {
    public static Logger log  = LoggerFactory.getLogger(Html2Pdf3.class);
    public static void main(String[] args) throws IOException{
        String inputHtml = "output/test.html";
        //新建Document对象
        Document doc = new Document();
        //添加section
        Section sec = doc.addSection();

        String htmlText = readTextFromFile(inputHtml);
        //添加段落并写入HTML文本
        sec.addParagraph().appendHTML(htmlText);

        //将文档另存为PDF
        doc.saveToFile("HTMLstringToPDF.pdf", FileFormat.PDF);
        doc.dispose();
    }
    public static String readTextFromFile(String fileName) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String content;
        while ((content = br.readLine()) != null) {
            sb.append(content);
        }
        return sb.toString();
    }
}
