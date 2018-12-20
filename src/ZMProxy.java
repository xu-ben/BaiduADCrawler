import java.io.IOException;

public class ZMProxy {

	private static String FREE_URL = "http://webapi.http.zhimacangku.com/getip?num=1&type=1&pro=%d&city=%d&yys=0&port=1&pack=37000&ts=0&ys=0&cs=0&lb=4&sb=0&pb=4&mr=3&regions=";

	private static String FIVE_URL = "http://webapi.http.zhimacangku.com/getip?num=1&type=1&pro=%d&city=%d&yys=0&port=1&time=1&ts=0&ys=0&cs=0&lb=4&sb=0&pb=4&mr=3&regions=";

	public static String fetchProxyFromServer(City city) throws IOException {
		return fetchProxyFromServer(FREE_URL, city);
	}

	public String fetchProxyFromServer2(City city) throws IOException {
		return fetchProxyFromServer(FIVE_URL, city);
	}

	private static String fetchProxyFromServer(String baseurl, City city) throws IOException {
		String url = String.format(baseurl, city.getProcode(), city.getCitycode());
		String cmd = String.format("curl -A \"%s\" \"%s\"", Commons.chromeUserAgent, url);
		for (int i = 0; i < 5; i++) { // 最多尝试5次
			System.err.println(cmd);
			String proxy = Commons.execCmdInDir(cmd, ".", 5);
			if (proxy.matches("\\d+\\.\\d+\\.\\d+\\.\\d+:\\d+")) {
				System.err.println(proxy);
				return proxy;
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void fetchAllProxysAndPrint() throws IOException {
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

	public static void test() {
		try {
			fetchProxyFromServer(City.CHANGSHA);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			// System.out.println(Commons.execCmdInDir("ping www.baidu.com",
			// ".", 10));
			System.out.println(Commons.execCmdInDir("ls", ".", 10));
			// fetchAllProxysAndPrint();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
