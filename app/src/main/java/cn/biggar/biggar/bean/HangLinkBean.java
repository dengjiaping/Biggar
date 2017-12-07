package cn.biggar.biggar.bean;

/**
 * Created by zl on 2017/2/22.
 * 挂链商品bean
 */

public class HangLinkBean {

    /**
     * ID : 2
     * E_Name : SINOMAX/赛诺椅腰垫汽车办公靠垫记忆绵脊椎保健腰垫（粉蓝色）
     * E_Img1 : http://big.cn/public/File/Articlecn/BD4313A9-4071-EA7F-E0E6-224DE8BAA45Azhutuddd.jpg
     * E_SellPrice : 249.00
     * is_Chain : 0
     */

    private String ID;
    private String E_Name;
    private String E_Img1;
    private String E_SellPrice;
    private int is_Chain;

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

    public String getE_SellPrice() {
        return E_SellPrice;
    }

    public void setE_SellPrice(String E_SellPrice) {
        this.E_SellPrice = E_SellPrice;
    }

    public int getIs_Chain() {
        return is_Chain;
    }

    public void setIs_Chain(int is_Chain) {
        this.is_Chain = is_Chain;
    }
}
