package cn.biggar.biggar.utils;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 张炼 on 2016-08-06.
 * 时间戳与时间格式互转
 */
public class TimeTransform {
    /**
     * 时间戳转成时间格式
     *
     * @param timestamp
     * @return
     */
    public static String TimestampToTime(String timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sd = sdf.format(new Date(Long.parseLong(timestamp) * 1000L));
        return sd;
    }

    /**
     * 时间戳 转 详细时间
     *
     * @param timestamp
     * @return
     */
    public static String TimestampToTimeDetails(String timestamp) {
        if(TextUtils.isEmpty(timestamp)){
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
        String sd = formatter.format(new Date(Long.parseLong(timestamp) * 1000L));
        return sd;
    }

    /**
     * 转化时间
     * @param timestamp  时间戳
     * @return
     */
    public static Date getDateBySeconds(long timestamp){
        return new Date(timestamp*1000L);
    }

    /**
     * 转化时间
     * @param timestamp  时间戳
     * @return
     */
    public static Date getDateBySeconds(String timestamp){
        try {
            return new Date(Integer.parseInt(timestamp) * 1000L);
        }catch (Exception e){
            return new Date();
        }
    }

    /**
     * 转换 为 --  时分
     * @return
     */
    public static String TimestampToTimeForHm(Date date) {
        return new SimpleDateFormat("HH:mm").format(date);
    }

    /**
     * 转换 为 --  月日
     * @return
     */
    public static String TimestampToTimeForMd(Date date) {
        return new SimpleDateFormat("MM-dd").format(date);
    }



    /**
     * 把日期转为字符串
     */
    public static String ConverToStringForYMD(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    /**
     * 把日期转为字符串
     */
    public static String ConverToStringForMD_Hm(Date date) {
        DateFormat df = new SimpleDateFormat("MM-dd HH:mm");
        return df.format(date);
    }

    /**
     * 把字符串转为日期
     *
     * @param strDate
     * @return
     * @throws Exception
     */
    public static Date ConverToDateByYMD(String strDate) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.parse(strDate);
    }

    /**
     * 把字符串转为日期 YMDHMS
     * @param strDate
     * @return
     * @throws Exception
     */
    public static Date ConverToDateByYMDHMS(String strDate) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.parse(strDate);
    }

    /**
     * 根据年 月 获取对应的月份 天数
     */
    public static int getDaysByYearMonth(int year, int month) {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 判断两个时间  是不是同一天
     * @param day1
     * @param day2
     * @return
     */
    public static boolean isSameDay(Date day1, Date day2) {
        if (day1 == null || day2 == null)
            return false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String ds1 = sdf.format(day1);
        String ds2 = sdf.format(day2);
        if (ds1.equals(ds2)) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 判断两个时间  是不是同一天
     * @param day1
     * @param day2
     * @return
     */
    public static boolean isSameDay(String day1, String day2) {
        if (day1 == null || day2 == null)
            return false;

        if (day1.equals(day2)) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 获取当前日期
     */
    public static String CardTime() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date date=new Date();
        return sdf.format(date);
    }
    /**
     * 获取两个日期相差 天
     * @param A
     * @param B
     * @return
     */
    public static int getD2ForDay(Date A,Date B){
        long nd = 1000*24*60*60;//一天的毫秒数
        // 获得两个时间的毫秒时间差异
        long diff;
        long a=A.getTime();
        long b=B.getTime();
        if(a>b){
            diff=a-b;
        }else{
            diff=b-a;
        }
        // 计算差多少天
        int day = (int) (diff / nd);

        return day;
    }


    /**
     * 获取 简单的日期
     * @time 如果 time 为今天   则返回 时分  否 返回 月日
     * @return
     */
    public static String getSimpleDate(String time){
        String Strdate="";
        if (!TextUtils.isEmpty(time)){
            Date date=getDateBySeconds(time);
            if(isSameDay(new Date(),date)){
                Strdate=TimestampToTimeForHm(date);
            }else{
                Strdate=TimestampToTimeForMd(date);
            }
        }
        return Strdate;
    }

    /**
     * 获得时间戳
     * @return
     */
    public static String getTimeStamp(){

        long time=System.currentTimeMillis()/1000;//获取系统时间的10位的时间戳

        String  str=String.valueOf(time);

        return str;

    }
}
