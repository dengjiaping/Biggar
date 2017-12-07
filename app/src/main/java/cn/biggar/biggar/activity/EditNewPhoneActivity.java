package cn.biggar.biggar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import per.sue.gear2.utils.VerifyUtils;

/**
 * Created by zl on 2017/2/28.
 * 绑定新手机号码
 */

public class EditNewPhoneActivity extends BiggarActivity {
    @BindView(R.id.edit_login_password)
    ClearEditText editLoginPassword;
    @BindView(R.id.next_button_tv)
    TextView nextButtonTv;
    private CommonPresenter mCommonP;
    public static EditNewPhoneActivity mFlag;
    public static Intent startIntent(Context context) {
        Intent intent = new Intent(context, EditNewPhoneActivity.class);
        return intent;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_edit_new_phone;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        editLoginPassword.addTextChangedListener(new DetectionEditView(getActivity(),editLoginPassword,nextButtonTv));
        mCommonP = new CommonPresenter(getActivity());
        mFlag = this;
    }

    @OnClick(R.id.next_button_tv)
    public void onClick() {
        final String mPhoneNum = editLoginPassword.getText().toString();
        if (vertifyInput(mPhoneNum)){
            mCommonP.getVerifyPassword(Preferences.getUserBean(getActivity()).getId(), "",mPhoneNum, new OnObjectListener<String>() {
                @Override
                public void onSuccess(String result) {
                    super.onSuccess(result);
                    dismissLoading();

                    startActivity(BindingNewPhoneActivity.startIntent(getActivity(),mPhoneNum));
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
    public boolean vertifyInput(String phone){
        boolean canSubmit = true;
        if (!VerifyUtils.isMobileNo(phone)){
            ToastUtils.showError("手机格式错误");
            canSubmit = false;
        }
        return canSubmit;
    }


}
