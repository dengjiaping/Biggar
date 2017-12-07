package cn.biggar.biggar.utils;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import cn.biggar.biggar.R;
import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.app.Constants;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by mx on 2016/8/18.
 * 常用   工具类
 */
public class Utils {

    public static Integer parseInt(String strNum) {
        int num = 0;
        try {
            num = Integer.parseInt(strNum);
        } catch (NumberFormatException e) {
        }
        return num;
    }

    /**
     * 获取  真实的 url
     *
     * @param url
     * @return
     */
    public static String getRealUrl(String url) {
        if (url == null) {
            return "";
        }
        if (url.startsWith("http")) {
            return url;
        }
        return BaseAPI.BASE_RES_URL + url;
    }

    public static String getQiniuUrl(String key) {
        return BaseAPI.QINIU_HOST + key;
    }

    /**
     * 获取 缩略值
     *
     * @param numberVaule 可 int 的String
     * @return
     */
    public static String getBreviaryValue(String numberVaule) {
        String result = "0";
        try {
            int number = Integer.parseInt(numberVaule);
            if (number < 999) {
                return number + "";
            } else if (number < 9999) {
                return "999+";
            } else if (number < 99999) {
                return "1万+";
            } else if (number < 999999) {
                return "10万+";
            } else if (number < 9999999) {
                return "100万+";
            }

        } catch (Exception e) {
        }
        return result;
    }

    /**
     * 根据比格币  获取 RMB
     *
     * @param biggarCoin
     * @return
     */
    public static String getRmbByBiggarCoin(String biggarCoin) {
        if (!TextUtils.isEmpty(biggarCoin)) {
            try {
                int price = Integer.parseInt(biggarCoin);
                price = price / Constants.UNIT_BIGGAR;
                if (price >= 0) {
                    return price + "";
                }
            } catch (Exception e) {
            }
        }
        return "0";
    }

    /**
     * 地区拼接
     *
     * @param province
     * @param city
     * @param district
     * @return
     */
    public static String getAreaText(String province, String city, String district) {
        String ad;
        if (province.equals("北京") || province.equals("上海") || province.equals("天津") || province.equals("重庆")) {
            ad = province + "市" + district;
        } else {
            ad = province + "省" + city + "市" + district;
        }
        return ad;
    }

    public static boolean ListIsEmpty(List list) {
        if (list == null) {
            return true;
        } else {
            return list.isEmpty();
        }
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context mContext, float pxValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

    /**
     * 改变字符串其中指定字符的颜色
     *
     * @param str
     * @param start
     * @param end
     * @return
     */

    public static SpannableStringBuilder setTextColor(Context context, String str, int start, int end) {
        SpannableStringBuilder style = new SpannableStringBuilder(str);
        style.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorPrimary)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return style;
    }

    /**
     * 获取屏幕的宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        int width = dm.widthPixels;
        return width;
    }

    /**
     * 获取屏幕的高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        return wm.getDefaultDisplay().getHeight();
    }

    /**
     * 隐藏键盘
     *
     * @param view
     */
    public static void hideKeyboard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘

    }

    public static String mapSec2Time(int time) {
        if (time < 60) {
            return time + "秒";
        }
        if (time >= 60 && time < 3600) {
            int v = (int) (time / 60f);
            return v + "分";
        }
        int hour = (int) (time / 3600f);
        if (hour < 24) {
            int m = (int) (time % 3600f / 60f);
            String result = hour + "小时" + m + "分";
            if (m <= 0) {
                result = hour + "小时";
            }
            return result;
        }
        if (hour % 24 == 0) {
            return hour / 24 + "天";
        }

        String result;
        int day = hour / 24;
        int h = hour % 24;
        if (h <= 0) {
            result = day + "天";
        } else {
            result = day + "天" + h + "小时";
        }
        return result;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }
}
