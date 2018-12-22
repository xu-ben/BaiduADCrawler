package ben.tools.crawler.bdadcrawler;

public enum KeyWord {
	FENG_XIONG("丰胸"),
	LONG_XIONG("隆胸");
	
	private String str;
	
	private KeyWord(String str) {
		this.str = str;
	}

	public String getStr() {
		return str;
	}
	
}
