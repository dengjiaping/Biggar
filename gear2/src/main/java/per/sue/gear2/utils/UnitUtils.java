package per.sue.gear2.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by su on 2015/8/31.
 * email 125906374@qq.com
 */
public class UnitUtils {


    /**
     * 有问题 使用下面的
     * @param context
     * @param valuePx
     * @return
     */
    public static int px2dip(Context context, int valuePx)
    {
        return  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valuePx, context.getResources().getDisplayMetrics());
    }
    /**
     * 有问题  使用下面的
     * @param context
     * @param valueDp
     * @return
     */
    public static int dip2px(Context context, int valueDp)
    {
        return  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, valueDp, context.getResources().getDisplayMetrics());
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px_new(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip_new(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
