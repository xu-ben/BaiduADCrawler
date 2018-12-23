package ben.tools.crawler;

import ben.tools.crawler.bdadcrawler.City;
import com.google.gson.Gson;

import java.io.IOException;

public class ZMProxy {

	private boolean useFreeApi = true;

	public ZMProxy(boolean useFreeApi) {
		this.useFreeApi = useFreeApi;
	}

	/**
	 * 调用芝麻代理api返回的结果
	 */
	private class Result {
		String msg;
		boolean success;
		int code;
	}

	private static String FREE_URL = "http://webapi.http.zhimacangku.com/getip?num=1&type=1&pro=%d&city=%d&yys=0&port=1&pack=37000&ts=0&ys=0&cs=0&lb=4&sb=0&pb=4&mr=1&regions=";

	private static String FIVE_URL = "http://webapi.http.zhimacangku.com/getip?num=1&type=1&pro=%d&city=%d&yys=0&port=1&time=1&ts=0&ys=0&cs=0&lb=4&sb=0&pb=4&mr=1&regions=";

	public String fetchProxyFromServer(City city) throws IOException {
	    if (useFreeApi) {
			return fetchProxyFromServer(FREE_URL, city, 3);
		} else {
			return fetchProxyFromServer(FIVE_URL, city, 3);
		}
	}

	private String fetchProxyFromServer(String baseurl, City city, int mosttry) throws IOException {
		String url = String.format(baseurl, city.getProcode(), city.getCitycode());
		String cmd = String.format("curl -A \"%s\" \"%s\"", Commons.chromeUserAgent, url);
		for (int i = 0; i < mosttry; i++) { // 最多尝试mosttry次
			System.err.println(cmd);
			String proxy = Commons.execCmdInDir(cmd, ".", 15);
			if (proxy.length() > 0 && proxy.charAt(0) == '{') {
				Gson gson = new Gson();
				Result res = gson.fromJson(proxy, Result.class);
				System.out.println(res.msg);
				// todo 添加更多逻辑
				if (res.msg.equals("您的该套餐已过期!")) {
					return null;
				}
			} else if (proxy.matches("\\s*\\d+\\.\\d+\\.\\d+\\.\\d+:\\d+\\s*")) {
				System.err.println(proxy);
				return proxy.trim();
			} else {
				System.err.println(proxy);
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public void fetchAllProxysAndPrint() throws IOException {
		City[] cities = City.values();
		String[] res = new String[cities.length];
		for (int i = 0; i < cities.length; i++) {
			City city = cities[i];
			if (city.isUseProxy()) {
				res[i] = fetchProxyFromServer(city);
			}
		}
		for (int i = 0; i < cities.length; i++) {
			System.out.println(res[i]);
		}
		System.out.println('\n');
		for (int i = 0; i < cities.length; i++) {
			System.out.println(cities[i].name() + "," + res[i]);
		}
	}

	public void test() {
		try {
			fetchProxyFromServer(City.CHANGSHA);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			// System.out.println(Commons.execCmdInDir("ping www.baidu.com",
			// ".", 10));
			System.out.println(Commons.execCmdInDir("ls", ".", 10));
			// fetchAllProxysAndPrint();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		
	}

}
