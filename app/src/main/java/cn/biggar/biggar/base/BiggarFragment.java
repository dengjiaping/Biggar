package cn.biggar.biggar.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Unbinder;
import cn.biggar.biggar.dialog.LoadingDialog;
import cn.biggar.biggar.utils.TUtil;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.ParameterizedType;

import butterknife.ButterKnife;
import per.sue.gear2.ui.UIBaseFragment;

/**
 * Created by SUE on 2016/7/14 0014.
 */
public abstract class BiggarFragment<T extends BasePresenter> extends UIBaseFragment {
    private LoadingDialog mDialogLoading;// loading
    protected T mPresenter;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        if (isLoadEventBus()){
            EventBus.getDefault().register(this);
        }
        initPresenter();
        return view;
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


    public boolean isLoadEventBus(){
        return false;
    }

    @Override
    public void onDestroy() {
        dismissLoading();
        if (isLoadEventBus()){
            EventBus.getDefault().unregister(this);
        }
        unbinder.unbind();
        super.onDestroy();
    }

    /**
     * 不是  接口showLoading
     */
    @Override
    public void showLoading() {
        super.showLoading();
            if (mDialogLoading != null && !mDialogLoading.isShowing()) {
                mDialogLoading.show();
            } else if (mDialogLoading == null) {
                mDialogLoading = new LoadingDialog(getActivity());
                mDialogLoading.show();
            }
    }

    /**
     * 关闭
     */
    public void dismissLoading(){
        if(mDialogLoading!=null){
            mDialogLoading.dismiss();
            mDialogLoading=null;
        }
    }

    public int getStatusBarHeight() {
        int statusBarHeight = 0;
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }
}
