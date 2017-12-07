package cn.biggar.biggar.bean;

import java.util.ArrayList;

/**
 * Created by 张炼 on 2016/8/12.
 */
public class LabelBean {

    /**
     * ID : 52
     * E_Name : 拳击
     * E_CreateDate : 1469084531
     * E_State : 1
     * E_Path : .Beauty
     * E_Hot : 1
     * E_Use : 0
     * E_TagNum : 0
     */

    private String ID;
    private String E_Name;
    private String E_CreateDate;
    private String E_State;
    private String E_Path;
    private String E_Hot;
    private String E_Use;
    private String E_TagNum;
    private Boolean E_States = false;

    public Boolean getE_States() {
        return E_States;
    }

    public void setE_States(Boolean e_States) {
        E_States = e_States;
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

    public String getE_Path() {
        return E_Path;
    }

    public void setE_Path(String E_Path) {
        this.E_Path = E_Path;
    }

    public String getE_Hot() {
        return E_Hot;
    }

    public void setE_Hot(String E_Hot) {
        this.E_Hot = E_Hot;
    }

    public String getE_Use() {
        return E_Use;
    }

    public void setE_Use(String E_Use) {
        this.E_Use = E_Use;
    }

    public String getE_TagNum() {
        return E_TagNum;
    }

    public void setE_TagNum(String E_TagNum) {
        this.E_TagNum = E_TagNum;
    }

    public void editData(ArrayList<String> nums,int position){
        if (this.getE_States()) {
            this.setE_States(true);
            for (int i = 0; i < 3; i++) {
                String p = nums.get(i);
                if (p.equals(String.valueOf(position))) {
                    nums.remove(i);
                }
            }
        } else {
            this.setE_States(false);
            nums.add(String.valueOf(position));
        }
    }
}
