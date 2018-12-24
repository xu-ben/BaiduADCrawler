package ben.crawler.bdadcrawler;

import org.junit.Test;
import java.io.IOException;

public class ParticularCityTest {

    @Test
    public void test0() throws IOException {
        Displayer.displayNormally(City.BEIJING, "20181221p");
    }

    @Test
    public void testAllDate0() throws IOException {
        Displayer.displayAllNormally(City.BEIJING);
    }

    @Test
    public void testAllDate1() throws IOException {
        Displayer.displayAllToCSV(City.BEIJING);
    }

    @Test
    public void testAll() throws IOException {
//        Displayer.displayAllDataToCSV();
//        City[] cities = {City.NANJING };
//        City[] cities = {City.GUANGZHOU};
        City[] cities = City.getAllCitiesExclude(new City[] {City.SHENZHEN});
        Displayer.displayAllDataToCSV(cities);
//        for (City c : cities) {
//            System.out.println(c.name());
//        }
    }

}
