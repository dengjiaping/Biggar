package cn.biggar.biggar.bean;

/**
 * Created by mx on 2016/11/3.
 * 消息
 */
public class MessageBean {


    /**
     * E_Name : 小橙子
     * E_HeadImg : /public/File/MemHead/18675701257/20160526/rBACE1bFmjqRkpACAAAhelCm2_I094_200x200_3.jpg
     * E_CreateDate : 1470650882
     * E_Type : 9
     * E_Message : 我领了一个悬赏
     * E_Invitation 0普通消息  1加入公会同意   2 公会邀请 同意 or 拒绝   3加入公共拒绝
     * E_GuildID
     */

    private String E_Name;
    private String E_HeadImg;
    private String E_CreateDate;
    private String E_Type;
    private String E_Message;
    private String E_Invitation;
    private String E_GuildID;
    private String ID;
    private String E_ToMemberID;
    private String E_Img1;
    private String E_Url;
    private String E_RelaID;
    private String E_tid;
    private String count;
    private String E_Number;
    private String E_RelaType;
    private String E_AgentState;
    private String E_ObjID;
    private String E_VideoImg;
    private String E_TypeVal;

    public String getE_ObjID() {
        return E_ObjID;
    }

    public void setE_ObjID(String e_ObjID) {
        E_ObjID = e_ObjID;
    }

    public String getE_VideoImg() {
        return E_VideoImg;
    }

    public void setE_VideoImg(String e_VideoImg) {
        E_VideoImg = e_VideoImg;
    }

    public String getE_TypeVal() {
        return E_TypeVal;
    }

    public void setE_TypeVal(String e_TypeVal) {
        E_TypeVal = e_TypeVal;
    }

    public String getE_AgentState() {
        return E_AgentState;
    }

    public void setE_AgentState(String e_AgentState) {
        E_AgentState = e_AgentState;
    }

    public String getE_RelaType() {
        return E_RelaType;
    }

    public void setE_RelaType(String e_RelaType) {
        E_RelaType = e_RelaType;
    }

    public String getE_Number() {
        return E_Number;
    }

    public void setE_Number(String e_Number) {
        E_Number = e_Number;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    private String time;//时间 服务器没有此字段


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getE_ToMemberID() {
        return E_ToMemberID;
    }

    public void setE_ToMemberID(String e_ToMemberID) {
        E_ToMemberID = e_ToMemberID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getE_Img1() {
        return E_Img1;
    }

    public void setE_Img1(String e_Img1) {
        E_Img1 = e_Img1;
    }

    public String getE_Url() {
        return E_Url;
    }

    public void setE_Url(String e_Url) {
        E_Url = e_Url;
    }

    public String getE_Invitation() {
        return E_Invitation;
    }

    public void setE_Invitation(String e_Invitation) {
        E_Invitation = e_Invitation;
    }

    public String getE_GuildID() {
        return E_GuildID;
    }

    public void setE_GuildID(String e_GuildID) {
        E_GuildID = e_GuildID;
    }

    public String getE_Name() {
        return E_Name;
    }

    public void setE_Name(String E_Name) {
        this.E_Name = E_Name;
    }

    public String getE_HeadImg() {
        return E_HeadImg;
    }

    public void setE_HeadImg(String E_HeadImg) {
        this.E_HeadImg = E_HeadImg;
    }

    public String getE_CreateDate() {
        return E_CreateDate;
    }

    public void setE_CreateDate(String E_CreateDate) {
        this.E_CreateDate = E_CreateDate;
    }

    public String getE_Type() {
        return E_Type;
    }

    public void setE_Type(String E_Type) {
        this.E_Type = E_Type;
    }

    public String getE_Message() {
        return E_Message;
    }

    public void setE_Message(String E_Message) {
        this.E_Message = E_Message;
    }

    public String getE_RelaID() {
        return E_RelaID;
    }

    public void setE_RelaID(String e_RelaID) {
        E_RelaID = e_RelaID;
    }

    public String getE_tid() {
        return E_tid;
    }

    public void setE_tid(String e_tid) {
        E_tid = e_tid;
    }
}
