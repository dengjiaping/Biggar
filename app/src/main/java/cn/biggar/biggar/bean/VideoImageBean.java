package cn.biggar.biggar.bean;

import java.util.List;

/**
 * Created by SUE on 2016/7/14 0014.
 */
public class VideoImageBean {
    private String avatar;
    private String name;
    private String playTimes = "16";
    private String conver;
    private String title = "女神";
    private String describe = "女神hen hao ";
    private String tags = "女神 ";


    private String ID;
    private Object E_Code;
    private String E_Name;
    private String E_ProductID;
    private String E_Path;
    private String E_Language;
    private String E_CreateUser;
    private String E_CreateDate;
    private Object E_UpdateUser;
    private String E_UpdateDate;
    private String E_CreateIP;
    private String E_State;
    private String E_Index;
    private String E_MemberID;
    private String E_TypeVal;  // 0视频   1图片
    private String E_BrandID;
    private String E_Flags;
    private String E_VLeng;
    private String E_Points;
    private String E_Plays;
    private String E_Img1;
    private String E_Content;
    private String E_Comments;
    private String E_Type;
    private String E_Concerns;
    private String E_Img2;
    private String E_Tags;
    private String E_GiftPoints;
    private Object E_Img3;
    private String E_Share;
    private String E_giftNum;
    private String E_GoUrl;
    private String E_ImgNum;
    private String E_HeadImg;
    public GoodsData goods_data;
    public List<String> E_Photo;

    public static class GoodsData {
        public String ID;
        public String E_Name;
        public String E_Img1;
        public String E_SellPrice;
        public String E_Url;
        public String E_CompanyID;
        public String E_BrandCnName;
    }

    public String getE_HeadImg() {
        return E_HeadImg;
    }

    public void setE_HeadImg(String e_HeadImg) {
        E_HeadImg = e_HeadImg;
    }

    public String getE_ImgNum() {
        return E_ImgNum;
    }

    public void setE_ImgNum(String e_ImgNum) {
        E_ImgNum = e_ImgNum;
    }

    public String getE_GoUrl() {
        return E_GoUrl;
    }

    public void setE_GoUrl(String e_GoUrl) {
        E_GoUrl = e_GoUrl;
    }

    public String getE_giftNum() {
        return E_giftNum;
    }

    public void setE_giftNum(String e_giftNum) {
        E_giftNum = e_giftNum;
    }

    public String getE_Share() {
        return E_Share;
    }

    public void setE_Share(String e_Share) {
        E_Share = e_Share;
    }

    public VideoImageBean(String conver, String name, String avatar) {
        this.conver = conver;
        this.name = name;
        this.avatar = avatar;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlayTimes() {
        return playTimes;
    }

    public void setPlayTimes(String playTimes) {
        this.playTimes = playTimes;
    }

    public String getConver() {
        return conver;
    }

    public void setConver(String conver) {
        this.conver = conver;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Object getE_Code() {
        return E_Code;
    }

    public void setE_Code(Object E_Code) {
        this.E_Code = E_Code;
    }

    public String getE_Name() {
        return E_Name;
    }

    public void setE_Name(String E_Name) {
        this.E_Name = E_Name;
    }

    public String getE_ProductID() {
        return E_ProductID;
    }

    public void setE_ProductID(String E_ProductID) {
        this.E_ProductID = E_ProductID;
    }

    public String getE_Path() {
        return E_Path;
    }

    public void setE_Path(String E_Path) {
        this.E_Path = E_Path;
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

    public Object getE_UpdateUser() {
        return E_UpdateUser;
    }

    public void setE_UpdateUser(Object E_UpdateUser) {
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

    public String getE_Index() {
        return E_Index;
    }

    public void setE_Index(String E_Index) {
        this.E_Index = E_Index;
    }

    public String getE_MemberID() {
        return E_MemberID;
    }

    public void setE_MemberID(String E_MemberID) {
        this.E_MemberID = E_MemberID;
    }

    public String getE_TypeVal() {
        return E_TypeVal;
    }

    public void setE_TypeVal(String E_TypeVal) {
        this.E_TypeVal = E_TypeVal;
    }

    public String getE_BrandID() {
        return E_BrandID;
    }

    public void setE_BrandID(String E_BrandID) {
        this.E_BrandID = E_BrandID;
    }

    public String getE_Flags() {
        return E_Flags;
    }

    public void setE_Flags(String E_Flags) {
        this.E_Flags = E_Flags;
    }

    public String getE_VLeng() {
        return E_VLeng;
    }

    public void setE_VLeng(String E_VLeng) {
        this.E_VLeng = E_VLeng;
    }

    public String getE_Points() {
        return E_Points;
    }

    public void setE_Points(String E_Points) {
        this.E_Points = E_Points;
    }

    public String getE_Plays() {
        return E_Plays;
    }

    public void setE_Plays(String E_Plays) {
        this.E_Plays = E_Plays;
    }

    public String getE_Img1() {
        return E_Img1;
    }

    public void setE_Img1(String E_Img1) {
        this.E_Img1 = E_Img1;
    }

    public String getE_Content() {
        return E_Content;
    }

    public void setE_Content(String E_Content) {
        this.E_Content = E_Content;
    }

    public String getE_Comments() {
        return E_Comments;
    }

    public void setE_Comments(String E_Comments) {
        this.E_Comments = E_Comments;
    }

    public String getE_Type() {
        return E_Type;
    }

    public void setE_Type(String E_Type) {
        this.E_Type = E_Type;
    }

    public String getE_Concerns() {
        return E_Concerns;
    }

    public void setE_Concerns(String E_Concerns) {
        this.E_Concerns = E_Concerns;
    }

    public String getE_Img2() {
        return E_Img2;
    }

    public void setE_Img2(String E_Img2) {
        this.E_Img2 = E_Img2;
    }

    public String getE_Tags() {
        return E_Tags;
    }

    public void setE_Tags(String E_Tags) {
        this.E_Tags = E_Tags;
    }

    public String getE_GiftPoints() {
        return E_GiftPoints;
    }

    public void setE_GiftPoints(String E_GiftPoints) {
        this.E_GiftPoints = E_GiftPoints;
    }

    public Object getE_Img3() {
        return E_Img3;
    }

    public void setE_Img3(Object E_Img3) {
        this.E_Img3 = E_Img3;
    }
}


