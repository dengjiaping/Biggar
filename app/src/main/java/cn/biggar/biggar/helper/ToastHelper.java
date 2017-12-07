package cn.biggar.biggar.helper;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import cn.biggar.biggar.R;
import es.dmoral.toasty.Toasty;

/**
 * Created by Chenwy on 2017/10/19.
 */

public class ToastHelper {
    private Context mContext;

    public ToastHelper() {
    }

    private static class ToastHolder {
        private static final ToastHelper TOAST_HELPER = new ToastHelper();
    }

    public static ToastHelper getInstance() {
        return ToastHolder.TOAST_HELPER;
    }


    public void init(Context context) {
        mContext = context;
        Toasty.Config.getInstance()
                .setSuccessColor(ContextCompat.getColor(mContext,R.color.colorPrimary))
                .setTextSize(14)
                .apply();
    }

    /**
     * 正常信息
     */
    public void showNormal(CharSequence message) {
        Toasty.normal(mContext, message).show();
    }

    /**
     * 成功信息
     */
    public void showSuccess(CharSequence message) {
        Toasty.success(mContext, message).show();
    }

    /**
     * 错误信息
     */
    public void showError(CharSequence message) {
        Toasty.error(mContext, message).show();
    }

    /**
     * 警告信息
     */
    public void showWarning(CharSequence message) {
        Toasty.warning(mContext, message).show();
    }
}
