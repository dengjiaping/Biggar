package cn.biggar.biggar.event;

/**
 * Created by mx on 2016/12/9.
 * 搜索
 */

public class SearchBrandEvent {
    public int type;
    public String searchKey;

    public SearchBrandEvent(int type, String key) {
        this.searchKey = key;
        this.type = type;
    }
}
