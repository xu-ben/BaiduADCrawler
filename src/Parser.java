import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

	private File targetFile;
	
	private City accessCity;
	
	private Parser(File targetFile, City accessCity) {
		this.targetFile = targetFile;
		this.accessCity = accessCity;
	}

	private Pattern urlPattern = Pattern.compile("data-landurl=[\"]([^\"]+)[\"]");

	private void parseURL(AD ad, StringBuilder urlText) {
		Matcher m = urlPattern.matcher(urlText);
		StringBuilder urlsb = new StringBuilder();
		while (m.find()) {
			urlsb.append(m.group(1));
		}
		ad.setUrl(urlsb.toString());
//		System.err.println(urlsb);
	}
	
	
	// 一般顔色是＃CC0000
	private Pattern textPattern = Pattern.compile("((?<=>)[^<>]+)|(<font[^>]+>([^<>]+)</font>)");
	private StringBuilder getTextFromFontHtml(StringBuilder html) {
		Matcher m = textPattern.matcher(html);
		StringBuilder text = new StringBuilder();
		while (m.find()) {
			if (m.group(1) != null) {
				text.append(m.group(1));
			}
			if (m.group(3) != null) {
				text.append(m.group(3));
			}
		}
		return text;
	}
	
	public StringBuilder getHtmlContextInALabel(String html) {
		Matcher matcher = aLabelPattern.matcher(html);
		StringBuilder context = new StringBuilder();
		while (matcher.find()) {
			context.append(matcher.group(2));
		}
		return context;
	}
	
	public StringBuilder getTextInALabel(String html) {
		Matcher matcher = aLabelPattern.matcher(html);
		StringBuilder context = new StringBuilder();
		while (matcher.find()) {
			context.append(matcher.group(2));
		}
		return getTextFromFontHtml(context);
	}

	private Pattern aLabelPattern = Pattern.compile("<a([^>]+)(>.+?<)/a>");
//	private Pattern aLabelPattern = Pattern
//			.compile("<a([^>]+)>((([^<>]+)|(<font color=#CC0000>[^<>]+</font>))+)</a>");
	private void parseh3(AD ad, String h3) {
		Matcher matcher = aLabelPattern.matcher(h3);
		StringBuilder titleHtml = new StringBuilder();
		StringBuilder urlText = new StringBuilder();
		while (matcher.find()) {
			urlText.append(matcher.group(1));
			titleHtml.append(matcher.group(2));
		}
		StringBuilder titleText = getTextFromFontHtml(titleHtml);
		ad.setTitle(titleText == null ? null : titleText.toString());
		// System.out.println(titleHtml);
		parseURL(ad, urlText);
	}
	
	
	private void parseBody(AD ad, String body) {
//		System.out.println(body);
		StringBuilder bodyText = getTextInALabel(body);
		ad.setContext(bodyText == null ? null : bodyText.toString());
	}

	private Pattern h3Pattern = Pattern.compile("<h3(.+?)</h3>");
	private Pattern orgPattern = Pattern.compile("<span[^<>]+>([^<>]+)</span>[^<>]*<span[^<>]+>([^<>]+)</span>");
	private Pattern bodyPattern = Pattern.compile("<div class=\"\">(.+?)</div>");

	public AD parseAAd(String text) {
		AD ad = new AD();
		Matcher h3m = h3Pattern.matcher(text);
		if (h3m.find()) {
			String h3 = h3m.group(0);
			parseh3(ad, h3);
		}
		Matcher orgm = orgPattern.matcher(text);
		if (orgm.find()) {
			String org = orgm.group(1);
			if (org != null) {
				ad.setOrganization(org);
			}
			String datestr = orgm.group(2);
			if (datestr != null) {
				ad.setDatestr(datestr);
			}
		}
		Matcher bodym = bodyPattern.matcher(text);
		if (bodym.find()) {
			String body = bodym.group(1);
			parseBody(ad, body);
		}
		return ad;
	}

	private Pattern jieouPattern = Pattern.compile("<!-- pc jieou new -->(.+?)(?=<!--)");

	public ArrayList<AD> getADlist(String text) {
		ArrayList<AD> adlist = new ArrayList<AD>();
		Matcher matcher = jieouPattern.matcher(text);
		// int t = 0;
		while (matcher.find()) {
			String adtext = matcher.group(1);
			// System.err.println("@@@@@@@@@@@@@@@@@@@@@@@@@@" + t++);
			// System.err.println(adtext);
			// System.err.println();
			// System.err.println();
			adlist.add(parseAAd(adtext));
		}
		return adlist;
	}

	private Pattern ppimPattern = Pattern.compile("<!-- new ppim -->([^\r\n]+)\n");

	public ArrayList<AD> runParser() throws IOException {
		ArrayList<AD> adlist = null;
		Matcher matcher = ppimPattern.matcher(Commons.getTextFromFile(targetFile));
		while (matcher.find()) {
			String allADTexts = matcher.group(1);
			adlist = getADlist(allADTexts + "<!--");
		}
		for (int i = 0, len = adlist.size(); i < len; i++) {
			adlist.get(i).setRank(i + 1);
		}
		return adlist;
	}
	
	public static ArrayList<AD> parseAFile(String filePath, City accessCity) throws IOException {
		if (filePath == null || filePath.trim().equals("")) {
			throw new FileNotFoundException("filePath required");
		}
		File targetFile = new File(filePath);
		if (!targetFile.exists() || !targetFile.isFile()) {
			throw new FileNotFoundException();
		}
		Parser parser = new Parser(targetFile, accessCity);
		return parser.runParser();
	}
	
	public static ArrayList<AD> parseAResultFile(City city) {
		final String basePath = "/home/ben/Develop/spider/html/";
		String filePath = basePath + city.name() + "";
		return null;
	}

}
