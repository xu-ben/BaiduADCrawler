package ben.crawler.bdadcrawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import java.io.IOException;

public class TestJsoup {

	@Test
	public void test() {
		try {
			Document doc = Jsoup.connect("http://www.gov.cn").get();
			System.out.println(doc.outerHtml());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
