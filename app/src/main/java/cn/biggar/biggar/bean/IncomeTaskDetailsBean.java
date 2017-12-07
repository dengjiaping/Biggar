package cn.biggar.biggar.bean;

/**
 * Created by mx on 2016/11/10.
 * 收入and任务 详情
 */

public class IncomeTaskDetailsBean {
    /**
     * E_HeadImg : http://biggar.image.alimmdn.com/upload/042A33E8-08B7-46DB-9FD6-392A4FF15A5A
     * E_Name : 雪儿广告
     * E_Sex :
     * E_VerWorker : 0
     * E_MPoints : 500
     * E_MGPoints : 20
     * E_CreateDate : 1476997605
     * E_TaskID : 11
     * name : 优质睡眠从心开始
     * E_State
     */

    private String E_HeadImg;
    private String E_Name;
    private String E_Sex;
    private String E_VerWorker;
    private String E_MPoints;
    private String E_MGPoints;
    private String E_CreateDate;
    private String E_TaskID;
    private String name;
    private String E_MembID;

    public String getE_MembID() {
        return E_MembID;
    }

    public void setE_MembID(String e_MembID) {
        E_MembID = e_MembID;
    }

    public String getE_State() {
        return E_State;
    }

    public void setE_State(String e_State) {
        E_State = e_State;
    }

    private String E_State;

    private String time;//本地添加的

    private String date;//本地添加的

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getE_VerWorker() {
        return E_VerWorker;
    }

    public void setE_VerWorker(String E_VerWorker) {
        this.E_VerWorker = E_VerWorker;
    }

    public String getE_MPoints() {
        return E_MPoints;
    }

    public void setE_MPoints(String E_MPoints) {
        this.E_MPoints = E_MPoints;
    }

    public String getE_MGPoints() {
        return E_MGPoints;
    }

    public void setE_MGPoints(String E_MGPoints) {
        this.E_MGPoints = E_MGPoints;
    }

    public String getE_CreateDate() {
        return E_CreateDate;
    }

    public void setE_CreateDate(String E_CreateDate) {
        this.E_CreateDate = E_CreateDate;
    }

    public String getE_TaskID() {
        return E_TaskID;
    }

    public void setE_TaskID(String E_TaskID) {
        this.E_TaskID = E_TaskID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
