package cn.biggar.biggar.bean;

import java.io.Serializable;

/**
 * Created by 张炼 on 2016/8/10.
 */
public class LiWuBean implements Serializable {


    /**
     * ID : 22
     * E_Name : 发发发发
     * E_Img1 : /public/app/images/gift/22.png
     * E_KPoints : 88
     * counts : 0
     */

    private String ID;
    private String E_Name;
    private String E_Img1;
    private String E_KPoints;
    private String E_Points;
    private int E_Num;
    private int counts;
    private boolean isCheck;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getE_Points() {
        return E_Points;
    }

    public void setE_Points(String e_Points) {
        E_Points = e_Points;
    }


    public int getE_Num() {
        return E_Num;
    }

    public void setE_Num(int e_Num) {
        E_Num = e_Num;
    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getE_Name() {
        return E_Name;
    }

    public void setE_Name(String E_Name) {
        this.E_Name = E_Name;
    }

    public String getE_Img1() {
        return E_Img1;
    }

    public void setE_Img1(String E_Img1) {
        this.E_Img1 = E_Img1;
    }

    public String getE_KPoints() {
        return E_KPoints;
    }

    public void setE_KPoints(String E_KPoints) {
        this.E_KPoints = E_KPoints;
    }

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }
}
