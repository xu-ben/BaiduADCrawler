package ben.tools.crawler.bdadcrawler;

import ben.tools.crawler.Commons;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用正则表达式解析html
 */
@Deprecated
public class RegexParser {

	private File targetFile;

	private static String basePath = "/home/ben/Develop/spider/html/";

	private RegexParser(File targetFile) {
		this.targetFile = targetFile;
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
	
	// a标签的内容里可能有引号，但标签属性里的字符串中不含有<>
	private Pattern aLabelPattern = Pattern.compile("<a([^>]+)(>.+?<)/a>");

	// a标签属性里的字符串中可能含有<>，但标签的内容里没有引号的情况
	private Pattern aLabelPattern2 = Pattern.compile("<a (.+?)(>[^\"]+<)/a>");
//	private Pattern aLabelPattern = Pattern
//			.compile("<a([^>]+)>((([^<>]+)|(<font color=#CC0000>[^<>]+</font>))+)</a>");
	private void parseContentInH3(AD ad, String h3) {
//		System.err.println(h3);
//		System.err.println();
		Matcher mh3 = aLabelPattern.matcher(h3);
		StringBuilder titleHtml = new StringBuilder();
		StringBuilder urlText = new StringBuilder();
		while (mh3.find()) {
			urlText.append(mh3.group(1));
			titleHtml.append(mh3.group(2));
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

	private Pattern h3Pattern = Pattern.compile("<h3[^>]*>(.+?)</h3>");
	private Pattern orgPattern = Pattern.compile("<span[^<>]+>([^<>]+)</span>[^<>]*<span[^<>]+>([^<>]+)</span>");
	private Pattern bodyPattern = Pattern.compile("<div class=\"\">(.+?)</div>");

	public AD parseAAd(String text) {
		AD ad = new AD();
		Matcher h3m = h3Pattern.matcher(text);
		if (h3m.find()) {
			String h3 = h3m.group(1);
			parseContentInH3(ad, h3);
		}
		Matcher orgm = orgPattern.matcher(text);
		if (orgm.find()) {
			String org = orgm.group(1);
			if (org != null) {
				ad.setOrganization(org);
			}
			String dateInPage = orgm.group(2);
			if (dateInPage != null) {
				ad.setDateInPage(dateInPage);
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

	// 更好的办法 <div class="zACHih" style="display:block !important;visibility:visible !important" data-pos="12"></div>
	private Pattern ppimPattern = Pattern.compile("<!-- new pp[^-]* -->([^\r\n]+)\n");
	

	public ArrayList<AD> runParser() throws IOException {
		ArrayList<AD> adlist = null;
		String fileContent = Commons.getTextFromFile(targetFile);
		if (fileContent == null || fileContent.trim().equals("")) {
			throw new FileNotFoundException("this file is empty");
		}
		Matcher matcher = ppimPattern.matcher(fileContent);
		while (matcher.find()) {
			String allADTexts = matcher.group(1);
			adlist = getADlist(allADTexts + "<!--");
		}
		return adlist;
	}

	public static FileNameStruct getAFileNameStruct(City city, String datestr, KeyWord key) throws FileNotFoundException {
		FileNameStruct fns = new FileNameStruct();
		fns.datestr = datestr;
		String dirPath = basePath + city.name().toLowerCase();
//		System.err.println(dirPath);
		File dir = new File(dirPath);
		if (dir == null || !dir.exists() || !dir.isDirectory()) {
			throw new FileNotFoundException();
		}
		String[] fileNameList = dir.list();
		Pattern p = Pattern.compile(key.ordinal() + "_" + datestr + "_(\\d+)\\.html");
		for (String fileName : fileNameList) {
			Matcher m = p.matcher(fileName);
			if (m.matches()) {
				fns.timestamp = Long.parseLong(m.group(1));
				fns.filePath = dirPath + "/" + fileName;
//				System.err.println(m.group(1));
//    			System.out.println(fileName);
				break;
			}
		}
		return fns;
	}

	public static FileNameStruct[] getFileNameStructArr(City city, KeyWord key) throws FileNotFoundException {
		String dirPath = basePath + city.name().toLowerCase();
//		System.err.println(dirPath);
		File dir = new File(dirPath);
		if (dir == null || !dir.exists() || !dir.isDirectory()) {
			throw new FileNotFoundException();
		}
		String[] fileNameList = dir.list();
		Arrays.sort(fileNameList);
		Pattern p = Pattern.compile(key.ordinal() + "_(\\d+\\w)_(\\d+)\\.html");
		ArrayList<FileNameStruct> fnslist = new ArrayList<>();
		for (String fileName : fileNameList) {
			Matcher m = p.matcher(fileName);
			if (m.matches()) {
				FileNameStruct fns = new FileNameStruct();
				fns.datestr = m.group(1);
				fns.timestamp = Long.parseLong(m.group(2));
				fns.filePath = dirPath + "/" + fileName;
				fnslist.add(fns);
			}
		}
		if (fnslist.size() > 0) {
			FileNameStruct[] ret = new FileNameStruct[fnslist.size()];
			return fnslist.toArray(ret);
		} else {
			return null;
		}
	}

	public static ADsInAFile[] findAndParseResultsInACity(City city, KeyWord key) throws IOException {
		FileNameStruct[] fnsarr = getFileNameStructArr(city, key);
		if (fnsarr == null) {
			return null;
		}
		ADsInAFile[] results = new ADsInAFile[fnsarr.length];
		for (int i = 0; i < fnsarr.length; i++) {
			FileNameStruct fns = fnsarr[i];
			results[i] = new ADsInAFile();
			results[i].setFilePath(fns.filePath);
			results[i].setDatestr(fns.datestr);
			results[i].setKeyword(key);
			ArrayList<AD> adlist = new RegexParser(new File(fns.filePath)).runParser();
			fillField(adlist, city, fns.timestamp, fns.datestr);
			results[i].setAdlist(adlist);
		}
		return results;
	}

	/**
	 * @param city
	 * @param datestr
	 * @param key
	 * @throws IOException
	 */
	public static ADsInAFile findAndParseAResultInBase(City city, String datestr, KeyWord key) throws IOException {
		FileNameStruct fns = getAFileNameStruct(city, datestr, key);
		long timestamp = fns.timestamp;
		if (timestamp < 0) {
			return null;
		}
		// System.out.println(fns.filePath);
		ADsInAFile result = new ADsInAFile();
		result.setDatestr(datestr);
		result.setKeyword(key);
		ArrayList<AD> adlist = new RegexParser(new File(fns.filePath)).runParser();
		fillField(adlist, city, timestamp, datestr);
		result.setFilePath(fns.filePath);
		result.setAdlist(adlist);
		return result;
	}


	private static ArrayList<AD> parseResultFile(String filePath) throws IOException {
		if (filePath == null || filePath.trim().equals("")) {
			throw new FileNotFoundException("filePath error");
		}
		File targetFile = new File(filePath);
		if (!targetFile.exists() || !targetFile.isFile()) {
			throw new FileNotFoundException();
		}
		return new RegexParser(targetFile).runParser();
	}
	
	private static void fillField(ArrayList<AD> adlist, City city, long timestamp, String datestr) {
		if (adlist == null) {
			return ;
		}
		for (int i = 0, len = adlist.size(); i < len; i++) {
			AD ad = adlist.get(i);
			ad.setRank(i + 1);
			ad.setCity(city.name().toLowerCase());
			ad.setTimestamp(timestamp);
			ad.setAccessDatestr(datestr);
		}
	}

	public static ArrayList<AD> parseAFile(String filePath, City accessCity, String datestr) throws IOException {
		ArrayList<AD> adlist = parseResultFile(filePath);
		fillField(adlist, accessCity, -1, datestr);
		return adlist;
	}
	
}
