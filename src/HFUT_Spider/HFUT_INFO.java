package HFUT_Spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

public class HFUT_INFO {

    static String Get_HFUTMain () throws IOException {
        URL url = new URL("https://news.hfut.edu.cn/tzgg1.htm");
        Document document = Jsoup.parse(url, 10000);
        Elements elements = document.getElementsByTag("li");
        StringBuilder info = new StringBuilder();
        info.append("合肥工业大学官网通知：");
        int num = 0;
        for (Element e : elements) {
            if(num != 0) info.append(e.getElementsByTag("p").text()).append('\n');
            if(num == 31) break;
            num++;
        }
        System.out.println(info);
        return info.toString();
    }

    static String Get_ComputerAndInformation () throws IOException {
        URL url = new URL("http://ci.hfut.edu.cn/tz/list.htm");
        Document document = Jsoup.parse(url, 10000);
        Elements elements = document.getElementsByClass("fl text txtov");
        StringBuilder info = new StringBuilder();
        info.append("计算机与信息学院通知：\n");
        for (Element e : elements) {
            info.append(e.text()).append('\n');
        }
        System.out.println(info);
        return info.toString();
    }

    static String Get_MechanicalEngineerin () throws IOException {
        URL url = new URL("http://jxxy.hfut.edu.cn/tzgg/list.htm");
        Document document = Jsoup.parse(url, 10000);
        Elements elements = document.getElementsByClass("catItemTitle");
        StringBuilder info = new StringBuilder();
        info.append("机械工程学院通知：\n");
        for (Element e : elements) {
            info.append(e.text()).append('\n');
        }
        System.out.println(info);
        return info.toString();
    }

    static String Get_MaterialsScience () throws IOException {
        URL url = new URL("http://mse.hfut.edu.cn/index/tzgg.htm");
        Document document = Jsoup.parse(url, 10000);
        Elements elements = document.getElementsByTag("li");
        StringBuilder info = new StringBuilder();
        info.append("材料科学与工程学院通知：\n");

        int num = 0;
        for (Element e : elements) {
            if(num > 52) info.append(e.text()).append('\n');
            num ++;
        }
        System.out.println(info);
        return info.toString();
    }

    static String Get_ElectricalEngineering () throws IOException {
        URL url = new URL("http://ea.hfut.edu.cn/xwzx/xygg1.htm");
        Document document = Jsoup.parse(url, 10000);
        Elements elements = document.getElementsByTag("tr");
        StringBuilder info = new StringBuilder();
        info.append("电气与自动化工程学院学院通知：\n");
        for (Element e : elements) {
            info.append(e.text()).append('\n');
        }
        System.out.println(info);
        return info.toString();
    }

    static String Get_CivilEngineering () throws IOException {
        URL url = new URL("http://civil.hfut.edu.cn/xygg/list.htm");
        Document document = Jsoup.parse(url, 10000);
        Elements elements = document.getElementsByClass("lb01");
        StringBuilder info = new StringBuilder();
        info.append("土木与水利工程工程学院学院通知：\n");
        for (Element e : elements) {
            info.append(e.text()).append('\n');
        }
        System.out.println(info);
        return info.toString();
    }

    static String Get_ChemicalEngineering () throws IOException {
        URL url = new URL("http://hgxy.hfut.edu.cn/tzgg/list.htm");
        Document document = Jsoup.parse(url, 10000);
        Elements elements = document.getElementsByTag("li");
        StringBuilder info = new StringBuilder();
        info.append("化学与化工学院学院通知：\n");
        int num = 0;
        for (Element e : elements) {
            if(num > 103)info.append(e.text()).append('\n');
            if(num > 122) break;
            num++;
        }
        System.out.println(info);
        return info.toString();
    }
}
