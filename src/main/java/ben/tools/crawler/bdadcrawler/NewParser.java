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


    private void parseTitlePart(AD ad, Element div) {
        // 正常里面就是一个h3标签, 但是这里先不用这个
        String titleText = div.text();
        ad.setTitle(titleText == null ? null : titleText.trim());
//        System.err.println(div.text());
        Elements links = div.getElementsByTag("a");
        for (Element link : links) {
//            String linkHref = link.attr("href");
            String linkHref = link.attr("data-landurl");
//            String linkText = link.text();
//            System.out.println(linkHref);
            if (linkHref != null && linkHref.trim().length() > 0) {// TODO
                ad.setUrl(linkHref);
            }
        }
    }

    /**
     *
     * @param ad
     * @param div
     * @param uselastdiv 只能取0或1，0表示org信息div不在此div内，1表示在
     * @throws IOException
     */
    private void parseBodyPart(AD ad, Element div, int uselastdiv) throws IOException {
        /**
         * 这个div下正常是两个div，其中第一个是放图片，第二个是其它, 如果只有一个div，那说明没有图片
          */
        Elements children = div.children();
        if (children.size() < 1) {
            // todo error;
            throw new IOException("");
        }
        Element bodydiv = children.get(children.size() - 1);

        /**
         * 这个div之下是多个div，如果uselastdiv＝1, 说明最后一个是放机构和日期的，其它的是内容
          */
        Elements divs = bodydiv.children();
        int size = divs.size();
        StringBuilder bodyText = new StringBuilder();
        for (int i = 0; i < size - uselastdiv; i++) {
            Element e = divs.get(i);
//            System.out.println("@@@:\ttype:" + e.tagName() + "\tid:" + e.id() + "\tclassName:" + e.className());
//            if (i > 0) {
//                bodyText.append('\n');
//            }
            bodyText.append(e.text());
        }
//        System.err.println(bodyText);
        ad.setContext(bodyText.toString());

        if (uselastdiv == 1) {
            parseBottomPart(ad, divs.get(size - 1));
        }

    }

    private void parseBottomPart(AD ad, Element div) {
        Element alabel = div.children().get(0); // TODO 加强健壮性
        // a标签里面是两个span，分别放机构名称和日期
        Elements spans = alabel.children();
        ad.setOrganization(spans.get(0).text());
        ad.setDateInPage(spans.get(1).text());
    }

    private boolean ifContainBottom(String text) {
        return (text.indexOf("广告") >= 0 && text.indexOf("评价") >= 0);
    }

    public AD parseAAd(Element addiv) throws IOException {
        AD ad = new AD();
        Elements children = addiv.children();

        // 第一个div一定是放title的，毫无疑问
        Element titleDiv = children.get(0);
        parseTitlePart(ad, titleDiv);

//        parseBodyAndBottom(ad, bodyDiv);
        // 之后分两种情况，一种是body和bottom合在一个div里，另一种是分开。
        Element bodyDiv = children.get(1);
        if (ifContainBottom(bodyDiv.text())) {
            parseBodyPart(ad, bodyDiv.children().get(0), 1);
        } else {
            parseBodyPart(ad, bodyDiv, 0);
            parseBottomPart(ad, children.get(2));
        }
        // 之后的div不用管
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
