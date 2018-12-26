package ben.crawler.bdadcrawler;

import ben.crawler.proxy.ZMProxy;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

public class Scheduler {

    private static Logger logger = Logger.getLogger(Scheduler.class.getName());

    private ZMProxy zmProxy = null;

    private String dataDirPath = null;

    public Scheduler(ZMProxy zmProxy, String dataDirPath) {
        this.zmProxy = zmProxy;
        this.dataDirPath = dataDirPath;
    }

    @SuppressWarnings("deprecation")
    private static String getDatestr(Date date) {
        String datestr = String.format("%d%02d%02d", date.getYear() + 1900, date.getMonth() + 1, date.getDate());
        if (date.getHours() >= 12) {
            return datestr + "p";
        } else {
            return datestr + "a";
        }
    }

    public boolean crawlSomeCity(City[] cities, String datestr) throws IOException {
        boolean success = true;
        for (City city : cities) {
            success &= this.crawl(city, datestr);
        }
        return success;
    }

    private boolean crawl(City city, String datestr) throws IOException {
        return new Spider(this.zmProxy, this.dataDirPath).crawl(city, datestr);
    }

    public boolean crawlAllCity(String datestr) throws IOException {
        boolean success = true;
        for (City city : City.values()) {
            if (!city.isUseProxy() || city.getCitycode() > 0) {
                success &= this.crawl(city, datestr);
            }
        }
        return success;
    }

    public boolean crawlAllCityAt(int year, int month, int day, int hour, int minutes) throws IOException {
        @SuppressWarnings("deprecation")
        Date date = new Date(year - 1900, month - 1, day, hour, minutes, 0);
        String datestr = getDatestr(date);
        long datetime = date.getTime();
        long ct = System.currentTimeMillis();

        long cycles = 0;
        while ((ct = System.currentTimeMillis()) < datetime) {
            try {
                if (Math.abs(ct - date.getTime()) < 5000) {
                    // TODO
                    logger.info(String.format("cycles: %1$d,\tdatetime is:%2$tF %2$tT\n", cycles, datetime));
                    return this.crawlAllCity(datestr);
//					return true;
                } else {
                    if (cycles % 100 == 0) {
                        logger.info(String.format("cycles: %d", cycles));
                    } else if (cycles % 1000 == 0) {
                        logger.info(String.format("cycles: %1$d,\tdatetime is:%2$tF %2$tT\n", cycles, datetime));
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

    public static void test() {
        String rootdir = "/home/ben/Develop/spider/html";
        ZMProxy zmProxy = new ZMProxy(true);
        String datestr = "20181218a";
        Scheduler scheduler = new Scheduler(zmProxy, rootdir);
        try {
            scheduler.crawlAllCity(datestr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void complement(ZMProxy zmProxy, String rootdir) {
        String datestr = "20181224a";
        Scheduler scheduler = new Scheduler(zmProxy, rootdir);
        try {
//            City[] cities = {City.KUNMING, City.SHENYANG, City.HAERBIN};
//            scheduler.crawlSomeCity(cities, datestr);
//            City[] cities = {City.BEIJING, City.SHANGHAI, City.GUANGZHOU, City.SHENZHEN, City.ZHENGZHOU, City.NANJING, City.FUZHOU, City.HEFEI, City.HAERBIN};
//            City[] cities = {City.BEIJING, City.SHENZHEN, City.HAERBIN};
//            scheduler.crawlSomeCity(City.getAllCitiesExclude(cities), datestr);
            scheduler.crawlSomeCity(new City[]{City.BEIJING}, datestr);
        } catch (IOException e) {
        }
    }

    public static void main(String[] args) {
		String rootdir = "/home/ben/Develop/spider/html";
        ZMProxy zmProxy = new ZMProxy(true);
        Scheduler scheduler = new Scheduler(zmProxy, rootdir);
//		crawlAllCityAt(2018, 12, 18, 15, 3);
//		crawlAllCityAt(2018, 12, 19, 10, 6);
//		crawlAllCityAt(2018, 12, 19, 15, 3);
//		crawlAllCityAt(2018, 12, 20, 9, 2);
//		crawlAllCityAt(2018, 12, 21, 10, 4);
//		crawlAllCityAt(2018, 12, 21, 10, 4);
        try {
            scheduler.crawlAllCityAt(2018, 12, 25, 16, 45);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        complement(zmProxy, rootdir);
//		test();

    }

}
