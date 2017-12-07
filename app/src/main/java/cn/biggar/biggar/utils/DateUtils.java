package cn.biggar.biggar.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Chenwy on 2016/9/22.
 */

public class DateUtils {
    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        return sdf.format(curDate);
    }

    //时间戳转换成日期
    public static String getDate(String time) {
        Long timestamp = Long.parseLong(time) * 1000;
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINESE);
        String format = sdf.format(date);
        return format;
    }

    public static String formatDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d", Locale.CHINESE);
            String format = sdf.format(sdf.parse(date));
            return format;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
