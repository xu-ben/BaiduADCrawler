package ben.crawler.proxy;

import ben.crawler.bdadcrawler.City;

/**
 * 芝麻代理的城市编号，通过编号，查api可得到代p
 */
public enum ZMCityCode {

    BEIJING(110000, 110105),

    SHANGHAI(310000, 310112),

    /**
     * 广东
     */
    GUANGZHOU(440000, 440100),
    SHENZHEN(440000, -1),

    /**
     * 河南
     */
    ZHENGZHOU(410000, 410600), //410100
    // 鹤壁
    HEBI(410000, 410600),

    /**
     * 江苏
     */
    NANJING(320000, 320100),

    /**
     * 福建
     */
    FUZHOU(350000, 350400), // 350100
    // 三明
    SANMING(350000, 350400),

    /**
     * 安徽
     */
    HEFEI(340000, 340100),

    /**
     * 湖南
     */
    CHANGSHA(430000, 430900), //430100
    // 益阳
    YIYANG(430000, 430900),

    /**
     * 湖北
     */
    WUHAN(420000, 420300), // 420100
    // 十堰
    SHIYAN(420000, 420300),

    /**
     * 浙江
     */
    HANGZHOU(330000, 330100),

    /**
     * 四川
     */
    CHENGDU(510100, 510600), // 510100
    // 德阳
    DEYANG(510000, 510600),

    CHONGQING(500000, 500300),

    /**
     * 云南
     */
    KUNMING(530000, 530400), // 530100
    // 玉溪
    YUXI(530000, 530400),

    SHENYANG(210000, 210100),

    /**
     * 黑龙江
     */
    HAERBIN(230000, -1);//230100


    private int proCode;

    private int cityCode;


    ZMCityCode(int proCode, int cityCode) {
        this.proCode = proCode;
        this.cityCode = cityCode;
    }

    public static ZMCityCode getZMCityCode(City city) {
        for (ZMCityCode code : ZMCityCode.values()) {
            if (code.name().equals(city.name())) {
                if (code.cityCode > 0) {
                    return code;
                } else {
                    return null;
                }
            }
        }
        return null;
    }



    public static void main(String[] args) {
        System.out.println(ZMCityCode.DEYANG);
        System.out.println(ZMCityCode.getZMCityCode(City.CHENGDU));
    }

    public int getProCode() {
        return proCode;
    }

    public int getCityCode() {
        return cityCode;
    }

    @Override
    public String toString() {
        return this.name() + "(" + this.proCode + "," + this.cityCode + ")";
    }
}

