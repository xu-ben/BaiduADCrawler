package ben.tools.crawler.bdadcrawler;

import ben.tools.crawler.Commons;
import ben.tools.crawler.ZMProxy;

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
	
}
