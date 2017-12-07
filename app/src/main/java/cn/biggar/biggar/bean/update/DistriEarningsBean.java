package cn.biggar.biggar.bean.update;

import java.util.List;

/**
 * Created by Chenwy on 2017/7/18.
 */

public class DistriEarningsBean {
    public String total_price;
    public List<DistriEarnings> list;
    public static class DistriEarnings{
        public String ID;
        public String E_ProID;
        public String E_CreateDate;
        public String E_UpdateDate;
        public String E_MembID;
        public String E_RateVal;
        public String E_BrandID;
        public String E_SellPrice;
        public String E_MembID1;
        public String E_MembID2;
        public String E_MembID3;
        public String E_State;
        public String E_OrderNo;
        public String E_Nums;
        public String E_Rate1;
        public String E_Rate2;
        public String E_Rate3;
        public String E_RatePrice1;
        public String E_RatePrice2;
        public String E_RatePrice3;
        public String E_BiggPrice;
        public String E_Name;
        public String E_Img1;
        public String E_UserName;
    }
}
