package ben.tools.crawler.bdadcrawler;

import ben.tools.crawler.Commons;
import ben.tools.crawler.ZMProxy;

import java.io.IOException;

public class Spider {

	private ZMProxy zmProxy = null;

	private String dataDirPath = null;

	public Spider(ZMProxy zmProxy, String dataDirPath) {
		this.zmProxy = zmProxy;
		this.dataDirPath = dataDirPath;
	}

	private static String bdurl = "https://www.baidu.com/s?ie=utf-8&wd=";

	private void crawlPageToFile(String cityname, String date, String proxy, KeyWord key) {
		String rootdir = dataDirPath + "/" + cityname;
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
		String proxy = city.isUseProxy() ? zmProxy.fetchProxyFromServer(city) : null;
		boolean ret = false;
		if (!city.isUseProxy() || proxy != null) {
			String citystr = city.name().toLowerCase();
			for (KeyWord key : KeyWord.values()) {
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
