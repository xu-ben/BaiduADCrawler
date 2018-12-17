
public enum City {
	BEIJING(110000, 110105),
	SHANGHAI(310000, 310112),
	GUANGZHOU(440000, 440100),
	SHENZHEN(440000, -1),
	ZHENGZHOU(410000, -1),
	NANJING(320000, 320100),
	FUZHOU(350000, -1),
	HEFEI(340000, 340100),
	CHANGSHA(430000, -1),
	WUHAN(420000, 420800),//用十yan的
	HANGZHOU(330000, 330100),
	CHENGDU(371200, 510100),
	CHONGQING(500000, 500300),
	KUNMING(530000, 530900),//用玉xi的
	SHENYANG(210000, 210100),
	HAERBIN(230000, 230100);
	

	private int procode;
	
	private int citycode;

	private City(int procode, int citycode) {
		this.procode = procode;
		this.citycode = citycode;
	}

	public int getProcode() {
		return procode;
	}

	public int getCitycode() {
		return citycode;
	}

	
}
