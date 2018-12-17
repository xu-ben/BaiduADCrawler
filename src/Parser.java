import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

	private File htmlFile;

	Parser(String filePath) throws FileNotFoundException {
		if (filePath == null) {
			throw new FileNotFoundException("filePath must not be null");
		}
		htmlFile = new File(filePath);
		if (!htmlFile.exists()) {
			throw new FileNotFoundException();
		}
	}

	private Pattern urlPattern = Pattern.compile("data-landurl=[\"]([^\"]+)[\"]");

	public void parseURL(AD ad, StringBuilder urlText) {
		Matcher m = urlPattern.matcher(urlText);
		StringBuilder urlsb = new StringBuilder();
		while (m.find()) {
			urlsb.append(m.group(1));
		}
		ad.setUrl(urlsb.toString());
//		System.err.println(urlsb);
//		System.err.println();
//		System.err.println();
	}

	private Pattern titleTextPattern = Pattern.compile("([^<>]+)|(<font[^>]+>([^<>]+)</font>)");

	public void parseTitle(AD ad, StringBuilder titleHtml) {
		Matcher m = titleTextPattern.matcher(titleHtml);
		StringBuilder title = new StringBuilder();
		while (m.find()) {
			if (m.group(1) != null) {
				title.append(m.group(1));
			}
			if (m.group(3) != null) {
				title.append(m.group(3));
			}
		}
		ad.setTitle(title.toString());
	}

	private Pattern titleHtmlPattern = Pattern
			.compile("<a([^>]+)>((([^<>]+)|(<font color=#CC0000>[^<>]+</font>))+)</a>");

	public void parseh3(AD ad, String h3) {
		Matcher matcher = titleHtmlPattern.matcher(h3);
		StringBuilder titleHtml = new StringBuilder();
		StringBuilder urlText = new StringBuilder();
		while (matcher.find()) {
			urlText.append(matcher.group(1));
			titleHtml.append(matcher.group(2));
		}
		parseTitle(ad, titleHtml);
		// System.out.println(titleHtml);
		parseURL(ad, urlText);
	}

	private Pattern h3Pattern = Pattern.compile("<h3(.+?)</h3>");

	public AD parseAAd(String text) {
		AD ad = new AD();
		Matcher matcher = h3Pattern.matcher(text);
		if (matcher.find()) {
			String h3 = matcher.group(0);
			parseh3(ad, h3);
			if (ad.getTitle() == null) {
				ad.setContext(h3);
			}
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

	public void runParser() {
		ArrayList<AD> adlist = null;
		try {
			Matcher matcher = ppimPattern.matcher(Commons.getTextFromFile(htmlFile));
			while (matcher.find()) {
				String text = matcher.group(1);
				// System.out.println(text);
				adlist = getADlist(text + "<!--");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// todo display
		for (int i = 0, len = adlist.size(); i < len; i++) {
			adlist.get(i).setRank(i + 1);
			displayAD(adlist.get(i));
		}
	}

	public static void displayAD(AD ad) {
		String title = ad.getTitle();
		if (title == null || title.trim().equals("")) {
			title = ad.getContext();
		}
		System.out.println(ad.getRank());
		System.out.println(title);
		System.out.println(ad.getUrl());
		System.out.println();
		System.out.println();
	}

	public static void main(String[] args) {
		String basepath = "/home/ben/Develop/spider/";
		String file = "html/haerbin/0_20181217a_1545008406917.html";
		try {
			Parser p = new Parser(basepath + file);
			p.runParser();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
