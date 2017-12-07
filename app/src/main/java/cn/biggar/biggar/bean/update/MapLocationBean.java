package cn.biggar.biggar.bean.update;

import java.io.Serializable;
import java.util.List;

import cn.biggar.biggar.utils.NumberUtils;

/**
 * Created by Chenwy on 2017/11/7.
 */

public class MapLocationBean implements Serializable {
    private static final long serialVersionUID = -908337434708818719L;

    public String level;
    public List<Arr> arr;

    public static class Arr implements Serializable {
        private static final long serialVersionUID = 2384141392742270673L;
        public String distance;
        public String E_Address;
        public String ID;

        /**
         * 店名
         */
        public String E_Name;
        /**
         * 经度
         */
        public String E_Add_X;

        /**
         * 纬度
         */
        public String E_Add_Y;

        /**
         * 背景图片
         */
        public String E_Img2;

        /**
         * 电话
         */
        public String E_Tel;

        public List<CardData> Card_data;

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Arr) {
                return ID.equals((((Arr) obj).ID));
            }
            return false;
        }
    }

    public static class CardData implements Serializable {
        private static final long serialVersionUID = -8824894156857042605L;
        public String E_Title;
        public String E_Spending;
        public String E_CardType;
        public String E_CardType2;
        public String E_Money;
    }
}
