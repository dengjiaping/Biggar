package cn.biggar.biggar.bean;

import com.google.gson.annotations.SerializedName;

/*
* 文件名：
* 描 述：
* 作 者：苏昭强
* 时 间：2016/5/10
*/
public class VideoTagBean {

    @SerializedName("ID")
    private String id;
    @SerializedName("E_Name")
    private String eName;
    @SerializedName("E_CreateDate")
    private String eCreateDate;
    @SerializedName("E_State")
    private int eState;
    @SerializedName("E_Path")
    private String ePath;
    @SerializedName("E_Hot")
    private String eHot;
    @SerializedName("E_Use")
    private String eUse;

    private boolean eStatus;//是否选中

    public boolean iseStatus() {
        return eStatus;
    }

    public void seteStatus(boolean eStatus) {
        this.eStatus = eStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    public String geteCreateDate() {
        return eCreateDate;
    }

    public void seteCreateDate(String eCreateDate) {
        this.eCreateDate = eCreateDate;
    }

    public int geteState() {
        return eState;
    }

    public void seteState(int eState) {
        this.eState = eState;
    }

    public String getePath() {
        return ePath;
    }

    public void setePath(String ePath) {
        this.ePath = ePath;
    }

    public String geteHot() {
        return eHot;
    }

    public void seteHot(String eHot) {
        this.eHot = eHot;
    }

    public String geteUse() {
        return eUse;
    }

    public void seteUse(String eUse) {
        this.eUse = eUse;
    }
}
