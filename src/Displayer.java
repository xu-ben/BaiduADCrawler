import java.io.IOException;
import java.util.ArrayList;

public class Displayer {

	public static void displayAD(AD ad) {
		System.out.println("rank:\t" + ad.getRank());
		System.out.println("title:\t" + ad.getTitle());
		System.out.println("org:\t" + ad.getOrganization());
		System.out.println("date:\t" + ad.getDatestr());
		System.out.println("url:\t" + ad.getUrl());
		System.out.println("body:\t" + ad.getContext());
		System.out.println();
		System.out.println();
	}

	
	public static void test() {
		String basepath = "/home/ben/Develop/spider/";
		String file = "html/haerbin/0_20181217a_1545008406917.html";
//		String file = "20181213p_.html";
		try {
			Parser p = new Parser(basepath + file);
			ArrayList<AD> adlist = p.runParser();
			for (AD ad : adlist) {
				displayAD(ad);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		test();
	}

}
