package cn.biggar.biggar.bean;

/**
 * Created by zl on 2016/10/26.
 * 活动筛选bean
 */
public class ActivityScreenBean {
    private String E_Name;
    private String ID;

    public boolean isState() {
        return isState;
    }

    public void setState(boolean state) {
        isState = state;
    }

    private boolean isState = false;

    public String getE_Name() {
        return E_Name;
    }

    public void setE_Name(String e_Name) {
        E_Name = e_Name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
