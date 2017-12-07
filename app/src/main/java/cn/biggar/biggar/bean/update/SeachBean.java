package cn.biggar.biggar.bean.update;

import java.util.List;

/**
 * Created by Chenwy on 2017/9/20.
 */

public class SeachBean {
    public List<Member> member;
    public List<Video> video;

    public static class Member {
        public String E_HeadImg;
        public String ID;
        public String E_Name;
    }

    public static class Video {
        public String ID;
        public String E_Name;
        public String E_Path;
        public String E_TypeVal;
        public String E_Img1;
        public String E_Plays;
        public String E_Title;
        public String E_HeadImg;
        public String E_MemberID;
    }
}
