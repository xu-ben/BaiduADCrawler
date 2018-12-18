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
		System.out.println("rank:\t" + ad.getRank());
		System.out.println("title:\t" + ad.getTitle());
		System.out.println("org:\t" + ad.getOrganization());
		System.out.println("date in page:\t" + ad.getDateInPage());
		System.out.println("url:\t" + ad.getUrl());
		System.out.println("body:\t" + ad.getContext());
		System.out.println("access location:\t" + ad.getCity());
		System.out.println("access date:\t" + ad.getAccessDatestr());
		System.out.println("access timestamp:\t" + ad.getTimestamp());
		System.out.println();
		System.out.println();
	}

	
	public static void test1() {
		String basepath = "/home/ben/Develop/spider/";
//		String file = "html/haerbin/0_20181217a_1545008406917.html";
		String file = "20181213p_.html";
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
			ArrayList<AD> adlist = Parser.parseResultInBase(City.BEIJING, "20181218p", KeyWords.FENGXIONG);
			for (AD ad : adlist) {
				fullDisplay(ad);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
//		test1();
		test2();
	}

}
