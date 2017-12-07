package cn.biggar.biggar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.presenter.CommonPresenter;
import cn.biggar.biggar.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import per.sue.gear2.presenter.OnObjectListener;
import per.sue.gear2.utils.VerifyUtils;

/**
 * 意见反馈
 */

public class FeedbackActivity extends BiggarActivity {
    @BindView(R.id.feed_back_content_et)
    EditText mContent;
    @BindView(R.id.feed_back_phone_et)
    EditText mPhone;
    @BindView(R.id.feed_back_emil_et)
    EditText mEmail;
    @BindView(R.id.feed_back_commit_et)
    TextView mBackCommit;
    String mContentText;
    String mPhoneText;
    String mEmilText;
    Context mContext;
    CommonPresenter mPersenter;
    private UserBean bean;

    public static Intent startIntent(Context context) {
        Intent intent = new Intent(context, FeedbackActivity.class);
        context.startActivity(intent);
        return intent;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_feed_back;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        mContext = this;
    }

    @OnClick(R.id.feed_back_commit_et)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.feed_back_commit_et:
                commit();
                break;
        }
    }

    private void commit() {
        mContentText = mContent.getText().toString().trim();
        mPhoneText = mPhone.getText().toString().trim();
        mEmilText = mEmail.getText().toString().trim();
        bean = Preferences.getUserBean(mContext);
        if (vertifyInput(mPhoneText, mEmilText)) {
            //请求借口
            showLoading();
            mPersenter = new CommonPresenter(mContext);
            mPersenter.feedBack(mContentText, mPhoneText, mEmilText, bean.getId(), bean.geteName(), new OnObjectListener<String>() {
                @Override
                public void onSuccess(String result) {
                    super.onSuccess(result);
                    dismissLoading();
                    ToastUtils.showNormal("反馈成功");
                }

                @Override
                public void onError(int code, String msg) {
                    super.onError(code, msg);
                    dismissLoading();
                    ToastUtils.showError("反馈失败");
                }
            });
        }
    }

    public boolean vertifyInput(String phomeText, String email) {
        boolean canSubmit = true;

        if (TextUtils.isEmpty(mContentText)) {
            ToastUtils.showError("请输入您宝贵意见");
            mContent.requestFocus();
            canSubmit = false;
        } else if (TextUtils.isEmpty(phomeText)) {
            ToastUtils.showError("手机号码不能为空");
            mPhone.requestFocus();
            canSubmit = false;
        } else if (!VerifyUtils.isMobileNo(phomeText)) {
            ToastUtils.showError("手机格式错误");
            mPhone.requestFocus();
            canSubmit = false;
        } else if (!VerifyUtils.isEmail(email)) {
            ToastUtils.showError("邮箱格式错误");
            mEmail.requestFocus();
            canSubmit = false;
        }
        return canSubmit;
    }

}
