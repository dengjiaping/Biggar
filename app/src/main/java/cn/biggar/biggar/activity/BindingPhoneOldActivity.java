package cn.biggar.biggar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.preference.AppPrefrences;
import cn.biggar.biggar.preference.Preferences;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zl on 2017/2/28.
 * 绑定的旧手机号码
 */

public class BindingPhoneOldActivity extends BiggarActivity {
    public String mPhoneNum;
    @BindView(R.id.binding_phone_tv)
    TextView mBindingPhoneTv;
    Context context;
    public static BindingPhoneOldActivity mFlag;
    public static Intent startIntent(Context context) {
        if (!AppPrefrences.getInstance(context).isLogined()) {
            return LoginActivity.startIntent(context);
        }
        Intent intent = new Intent(context, BindingPhoneOldActivity.class);

        return intent;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPhoneNum = Preferences.getUserBean(getActivity()).getE_Mobile();
        mPhoneNum = mPhoneNum.substring(0, mPhoneNum.length() - (mPhoneNum.substring(3)).length()) + "****" + mPhoneNum.substring(7);
        mBindingPhoneTv.setText("已绑定手机号:  " + mPhoneNum);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_binding_phone_old;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {


        context = this;
        mFlag = this;
    }

    @OnClick(R.id.edit_phone_tv)
    public void onClick() {
        startActivity(VerifyPasswordActivity.startIntent(getActivity()));
    }
}
