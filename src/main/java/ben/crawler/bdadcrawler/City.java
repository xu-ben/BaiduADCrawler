package ben.crawler.bdadcrawler;

public enum City {
	BEIJING(110000, 110105, false),
	SHANGHAI(310000, 310112),
	GUANGZHOU(440000, 440100),
	SHENZHEN(440000, -1),
	ZHENGZHOU(410000, 410600),//用鹤壁代替
	NANJING(320000, 320100),
	FUZHOU(350000, 350400),//用三明代替
	HEFEI(340000, 340100),
	CHANGSHA(430000, 430600),//用岳阳代替
	WUHAN(420000, -1),//用十堰420800代替
	HANGZHOU(330000, 330100),
	CHENGDU(510000, 510600),//用德阳代替510100
	CHONGQING(500000, 500300),
	KUNMING(530000, 530400),//用玉溪代替
	SHENYANG(210000, 210100),
	HAERBIN(230000, -1);//230100 暂时没有ip了
	

	private int procode;
	
	private int citycode;
	
	// 意思是运行爬虫的机器就在此城市, 不必使用代理
	private boolean useProxy; 

	City(int procode, int citycode) {
		this.procode = procode;
		this.citycode = citycode;
		this.useProxy = true;
	}

	City(int procode, int citycode, boolean useProxy) {
		this.procode = procode;
		this.citycode = citycode;
		this.useProxy = useProxy;
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

	public int getProcode() {
		return procode;
	}

	public int getCitycode() {
		return citycode;
	}

	public boolean isUseProxy() {
		return useProxy;
	}
	
}
