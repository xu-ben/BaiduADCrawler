package ben.tools.crawler.bdadcrawler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Displayer {

    public static void displaySimply(AD ad) {
        System.out.println(ad.getRank());
        System.out.println(ad.getTitle());
        System.out.println(ad.getOrganization());
        System.out.println(ad.getDateInPage());
        System.out.println(ad.getUrl());
        System.out.println(ad.getContext());
        System.out.println();
        System.out.println();
    }

    public static void displayNormally(AD ad) {
        System.out.println("////////////////////////////////////////");
        System.out.println("rank:\t" + ad.getRank());
        System.out.println("title:\t" + ad.getTitle());
        System.out.println("org:\t" + ad.getOrganization());
        System.out.println("date in page:\t" + ad.getDateInPage());
        System.out.println("url:\t" + ad.getUrl());
        System.out.println("body:\t" + ad.getContext());
        System.out.println("access location:\t" + ad.getCity());
        System.out.println("access date:\t" + ad.getAccessDatestr());
        System.out.println("access timestamp:\t" + ad.getTimestamp());
    }


    /**
     * 把某个时间，指定城市的搜索结果输出
     *
     * @param city
     * @param datestr
     * @throws IOException
     */
    public static void displayNormally(City city, String datestr) throws IOException {
        for (KeyWord key : KeyWord.values()) {
            System.out.println("////////////////////////////////////////////////////////////////////////////////");
            System.out.println("City: " + city.name().toLowerCase() + "\tKeyword: " + key.getStr());
            ADsInAFile parseRes = Parser.findAndParseAResultInBase(city, datestr, key);
            ArrayList<AD> adlist = parseRes.getAdlist();
            String filePath = parseRes.getFilePath();
            if (filePath == null) {
                System.out.println("no this result file");
            } else {
                System.out.println(filePath);
                if (adlist != null && adlist.size() > 0) {
                    for (AD ad : adlist) {
                        displayNormally(ad);
                    }
                } else {
                    System.out.println("no ADs in this file");
                }
            }
            System.out.println("////////////////////////////////////////////////////////////////////////////////\n\n");
        }
    }

    public static void displayAllNormally(City city) throws IOException {
//        for (KeyWord key : KeyWord.values()) {
//
//        }
        KeyWord key = KeyWord.FENG_XIONG;
        ADsInAFile[] parseResults = Parser.findAndParseResultsInACity(city, key);
        if (parseResults == null) {
            return ;
        }
        for (ADsInAFile result : parseResults) {
            System.out.println("////////////////////////////////////////////////////////////////////////////////");
            System.out.println("Date: " + result.getDatestr() + "\tKeyword: " + key.getStr());
            ArrayList<AD> adlist = result.getAdlist();
            String filePath = result.getFilePath();
            System.out.println(filePath);
            if (adlist != null && adlist.size() > 0) {
                for (AD ad : adlist) {
                    displayNormally(ad);
                }
            } else {
                System.out.println("no ADs in this file");
            }
            System.out.println("////////////////////////////////////////////////////////////////////////////////\n\n");
        }

    }

    public static void displayNormally(String datestr) throws IOException {
        for (City city : City.values()) {
            try {
                displayNormally(city, datestr);
            } catch (FileNotFoundException e) {
                System.err.println("no result file found");
            }
        }
    }

}
