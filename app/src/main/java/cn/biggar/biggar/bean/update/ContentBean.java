package cn.biggar.biggar.bean.update;

import java.util.List;

import cn.biggar.biggar.bean.CardBean;

/**
 * Created by Chenwy on 2017/9/4.
 */

public class ContentBean {
    public String ID;
    public String E_Code;
    public String E_Name;
    public String E_CreateDate;
    public String E_Img1;
    public String E_Img2;
    public List<String> E_Img3;
    public String E_Path;
    public String E_TypeVal;
    public String E_Plays;
    public String E_Comments;
    public String E_Concerns;
    public String E_GiftPoints;
    public String E_Share;
    public String E_ShareJsonTitle;
    public String E_MemberID;
    public String E_HeadImg;
    public String E_CreateUser;
    public String MID;
    public String E_IsConcern;
    public String E_RATE_SHAREURL;
    public String E_Content;
    public String E_VerWorker;
    public String is_Float;
    public String E_ShareTitle;
    public String E_ShareContent;

    public List<GoodsData> goods_data;
    public List<MemberList> member_list;
    public List<CommentList> commentlist;
    public List<CardBean> red_data;

    public static class GoodsData{
        public String ID;
        public String E_Name;
        public String E_Img1;
        public String E_SellPrice;
        public String E_Url;
        public String E_BrandCnName;
    }

    public static class MemberList{
        public String E_HeadImg;
        public String E_Name;
        public String E_Points;
        public String E_CMemberID;
    }

    public static class CommentList{
        public String ID;
        public String E_CreateUser;
        public String E_CreateDate;
        public String E_MemberID;
        public String E_Content;
        public String E_Clicks;
        public String E_RelationID;
        public String E_Fabulous;
        public String E_HeadImg;
    }
}
