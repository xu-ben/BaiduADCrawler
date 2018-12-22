package ben.tools.crawler.bdadcrawler;

public enum KeyWord {
	FENGXIONG("丰胸"),
	LONGXIONG("隆胸");
	
	private String str;
	
	private KeyWord(String str) {
		this.str = str;
	}

	public String getStr() {
		return str;
	}
	
}
