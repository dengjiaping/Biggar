package cn.biggar.biggar.event;

/**
 * Created by Chenwy on 2017/9/21.
 */

public class SearchEvent {
    public String searchName;
    public int currentItem;

    public SearchEvent(String searchName, int currentItem) {
        this.searchName = searchName;
        this.currentItem = currentItem;
    }
}
