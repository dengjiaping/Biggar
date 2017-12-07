package cn.biggar.biggar.bean;

/**
 * Created by SUE on 2016/7/20 0020.
 */
public class BrandBean  {


    /**
     * ID : 5
     * E_Logo : /public/File/brandimg/20160628/5772684951043.jpg
     * E_Img3 : /public/File/brandimg/20160607/57565150b5af8.jpg
     * E_CompanyTitle : SOUL首傲耳机
     * E_BrandCnName : soul首傲耳机
     */

    private String ID;
    private String E_BrandID;
    private String E_Logo;
    private String E_Img3;
    private String E_CompanyTitle;
    private String E_BrandCnName;
    private String E_Num;

    public String getE_Num() {
        return E_Num;
    }

    public void setE_Num(String e_Num) {
        E_Num = e_Num;
    }

    public String getE_BrandID() {
        return E_BrandID;
    }

    public void setE_BrandID(String e_BrandID) {
        E_BrandID = e_BrandID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getE_Logo() {
        return E_Logo;
    }

    public void setE_Logo(String E_Logo) {
        this.E_Logo = E_Logo;
    }

    public String getE_Img3() {
        return E_Img3;
    }

    public void setE_Img3(String E_Img3) {
        this.E_Img3 = E_Img3;
    }

    public String getE_CompanyTitle() {
        return E_CompanyTitle;
    }

    public void setE_CompanyTitle(String E_CompanyTitle) {
        this.E_CompanyTitle = E_CompanyTitle;
    }

    public String getE_BrandCnName() {
        return E_BrandCnName;
    }

    public void setE_BrandCnName(String E_BrandCnName) {
        this.E_BrandCnName = E_BrandCnName;
    }
}
