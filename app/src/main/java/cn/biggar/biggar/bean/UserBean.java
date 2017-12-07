package cn.biggar.biggar.bean;


import java.io.Serializable;
import java.util.List;

/*
* 文件名：
* 描 述：
* 作 者：苏昭强
* 时 间：2016/4/25
*/
public class UserBean implements Serializable, Cloneable {


    /**
     * ID : 15
     * E_Language : cn
     * E_CreateUser : system
     * E_CreateDate : 2016-03-25 13:14:15
     * E_UpdateUser : system
     * E_UpdateDate : 2016-08-23 10:07:01
     * E_CreateIP : 59.41.65.200
     * E_State : 1
     * E_User : water
     * E_Mail :
     * E_Mobile : 13660139338
     * E_Name : Mrs’water
     * E_QQMsn : null
     * E_FamilyAdd : null
     * E_Adress : null
     * E_BGImg : http://www.biggar.cn/public/app/images/personal/defaultBG.png
     * E_HeadImg : http://www.biggar.cn/public/File/MemHead/13660139338/20160511/t01c2f4cfe3cc5948c6.jpg
     * E_RelationQQ : null
     * E_HeadFlag : 0
     * E_RelationWx : null
     * E_RelationWb : null
     * E_PromotionIMemID : null
     * E_Height : null
     * E_ComMoney : 0.00
     * E_Points : 3720
     * E_IdentityCard : null
     * E_Tags : []
     * E_Level : 1
     * E_TrueName :
     * E_Sex : null
     * E_Weixin : null
     * E_Marriage : null
     * E_Education : null
     * E_Birthday : 0000-00-00 00:00:00
     * E_Income : null
     * E_TaskCout : 5
     * E_Problem : null
     * E_Answer : null
     * E_ChouJianNum : 1
     * E_ChouJianTime : 1463220474
     * E_Index : 104
     * E_LoginAdd : null
     * E_LoginFrome : null
     * E_BrokerID : 0
     * E_MobileIME : null
     * E_LoginGonhun : 0
     * E_MobileSYS : null
     * E_OrderBy : 0
     * E_Concern_ta : 1
     * E_Concern_me : 20
     * E_Signature : 你看看
     * E_IsShow : 1
     * E_Uploads : 3
     * E_ShareID : 0
     * E_Worker : null
     * E_VerWorker : 0
     * E_LvName : 部落族人
     * E_BrokerName : null
     * E_BrokerImg : null
     * E_SharePersonal : http://www.biggar.cn/app.php/index/personal_show.html?ID=15&SMID=0
     * E_ShareHeadImg : http://www.biggar.cn/public/File/MemHead/13660139338/20160511/t01c2f4cfe3cc5948c6.jpg
     * E_BrandConcerMe : 0
     * E_MemberCardNum : 2
     * E_MemberCardNet : 2
     * E_MemberCardNIce : Mrs’water在比格的网红指数是2,0个品牌关注了我！拥有2张优惠券
     */

    private String ID;
    private String E_Language;
    private String E_CreateUser;
    private String E_CreateDate;
    private String E_UpdateUser;
    private String E_UpdateDate;
    private String E_CreateIP;
    private String E_State;
    private String E_User;
    private String E_Mail;
    private String E_Mobile;
    private String E_Name;
    private Object E_QQMsn;
    private String E_FamilyAdd;
    private String E_Adress;
    private String E_BGImg;
    private String E_HeadImg;
    private Object E_RelationQQ;
    private String E_HeadFlag;
    private Object E_RelationWx;
    private Object E_RelationWb;
    private Object E_PromotionIMemID;
    private String E_Height;
    private String E_ComMoney;
    private String E_Points;
    private Object E_IdentityCard;
    private String E_Level;
    private String E_TrueName;
    private String E_Sex;
    private String E_Weixin;
    private String E_Marriage;
    private String E_Education;
    private String E_Birthday;
    private Object E_Income;
    private String E_TaskCout;
    private Object E_Problem;
    private Object E_Answer;
    private String E_ChouJianNum;
    private String E_ChouJianTime;
    private String E_Index;
    private Object E_LoginAdd;
    private Object E_LoginFrome;
    private String E_BrokerID;
    private Object E_MobileIME;
    private String E_LoginGonhun;
    private Object E_MobileSYS;
    private String E_OrderBy;
    private String E_Concern_ta;
    private String E_Concern_me;
    private String E_Signature;
    private String E_IsShow;
    private String E_Uploads;
    private String E_ShareID;
    private String E_Worker;
    private String E_VerWorker;
    private String E_LvName;
    private String E_BrokerName;
    private String E_BrokerImg;
    private String E_SharePersonal;
    private String E_ShareHeadImg;
    private String E_BrandConcerMe;
    private String E_MemberCardNum;
    private String E_MemberCardNet;
    private String E_MemberCardNIce;
    private String E_blog;
    private String E_platform;
    private String E_Constellation;
    private String E_Wechatprice;
    private String E_Experience;
    private String E_Weight;

    public String E_Task;
    public String E_Shop;
    public String E_Read;

    public String getE_Experience() {
        return E_Experience;
    }

    public void setE_Experience(String e_Experience) {
        E_Experience = e_Experience;
    }

    public String getE_Weight() {
        return E_Weight;
    }

    public void setE_Weight(String e_Weight) {
        E_Weight = e_Weight;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String token;

    public String getBusiness_state() {
        return business_state;
    }

    public void setBusiness_state(String business_state) {
        this.business_state = business_state;
    }

    private String business_state;

    private String E_Net;//名人堂用到

    public String getIs_business() {
        return is_business;
    }

    public void setIs_business(String is_business) {
        this.is_business = is_business;
    }

    public String getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
    }

    private String is_business;//是否是商家
    private String business_id;//商家ID

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    private String count;//发表视频或者图片的条数
    private String price;//创造的价值


    public String getE_Wechatprice() {
        return E_Wechatprice;
    }

    public void setE_Wechatprice(String e_Wechatprice) {
        E_Wechatprice = e_Wechatprice;
    }

    public String getE_blog() {
        return E_blog;
    }

    public void setE_blog(String e_blog) {
        E_blog = e_blog;
    }

    public String getE_platform() {
        return E_platform;
    }

    public void setE_platform(String e_platform) {
        E_platform = e_platform;
    }

    public String getE_Constellation() {
        return E_Constellation;
    }

    public void setE_Constellation(String e_Constellation) {
        E_Constellation = e_Constellation;
    }

    public List<String> getE_Albumimg() {
        return E_Albumimg;
    }

    public void setE_Albumimg(List<String> e_Albumimg) {
        E_Albumimg = e_Albumimg;
    }

    private List<String> E_Albumimg;//相册集合

    public String getE_Net() {
        return E_Net;
    }

    public void setE_Net(String e_Net) {
        E_Net = e_Net;
    }

    public String getGuild_id() {
        return guild_id;
    }

    public void setGuild_id(String guild_id) {
        this.guild_id = guild_id;
    }

    private String guild_id;


    private int is_guild;//0没有公会，1有公会

    public boolean is_audit() {
        return is_audit;
    }

    public void setIs_audit(boolean is_audit) {
        this.is_audit = is_audit;
    }

    private boolean is_audit = false;//是否审核

    public int getIs_guild() {
        return is_guild;
    }

    public void setIs_guild(int is_guild) {
        this.is_guild = is_guild;
    }

    public Object clone() {
        Object o = null;
        try {
            o = super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println(e.toString());
        }
        return o;
    }

    public String getE_IsPresent() {
        return E_IsPresent;
    }

    public void setE_IsPresent(String e_IsPresent) {
        E_IsPresent = e_IsPresent;
    }

    private String E_IsPresent;//是否赠送


    public String getId() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getE_Language() {
        return E_Language;
    }

    public void setE_Language(String E_Language) {
        this.E_Language = E_Language;
    }

    public String getE_CreateUser() {
        return E_CreateUser;
    }

    public void setE_CreateUser(String E_CreateUser) {
        this.E_CreateUser = E_CreateUser;
    }

    public String getE_CreateDate() {
        return E_CreateDate;
    }

    public void setE_CreateDate(String E_CreateDate) {
        this.E_CreateDate = E_CreateDate;
    }

    public String getE_UpdateUser() {
        return E_UpdateUser;
    }

    public void setE_UpdateUser(String E_UpdateUser) {
        this.E_UpdateUser = E_UpdateUser;
    }

    public String getE_UpdateDate() {
        return E_UpdateDate;
    }

    public void setE_UpdateDate(String E_UpdateDate) {
        this.E_UpdateDate = E_UpdateDate;
    }

    public String getE_CreateIP() {
        return E_CreateIP;
    }

    public void setE_CreateIP(String E_CreateIP) {
        this.E_CreateIP = E_CreateIP;
    }

    public String getE_State() {
        return E_State;
    }

    public void setE_State(String E_State) {
        this.E_State = E_State;
    }

    public String getE_User() {
        return E_User;
    }

    public void setE_User(String E_User) {
        this.E_User = E_User;
    }

    public String getE_Mail() {
        return E_Mail;
    }

    public void setE_Mail(String E_Mail) {
        this.E_Mail = E_Mail;
    }

    public String getE_Mobile() {
        return E_Mobile;
    }

    public void setE_Mobile(String E_Mobile) {
        this.E_Mobile = E_Mobile;
    }

    public String geteName() {
        return E_Name;
    }

    public void setE_Name(String E_Name) {
        this.E_Name = E_Name;
    }

    public Object getE_QQMsn() {
        return E_QQMsn;
    }

    public void setE_QQMsn(Object E_QQMsn) {
        this.E_QQMsn = E_QQMsn;
    }

    public String getE_FamilyAdd() {
        return E_FamilyAdd;
    }

    public void setE_FamilyAdd(String E_FamilyAdd) {
        this.E_FamilyAdd = E_FamilyAdd;
    }

    public String getE_Adress() {
        return E_Adress;
    }

    public void setE_Adress(String E_Adress) {
        this.E_Adress = E_Adress;
    }

    public String getE_BGImg() {
        return E_BGImg;
    }

    public void setE_BGImg(String E_BGImg) {
        this.E_BGImg = E_BGImg;
    }

    public String getE_HeadImg() {
        return E_HeadImg;
    }

    public void setE_HeadImg(String E_HeadImg) {
        this.E_HeadImg = E_HeadImg;
    }

    public Object getE_RelationQQ() {
        return E_RelationQQ;
    }

    public void setE_RelationQQ(Object E_RelationQQ) {
        this.E_RelationQQ = E_RelationQQ;
    }

    public String getE_HeadFlag() {
        return E_HeadFlag;
    }

    public void setE_HeadFlag(String E_HeadFlag) {
        this.E_HeadFlag = E_HeadFlag;
    }

    public Object getE_RelationWx() {
        return E_RelationWx;
    }

    public void setE_RelationWx(Object E_RelationWx) {
        this.E_RelationWx = E_RelationWx;
    }

    public Object getE_RelationWb() {
        return E_RelationWb;
    }

    public void setE_RelationWb(Object E_RelationWb) {
        this.E_RelationWb = E_RelationWb;
    }

    public Object getE_PromotionIMemID() {
        return E_PromotionIMemID;
    }

    public void setE_PromotionIMemID(Object E_PromotionIMemID) {
        this.E_PromotionIMemID = E_PromotionIMemID;
    }

    public String getE_Height() {
        return E_Height;
    }

    public void setE_Height(String E_Height) {
        this.E_Height = E_Height;
    }

    public String getE_ComMoney() {
        return E_ComMoney;
    }

    public void setE_ComMoney(String E_ComMoney) {
        this.E_ComMoney = E_ComMoney;
    }

    public String getE_Points() {
        return E_Points;
    }

    public void setE_Points(String E_Points) {
        this.E_Points = E_Points;
    }

    public Object getE_IdentityCard() {
        return E_IdentityCard;
    }

    public void setE_IdentityCard(Object E_IdentityCard) {
        this.E_IdentityCard = E_IdentityCard;
    }

    public String getE_Level() {
        return E_Level;
    }

    public void setE_Level(String E_Level) {
        this.E_Level = E_Level;
    }

    public String getE_TrueName() {
        return E_TrueName;
    }

    public void setE_TrueName(String E_TrueName) {
        this.E_TrueName = E_TrueName;
    }

    public String getE_Sex() {
        return E_Sex;
    }

    public void setE_Sex(String E_Sex) {
        this.E_Sex = E_Sex;
    }

    public String getE_Weixin() {
        return E_Weixin;
    }

    public void setE_Weixin(String E_Weixin) {
        this.E_Weixin = E_Weixin;
    }

    public String getE_Marriage() {
        return E_Marriage;
    }

    public void setE_Marriage(String E_Marriage) {
        this.E_Marriage = E_Marriage;
    }

    public String getE_Education() {
        return E_Education;
    }

    public void setE_Education(String E_Education) {
        this.E_Education = E_Education;
    }

    public String getE_Birthday() {
        return E_Birthday;
    }

    public void setE_Birthday(String E_Birthday) {
        this.E_Birthday = E_Birthday;
    }

    public Object getE_Income() {
        return E_Income;
    }

    public void setE_Income(Object E_Income) {
        this.E_Income = E_Income;
    }

    public String getE_TaskCout() {
        return E_TaskCout;
    }

    public void setE_TaskCout(String E_TaskCout) {
        this.E_TaskCout = E_TaskCout;
    }

    public Object getE_Problem() {
        return E_Problem;
    }

    public void setE_Problem(Object E_Problem) {
        this.E_Problem = E_Problem;
    }

    public Object getE_Answer() {
        return E_Answer;
    }

    public void setE_Answer(Object E_Answer) {
        this.E_Answer = E_Answer;
    }

    public String getE_ChouJianNum() {
        return E_ChouJianNum;
    }

    public void setE_ChouJianNum(String E_ChouJianNum) {
        this.E_ChouJianNum = E_ChouJianNum;
    }

    public String getE_ChouJianTime() {
        return E_ChouJianTime;
    }

    public void setE_ChouJianTime(String E_ChouJianTime) {
        this.E_ChouJianTime = E_ChouJianTime;
    }

    public String getE_Index() {
        return E_Index;
    }

    public void setE_Index(String E_Index) {
        this.E_Index = E_Index;
    }

    public Object getE_LoginAdd() {
        return E_LoginAdd;
    }

    public void setE_LoginAdd(Object E_LoginAdd) {
        this.E_LoginAdd = E_LoginAdd;
    }

    public Object getE_LoginFrome() {
        return E_LoginFrome;
    }

    public void setE_LoginFrome(Object E_LoginFrome) {
        this.E_LoginFrome = E_LoginFrome;
    }

    public String getE_BrokerID() {
        return E_BrokerID;
    }

    public void setE_BrokerID(String E_BrokerID) {
        this.E_BrokerID = E_BrokerID;
    }

    public Object getE_MobileIME() {
        return E_MobileIME;
    }

    public void setE_MobileIME(Object E_MobileIME) {
        this.E_MobileIME = E_MobileIME;
    }

    public String getE_LoginGonhun() {
        return E_LoginGonhun;
    }

    public void setE_LoginGonhun(String E_LoginGonhun) {
        this.E_LoginGonhun = E_LoginGonhun;
    }

    public Object getE_MobileSYS() {
        return E_MobileSYS;
    }

    public void setE_MobileSYS(Object E_MobileSYS) {
        this.E_MobileSYS = E_MobileSYS;
    }

    public String getE_OrderBy() {
        return E_OrderBy;
    }

    public void setE_OrderBy(String E_OrderBy) {
        this.E_OrderBy = E_OrderBy;
    }

    public String getE_Concern_ta() {
        return E_Concern_ta;
    }

    public void setE_Concern_ta(String E_Concern_ta) {
        this.E_Concern_ta = E_Concern_ta;
    }

    public String getE_Concern_me() {
        return E_Concern_me;
    }

    public void setE_Concern_me(String E_Concern_me) {
        this.E_Concern_me = E_Concern_me;
    }

    public String getE_Signature() {
        return E_Signature;
    }

    public void setE_Signature(String E_Signature) {
        this.E_Signature = E_Signature;
    }

    public String getE_IsShow() {
        return E_IsShow;
    }

    public void setE_IsShow(String E_IsShow) {
        this.E_IsShow = E_IsShow;
    }

    public String getE_Uploads() {
        return E_Uploads;
    }

    public void setE_Uploads(String E_Uploads) {
        this.E_Uploads = E_Uploads;
    }

    public String getE_ShareID() {
        return E_ShareID;
    }

    public void setE_ShareID(String E_ShareID) {
        this.E_ShareID = E_ShareID;
    }

    public String getE_Worker() {
        return E_Worker;
    }

    public void setE_Worker(String E_Worker) {
        this.E_Worker = E_Worker;
    }

    public String getE_VerWorker() {
        return E_VerWorker;
    }

    public void setE_VerWorker(String E_VerWorker) {
        this.E_VerWorker = E_VerWorker;
    }

    public String getE_LvName() {
        return E_LvName;
    }

    public void setE_LvName(String E_LvName) {
        this.E_LvName = E_LvName;
    }

    public String getE_BrokerName() {
        return E_BrokerName;
    }

    public void setE_BrokerName(String E_BrokerName) {
        this.E_BrokerName = E_BrokerName;
    }

    public String getE_BrokerImg() {
        return E_BrokerImg;
    }

    public void setE_BrokerImg(String E_BrokerImg) {
        this.E_BrokerImg = E_BrokerImg;
    }

    public String getE_SharePersonal() {
        return E_SharePersonal;
    }

    public void setE_SharePersonal(String E_SharePersonal) {
        this.E_SharePersonal = E_SharePersonal;
    }

    public String getE_ShareHeadImg() {
        return E_ShareHeadImg;
    }

    public void setE_ShareHeadImg(String E_ShareHeadImg) {
        this.E_ShareHeadImg = E_ShareHeadImg;
    }

    public String getE_BrandConcerMe() {
        return E_BrandConcerMe;
    }

    public void setE_BrandConcerMe(String E_BrandConcerMe) {
        this.E_BrandConcerMe = E_BrandConcerMe;
    }

    public String getE_MemberCardNum() {
        return E_MemberCardNum;
    }

    public void setE_MemberCardNum(String E_MemberCardNum) {
        this.E_MemberCardNum = E_MemberCardNum;
    }

    public String getE_MemberCardNet() {
        return E_MemberCardNet;
    }

    public void setE_MemberCardNet(String E_MemberCardNet) {
        this.E_MemberCardNet = E_MemberCardNet;
    }

    public String getE_MemberCardNIce() {
        return E_MemberCardNIce;
    }

    public void setE_MemberCardNIce(String E_MemberCardNIce) {
        this.E_MemberCardNIce = E_MemberCardNIce;
    }
}
