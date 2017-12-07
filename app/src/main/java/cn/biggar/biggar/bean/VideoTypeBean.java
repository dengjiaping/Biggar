package cn.biggar.biggar.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*
* 文件名：
* 描 述：
* 作 者：苏昭强
* 时 间：2016/5/6
*/
public class VideoTypeBean  implements Serializable{

    @SerializedName("E_Name")
    private String eName;

    @SerializedName("E_Path")
    private String ePath;


    @SerializedName("E_Img2")
    private String eImg2;

    public String geteImg3() {
        return eImg3;
    }

    public void seteImg3(String eImg3) {
        this.eImg3 = eImg3;
    }

    @SerializedName("E_Img3")
    private String eImg3;

    public Boolean getE_States() {
        return E_States;
    }

    public void setE_States(Boolean e_States) {
        E_States = e_States;
    }

    private Boolean E_States = false;

    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    public String getePath() {
        return ePath;
    }

    public void setePath(String ePath) {
        this.ePath = ePath;
    }

    public String geteImg2() {
        return eImg2;
    }

    public void seteImg2(String eImg2) {
        this.eImg2 = eImg2;
    }
}
