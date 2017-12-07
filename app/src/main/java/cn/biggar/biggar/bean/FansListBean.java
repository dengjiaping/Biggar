package cn.biggar.biggar.bean;

import java.io.Serializable;

/**
 * Created by mx on 2016/9/13.
 * 粉丝列表  / 关注列表
 */
public class FansListBean implements Serializable {
    private static final long serialVersionUID = 6327920811901137066L;
    private String ID;
    private String E_Name;
    private String E_VerWorker;
    private String E_HeadImg;
    private String E_Worker;
    private String E_Albumimg;
    private int isConcern;//是否互相关注  1关注
    private boolean isSelected;  //邀请成员 是否选中
    private String E_State;
    private String E_Tags;
    private String E_Signature;

    public String getE_Signature() {
        return E_Signature;
    }

    public void setE_Signature(String e_Signature) {
        E_Signature = e_Signature;
    }

    public String getE_Tags() {
        return E_Tags;
    }

    public void setE_Tags(String e_Tags) {
        E_Tags = e_Tags;
    }

    public String getE_State() {
        return E_State;
    }

    public void setE_State(String e_State) {
        E_State = e_State;
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

    public void setE_Name(String e_Name) {
        E_Name = e_Name;
    }

    public String getE_VerWorker() {
        return E_VerWorker;
    }

    public void setE_VerWorker(String e_VerWorker) {
        E_VerWorker = e_VerWorker;
    }

    public String getE_HeadImg() {
        return E_HeadImg;
    }

    public void setE_HeadImg(String e_HeadImg) {
        E_HeadImg = e_HeadImg;
    }

    public String getE_Worker() {
        return E_Worker;
    }

    public void setE_Worker(String e_Worker) {
        E_Worker = e_Worker;
    }

    public String getE_Albumimg() {
        return E_Albumimg;
    }

    public void setE_Albumimg(String e_Albumimg) {
        E_Albumimg = e_Albumimg;
    }

    public int getIsConcern() {
        return isConcern;
    }

    public void setIsConcern(int isConcern) {
        this.isConcern = isConcern;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
