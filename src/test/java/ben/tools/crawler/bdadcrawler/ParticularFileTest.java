package ben.tools.crawler.bdadcrawler;

import org.junit.Test;
import java.io.IOException;
import java.util.ArrayList;

public class ParticularFileTest {

    private static String basePath = "/home/ben/Develop/spider/";

    @Test
    public void test0() throws IOException {
        String file = "20181213p_.html";
        ArrayList<AD> adlist = Parser.parseAFile(basePath + file, City.BEIJING, "20181213p");
        for (AD ad : adlist) {
            Displayer.displaySimply(ad);
        }
    }

    @Test
    public void test1() throws IOException {
        String file = "html/haerbin/0_20181217a_1545008406917.html";
        ArrayList<AD> adlist = Parser.parseAFile(basePath + file, City.HAERBIN, "20181217a");
        for (AD ad : adlist) {
            Displayer.displaySimply(ad);
        }
    }

    @Test
    public void test2() throws IOException {
		String file = "html/beijing/1_20181219a_1545185157206.html";
//		String file = "html/beijing/0_20181219a_1545185155911.html";
        ArrayList<AD> adlist = Parser.parseAFile(basePath + file, City.BEIJING, "20181219a");
        for (AD ad : adlist) {
            Displayer.displaySimply(ad);
        }
    }

    @Test
    public void test3() throws IOException { // todo
        String file = "html/hefei/0_20181219a_1545185188303.origin.html";
        ArrayList<AD> adlist = Parser.parseAFile(basePath + file, City.HEFEI, "20181219a");
        for (AD ad : adlist) {
            Displayer.displaySimply(ad);
        }
    }

    //todo
//    String file = "html/beijing/0_20181214p_1544790833628.html"; 4, body

}
