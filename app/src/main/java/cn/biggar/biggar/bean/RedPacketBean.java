package cn.biggar.biggar.bean;

/**
 * Created by zl on 2016/12/13.
 * 红包bean
 */

public class RedPacketBean {


    /**
     * ID : 12
     * E_MemberID : 1326
     * E_Title : soul COMBAT+优惠券
     * E_AdID : 10
     * E_Need : 0
     * E_Img :
     * E_BrandID : 5
     * E_Money : 20.00
     * reds : 0
     * state : 1
     * E_Points : 0
     * E_Img1 : http://www.biggar.cn/public/File/brandimg/20161114/5829741049f99.jpg
     * E_H5URL : http://www.biggar.cn/app.php/Special/qunyinghui.html
     */

    private String ID;
    private String E_MemberID;
    private String E_Title;
    private String E_AdID;
    private String E_Need;
    private String E_Img;
    private String E_BrandID;
    private String E_Money;
    private int reds;
    private int state;
    private String E_Points;
    private String E_Img1;
    private String E_H5URL;

    private String E_CardType;//品牌福利时

    public String getE_CardType() {
        return E_CardType;
    }

    public void setE_CardType(String e_CardType) {
        E_CardType = e_CardType;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getE_MemberID() {
        return E_MemberID;
    }

    public void setE_MemberID(String E_MemberID) {
        this.E_MemberID = E_MemberID;
    }

    public String getE_Title() {
        return E_Title;
    }

    public void setE_Title(String E_Title) {
        this.E_Title = E_Title;
    }

    public String getE_AdID() {
        return E_AdID;
    }

    public void setE_AdID(String E_AdID) {
        this.E_AdID = E_AdID;
    }

    public String getE_Need() {
        return E_Need;
    }

    public void setE_Need(String E_Need) {
        this.E_Need = E_Need;
    }

    public String getE_Img() {
        return E_Img;
    }

    public void setE_Img(String E_Img) {
        this.E_Img = E_Img;
    }

    public String getE_BrandID() {
        return E_BrandID;
    }

    public void setE_BrandID(String E_BrandID) {
        this.E_BrandID = E_BrandID;
    }

    public String getE_Money() {
        return E_Money;
    }

    public void setE_Money(String E_Money) {
        this.E_Money = E_Money;
    }

    public int getReds() {
        return reds;
    }

    public void setReds(int reds) {
        this.reds = reds;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getE_Points() {
        return E_Points;
    }

    public void setE_Points(String E_Points) {
        this.E_Points = E_Points;
    }

    public String getE_Img1() {
        return E_Img1;
    }

    public void setE_Img1(String E_Img1) {
        this.E_Img1 = E_Img1;
    }

    public String getE_H5URL() {
        return E_H5URL;
    }

    public void setE_H5URL(String E_H5URL) {
        this.E_H5URL = E_H5URL;
    }
}
