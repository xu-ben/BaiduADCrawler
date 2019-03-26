package ben.crawler.bdadcrawler;

public enum City {
	BEIJING(true),
	SHANGHAI,
	GUANGZHOU,
	SHENZHEN(),
	ZHENGZHOU,
	NANJING,
	FUZHOU,
	HEFEI,
	CHANGSHA,
	WUHAN,
	HANGZHOU,
	CHENGDU,
	CHONGQING,
	KUNMING,
	SHENYANG,
	HAERBIN();
	

	// 意思是运行爬虫的机器就在此城市, 不必使用代理
    private boolean isLocal;

	City() {
		this.isLocal = false;
	}

	City(boolean isLocal) {
		this.isLocal = isLocal;
	}

	/**
	 * 可以通过各种代理网站拿到代理ip的城市
	 * @return
	 */
	public static City[] getAllProxiableCities() {
		final City[] cities = {City.SHENZHEN, City.HAERBIN};
		return City.getAllCitiesExclude(cities);
    }

	public static City[] getAllCitiesExclude(City[] cities) {
		City[] all = City.values();
	    if (cities == null || cities.length == 0) {
	        return all;
		}
		City[] ret = new City[all.length - cities.length];
	    for (int i = 0, I = 0; i < all.length; i++) {
	        int j = 0;
	        for (; j < cities.length; j++) {
	            if (all[i] == cities[j]) {
	                break;
				}
			}
			if (j == cities.length) {
				ret[I++] = all[i];
			}
		}
		return ret;
	}

	public boolean isLocal() {
		return this.isLocal;
	}

}
