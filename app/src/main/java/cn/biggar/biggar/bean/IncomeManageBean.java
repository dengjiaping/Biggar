package cn.biggar.biggar.bean;

/**
 * Created by 张炼 on 2016/11/9.
 * 收入管理
 */
public class IncomeManageBean {
    public void setLetter(char letter) {
        this.letter = letter;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public char getLetter() {
        return letter;
    }

    public boolean isFirst() {
        return first;
    }

    private char letter; // 大写字母
    private boolean first;//第一个
    /**
     * E_MembID : 14
     * count : 1
     * money : 1000
     * E_HeadImg : http://biggar.image.alimmdn.com/upload/110FF71D-6C16-41D6-B728-D2DEF873C3A5
     * E_Name : wilson
     * E_Sex : 男
     * E_TaskCout : 13
     * E_Income : 50000
     * E_VerWorker : 1
     */

    private String E_MembID;
    private String count;
    private String money;
    private String E_HeadImg;
    private String E_Name;
    private String E_Sex;
    private String E_TaskCout;
    private String E_Income;
    private String E_VerWorker;

    /**
     * totals : 1510
     * guildMoney : 60
     */

    private String totals;
    private String guildMoney;
    private String E_MPoints;
    private String createdata;
    private String E_Signature;

    public String getE_Rate() {
        return E_Rate;
    }

    public void setE_Rate(String e_Rate) {
        E_Rate = e_Rate;
    }

    public String getE_Signature() {
        return E_Signature;
    }

    public void setE_Signature(String e_Signature) {
        E_Signature = e_Signature;
    }

    private String E_Rate;

    public String getCreatedata() {
        return createdata;
    }

    public void setCreatedata(String createdata) {
        this.createdata = createdata;
    }

    public String getE_MPoints() {
        return E_MPoints;
    }

    public void setE_MPoints(String e_MPoints) {
        E_MPoints = e_MPoints;
    }

    public String getE_MembID() {
        return E_MembID;
    }

    public void setE_MembID(String E_MembID) {
        this.E_MembID = E_MembID;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getE_HeadImg() {
        return E_HeadImg;
    }

    public void setE_HeadImg(String E_HeadImg) {
        this.E_HeadImg = E_HeadImg;
    }

    public String getE_Name() {
        return E_Name;
    }

    public void setE_Name(String E_Name) {
        this.E_Name = E_Name;
    }

    public String getE_Sex() {
        return E_Sex;
    }

    public void setE_Sex(String E_Sex) {
        this.E_Sex = E_Sex;
    }

    public String getE_TaskCout() {
        return E_TaskCout;
    }

    public void setE_TaskCout(String E_TaskCout) {
        this.E_TaskCout = E_TaskCout;
    }

    public String getE_Income() {
        return E_Income;
    }

    public void setE_Income(String E_Income) {
        this.E_Income = E_Income;
    }

    public String getE_VerWorker() {
        return E_VerWorker;
    }

    public void setE_VerWorker(String E_VerWorker) {
        this.E_VerWorker = E_VerWorker;
    }

    public String getTotals() {
        return totals;
    }

    public void setTotals(String totals) {
        this.totals = totals;
    }

    public String getGuildMoney() {
        return guildMoney;
    }

    public void setGuildMoney(String guildMoney) {
        this.guildMoney = guildMoney;
    }
}
