package ben.crawler.bdadcrawler;

import java.util.ArrayList;

public class ADsInAFile {

    private KeyWord keyword;

    private String datestr = null;

    private String filePath = null; // 如果文件不存在，应设为null

    private ArrayList<AD> adlist = null;

    public ADsInAFile() {
    }

    public ADsInAFile(KeyWord keyword, String datestr, String filePath, ArrayList<AD> adlist) {
        this.keyword = keyword;
        this.datestr = datestr;
        this.filePath = filePath;
        this.adlist = adlist;
    }

    public KeyWord getKeyword() {
        return keyword;
    }

    public void setKeyword(KeyWord keyword) {
        this.keyword = keyword;
    }

    public String getDatestr() {
        return datestr;
    }

    public void setDatestr(String datestr) {
        this.datestr = datestr;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public ArrayList<AD> getAdlist() {
        return adlist;
    }

    public void setAdlist(ArrayList<AD> adlist) {
        this.adlist = adlist;
    }
}
