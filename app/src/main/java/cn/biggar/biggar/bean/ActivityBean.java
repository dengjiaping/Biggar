package cn.biggar.biggar.bean;

/**
 * Created by mx on 2016/8/11.
 * 通告
 */
public class ActivityBean implements Cloneable{

    /**
     * ID : 11
     * E_Name : 优质睡眠从心开始
     * E_Path : null
     * E_Img1 : /public/File/brandimg/20160714/57875e42b2af9.jpg
     * E_Img2 : null
     * E_Img3 : null
     * E_Leve : 0
     * E_Language : cn
     * E_CreateUser : sinomax
     * E_CreateDate : 2016-07-14 17:44:43
     * E_UpdateUser : sinomax
     * E_UpdateDate : 2016-07-26 11:21:06
     * E_CreateIP : 116.23.231.25
     * E_State : 1
     * E_Index : 0
     * E_pguid : null
     * E_NameVice : 这个暑假，这个夏天，你睡得好吗？
     * E_FinishProID :
     * E_FinishPro :
     * E_PayPoint : 0
     * E_FinishPoint : 100
     * E_TaskMemCount : 5
     * E_InOrderCount : 12
     * E_FinishDay : 60
     * E_MemLeve : 1-3
     * E_ExcisePoint : 10000
     * E_Content : 请上传各位大朋友小朋友宠物朋友的萌萌睡姿的视频，转发朋友圈。
     获得最高点击率的5位童鞋们，将会获得丰厚的奖励哦。
     大家快快上传各种萌萌睡姿吧！
     这个夏天，优质睡眠从心开始
     * E_BrandID : 6
     * E_VideoData : null
     * E_TaskType : 5
     * E_ExciseRate : 0
     * E_UseType : 1
     * E_TaskStartTime : 1468512000
     * E_TaskFinishPoint : 100
     * E_TaskFinishCounts : 5
     * E_TaskFinishBFB : ||||
     * E_CardData : null
     * E_BrandData : 101|103|105|107|123|128|129
     * E_Clicks : 4
     * E_Comments : 0
     * E_TaskMemPast : 0
     * E_Surplus : 5
     */

    private String E_BrandName;
    private String ID;
    private String E_Name;
    private Object E_Path;
    private String E_Img1;
    private Object E_Img2;
    private String E_Img3;
    private String E_Leve;
    private String E_Language;
    private String E_CreateUser;
    private String E_CreateDate;
    private String E_UpdateUser;
    private String E_UpdateDate;
    private String E_CreateIP;
    private String E_State;
    private String E_Index;
    private Object E_pguid;
    private String E_NameVice;
    private String E_FinishProID;
    private String E_FinishPro;
    private String E_PayPoint;
    private String E_FinishPoint;
    private int E_TaskMemCount;
    private String E_InOrderCount;
    private String E_FinishDay;
    private String E_MemLeve;
    private String E_ExcisePoint;
    private String E_Content;
    private String E_BrandID;
    private Object E_VideoData;
    private String E_TaskType;
    private String E_ExciseRate;
    private int E_UseType;//"1" 拍照 "2" 分享  "3"拍照和分享
    private String E_TaskStartTime;
    private int E_TaskFinishPoint;
    private String E_TaskFinishCounts;
    private String E_TaskFinishBFB;
    private Object E_CardData;
    private String E_BrandData;
    private String E_Clicks;
    private String E_Comments;
    private int E_TaskMemPast;
    private int E_Surplus;

    public int getE_OpenUrl() {
        return E_OpenUrl;
    }

    public void setE_OpenUrl(int e_OpenUrl) {
        E_OpenUrl = e_OpenUrl;
    }

    private int E_OpenUrl;
    private String E_ExciseSumPoint;

    public String getE_GoUrl() {
        return E_GoUrl;
    }

    public void setE_GoUrl(String e_GoUrl) {
        E_GoUrl = e_GoUrl;
    }

    private String E_GoUrl;

    public String getE_ExciseSumPoint() {
        return E_ExciseSumPoint;
    }

    public void setE_ExciseSumPoint(String e_ExciseSumPoint) {
        E_ExciseSumPoint = e_ExciseSumPoint;
    }

    public String getE_BrandName() {
        return E_BrandName;
    }

    public void setE_BrandName(String e_BrandName) {
        E_BrandName = e_BrandName;
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

    public Object getE_Path() {
        return E_Path;
    }

    public void setE_Path(Object E_Path) {
        this.E_Path = E_Path;
    }

    public String getE_Img1() {
        return E_Img1;
    }

    public void setE_Img1(String E_Img1) {
        this.E_Img1 = E_Img1;
    }

    public Object getE_Img2() {
        return E_Img2;
    }

    public void setE_Img2(Object E_Img2) {
        this.E_Img2 = E_Img2;
    }

    public String getE_Img3() {
        return E_Img3;
    }

    public void setE_Img3(String E_Img3) {
        this.E_Img3 = E_Img3;
    }

    public String getE_Leve() {
        return E_Leve;
    }

    public void setE_Leve(String E_Leve) {
        this.E_Leve = E_Leve;
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

    public Object getE_pguid() {
        return E_pguid;
    }

    public void setE_pguid(Object E_pguid) {
        this.E_pguid = E_pguid;
    }

    public String getE_NameVice() {
        return E_NameVice;
    }

    public void setE_NameVice(String E_NameVice) {
        this.E_NameVice = E_NameVice;
    }

    public String getE_FinishProID() {
        return E_FinishProID;
    }

    public void setE_FinishProID(String E_FinishProID) {
        this.E_FinishProID = E_FinishProID;
    }

    public String getE_FinishPro() {
        return E_FinishPro;
    }

    public void setE_FinishPro(String E_FinishPro) {
        this.E_FinishPro = E_FinishPro;
    }

    public String getE_PayPoint() {
        return E_PayPoint;
    }

    public void setE_PayPoint(String E_PayPoint) {
        this.E_PayPoint = E_PayPoint;
    }

    public String getE_FinishPoint() {
        return E_FinishPoint;
    }

    public void setE_FinishPoint(String E_FinishPoint) {
        this.E_FinishPoint = E_FinishPoint;
    }

    public int getE_TaskMemCount() {
        return E_TaskMemCount;
    }

    public void setE_TaskMemCount(int E_TaskMemCount) {
        this.E_TaskMemCount = E_TaskMemCount;
    }

    public String getE_InOrderCount() {
        return E_InOrderCount;
    }

    public void setE_InOrderCount(String E_InOrderCount) {
        this.E_InOrderCount = E_InOrderCount;
    }

    public String getE_FinishDay() {
        return E_FinishDay;
    }

    public void setE_FinishDay(String E_FinishDay) {
        this.E_FinishDay = E_FinishDay;
    }

    public String getE_MemLeve() {
        return E_MemLeve;
    }

    public void setE_MemLeve(String E_MemLeve) {
        this.E_MemLeve = E_MemLeve;
    }

    public String getE_ExcisePoint() {
        return E_ExcisePoint;
    }

    public void setE_ExcisePoint(String E_ExcisePoint) {
        this.E_ExcisePoint = E_ExcisePoint;
    }

    public String getE_Content() {
        return E_Content;
    }

    public void setE_Content(String E_Content) {
        this.E_Content = E_Content;
    }

    public String getE_BrandID() {
        return E_BrandID;
    }

    public void setE_BrandID(String E_BrandID) {
        this.E_BrandID = E_BrandID;
    }

    public Object getE_VideoData() {
        return E_VideoData;
    }

    public void setE_VideoData(Object E_VideoData) {
        this.E_VideoData = E_VideoData;
    }

    public String getE_TaskType() {
        return E_TaskType;
    }

    public void setE_TaskType(String E_TaskType) {
        this.E_TaskType = E_TaskType;
    }

    public String getE_ExciseRate() {
        return E_ExciseRate;
    }

    public void setE_ExciseRate(String E_ExciseRate) {
        this.E_ExciseRate = E_ExciseRate;
    }

    public int getE_UseType() {
        return E_UseType;
    }

    public void setE_UseType(int E_UseType) {
        this.E_UseType = E_UseType;
    }

    public String getE_TaskStartTime() {
        return E_TaskStartTime;
    }

    public void setE_TaskStartTime(String E_TaskStartTime) {
        this.E_TaskStartTime = E_TaskStartTime;
    }

    public int getE_TaskFinishPoint() {
        return E_TaskFinishPoint;
    }

    public void setE_TaskFinishPoint(int E_TaskFinishPoint) {
        this.E_TaskFinishPoint = E_TaskFinishPoint;
    }

    public String getE_TaskFinishCounts() {
        return E_TaskFinishCounts;
    }

    public void setE_TaskFinishCounts(String E_TaskFinishCounts) {
        this.E_TaskFinishCounts = E_TaskFinishCounts;
    }

    public String getE_TaskFinishBFB() {
        return E_TaskFinishBFB;
    }

    public void setE_TaskFinishBFB(String E_TaskFinishBFB) {
        this.E_TaskFinishBFB = E_TaskFinishBFB;
    }

    public Object getE_CardData() {
        return E_CardData;
    }

    public void setE_CardData(Object E_CardData) {
        this.E_CardData = E_CardData;
    }

    public String getE_BrandData() {
        return E_BrandData;
    }

    public void setE_BrandData(String E_BrandData) {
        this.E_BrandData = E_BrandData;
    }

    public String getE_Clicks() {
        return E_Clicks;
    }

    public void setE_Clicks(String E_Clicks) {
        this.E_Clicks = E_Clicks;
    }

    public String getE_Comments() {
        return E_Comments;
    }

    public void setE_Comments(String E_Comments) {
        this.E_Comments = E_Comments;
    }

    public int getE_TaskMemPast() {
        return E_TaskMemPast;
    }

    public void setE_TaskMemPast(int E_TaskMemPast) {
        this.E_TaskMemPast = E_TaskMemPast;
    }

    public int getE_Surplus() {
        return E_Surplus;
    }

    public void setE_Surplus(int E_Surplus) {
        this.E_Surplus = E_Surplus;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        ActivityBean sc = null;
        try
        {
            sc = (ActivityBean) super.clone();
        } catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        return sc;
    }
}
