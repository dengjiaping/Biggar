package cn.biggar.biggar.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Chenwy on 2017/7/31.
 */

public class NumberUtils {
    public static int parseInt(String numStr) {
        if (TextUtils.isEmpty(numStr)) {
            return 0;
        }
        return Integer.parseInt(numStr);
    }

    public static float parseFloat(String numStr) {
        if (TextUtils.isEmpty(numStr)) {
            return 0.0f;
        }
        return Float.parseFloat(numStr);
    }

    public static double parseDouble(String numStr) {
        if (TextUtils.isEmpty(numStr)) {
            return 0.0d;
        }
        return Double.parseDouble(numStr);
    }

    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

}
