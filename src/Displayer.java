import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Displayer {

	public static void simpleDisplay(AD ad) {
		System.out.println(ad.getRank());
		System.out.println(ad.getTitle());
		System.out.println(ad.getOrganization());
		System.out.println(ad.getDateInPage());
		System.out.println(ad.getUrl());
		System.out.println(ad.getContext());
		System.out.println();
		System.out.println();
	}

	public static void fullDisplay(AD ad) {
		System.out.println("////////////////////////////////////////");
		System.out.println("rank:\t" + ad.getRank());
		System.out.println("title:\t" + ad.getTitle());
		System.out.println("org:\t" + ad.getOrganization());
		System.out.println("date in page:\t" + ad.getDateInPage());
		System.out.println("url:\t" + ad.getUrl());
		System.out.println("body:\t" + ad.getContext());
		System.out.println("access location:\t" + ad.getCity());
		System.out.println("access date:\t" + ad.getAccessDatestr());
		System.out.println("access timestamp:\t" + ad.getTimestamp());
	}

	
	/**
	 * 把某个时间，指定城市的结果输出
	 * @param city
	 * @param datestr
	 * @throws IOException 
	 */
	public static void displayAtACityAndOnADate(City city, String datestr) throws IOException {
		for (KeyWords key : KeyWords.values()) {
			System.out.println("////////////////////////////////////////////////////////////////////////////////");
			System.out.println("City: " + city.name().toLowerCase() + "\tKeyword: " + key.getStr());
			ArrayList<AD> adlist = Parser.parseResultInBase(city, datestr, key);
			if (adlist != null) {
				for (AD ad : adlist) {
					fullDisplay(ad);
				}
			} else {
				System.out.println("no ADs found");
			}
			System.out.println("////////////////////////////////////////////////////////////////////////////////\n\n");
		}
	}

	
	public static void test1() {
		String basepath = "/home/ben/Develop/spider/";
//		String file = "html/haerbin/0_20181217a_1545008406917.html";
//		String file = "20181213p_.html";
		String file = "html/hefei/0_20181219a_1545185188303.origin.html";
		try {
			ArrayList<AD> adlist = Parser.parseAFile(basepath + file, City.BEIJING, "20181213p");
			for (AD ad : adlist) {
				simpleDisplay(ad);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void test2() {
		try {
			displayAtACityAndOnADate(City.HEFEI, "20181219a");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void test3() {
		try {
			for (City city : City.values()) {
				try {
					displayAtACityAndOnADate(city, "20181219a");
				} catch (FileNotFoundException e) {
					System.out.println("no result file found");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
//		test1();
//		test2();
		test3();
	}

}
