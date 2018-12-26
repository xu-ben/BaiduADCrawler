package ben.crawler.bdadcrawler;

import ben.crawler.proxy.ArrearsException;
import ben.crawler.proxy.ZMProxy;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.logging.Logger;

public class Scheduler {

    private static Logger logger = Logger.getLogger(Scheduler.class.getName());

    private ZMProxy zmProxy = null;

    private String dataDirPath = null;

    public Scheduler(ZMProxy zmProxy, String dataDirPath) {
        this.zmProxy = zmProxy;
        this.dataDirPath = dataDirPath;
    }

    private static String getDatestr(long timestamp) {
//        String.format(Locale.US, "%1$tY%1$tm%1$td%1$tp", timestamp);
        String str = String.format(Locale.US, "%1$tY%1$tm%1$td%1$tp", timestamp);
        return str.substring(0, str.length() - 1); // todo 这样是显示a或p而不是am或pm，将来可改
    }

    public boolean crawlSomeCity(City[] cities, String datestr) throws IOException {
        boolean success = true;
        for (City city : cities) {
            if (!city.isUseProxy() || city.getCitycode() > 0) {
                success &= this.crawl(city, datestr);
            }
        }
        return success;
    }

    private boolean crawl(City city, String datestr) throws IOException {
        return new Spider(this.zmProxy, this.dataDirPath).crawl(city, datestr);
    }

    public boolean crawlAllCity(String datestr) throws IOException {
        return crawlSomeCity(City.values(), datestr);
    }

    /**
     * 在指定的时间执行爬虫
     */
    public boolean crawlAllCityAt(long dateTimestamp) throws IOException {
        String datestr = getDatestr(dateTimestamp);
        long ct, cycles = 0;
        while ((ct = System.currentTimeMillis()) < dateTimestamp) {
            try {
                if (Math.abs(ct - dateTimestamp) < 5000) {
                    // TODO
                    logger.info(String.format("cycles: %1$d,\tdatetime is:%2$tF %2$tT\n", cycles, dateTimestamp));
                    return this.crawlAllCity(datestr);
//                    return true;
                } else {
                    if (cycles % 100 == 0) {
                        logger.info(String.format("cycles: %d", cycles));
                    } else if (cycles % 1000 == 0) {
                        logger.info(String.format("cycles: %1$d,\tdatetime is:%2$tF %2$tT\n", cycles, dateTimestamp));
                    }
                    cycles++;
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        }
        logger.severe("run to here...");
        return false;
    }


    private static City[] getAllCitiesProxiable() {
        final City[] cities = {City.BEIJING, City.SHENZHEN, City.HAERBIN};
        final City[] results = City.getAllCitiesExclude(cities);
        return results;
    }


    public static void complement(Scheduler scheduler) throws IOException {
        String datestr = "20181226a";
//        City[] cities = {City.KUNMING, City.SHENYANG, City.HAERBIN};
//        scheduler.crawlSomeCity(cities, datestr);
        City[] cities = {City.BEIJING, City.SHANGHAI, City.GUANGZHOU, City.SHENZHEN, City.ZHENGZHOU, City.NANJING, City.FUZHOU, City.HEFEI, City.HAERBIN};
        scheduler.crawlSomeCity(City.getAllCitiesExclude(cities), datestr);
//        scheduler.crawlSomeCity(new City[]{City.BEIJING}, datestr);
//        scheduler.crawlSomeCity(City.getAllCitiesExclude(new City[]{City.BEIJING}), datestr);
//        scheduler.crawlSomeCity(getAllCitiesProxiable(), datestr);
    }

    public static void main(String[] args) {
        String rootdir = "/home/ben/Develop/spider/html";
        ZMProxy zmProxy = new ZMProxy(true);
        try {
            Scheduler scheduler = new Scheduler(zmProxy, rootdir);
            Calendar date = Calendar.getInstance();
            date.set(2018, 12 - 1, 26, 16, 44, 0);
            scheduler.crawlAllCityAt(date.getTimeInMillis());
//            complement(scheduler);
        } catch (ArrearsException ae) {
            ae.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            zmProxy.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
