
package cn.biggar.biggar.utils;

import cn.biggar.biggar.helper.ToastHelper;

public class ToastUtils {

    public static void showError(CharSequence message) {
        ToastHelper.getInstance().showError(message);
    }

    public static void showNormal(CharSequence message) {
        ToastHelper.getInstance().showNormal(message);
    }


    public static void showWarning(CharSequence message) {
        ToastHelper.getInstance().showWarning(message);
    }
}
