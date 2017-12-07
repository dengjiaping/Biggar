package cn.biggar.biggar.bean.update;

import com.flyco.tablayout.listener.CustomTabEntity;

/**
 * Created by Chenwy on 2017/5/25.
 */

public class DiscoverTabEntity implements CustomTabEntity {
    public String title;
    public int tabSelectedIcon;
    public int tabUnselectedIcon;

    public DiscoverTabEntity(String title, int tabSelectedIcon, int tabUnselectedIcon) {
        this.title = title;
        this.tabSelectedIcon = tabSelectedIcon;
        this.tabUnselectedIcon = tabUnselectedIcon;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTabSelectedIcon(int tabSelectedIcon) {
        this.tabSelectedIcon = tabSelectedIcon;
    }

    public void setTabUnselectedIcon(int tabUnselectedIcon) {
        this.tabUnselectedIcon = tabUnselectedIcon;
    }

    @Override
    public String getTabTitle() {
        return title;
    }

    @Override
    public int getTabSelectedIcon() {
        return tabSelectedIcon;
    }

    @Override
    public int getTabUnselectedIcon() {
        return tabUnselectedIcon;
    }
}
