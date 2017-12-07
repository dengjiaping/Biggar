package cn.biggar.biggar.bean.update;

import java.util.List;

/**
 * Created by Chenwy on 2017/6/6.
 */

public class MoneyBean {
//    public String ID;
//    public String E_AddMess;
//    public String E_CreateDate;
//    public String E_Money;
//    public String E_Img;
    public List<Detail> detail;
    public String content;
    public static class Detail{
        public String E_Price;
        public String user_id;
        public String num;
        public String pid;
        public String date;
        public String E_Title;
        public String E_Name;
    }
}
