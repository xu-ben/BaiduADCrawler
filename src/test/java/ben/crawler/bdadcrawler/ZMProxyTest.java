package ben.crawler.bdadcrawler;

import ben.crawler.proxy.ZMCityCode;
import ben.crawler.proxy.ZMProxy;
import cn.xuben.net.WebClientAgent;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.net.Proxy;
import java.util.Base64;

public class ZMProxyTest {

    public void test() {
        try {
            ZMProxy zmProxy = new ZMProxy(true);
            zmProxy.close();
//			zmProxy.fetchProxyFromServer(City.SHANGHAI);
//			Document doc = Jsoup.connect("http://www.gov.cn").get();
//			System.out.println(doc.outerHtml());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testGetProxy() throws IOException {
        ZMProxy zmProxy = new ZMProxy(true);
        Proxy proxy = zmProxy.fetchProxyFromServer(ZMCityCode.getZMCityCode(City.SHANGHAI));
        System.err.println(proxy);
    }


    @Test
    public void testWebClient() throws IOException {
        WebClientAgent conn = WebClientAgent.getInstance();
        StringBuilder content = conn.doGetSimply("https://www.jd.com", "utf-8");
        System.err.println(content);
    }




}
