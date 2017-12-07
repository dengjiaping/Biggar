package me.dudu.livegiftview;

public class GiftModel {
    private String ID;
    private String E_Name;
    private String E_Img1;
    private String E_KPoints;
    private String E_Points;
    private int E_Num;
    private int counts;
    private boolean isCheck;
    private int sendGiftCount;
    private String E_Combo;
    private String E_Img2;
    private String Is_red;

    public String getIs_red() {
        return Is_red;
    }

    public void setIs_red(String is_red) {
        Is_red = is_red;
    }

    public String getE_Combo() {
        return E_Combo;
    }

    public void setE_Combo(String e_Combo) {
        E_Combo = e_Combo;
    }

    public String getE_Img2() {
        return E_Img2;
    }

    public void setE_Img2(String e_Img2) {
        E_Img2 = e_Img2;
    }

    public int getSendGiftCount() {
        return sendGiftCount;
    }

    public void setSendGiftCount(int sendGiftCount) {
        this.sendGiftCount = sendGiftCount;
    }

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
