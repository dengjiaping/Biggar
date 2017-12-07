package cn.biggar.biggar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.presenter.CommonPresenter;
import cn.biggar.biggar.presenter.UserPersenter;
import cn.biggar.biggar.utils.DetectionEditView;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.view.ClearEditText;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import per.sue.gear2.presenter.OnObjectListener;

/**
 * Created by zl on 2017/2/28.
 * 绑定新手机号
 */

public class BindingNewPhoneActivity extends BiggarActivity {
    @BindView(R.id.binding_new_phone_hint_tv)
    TextView mNewPhoneHintTv;
    @BindView(R.id.edit_code_ev)
    ClearEditText mCodeEv;
    @BindView(R.id.get_binding_code_tv)
    TextView getCodeTv;
    @BindView(R.id.commit_button_tv)
    TextView mButtonTv;
    @BindView(R.id.get_code_number_tv)
    TextView getCodeNumberTv;
    String mPhoneNum, mPhoneNum01;

    private CommonPresenter mCommonP;
    private int mCodeTime = 60;
    private boolean canGetCode = true;
    private UserPersenter mUserP;
    private Handler mHandler = new Handler();
    private Runnable mTimeRun = new Runnable() {
        @Override
        public void run() {
            if (mCodeTime != 0) {
                mCodeTime--;
                String timeFormat = getResources().getString(R.string.regist_label_time_code);
                String timeFormatText = getResources().getString(R.string.regist_label_time_code_text);
                getCodeNumberTv.setText(String.format(timeFormat, mCodeTime));
                getCodeTv.setText(timeFormatText);
                mHandler.postDelayed(mTimeRun, 1000);
            } else {
                enableCodeView();
            }
        }
    };

    public static Intent startIntent(Context context, String phone) {
        Intent intent = new Intent(context, BindingNewPhoneActivity.class);
        intent.putExtra("phone", phone);
        return intent;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_binding_new_phone;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        mPhoneNum = getIntent().getStringExtra("phone");
        mCommonP = new CommonPresenter(getActivity());
        mUserP = new UserPersenter(this);
        mCodeEv.addTextChangedListener(new DetectionEditView(getActivity(), mCodeEv, mButtonTv));
        if (!TextUtils.isEmpty(mPhoneNum)) {
            mPhoneNum01 = mPhoneNum.substring(0, mPhoneNum.length() - (mPhoneNum.substring(3)).length()) + "****" + mPhoneNum.substring(7);
            mNewPhoneHintTv.setText("点击按钮后，验证码短信将发送到：\n +86" + mPhoneNum01 + ",请在一分钟内输入验证码");
        }
    }

    @OnClick({R.id.get_binding_code_tv, R.id.commit_button_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_binding_code_tv:

                getCode();
                break;
            case R.id.commit_button_tv:
                submit();
                break;
        }
    }

    /**
     * 提交
     */
    private void submit() {
        String code = mCodeEv.getText().toString();
        if (TextUtils.isEmpty(code)) {
            ToastUtils.showError("请输入手机验证码");
            return;
        }
        showLoading();
        if (verifyCode(mPhoneNum, code)) {
            showLoading();
            mUserP.updatePhone(code, Preferences.getUserBean(getActivity()).getId(), mPhoneNum, new OnObjectListener<String>() {
                @Override
                public void onSuccess(String result) {
                    super.onSuccess(result);
                    updataSuccess(mPhoneNum);
                    dismissLoading();
                }

                @Override
                public void onError(int code, String msg) {
                    super.onError(code, msg);
                    updateDefeated();
                    dismissLoading();
                }
            });
        }
    }

    /**
     * 修改成功
     *
     * @param phoneNumber
     */
    private void updataSuccess(final String phoneNumber) {
        enableCodeView();
        ToastUtils.showNormal("您已成功绑定新手机号");
        dismissLoading();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                UserBean bean = Preferences.getUserBean(getActivity());
                bean.setE_Mobile(phoneNumber);
                Preferences.storeUserBean(getActivity(), bean);
                finish();
                EditNewPhoneActivity.mFlag.finish();
                BindingPhoneOldActivity.mFlag.finish();
                VerifyPasswordActivity.mFlag.finish();
                startActivityForResult(BindingPhoneOldActivity.startIntent(getActivity()), 2);
                EventBus.getDefault().post(phoneNumber);//通知 悬赏通告fragment 更新UI
            }
        }, 1000);
    }

    /**
     * 修改失败
     */
    private void updateDefeated() {
        ToastUtils.showError("绑定失败");
        enableCodeView();
        dismissLoading();
    }

    /**
     * 获得验证码
     */
    private void getCode() {
        if (canGetCode) {
            showLoading();
            mCommonP.getCode(mPhoneNum, "BindMobile", new OnObjectListener<String>() {
                @Override
                public void onSuccess(String result) {
                    super.onSuccess(result);
                    notEnableCodeView();
                    dismissLoading();
                }

                @Override
                public void onError(int code, String msg) {
                    super.onError(code, msg);
                    dismissLoading();
                    ToastUtils.showError(msg);
                }
            });
        } else {
            ToastUtils.showError("请稍等");
        }
    }

    boolean isOk = true;

    /**
     * 验证验证码
     */
    private boolean verifyCode(String phone, String code) {
        mCommonP.verifyCode(phone, code, new OnObjectListener<String>() {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                // 创建
                isOk = true;
                dismissLoading();
            }

            @Override
            public void onError(int code, String msg) {
                super.onError(code, msg);
                isOk = false;
                ToastUtils.showError("验证码错误");
                dismissLoading();
            }
        });
        return isOk;
    }

    private void notEnableCodeView() {
        mHandler.postDelayed(mTimeRun, 1000);
        canGetCode = false;
        mCodeTime = 60;
        getCodeTv.setEnabled(false);
    }

    /**
     * 获取验证码初始化
     */
    private void enableCodeView() {
        mHandler.removeCallbacks(mTimeRun);
        getCodeTv.setText(getResources().getText(R.string.regist_label_get_code));
        getCodeTv.setEnabled(true);
        canGetCode = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mHandler.removeCallbacks(mTimeRun);
        } catch (Exception e) {
        }
    }
}
