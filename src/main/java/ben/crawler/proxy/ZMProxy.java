package ben.crawler.proxy;

import ben.crawler.Commons;
import ben.crawler.bdadcrawler.City;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZMProxy {

    private static Logger logger = Logger.getLogger(ZMProxy.class.getName());

    private static String appkey = ResourceBundle.getBundle("config").getString("zmproxy.appKey");

    private static String neek = ResourceBundle.getBundle("config").getString("zmproxy.neek");

    private static String CN_API_BASE = "http://web.http.cnapi.cc/index/index/";

    private static String GET_IP_BASE = "http://webapi.http.zhimacangku.com/getip?";

    private static String FREE_URL = GET_IP_BASE + "num=1&type=1&pro=%d&city=%d&yys=0&port=1&pack=37000&ts=0&ys=0&cs=0&lb=4&sb=0&pb=4&mr=1&regions=";

    private static String FIVE_URL = GET_IP_BASE + "num=1&type=1&pro=%d&city=%d&yys=0&port=1&time=1&ts=0&ys=0&cs=0&lb=4&sb=0&pb=4&mr=1&regions=";

    private static String ADD_WHITE_LIST = CN_API_BASE + "save_white" + "?neek=" + neek + "&appkey=" + appkey + "&white=";

    private static String DEL_WHITE_LIST = CN_API_BASE + "del_white" + "?neek=" + neek + "&appkey=" + appkey + "&white=";

    private boolean useFreeApi;

    public ZMProxy(boolean useFreeApi) {
        this.useFreeApi = useFreeApi;
    }

    /**
     * 调用芝麻代理api返回的结果
     */
    private class Result {
        String msg;
        boolean success;
        int code;
    }

    public String fetchProxyFromServer(City city) throws IOException {
        if (useFreeApi) {
            return fetchProxyFromServer(FREE_URL, city, 3);
        } else {
            return fetchProxyFromServer(FIVE_URL, city, 3);
        }
    }

    private static Pattern pWhiteList = Pattern.compile("请将(\\d+\\.\\d+\\.\\d+\\.\\d+)设置为白名单！");

    /**
     * @param str
     * @return true如果可以再次尝试; false不用再试了
     */
    private boolean treatResult(String str) throws IOException {
        Gson gson = new Gson();
        Result res = gson.fromJson(str, Result.class);
        logger.info(res.msg);
        if (res.msg.equals("您的该套餐已过期!")) {
            return false;
        }
        if (res.msg.equals("请更换条件再试!")) {
            return true;
        }
        Matcher m = pWhiteList.matcher(res.msg);
        if (m.matches()) {
            // todo
            String url = ADD_WHITE_LIST + m.group(1);
            String cmd = String.format("curl -A \"%s\" \"%s\"", Commons.chromeUserAgent, url);
            logger.info(cmd);
            String ret = Commons.execCmdInDir(cmd, ".", 15);
            return false;
        }
        // todo 添加更多逻辑
        return true;
    }

    private String fetchProxyFromServer(String baseurl, City city, int mosttry) throws IOException {
        String url = String.format(baseurl, city.getProcode(), city.getCitycode());
        String cmd = String.format("curl -A \"%s\" \"%s\"", Commons.chromeUserAgent, url);
        for (int i = 0; i < mosttry; i++) { // 最多尝试mosttry次
            logger.info(cmd);
            String res = Commons.execCmdInDir(cmd, ".", 15);
            if (res == null || res.length() == 0) { // null说明超时了, 为空也不正常
                continue;
            }
            logger.info(res);
            if (res.charAt(0) == '{') {
                if (!treatResult(res)) {
                    return null;
                }
            } else if (res.matches("\\s*\\d+\\.\\d+\\.\\d+\\.\\d+:\\d+\\s*")) {
                return res.trim();
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void fetchAllProxysAndPrint() throws IOException {
        City[] cities = City.values();
        String[] res = new String[cities.length];
        for (int i = 0; i < cities.length; i++) {
            City city = cities[i];
            if (city.isUseProxy()) {
                res[i] = fetchProxyFromServer(city);
            }
        }
        for (int i = 0; i < cities.length; i++) {
            System.out.println(res[i]);
        }
        System.out.println('\n');
        for (int i = 0; i < cities.length; i++) {
            System.out.println(cities[i].name() + "," + res[i]);
        }
    }

}
