package per.sue.gear2.utils;/*
* 文件名：
* 描 述：
* 作 者：ld
* 时 间：2015/11/13
* 版 权：锐思科技
*/

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class MetricsUtils {
    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int[] getScreenHight(Activity context) {
        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int[] arr = {0, 0};
        arr[0] = metrics.widthPixels;
        arr[1] = metrics.heightPixels;
        return arr;
    }

    /**
     * 获取应用高度
     */
    public static int[] getAppScreenHight(Activity context) {
        Rect outRect = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        int[] arr = {0, 0};
        arr[0] = outRect.width();
        arr[1] = outRect.height();
        return arr;
    }
    /**
     * 获取手机高度
     */
    public static int[] getScreenHight(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        int width2 = dm.widthPixels;
        int height2 = dm.heightPixels;
        // PhoneWH wh = new PhoneWH(width2, height2);
        int[] it = new int[2];
        it[0] = width2;
        it[1] = height2;
        return it;
    }
}
