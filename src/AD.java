
public class AD {
	
	private int rank;
	
	private String city;

	private String title;
	
	private String organization;
	
	private String context;
	
	private String url;
	
	private String dateInPage;
	
	private String accessDatestr;
	
	private long timestamp;
	
	public AD() {
		this.rank = 0;
		this.city = null;
		this.title = null;
		this.organization = null;
		this.context = null;
		this.url = null;
		this.dateInPage = null;
		this.timestamp = 0;
	}


	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDateInPage() {
		return dateInPage;
	}

	public void setDateInPage(String dateInPage) {
		this.dateInPage = dateInPage;
	}

	public String getAccessDatestr() {
		return accessDatestr;
	}

	public void setAccessDatestr(String accessDatestr) {
		this.accessDatestr = accessDatestr;
	}

	
}
