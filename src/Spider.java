import java.io.IOException;
import java.util.Date;

public class Spider {

	private String bdurl = "https://www.baidu.com/s?ie=utf-8&wd=";

	private void crawlPageToFile(String cityname, String date, String proxy, KeyWords key) {
		String rootdir = String.format("/home/ben/Develop/spider/html/%s", cityname);
		if (proxy == null) {
			proxy = "";
		} else {
			proxy = "-x " + proxy;
		}
		long time = System.currentTimeMillis();
		final String basestr = "curl -A \"%s\" %s \"%s%s\" > %d_%s_%d.html";
		String agent = Commons.chromeUserAgent;
		String cmd = String.format(basestr, agent, proxy, bdurl, key.getStr(), key.ordinal(), date, time);
		System.err.println(cmd);
		try {
			String ret = Commons.execCmdInDir(cmd, rootdir, 5);
			if (ret != null && !ret.trim().equals("")) {
				System.err.println(ret);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public boolean crawl(City city, String date) throws IOException {
		System.err.println("////////////////////////////////////////////////////////////////////////////////");
		System.err.println(city.name().toLowerCase());
		String proxy = city.isUseProxy() ? ZMProxy.fetchProxyFromServer(city) : null;
		boolean ret = false;
		if (!city.isUseProxy() || proxy != null) {
			String citystr = city.name().toLowerCase();
			for (KeyWords key : KeyWords.values()) {
				crawlPageToFile(citystr, date, proxy, key);
			}
			ret = true;
		} else {
			System.err.println("get IP proxy error!");
		}
		System.err.println();
		return ret;
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
	
	public boolean crawlExcludeSomeCity(City[] cities, String datestr) {
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
				try {
					success &= this.crawl(city, datestr);
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return success;
	}
	
	public boolean crawlSomeCity(City[] cities, String datestr) {
		boolean success = true;
		for (City city : cities) { 
			try {
				success &= this.crawl(city, datestr);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return success;
	}
	
	public boolean crawlAllCity(String datestr) {
		boolean success = true;
		for (City city : City.values()) {
			if (!city.isUseProxy() || city.getCitycode() > 0) {
				try {
					success &= this.crawl(city, datestr);
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return success;
	}

	public static boolean crawlAllCityAt(int year, int month, int day, int hour, int minutes) {
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
					Spider spider = new Spider();
					return spider.crawlAllCity(datestr);
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
		Spider spider = new Spider();
		spider.crawlAllCity(datestr);
	}
	
	public static void complement() {
		String datestr = "20181221p";
		Spider spider = new Spider();
		City[] cities = {City.KUNMING, City.SHENYANG, City.HAERBIN};
		spider.crawlSomeCity(cities, datestr);
//		City[] cities = {City.BEIJING, City.SHANGHAI, City.GUANGZHOU, City.ZHENGZHOU, City.NANJING};
//		spider.crawlExcludeSomeCity(cities, datestr);
	}

	public static void main(String[] args) {
//		System.err.println(crawlAllCityAt(2018, 12, 18, 15, 3));
//		System.err.println(crawlAllCityAt(2018, 12, 19, 10, 6));
//		System.err.println(crawlAllCityAt(2018, 12, 19, 15, 3));
//		System.err.println(crawlAllCityAt(2018, 12, 20, 9, 2));
//		System.err.println(crawlAllCityAt(2018, 12, 21, 10, 4));
//		System.err.println(crawlAllCityAt(2018, 12, 21, 15, 53));
		System.err.println(crawlAllCityAt(2018, 12, 22, 10, 5));
//		complement();
//		test();

	}

}
