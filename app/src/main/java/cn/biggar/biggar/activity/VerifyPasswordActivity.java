package cn.biggar.biggar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.presenter.CommonPresenter;
import cn.biggar.biggar.utils.DetectionEditView;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.view.ClearEditText;

import butterknife.BindView;
import butterknife.OnClick;
import per.sue.gear2.presenter.OnObjectListener;

/**
 * Created by zl on 2017/2/28.
 * 验证密码
 */

public class VerifyPasswordActivity extends BiggarActivity {

    @BindView(R.id.edit_login_password)
    ClearEditText mEditLoginPassword;
    @BindView(R.id.next_button_tv)
    TextView mNextButtonTv;
    CommonPresenter mPersenter;
    public static VerifyPasswordActivity mFlag;
    public static Intent startIntent(Context context) {
        Intent intent = new Intent(context, VerifyPasswordActivity.class);

        return intent;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_verify_password;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        mEditLoginPassword.addTextChangedListener(new DetectionEditView(getActivity(),mEditLoginPassword,mNextButtonTv));
        mPersenter = new CommonPresenter(getActivity());
    }


    @OnClick({R.id.password_tip_tv, R.id.next_button_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.password_tip_tv:
                ForgetPwdActivity.startIntent(getActivity(), getActivity().getString(R.string.forget_password));
                break;
            case R.id.next_button_tv:
                String mPassWord = mEditLoginPassword.getText().toString();
                verifyPass(mPassWord);
                break;
        }
    }

    private void verifyPass(String password) {
        showLoading();
        mPersenter.getVerifyPassword(Preferences.getUserBean(getActivity()).getId(), password,"", new OnObjectListener<String>() {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                dismissLoading();
                startActivity(EditNewPhoneActivity.startIntent(getActivity()));
            }

            @Override
            public void onError(int code, String msg) {
                super.onError(code, msg);
                dismissLoading();
                ToastUtils.showError(msg);
            }
        });
    }

}
