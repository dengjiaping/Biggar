package cn.biggar.biggar.bean;

/**
 * Created by SUE on 2016/7/21 0021.
 */
public class GoodsBean {


    /**
     * ID : 116
     * E_Name : soul COMBAT+ 极级超跃动性运动型罩耳式头戴式耳机 正品行货
     * E_PriceTag : 2068.00
     * E_ContentMin : soul COMBAT+ 极级超跃动性运动型罩耳式头戴式耳机 正品行货
     * E_Img1 : /public/File/brandimg/20160527/5747e75038d4c.jpg
     * E_Discount 折扣
     */

    private String ID;
    private String E_Name;
    private String E_PriceTag;
    private String E_ContentMin;
    private String E_Img1;
    private String E_Discount;
    private String E_SellPrice;//折后价
    private boolean isCheck;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    private String new_price;//折后价格  服务器不返回  （以不用了  服务器给返回了）

    public String getE_SellPrice() {
        return E_SellPrice;
    }

    public void setE_SellPrice(String e_SellPrice) {
        E_SellPrice = e_SellPrice;
    }

    public String getNew_price() {
        return new_price;
    }

    public void setNew_price(String new_price) {
        this.new_price = new_price;
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

    public String getE_PriceTag() {
        return E_PriceTag;
    }

    public void setE_PriceTag(String E_PriceTag) {
        this.E_PriceTag = E_PriceTag;
    }

    public String getE_ContentMin() {
        return E_ContentMin;
    }

    public void setE_ContentMin(String E_ContentMin) {
        this.E_ContentMin = E_ContentMin;
    }

    public String getE_Img1() {
        return E_Img1;
    }

    public void setE_Img1(String E_Img1) {
        this.E_Img1 = E_Img1;
    }

    public String getE_Discount() {
        return E_Discount;
    }

    public void setE_Discount(String e_Discount) {
        E_Discount = e_Discount;
    }
}
