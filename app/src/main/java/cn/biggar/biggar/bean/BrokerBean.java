package cn.biggar.biggar.bean;

import java.io.Serializable;

/**
 * Created by 张炼 on 2016/9/8.
 */
public class BrokerBean implements Serializable{

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
     * ID : 2
     * E_MembID : 1136
     * E_BrokerID : 2
     * E_RedsDate : 1472809120
     * E_State : 1
     * E_OptionDate : 1473233491
     * E_LoginName : dazhi88
     * E_LoginPWD : 830bebe3ad9b3873080d7ac7646978a2
     * E_UpdateDate : 1473763450
     * E_Contact :
     * E_Mobile :
     * E_QQMsn :
     * E_TEl :
     * E_Adress :
     * E_prov : 广东
     * E_city : 广州
     * E_dist : 天河区
     * E_country : 中国
     * E_Post :
     * E_Index : 0
     * E_Logins : 21
     * E_Rate : 0
     * E_CompanyName : 大瑞股份
     * E_Sex : 男
     * E_BiggerID : null
     * E_WebHot : 3
     * E_Money : 0.00
     * E_Points : 0
     * E_CertificatesN : null
     * E_CertificatesType : 1
     * E_HeadImg : /public/File/brandimg/20160901/57c7f52544dfb.png
     * E_LicenseImg : null
     * E_MechanismImg : null
     * E_IdentityImg1 : null
     * E_IdentityImg2 : null
     * E_Token : null
     * state : 1
     */

    private String ID;
    private String E_MembID;
    private String E_BrokerID;
    private String E_RedsDate;
    private String E_State;
    private String E_OptionDate;
    private String E_LoginName;
    private String E_LoginPWD;

    public String getE_AddOpen() {
        return E_AddOpen;
    }

    public void setE_AddOpen(String e_AddOpen) {
        E_AddOpen = e_AddOpen;
    }

    private String E_AddOpen;

    public String getE_UpdateDate() {
        return E_UpdateDate;
    }

    public void setE_UpdateDate(String e_UpdateDate) {
        E_UpdateDate = e_UpdateDate;
    }

    private String E_UpdateDate;
    private String E_Contact;
    private String E_Mobile;
    private String E_QQMsn;
    private String E_TEl;
    private String E_Adress;
    private String E_prov;
    private String E_city;
    private String E_dist;
    private String E_country;
    private String E_Post;
    private String E_Index;
    private String E_Logins;
    private String E_Rate;
    private String E_CompanyName;
    private String E_Sex;
    private Object E_BiggerID;
    private String E_WebHot;
    private String E_Money;
    private String E_Points;
    private Object E_CertificatesN;
    private String E_CertificatesType;

    public String getE_CreateTime() {
        return E_CreateTime;
    }

    public void setE_CreateTime(String e_CreateTime) {
        E_CreateTime = e_CreateTime;
    }

    private String E_CreateTime;

    public String getE_Guildnum() {
        return E_Guildnum;
    }

    public void setE_Guildnum(String e_Guildnum) {
        E_Guildnum = e_Guildnum;
    }

    private String E_Guildnum;

    public int  getIs_guild() {
        return is_guild;
    }

    public void setIs_guild(int is_guild) {
        this.is_guild = is_guild;
    }

    private int is_guild;//是否是公户的成员  “1” 是，“0” 不是
    private String E_HeadImg;

    public String getE_AddDesc() {
        return E_AddDesc;
    }

    public void setE_AddDesc(String e_AddDesc) {
        E_AddDesc = e_AddDesc;
    }

    private String E_AddDesc;//招募信息

    public int getIs_qunzhu() {
        return is_qunzhu;
    }

    public void setIs_qunzhu(int is_qunzhu) {
        this.is_qunzhu = is_qunzhu;
    }

    private int is_qunzhu;


    public int getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(int is_admin) {
        this.is_admin = is_admin;
    }

    private int is_admin;

    public String getE_Notice() {
        return E_Notice;
    }

    public void setE_Notice(String e_Notice) {
        E_Notice = e_Notice;
    }

    private String E_Notice;

    public String getE_introduction() {
        return E_introduction;
    }

    public void setE_introduction(String e_introduction) {
        E_introduction = e_introduction;
    }

    private String E_introduction;
    private Object E_LicenseImg;
    private Object E_MechanismImg;
    private Object E_IdentityImg1;
    private Object E_IdentityImg2;
    private Object E_Token;
    private int state;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getE_MembID() {
        return E_MembID;
    }

    public void setE_MembID(String E_MembID) {
        this.E_MembID = E_MembID;
    }

    public String getE_BrokerID() {
        return E_BrokerID;
    }

    public void setE_BrokerID(String E_BrokerID) {
        this.E_BrokerID = E_BrokerID;
    }

    public String getE_RedsDate() {
        return E_RedsDate;
    }

    public void setE_RedsDate(String E_RedsDate) {
        this.E_RedsDate = E_RedsDate;
    }

    public String getE_State() {
        return E_State;
    }

    public void setE_State(String E_State) {
        this.E_State = E_State;
    }

    public String getE_OptionDate() {
        return E_OptionDate;
    }

    public void setE_OptionDate(String E_OptionDate) {
        this.E_OptionDate = E_OptionDate;
    }

    public String getE_LoginName() {
        return E_LoginName;
    }

    public void setE_LoginName(String E_LoginName) {
        this.E_LoginName = E_LoginName;
    }

    public String getE_LoginPWD() {
        return E_LoginPWD;
    }

    public void setE_LoginPWD(String E_LoginPWD) {
        this.E_LoginPWD = E_LoginPWD;
    }



    public String getE_Contact() {
        return E_Contact;
    }

    public void setE_Contact(String E_Contact) {
        this.E_Contact = E_Contact;
    }

    public String getE_Mobile() {
        return E_Mobile;
    }

    public void setE_Mobile(String E_Mobile) {
        this.E_Mobile = E_Mobile;
    }

    public String getE_QQMsn() {
        return E_QQMsn;
    }

    public void setE_QQMsn(String E_QQMsn) {
        this.E_QQMsn = E_QQMsn;
    }

    public String getE_TEl() {
        return E_TEl;
    }

    public void setE_TEl(String E_TEl) {
        this.E_TEl = E_TEl;
    }

    public String getE_Adress() {
        return E_Adress;
    }

    public void setE_Adress(String E_Adress) {
        this.E_Adress = E_Adress;
    }

    public String getE_prov() {
        return E_prov;
    }

    public void setE_prov(String E_prov) {
        this.E_prov = E_prov;
    }

    public String getE_city() {
        return E_city;
    }

    public void setE_city(String E_city) {
        this.E_city = E_city;
    }

    public String getE_dist() {
        return E_dist;
    }

    public void setE_dist(String E_dist) {
        this.E_dist = E_dist;
    }

    public String getE_country() {
        return E_country;
    }

    public void setE_country(String E_country) {
        this.E_country = E_country;
    }

    public String getE_Post() {
        return E_Post;
    }

    public void setE_Post(String E_Post) {
        this.E_Post = E_Post;
    }

    public String getE_Index() {
        return E_Index;
    }

    public void setE_Index(String E_Index) {
        this.E_Index = E_Index;
    }

    public String getE_Logins() {
        return E_Logins;
    }

    public void setE_Logins(String E_Logins) {
        this.E_Logins = E_Logins;
    }

    public String getE_Rate() {
        return E_Rate;
    }

    public void setE_Rate(String E_Rate) {
        this.E_Rate = E_Rate;
    }

    public String getE_CompanyName() {
        return E_CompanyName;
    }

    public void setE_CompanyName(String E_CompanyName) {
        this.E_CompanyName = E_CompanyName;
    }

    public String getE_Sex() {
        return E_Sex;
    }

    public void setE_Sex(String E_Sex) {
        this.E_Sex = E_Sex;
    }

    public Object getE_BiggerID() {
        return E_BiggerID;
    }

    public void setE_BiggerID(Object E_BiggerID) {
        this.E_BiggerID = E_BiggerID;
    }

    public String getE_WebHot() {
        return E_WebHot;
    }

    public void setE_WebHot(String E_WebHot) {
        this.E_WebHot = E_WebHot;
    }

    public String getE_Money() {
        return E_Money;
    }

    public void setE_Money(String E_Money) {
        this.E_Money = E_Money;
    }

    public String getE_Points() {
        return E_Points;
    }

    public void setE_Points(String E_Points) {
        this.E_Points = E_Points;
    }

    public Object getE_CertificatesN() {
        return E_CertificatesN;
    }

    public void setE_CertificatesN(Object E_CertificatesN) {
        this.E_CertificatesN = E_CertificatesN;
    }

    public String getE_CertificatesType() {
        return E_CertificatesType;
    }

    public void setE_CertificatesType(String E_CertificatesType) {
        this.E_CertificatesType = E_CertificatesType;
    }

    public String getE_HeadImg() {
        return E_HeadImg;
    }

    public void setE_HeadImg(String E_HeadImg) {
        this.E_HeadImg = E_HeadImg;
    }

    public Object getE_LicenseImg() {
        return E_LicenseImg;
    }

    public void setE_LicenseImg(Object E_LicenseImg) {
        this.E_LicenseImg = E_LicenseImg;
    }

    public Object getE_MechanismImg() {
        return E_MechanismImg;
    }

    public void setE_MechanismImg(Object E_MechanismImg) {
        this.E_MechanismImg = E_MechanismImg;
    }

    public Object getE_IdentityImg1() {
        return E_IdentityImg1;
    }

    public void setE_IdentityImg1(Object E_IdentityImg1) {
        this.E_IdentityImg1 = E_IdentityImg1;
    }

    public Object getE_IdentityImg2() {
        return E_IdentityImg2;
    }

    public void setE_IdentityImg2(Object E_IdentityImg2) {
        this.E_IdentityImg2 = E_IdentityImg2;
    }

    public Object getE_Token() {
        return E_Token;
    }

    public void setE_Token(Object E_Token) {
        this.E_Token = E_Token;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
