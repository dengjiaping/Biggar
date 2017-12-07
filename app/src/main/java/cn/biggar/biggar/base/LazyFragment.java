package cn.biggar.biggar.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.dialog.LoadingDialog;
import cn.biggar.biggar.utils.TUtil;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.ParameterizedType;

import butterknife.ButterKnife;
import per.sue.gear2.ui.UIBaseFragment;

/**
 * Created by Chenwy on 2017/4/26.
 */

public abstract class LazyFragment<T extends BasePresenter> extends Fragment {
    protected Context mContext;
    protected Activity mActivity;
    private View mRootView;

    protected boolean isVisible;
    private boolean isPrepared;
    private boolean isFirst;
    public T mPresenter;
    private Bundle savedInstanceState;
    private LoadingDialog mDialogLoading;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context.getApplicationContext();
        mActivity = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutResId(), container, false);
            isFirst = true;
            isPrepared = true;
        }
        ButterKnife.bind(this, mRootView);
        this.savedInstanceState = savedInstanceState;
        initPresenter();
        if (getUserVisibleHint()) {
            onVisible();
        }
        return mRootView;
    }

    //presenter 初始化
    private void initPresenter() {
        if (this instanceof BaseView &&
                this.getClass().getGenericSuperclass() instanceof ParameterizedType &&
                ((ParameterizedType) (this.getClass().getGenericSuperclass())).getActualTypeArguments().length > 0) {
            mPresenter = TUtil.getT(this, 0);
            if (mPresenter != null) {
                mPresenter.mContext = this.getActivity();
            }
            mPresenter.setView(this);
            Logger.e(this.getClass().getSimpleName() + " presenter 初始化成功...");
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
            Logger.e(this.getClass().getSimpleName() + " 可见...");
        } else {
            isVisible = false;
            Logger.e(this.getClass().getSimpleName() + " 不可见...");
        }
    }

    /**
     * showLoading
     */
    public void showLoading() {
        if (mDialogLoading != null && !mDialogLoading.isShowing()) {
            mDialogLoading.show();
        } else if (mDialogLoading == null) {
            mDialogLoading = new LoadingDialog(getActivity());
            mDialogLoading.show();
        }
    }

    /**
     * dismissLoading
     */
    public void dismissLoading() {
        if (mDialogLoading != null) {
            mDialogLoading.dismiss();
            mDialogLoading = null;
        }
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

    /**
     * 可见
     */
    private void onVisible() {
        lazy();
    }

    /**
     * 懒加载
     */
    private void lazy() {
        if (isPrepared && isVisible && isFirst) {
            if (isOpenEventBus()) {
                EventBus.getDefault().register(this);
            }
            lazyLoadData(savedInstanceState);
            isFirst = false;
        }
    }

    protected abstract int getLayoutResId();

    protected abstract void lazyLoadData(Bundle savedInstanceState);

    @Override
    public void onDestroy() {
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        if (isOpenEventBus()) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    public boolean isOpenEventBus() {
        return false;
    }
}
