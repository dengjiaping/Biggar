package per.sue.gear2.widget.nav;

/**
 * Created by SUE on 2016/8/1 0001.
 */
public class TableHostBean {

    private String title;
    private int iconResId;

    public TableHostBean(String title, int iconResId) {
        this.title = title;
        this.iconResId = iconResId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }
}
