package cn.biggar.biggar.bean;

/**
 * Created by zl on 2016/10/25.
 * 成员审核
 */
public class MemberAuditBean {

    /**
     * ID : 1045
     * E_Name : Kevin SO
     * E_HeadImg : http://biggar.image.alimmdn.com/upload/3F1B6E1E-8B58-4992-A0BC-573B6726E7AA
     * E_FamilyAdd : null
     * R_State : 0
     */

    private String ID;
    private String E_Name;
    private String E_HeadImg;
    private String E_FamilyAdd;
    private String R_State;

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

    public String getE_HeadImg() {
        return E_HeadImg;
    }

    public void setE_HeadImg(String E_HeadImg) {
        this.E_HeadImg = E_HeadImg;
    }

    public String getE_FamilyAdd() {
        return E_FamilyAdd;
    }

    public void setE_FamilyAdd(String E_FamilyAdd) {
        this.E_FamilyAdd = E_FamilyAdd;
    }

    public String getR_State() {
        return R_State;
    }

    public void setR_State(String R_State) {
        this.R_State = R_State;
    }
}
