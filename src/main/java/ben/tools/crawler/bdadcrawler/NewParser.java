package ben.tools.crawler.bdadcrawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewParser {

    private File targetFile;

    private NewParser(File targetFile) {
        this.targetFile = targetFile;
    }

    private Pattern urlPattern = Pattern.compile("data-landurl=[\"]([^\"]+)[\"]");

    private void parseURL(AD ad, StringBuilder urlText) {
        Matcher m = urlPattern.matcher(urlText);
        StringBuilder urlsb = new StringBuilder();
        while (m.find()) {
            urlsb.append(m.group(1));
        }
        ad.setUrl(urlsb.toString());
//		System.err.println(urlsb);
    }


    // 一般顔色是＃CC0000
    private Pattern textPattern = Pattern.compile("((?<=>)[^<>]+)|(<font[^>]+>([^<>]+)</font>)");

    private StringBuilder getTextFromFontHtml(StringBuilder html) {
        Matcher m = textPattern.matcher(html);
        StringBuilder text = new StringBuilder();
        while (m.find()) {
            if (m.group(1) != null) {
                text.append(m.group(1));
            }
            if (m.group(3) != null) {
                text.append(m.group(3));
            }
        }
        return text;
    }

    public StringBuilder getHtmlContextInALabel(String html) {
        Matcher matcher = aLabelPattern.matcher(html);
        StringBuilder context = new StringBuilder();
        while (matcher.find()) {
            context.append(matcher.group(2));
        }
        return context;
    }

    public StringBuilder getTextInALabel(String html) {
        Matcher matcher = aLabelPattern.matcher(html);
        StringBuilder context = new StringBuilder();
        while (matcher.find()) {
            context.append(matcher.group(2));
        }
        return getTextFromFontHtml(context);
    }

    // a标签的内容里可能有引号，但标签属性里的字符串中不含有<>
    private Pattern aLabelPattern = Pattern.compile("<a([^>]+)(>.+?<)/a>");

    // a标签属性里的字符串中可能含有<>，但标签的内容里没有引号的情况
    private Pattern aLabelPattern2 = Pattern.compile("<a (.+?)(>[^\"]+<)/a>");

    //	private Pattern aLabelPattern = Pattern
//			.compile("<a([^>]+)>((([^<>]+)|(<font color=#CC0000>[^<>]+</font>))+)</a>");
    private void parseContentInH3(AD ad, String h3) {
//		System.err.println(h3);
//		System.err.println();
        Matcher mh3 = aLabelPattern.matcher(h3);
        StringBuilder titleHtml = new StringBuilder();
        StringBuilder urlText = new StringBuilder();
        while (mh3.find()) {
            urlText.append(mh3.group(1));
            titleHtml.append(mh3.group(2));
        }
        StringBuilder titleText = getTextFromFontHtml(titleHtml);
        ad.setTitle(titleText == null ? null : titleText.toString());
        // System.out.println(titleHtml);
        parseURL(ad, urlText);
    }


    private void parseBody(AD ad, String body) {
//		System.out.println(body);
        StringBuilder bodyText = getTextInALabel(body);
        ad.setContext(bodyText == null ? null : bodyText.toString());
    }

    private Pattern h3Pattern = Pattern.compile("<h3[^>]*>(.+?)</h3>");
    private Pattern orgPattern = Pattern.compile("<span[^<>]+>([^<>]+)</span>[^<>]*<span[^<>]+>([^<>]+)</span>");
    private Pattern bodyPattern = Pattern.compile("<div class=\"\">(.+?)</div>");

    public AD parseAAd(Element addiv) {
        AD ad = new AD();
        System.out.println(addiv.outerHtml());
        System.exit(0);
//        Matcher h3m = h3Pattern.matcher(text);
//        if (h3m.find()) {
//            String h3 = h3m.group(1);
//            parseContentInH3(ad, h3);
//        }
//        Matcher orgm = orgPattern.matcher(text);
//        if (orgm.find()) {
//            String org = orgm.group(1);
//            if (org != null) {
//                ad.setOrganization(org);
//            }
//            String dateInPage = orgm.group(2);
//            if (dateInPage != null) {
//                ad.setDateInPage(dateInPage);
//            }
//        }
//        Matcher bodym = bodyPattern.matcher(text);
//        if (bodym.find()) {
//            String body = bodym.group(1);
//            parseBody(ad, body);
//        }
        return ad;
    }

    public ArrayList<AD> runParser() throws IOException {
        ArrayList<AD> adlist = new ArrayList<AD>();
        String fileContent = Commons.getTextFromFile(targetFile);
        if (fileContent == null || fileContent.trim().equals("")) {
            throw new FileNotFoundException("this file is empty");
        }
        Document doc = Jsoup.parse(targetFile, "UTF-8");
//        System.err.println(doc.outerHtml());
//        Elements divs = doc.select("div#content_left");
        Elements divs = doc.select("div#content_left").get(0).children();
//        System.err.println(divs.outerHtml());
//        Elements tmp = divs.select("div");
        for (Element e : divs) {
            // TODO 优化判断逻辑 可以去看className，正常结果的className好像含有result字符串
            if (e.id() != null && e.id().length() > 3) {
                AD ret = parseAAd(e);
                if (ret != null) {
                    adlist.add(ret);
                }
//                System.out.println("@@@:\ttype:" + e.tagName() + "\tid:" + e.id() + "\tclassName:" + e.className());
            }
        }
        return adlist;
    }


    public static long getTimestampOfResultFile(City city, String datestr, KeyWords key) throws FileNotFoundException {
        final String basePath = "/home/ben/Develop/spider/html/";
        String dirPath = basePath + city.name().toLowerCase();
//		System.err.println(dirPath);
        File dir = new File(dirPath);
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
            throw new FileNotFoundException();
        }
        String[] fileNameList = dir.list();
        Pattern p = Pattern.compile(key.ordinal() + "_" + datestr + "_(\\d+)\\.html");
        for (String fileName : fileNameList) {
            Matcher m = p.matcher(fileName);
            if (m.matches()) {
                return Long.parseLong(m.group(1));
//				System.err.println(m.group(1));
            }
//			System.out.println(fileName);
        }
        return -1;
    }

    /**
     * @param city
     * @param datestr
     * @param key
     * @return 返回解析得到的所有广告
     * @throws IOException
     */
    public static ArrayList<AD> findAndParseAResultInBase(City city, String datestr, KeyWords key) throws IOException {
        ArrayList<AD> adlist = new ArrayList<AD>();
        findAndParseAResultInBase(city, datestr, key, adlist);
        return adlist;
    }

    /**
     * @param city
     * @param datestr
     * @param key
     * @param adlist  解析的结果将会被add到此list中
     * @return 如何文件存在，返回文件全路径字符串，否则返回null
     * @throws IOException
     */
    public static String findAndParseAResultInBase(City city, String datestr, KeyWords key, ArrayList<AD> adlist) throws IOException {
        final String basePath = "/home/ben/Develop/spider/html/";
        long timestamp = getTimestampOfResultFile(city, datestr, key);
        if (timestamp < 0) {
            return null;
        }
        StringBuilder filePath = new StringBuilder(basePath);
        filePath.append(city.name().toLowerCase());
        filePath.append('/');
        filePath.append(key.ordinal());
        filePath.append('_');
        filePath.append(datestr);
        filePath.append('_');
        filePath.append(timestamp);
        filePath.append(".html");
        String pathstr = filePath.toString();
        // System.out.println(filePath);
        try {
            ArrayList<AD> results = parseResultFile(pathstr);
            fillField(results, city, timestamp, datestr);
            if (results != null) {// result == null说明没有广告
                adlist.addAll(results);
            }
            return pathstr;
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    private static ArrayList<AD> parseResultFile(String filePath) throws IOException {
        if (filePath == null || filePath.trim().equals("")) {
            throw new FileNotFoundException("filePath error");
        }
        File targetFile = new File(filePath);
        if (!targetFile.exists() || !targetFile.isFile()) {
            throw new FileNotFoundException();
        }
        return new NewParser(targetFile).runParser();
    }

    private static void fillField(ArrayList<AD> adlist, City city, long timestamp, String datestr) {
        if (adlist == null) {
            return;
        }
        for (int i = 0, len = adlist.size(); i < len; i++) {
            AD ad = adlist.get(i);
            ad.setRank(i + 1);
            ad.setCity(city.name().toLowerCase());
            ad.setTimestamp(timestamp);
            ad.setAccessDatestr(datestr);
        }
    }

    public static ArrayList<AD> parseAFile(String filePath, City accessCity, String datestr) throws IOException {
        ArrayList<AD> adlist = parseResultFile(filePath);
        fillField(adlist, accessCity, -1, datestr);
        return adlist;
    }

}
