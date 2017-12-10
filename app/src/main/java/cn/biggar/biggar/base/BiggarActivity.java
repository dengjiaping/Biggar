package cn.biggar.biggar.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import butterknife.Unbinder;
import cn.biggar.biggar.R;
import cn.biggar.biggar.dialog.LoadingDialog;
import cn.biggar.biggar.utils.AppConfig;
import cn.biggar.biggar.utils.TUtil;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.barlibrary.OSUtils;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.ParameterizedType;

import butterknife.ButterKnife;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import per.sue.gear2.ui.IBaseView;

/**
 * Created by SUE on 2016/7/14 0014.
 */
public abstract class BiggarActivity<T extends BasePresenter> extends SwipeBackActivity implements IBaseView {
    private LoadingDialog mDialogLoading;
    public T mPresenter;
    private Unbinder unbinder;
    protected ImmersionBar mImmersionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置布局
        setContentView(getLayoutResId());
        setSwipeBackEnable(isCanSwipeBack());
        //设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //注解
        unbinder = ButterKnife.bind(this);
        //沉浸式
        initImmersionBar();
        //eventbus
        if (isLoadEventBus()) {
            EventBus.getDefault().register(this);
        }
        //初始化Presenter
        this.initPresenter();
        MobclickAgent.enableEncrypt(true);//enable为true，SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
        onInitialize(savedInstanceState);
    }


    private void initPresenter() {
        if (this instanceof BaseView &&
                this.getClass().getGenericSuperclass() instanceof ParameterizedType &&
                ((ParameterizedType) (this.getClass().getGenericSuperclass())).getActualTypeArguments().length > 0) {
            mPresenter = TUtil.getT(this, 0);
            if (mPresenter != null) {
                mPresenter.mContext = this;
                mPresenter.setView(this);
                Logger.e(this.getClass().getSimpleName() + " presenter 初始化成功...");
            }
        }
    }

    protected void initImmersionBar() {
        if (ImmersionBar.isSupportStatusBarDarkFont() || Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mImmersionBar = ImmersionBar.with(this);
            mImmersionBar
                    .fitsSystemWindows(true, AppConfig.ENABLE_FEAST ? R.color.color_feast : R.color.whites)
                    .statusBarDarkFont(AppConfig.ENABLE_FEAST ? AppConfig.STATUS_BAR_DARK_FONT : true,
                            AppConfig.ENABLE_FEAST ? AppConfig.STATUS_BAR_ALPHA : 0.5f)
                    .keyboardEnable(keyboardEnable())
                    .keyboardMode(getKeyboardMode())
                    .init();
        }
    }

    protected boolean keyboardEnable() {
        return false;
    }


    protected int getKeyboardMode() {
        return WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
    }

    public boolean isLoadEventBus() {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        dismissLoading();
        unbinder.unbind();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        if (isLoadEventBus()) {
            EventBus.getDefault().unregister(this);
        }

        if (mImmersionBar != null) {
            mImmersionBar.destroy();
        }
        super.onDestroy();
    }

    protected void hideKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    protected boolean isKeyboardVisiable() {
        if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE) {
            return true;
        }
        return false;
    }


    protected void addFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    public void setEmptyView(View emptyView) {
        ((TextView) emptyView.findViewById(R.id.tv_empty_info)).setText("暂无内容");
    }

    public void setErrorView(View emptyView) {
        setErrorView(emptyView, null);
    }

    public void setErrorView(View emptyView, String errorMsg) {
        ((TextView) emptyView.findViewById(R.id.tv_empty_info))
                .setText(TextUtils.isEmpty(errorMsg) ? "请求失败，请检查网络" : errorMsg);
    }

    public boolean isCanSwipeBack() {
        //5.0 以上允许滑动返回
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }


    /**
     * show 加載dialog
     */
    public void showLoading() {
        if (mDialogLoading != null && !mDialogLoading.isShowing()) {
            mDialogLoading.show();
        } else if (mDialogLoading == null) {
            mDialogLoading = new LoadingDialog(this);
            mDialogLoading.show();
        }
    }

    /**
     * 关闭
     */
    public void dismissLoading() {
        if (mDialogLoading != null) {
            mDialogLoading.dismiss();
            mDialogLoading = null;
        }
    }

    public Activity getActivity() {
        return this;
    }

    public void onLoadFailed() {
    }

    protected void showProgressDialog(String tip) {
    }

    protected void dismissProgressDialog() {
    }

    public void onError(int code, String message) {
    }

    public void onCompleted() {
    }
}
