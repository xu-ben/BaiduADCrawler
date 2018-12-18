
public class AD {
	
	private int rank;
	
	private String city;

	private String title;
	
	private String organization;
	
	private String context;
	
	private String url;
	
	private String datestr;
	
	private long timestamp;
	
	public AD() {
		this.rank = 0;
		this.city = null;
		this.title = null;
		this.organization = null;
		this.context = null;
		this.url = null;
		this.datestr = null;
		this.timestamp = 0;
	}

	public AD(int rank, String city, String title, String organization, String context, String url, String datestr,
			long timestamp) {
		this.rank = rank;
		this.city = city;
		this.title = title;
		this.organization = organization;
		this.context = context;
		this.url = url;
		this.datestr = datestr;
		this.timestamp = timestamp;
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

	public String getDatestr() {
		return datestr;
	}

	public void setDatestr(String datestr) {
		this.datestr = datestr;
	}

	
}
