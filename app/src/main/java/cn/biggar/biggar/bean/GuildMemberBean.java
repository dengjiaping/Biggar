package cn.biggar.biggar.bean;

/**
 * Created by zl on 2016/10/27.
 * 公会成员bean
 */
public class GuildMemberBean {

    /**
     * ID : 7
     * E_Name : Liz酱
     * E_User : min981231
     * E_VerWorker : 0
     * E_Level : 1
     * E_Signature : 脱了衣服是禽兽，穿上衣服是衣冠禽兽！
     * E_LoginGonhun : 0
     * E_HeadImg : http://biggar.image.alimmdn.com/upload/8A057B71-B8DE-4EE5-8235-306F153FFDF9
     * E_Worker : null
     * is_concern : 4
     * is_qunzhu : 0
     * is_admin : 0
     */

    private String ID;
    private String E_Name;
    private String E_User;
    private String E_VerWorker;
    private String E_Level;
    private String E_Signature;
    private String E_LoginGonhun;
    private String E_HeadImg;
    private Object E_Worker;
    private int is_concern;
    private int is_qunzhu;
    private int is_admin;

    public String getE_Sex() {
        return E_Sex;
    }

    public void setE_Sex(String e_Sex) {
        E_Sex = e_Sex;
    }

    private String E_Sex;

    public String getE_LvName() {
        return E_LvName;
    }

    public void setE_LvName(String e_LvName) {
        E_LvName = e_LvName;
    }

    private String E_LvName;

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

    public String getE_User() {
        return E_User;
    }

    public void setE_User(String E_User) {
        this.E_User = E_User;
    }

    public String getE_VerWorker() {
        return E_VerWorker;
    }

    public void setE_VerWorker(String E_VerWorker) {
        this.E_VerWorker = E_VerWorker;
    }

    public String getE_Level() {
        return E_Level;
    }

    public void setE_Level(String E_Level) {
        this.E_Level = E_Level;
    }

    public String getE_Signature() {
        return E_Signature;
    }

    public void setE_Signature(String E_Signature) {
        this.E_Signature = E_Signature;
    }

    public String getE_LoginGonhun() {
        return E_LoginGonhun;
    }

    public void setE_LoginGonhun(String E_LoginGonhun) {
        this.E_LoginGonhun = E_LoginGonhun;
    }

    public String getE_HeadImg() {
        return E_HeadImg;
    }

    public void setE_HeadImg(String E_HeadImg) {
        this.E_HeadImg = E_HeadImg;
    }

    public Object getE_Worker() {
        return E_Worker;
    }

    public void setE_Worker(Object E_Worker) {
        this.E_Worker = E_Worker;
    }

    public int getIs_concern() {
        return is_concern;
    }

    public void setIs_concern(int is_concern) {
        this.is_concern = is_concern;
    }

    public int getIs_qunzhu() {
        return is_qunzhu;
    }

    public void setIs_qunzhu(int is_qunzhu) {
        this.is_qunzhu = is_qunzhu;
    }

    public int getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(int is_admin) {
        this.is_admin = is_admin;
    }
}
