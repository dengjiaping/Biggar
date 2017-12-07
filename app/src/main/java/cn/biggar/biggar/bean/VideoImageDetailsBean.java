package cn.biggar.biggar.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mx on 2016/8/8.
 * 视频 图片 详情
 */
public class VideoImageDetailsBean implements Serializable{

    /**
     * ID : 4
     * E_Code : null
     * E_Name : 教你选择合适自己的面膜
     * E_ProductID : 0
     * E_Path : http://biggar.file.alimmdn.com/video_20160526191720459
     * E_Language : cn
     * E_CreateUser : 雪儿
     * E_CreateDate : 1464261906
     * E_UpdateUser : null
     * E_UpdateDate : 1464261906
     * E_CreateIP : 59.41.65.42
     * E_State : 1
     * E_Index : 11
     * E_MemberID : 8
     * E_TypeVal : 0
     * E_BrandID : 0
     * E_Flags : 1
     * E_VLeng : 30
     * E_Points : 0
     * E_Plays : 230
     * E_Img1 : http://biggar.image.alimmdn.com/cover_s20160526191720378
     * E_Content : #美妆#教你如何选择适合自己的清洁面膜～
     * E_Comments : 0
     * E_Type : .Brand
     * E_Concerns : 0
     * E_Img2 : http://biggar.image.alimmdn.com/first_s20160526191720444
     * E_Tags : ["美妆"]
     * E_GiftPoints : 0
     * E_Img3 :
     * E_Share : 0
     * E_RATE_PRODUCT : [{"ID":"90","E_Name":"XCOR舒脊坐垫","E_NameMin":"塑臀娇姿，时尚耐用","E_PriceTag":"999.00","E_Discount":"1","E_CompanyID":"4","E_CompanyName":null,"E_Img1":"/public/File/brandimg/20160526/57470b00cdcc6.jpg"},{"ID":"91","E_Name":"XCOR护颈车垫","E_NameMin":"减轻驾驶冲击，护颈椎","E_PriceTag":"999.00","E_Discount":"1","E_CompanyID":"4","E_CompanyName":null,"E_Img1":"/public/File/brandimg/20160526/57470d338f09d.jpg"},{"ID":"92","E_Name":"XCOR工学U形枕","E_NameMin":"预防颈椎劳损、变形","E_PriceTag":"1099.00","E_Discount":"1","E_CompanyID":"4","E_CompanyName":null,"E_Img1":"/public/File/brandimg/20160526/57470ddd8c3ff.jpg"},{"ID":"93","E_Name":"XCOR多功能腰垫","E_NameMin":"智能调温，四季通用","E_PriceTag":"1199.00","E_Discount":"1","E_CompanyID":"4","E_CompanyName":null,"E_Img1":"/public/File/brandimg/20160526/57470e5976200.jpg"},{"ID":"96","E_Name":"XCOR太空飞船模型","E_NameMin":"XCOR太空飞船模型 | 官方珍藏版，只接受批量定制，500架起订。","E_PriceTag":"960.00","E_Discount":"1","E_CompanyID":"4","E_CompanyName":null,"E_Img1":"/public/File/brandimg/20160526/57470f9654cc1.jpg"}]
     * E_RATE_VIDEO : [{"ID":"38","E_Code":null,"E_Name":"韩国妹纸性感热舞","E_ProductID":"0","E_Path":"http://biggar.file.alimmdn.com/video_20160527171058200","E_Language":"cn","E_CreateUser":"雪儿","E_CreateDate":"1464340374","E_UpdateUser":null,"E_UpdateDate":"1464340374","E_CreateIP":"59.41.65.42","E_State":"1","E_Index":"50","E_MemberID":"8","E_TypeVal":"0","E_BrandID":"0","E_Flags":"1","E_VLeng":"30","E_Points":"0","E_Plays":"253","E_Img1":"http://biggar.image.alimmdn.com/cover_s20160527171057893","E_Content":"#比格视频秀# 网红赚钱的神奇啊！！！！！！","E_Comments":"0","E_Type":".Fashion","E_Concerns":"3","E_Img2":"http://biggar.image.alimmdn.com/first_s20160527171058037","E_Tags":"搭配潮人","E_GiftPoints":"0","E_Img3":null,"E_Share":"0"}]
     * E_RATE_COMMENT : 1
     * E_RATE_SHAREURL : http://192.168.1.113/app.php/Index/play_fenxiang.html?ID=4&SMID=0
     */

    private String ID;
    private String E_Code;
    private String E_Name;
    private String E_ProductID;
    private String E_Path;
    private String E_Language;
    private String E_CreateUser;
    private String E_CreateDate;
    private String E_UpdateUser;
    private String E_UpdateDate;
    private String E_CreateIP;
    private String E_State;
    private String E_Index;
    private String E_MemberID;
    private String E_TypeVal;
    private String E_BrandID;
    private String E_Flags;
    private String E_VLeng;
    private String E_Points;
    private int E_Plays;
    private String E_Img1;
    private String E_Content;
    private String E_Comments;
    private String E_Type;
    private String E_Concerns;
    private String E_Img2;
    private String E_GiftPoints;
    private String[] E_Img3;
    private String E_Share;
    private String E_RATE_COMMENT;
    private String E_RATE_SHAREURL;
    private List<String> E_Tags;
    private String E_Worker;
    private String E_VerWorker;

    public String getGift_nums() {
        return gift_nums;
    }

    public void setGift_nums(String gift_nums) {
        this.gift_nums = gift_nums;
    }

    private String gift_nums;

    public String getCounts() {
        return counts;
    }

    public void setCounts(String counts) {
        this.counts = counts;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    private String counts;//传播次数
    private String price;//传播价值

    /**
     * ID : 90
     * E_Name : XCOR舒脊坐垫
     * E_NameMin : 塑臀娇姿，时尚耐用
     * E_PriceTag : 999.00
     * E_Discount : 1
     * E_CompanyID : 4
     * E_CompanyName : null
     * E_Img1 : /public/File/brandimg/20160526/57470b00cdcc6.jpg
     */

    private List<ERATEPRODUCTBean> E_RATE_PRODUCT;
    private List<GoodsData> goods_data;

    public String getE_Worker() {
        return E_Worker;
    }

    public void setE_Worker(String e_Worker) {
        E_Worker = e_Worker;
    }

    public String getE_VerWorker() {
        return E_VerWorker;
    }

    public void setE_VerWorker(String e_VerWorker) {
        E_VerWorker = e_VerWorker;
    }

    /**
     * ID : 38
     * E_Code : null
     * E_Name : 韩国妹纸性感热舞
     * E_ProductID : 0
     * E_Path : http://biggar.file.alimmdn.com/video_20160527171058200
     * E_Language : cn
     * E_CreateUser : 雪儿
     * E_CreateDate : 1464340374
     * E_UpdateUser : null
     * E_UpdateDate : 1464340374
     * E_CreateIP : 59.41.65.42
     * E_State : 1
     * E_Index : 50
     * E_MemberID : 8
     * E_TypeVal : 0
     * E_BrandID : 0
     * E_Flags : 1
     * E_VLeng : 30
     * E_Points : 0
     * E_Plays : 253
     * E_Img1 : http://biggar.image.alimmdn.com/cover_s20160527171057893
     * E_Content : #比格视频秀# 网红赚钱的神奇啊！！！！！！
     * E_Comments : 0
     * E_Type : .Fashion
     * E_Concerns : 3
     * E_Img2 : http://biggar.image.alimmdn.com/first_s20160527171058037
     * E_Tags : 搭配潮人
     * E_GiftPoints : 0
     * E_Img3 : null
     * E_Share : 0
     */

    private List<ERATEVIDEOBean> E_RATE_VIDEO;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getE_Code() {
        return E_Code;
    }

    public void setE_Code(String E_Code) {
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

    public int getE_Plays() {
        return E_Plays;
    }

    public void setE_Plays(int E_Plays) {
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

    public String getE_GiftPoints() {
        return E_GiftPoints;
    }

    public void setE_GiftPoints(String E_GiftPoints) {
        this.E_GiftPoints = E_GiftPoints;
    }

    public String[] getE_Img3() {
        return E_Img3;
    }

    public void setE_Img3(String[] E_Img3) {
        this.E_Img3 = E_Img3;
    }

    public String getE_Share() {
        return E_Share;
    }

    public void setE_Share(String E_Share) {
        this.E_Share = E_Share;
    }

    public String getE_RATE_COMMENT() {
        return E_RATE_COMMENT;
    }

    public void setE_RATE_COMMENT(String E_RATE_COMMENT) {
        this.E_RATE_COMMENT = E_RATE_COMMENT;
    }

    public String getE_RATE_SHAREURL() {
        return E_RATE_SHAREURL;
    }

    public void setE_RATE_SHAREURL(String E_RATE_SHAREURL) {
        this.E_RATE_SHAREURL = E_RATE_SHAREURL;
    }

    public List<String> getE_Tags() {
        return E_Tags;
    }

    public void setE_Tags(List<String> E_Tags) {
        this.E_Tags = E_Tags;
    }

    public List<GoodsData> getGoods_data() {
        return goods_data;
    }

    public void setGoods_data(List<GoodsData> goods_data) {
        this.goods_data = goods_data;
    }

    public List<ERATEPRODUCTBean> getE_RATE_PRODUCT() {
        return E_RATE_PRODUCT;
    }

    public void setE_RATE_PRODUCT(List<ERATEPRODUCTBean> E_RATE_PRODUCT) {
        this.E_RATE_PRODUCT = E_RATE_PRODUCT;
    }

    public List<ERATEVIDEOBean> getE_RATE_VIDEO() {
        return E_RATE_VIDEO;
    }

    public void setE_RATE_VIDEO(List<ERATEVIDEOBean> E_RATE_VIDEO) {
        this.E_RATE_VIDEO = E_RATE_VIDEO;
    }

    public static class GoodsData{
        private String ID;
        private String E_Name;
        private String E_Img1;
        private String E_SellPrice;
        private String E_Url;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getE_Name() {
            return E_Name;
        }

        public void setE_Name(String e_Name) {
            E_Name = e_Name;
        }

        public String getE_Img1() {
            return E_Img1;
        }

        public void setE_Img1(String e_Img1) {
            E_Img1 = e_Img1;
        }

        public String getE_SellPrice() {
            return E_SellPrice;
        }

        public void setE_SellPrice(String e_SellPrice) {
            E_SellPrice = e_SellPrice;
        }

        public String getE_Url() {
            return E_Url;
        }

        public void setE_Url(String e_Url) {
            E_Url = e_Url;
        }
    }

    public static class ERATEPRODUCTBean implements Serializable{
        private String ID;
        private String E_Name;
        private String E_NameMin;
        private String E_PriceTag;
        private String E_Discount;
        private String E_CompanyID;
        private String E_CompanyName;
        private String E_Img1;

        public String getE_Img2() {
            return E_Img2;
        }

        public void setE_Img2(String e_Img2) {
            E_Img2 = e_Img2;
        }

        private String E_Img2;
        private String E_Url;

        public String getE_TaobaoLing() {
            return E_TaobaoLing;
        }

        public void setE_TaobaoLing(String e_TaobaoLing) {
            E_TaobaoLing = e_TaobaoLing;
        }

        public String getE_Type() {
            return E_Type;
        }

        public void setE_Type(String e_Type) {
            E_Type = e_Type;
        }

        private String E_TaobaoLing;
        private String E_Type;


        private String new_price;//折后价格  服务器不返回

        public String getE_Url() {
            return E_Url;
        }

        public void setE_Url(String e_Url) {
            E_Url = e_Url;
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

        public String getE_NameMin() {
            return E_NameMin;
        }

        public void setE_NameMin(String E_NameMin) {
            this.E_NameMin = E_NameMin;
        }

        public String getE_PriceTag() {
            return E_PriceTag;
        }

        public void setE_PriceTag(String E_PriceTag) {
            this.E_PriceTag = E_PriceTag;
        }

        public String getE_Discount() {
            return E_Discount;
        }

        public void setE_Discount(String E_Discount) {
            this.E_Discount = E_Discount;
        }

        public String getE_CompanyID() {
            return E_CompanyID;
        }

        public void setE_CompanyID(String E_CompanyID) {
            this.E_CompanyID = E_CompanyID;
        }

        public String getE_CompanyName() {
            return E_CompanyName;
        }

        public void setE_CompanyName(String E_CompanyName) {
            this.E_CompanyName = E_CompanyName;
        }

        public String getE_Img1() {
            return E_Img1;
        }

        public void setE_Img1(String E_Img1) {
            this.E_Img1 = E_Img1;
        }
    }

    public static class ERATEVIDEOBean implements Serializable{
        private String ID;
        private String E_Code;
        private String E_Name;
        private String E_ProductID;
        private String E_Path;
        private String E_Language;
        private String E_CreateUser;
        private String E_CreateDate;
        private String E_UpdateUser;
        private String E_UpdateDate;
        private String E_CreateIP;
        private String E_State;
        private String E_Index;
        private String E_MemberID;
        private String E_TypeVal;
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
        private String E_Img3;
        private String E_Share;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getE_Code() {
            return E_Code;
        }

        public void setE_Code(String E_Code) {
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

        public String getE_Img3() {
            return E_Img3;
        }

        public void setE_Img3(String E_Img3) {
            this.E_Img3 = E_Img3;
        }

        public String getE_Share() {
            return E_Share;
        }

        public void setE_Share(String E_Share) {
            this.E_Share = E_Share;
        }
    }
}
