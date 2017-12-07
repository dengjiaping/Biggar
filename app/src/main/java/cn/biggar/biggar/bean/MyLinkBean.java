package cn.biggar.biggar.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/3.
 */

public class MyLinkBean implements Serializable{

    /**
     * E_Img1 : /storage/emulated/0/tmpImages/1488797117231.jpg
     * E_Name : 测试
     * E_CreateDate : 1488797152
     * E_State : 0 //是否审核
     * E_Url :
     * E_Img2 :
     * E_TaobaoLing :
     * E_Type : 0
     * E_Suspend : 0 //是否开通
     */

    private String E_Img1;
    private String E_Name;
    private String E_CreateDate;
    private String E_State;
    private String E_Url;
    private String E_Img2;
    private String E_TaobaoLing;
    private String E_Type;
    private String E_Suspend;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    private String ID;

    public String getE_Img1() {
        return E_Img1;
    }

    public void setE_Img1(String E_Img1) {
        this.E_Img1 = E_Img1;
    }

    public String getE_Name() {
        return E_Name;
    }

    public void setE_Name(String E_Name) {
        this.E_Name = E_Name;
    }

    public String getE_CreateDate() {
        return E_CreateDate;
    }

    public void setE_CreateDate(String E_CreateDate) {
        this.E_CreateDate = E_CreateDate;
    }

    public String getE_State() {
        return E_State;
    }

    public void setE_State(String E_State) {
        this.E_State = E_State;
    }

    public String getE_Url() {
        return E_Url;
    }

    public void setE_Url(String E_Url) {
        this.E_Url = E_Url;
    }

    public String getE_Img2() {
        return E_Img2;
    }

    public void setE_Img2(String E_Img2) {
        this.E_Img2 = E_Img2;
    }

    public String getE_TaobaoLing() {
        return E_TaobaoLing;
    }

    public void setE_TaobaoLing(String E_TaobaoLing) {
        this.E_TaobaoLing = E_TaobaoLing;
    }

    public String getE_Type() {
        return E_Type;
    }

    public void setE_Type(String E_Type) {
        this.E_Type = E_Type;
    }

    public String getE_Suspend() {
        return E_Suspend;
    }

    public void setE_Suspend(String E_Suspend) {
        this.E_Suspend = E_Suspend;
    }
}
