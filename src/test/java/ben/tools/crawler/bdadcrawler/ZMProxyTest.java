package ben.tools.crawler.bdadcrawler;

import ben.tools.crawler.ZMProxy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.IOException;

public class ZMProxyTest {

	@Test
	public void test() {
		try {
			ZMProxy zmProxy = new ZMProxy(true);
			zmProxy.fetchProxyFromServer(City.SHANGHAI);
//			Document doc = Jsoup.connect("http://www.gov.cn").get();
//			System.out.println(doc.outerHtml());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
