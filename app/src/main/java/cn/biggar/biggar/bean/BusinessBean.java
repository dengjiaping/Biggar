package cn.biggar.biggar.bean;

/**
 * Created by mx on 2016/12/1.
 */

public class BusinessBean {


    /**
     * ID : 1271
     * E_User : 13415380642
     * E_Name : lost
     * E_VerWorker : 0
     * business_id : 41
     * is_business : 1
     * is_guild : 0
     * guild_id : 0
     */

    private String ID;
    private String E_User;
    private String E_Name;
    private String E_VerWorker;
    private String business_id;
    private int is_business;
    private String is_guild;
    private String guild_id;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getE_User() {
        return E_User;
    }

    public void setE_User(String E_User) {
        this.E_User = E_User;
    }

    public String getE_Name() {
        return E_Name;
    }

    public void setE_Name(String E_Name) {
        this.E_Name = E_Name;
    }

    public String getE_VerWorker() {
        return E_VerWorker;
    }

    public void setE_VerWorker(String E_VerWorker) {
        this.E_VerWorker = E_VerWorker;
    }

    public String getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
    }

    public int getIs_business() {
        return is_business;
    }

    public void setIs_business(int is_business) {
        this.is_business = is_business;
    }

    public String getIs_guild() {
        return is_guild;
    }

    public void setIs_guild(String is_guild) {
        this.is_guild = is_guild;
    }

    public String getGuild_id() {
        return guild_id;
    }

    public void setGuild_id(String guild_id) {
        this.guild_id = guild_id;
    }
}
