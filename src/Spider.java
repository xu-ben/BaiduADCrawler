import java.io.IOException;
import java.util.Date;

public class Spider {

	private String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36";

	private String bdurl = "https://www.baidu.com/s?ie=utf-8&wd=";

	private String[] keys = { "丰胸", "隆胸" };
	
	private void crawlPageToFile(String cityname, String date, String proxy, int keyindex) {
		String rootdir = String.format("/home/ben/Develop/spider/html/%s", cityname);
		long time = System.currentTimeMillis();
		final String basestr = "curl -A \"%s\" -x %s \"%s%s\" > %d_%s_%d.html";
		String cmd = String.format(basestr, userAgent, proxy, bdurl, keys[keyindex], keyindex, date, time);
		System.err.println(cmd);
		try {
			String ret = Commons.ExecCmdInDir(cmd, rootdir);
			if (ret != null && !ret.trim().equals("")) {
				System.err.println(ret);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public String getProxyStr(City city) throws IOException {
		String rootdir = "/home/ben/Develop/spider/html";
		String baseurl = "http://webapi.http.zhimacangku.com/getip?num=1&type=1&pro=%d&city=%d&yys=0&port=1&pack=37000&ts=0&ys=0&cs=0&lb=4&sb=0&pb=45&mr=1&regions=";
		String url = String.format(baseurl, city.getProcode(), city.getCitycode());
		String cmd = String.format("curl -A \"%s\" \"%s\"", userAgent, url);
		System.err.println(cmd);
		String ip = Commons.ExecCmdInDir(cmd, rootdir).trim();
		System.err.println(ip);
		if (!ip.matches("\\d+\\.\\d+\\.\\d+\\.\\d+:\\d+")) {
			return null;
		}
		return ip;
	}

	public boolean crawl(City city, String date) throws IOException {
		System.err.println(city.name().toLowerCase());
		String proxy = getProxyStr(city);
		boolean ret = false;
		if (proxy != null) {
			String citystr = city.name().toLowerCase();
			crawlPageToFile(citystr, date, proxy, 0);
			crawlPageToFile(citystr, date, proxy, 1);
			ret = true;
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
	
	
	public boolean crawlAllCity(String datestr) {
		boolean success = true;
		for (City city : City.values()) {
			if (city.getCitycode() > 0) {
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
//					Spider spider = new Spider();
//					return spider.crawlAllCity(datestr);
//					Thread.sleep(5000);
					return true;
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
		String datestr = "20181217p";
		Spider spider = new Spider();
		spider.crawlAllCity(datestr);
	}

	public static void main(String[] args) {
		System.err.println(crawlAllCityAt(2018, 12, 17, 17, 30));

//		test();
//		Spider spider = new Spider();
//		Date date = new Date(System.currentTimeMillis());
//		System.out.println(spider.getDatestr(date));
//		spider.getProxyStr(City.GUANGZHOU);
//		spider.crawl(City.SHANGHAI, "20181214p");
		// spider.crawl(City.BEIJING.name().toLowerCase(), "20181214p");
		// for (City c : City.values()) {
		// spider.crawl(c.name().toLowerCase(), "20181214p");
		// System.out.println(c.name());
		// System.out.println(c.name().toLowerCase());
		// System.out.println(c.ordinal());
		// }

	}

}
