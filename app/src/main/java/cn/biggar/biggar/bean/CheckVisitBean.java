package cn.biggar.biggar.bean;

/**
 * Created by zl on 2016/10/12.
 * 谁看过我
 */
public class CheckVisitBean {

    /**
     * E_CreateDate : 1475997995
     * E_MemberID : 1058
     * E_BMemberID : 1027
     * E_LookCounts : 1
     * E_Name : Ada
     * E_Worker : null
     * E_Signature : null
     */

    private String E_CreateDate;
    private String E_MemberID;
    private String E_BMemberID;
    private int E_LookCounts;
    private String E_Name;
    private Object E_Worker;
    private String E_Signature;

    public String getE_HeadImg() {
        return E_HeadImg;
    }

    public void setE_HeadImg(String e_HeadImg) {
        E_HeadImg = e_HeadImg;
    }

    private String E_HeadImg;

    public String getE_CreateDate() {
        return E_CreateDate;
    }

    public void setE_CreateDate(String E_CreateDate) {
        this.E_CreateDate = E_CreateDate;
    }

    public String getE_MemberID() {
        return E_MemberID;
    }

    public void setE_MemberID(String E_MemberID) {
        this.E_MemberID = E_MemberID;
    }

    public String getE_BMemberID() {
        return E_BMemberID;
    }

    public void setE_BMemberID(String E_BMemberID) {
        this.E_BMemberID = E_BMemberID;
    }

    public int getE_LookCounts() {
        return E_LookCounts;
    }

    public void setE_LookCounts(int E_LookCounts) {
        this.E_LookCounts = E_LookCounts;
    }

    public String getE_Name() {
        return E_Name;
    }

    public void setE_Name(String E_Name) {
        this.E_Name = E_Name;
    }

    public Object getE_Worker() {
        return E_Worker;
    }

    public void setE_Worker(Object E_Worker) {
        this.E_Worker = E_Worker;
    }

    public String getE_Signature() {
        return E_Signature;
    }

    public void setE_Signature(String E_Signature) {
        this.E_Signature = E_Signature;
    }
}
