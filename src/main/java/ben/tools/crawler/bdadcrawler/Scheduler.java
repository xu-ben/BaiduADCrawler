package ben.tools.crawler.bdadcrawler;

import java.io.IOException;
import java.util.Date;

public class Scheduler {

	@SuppressWarnings("deprecation")
	private static String getDatestr(Date date) {
		String datestr = String.format("%d%02d%02d", date.getYear() + 1900, date.getMonth() + 1, date.getDate());
		if (date.getHours() >= 12) {
			return datestr + "p";
		} else {
			return datestr + "a";
		}
	}
	
	public boolean crawlExcludeSomeCity(City[] cities, String datestr) throws IOException {
		boolean success = true;
		for (City city : City.values()) {
			boolean excluded = false;
			for (City c : cities) {
				if (c == city) {
					excluded = true;
					break;
				}
			}
			if (excluded) {
				continue;
			}
			if (!city.isUseProxy() || city.getCitycode() > 0) {
                success &= this.crawl(city, datestr);
			}
		}
		return success;
	}
	
	public boolean crawlSomeCity(City[] cities, String datestr) throws IOException {
		boolean success = true;
		for (City city : cities) { 
            success &= this.crawl(city, datestr);
		}
		return success;
	}

	private boolean crawl(City city, String datestr) throws IOException {
		return new Spider().crawl(city, datestr);
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
		
		long lognum = 0;
		while ((ct = System.currentTimeMillis()) < datetime) {
			try {
				if (Math.abs(ct - date.getTime()) < 5000) {
					// TODO
					System.err.printf("datetime:%d,\t nowtime:%d,\t%s\n", datetime, ct, datestr);
					return this.crawlAllCity(datestr);
//					return true;
				} else {
					if (lognum % 100 == 0) {
						System.out.printf("nowtime:%s\n", new Date(ct).toString());
					} else if (lognum % 100 == 0) {
						System.out.printf("datetime:%s,\t nowtime:%s\n", new Date(datetime).toString(), new Date(ct).toString());
					}
					lognum++;
					Thread.sleep(1000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				return false;
			}
		}
		System.out.println("here");
		return false;
	}

	public static void test() {
		String datestr = "20181218a";
		Scheduler scheduler = new Scheduler();
		try {
			scheduler.crawlAllCity(datestr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void complement() {
		String datestr = "20181222p";
		Scheduler scheduler = new Scheduler();
		try {
//		City[] cities = {City.KUNMING, City.SHENYANG, City.HAERBIN};
//		scheduler.crawlSomeCity(cities, datestr);
			City[] cities = {City.BEIJING, City.SHANGHAI, City.GUANGZHOU, City.ZHENGZHOU, City.NANJING, City.HAERBIN};
			scheduler.crawlExcludeSomeCity(cities, datestr);
		} catch (IOException e) {
		}
	}

	public static void main(String[] args) {
//		System.err.println(crawlAllCityAt(2018, 12, 18, 15, 3));
//		System.err.println(crawlAllCityAt(2018, 12, 19, 10, 6));
//		System.err.println(crawlAllCityAt(2018, 12, 19, 15, 3));
//		System.err.println(crawlAllCityAt(2018, 12, 20, 9, 2));
//		System.err.println(crawlAllCityAt(2018, 12, 21, 10, 4));
//		System.err.println(crawlAllCityAt(2018, 12, 22, 18, 6));
		complement();
//		test();

	}

}