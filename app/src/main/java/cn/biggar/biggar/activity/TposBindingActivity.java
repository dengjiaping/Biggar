package cn.biggar.biggar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.presenter.CommonPresenter;
import cn.biggar.biggar.presenter.UserPersenter;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.view.ClearEditText;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import cn.biggar.biggar.view.RootLayout;
import per.sue.gear2.presenter.OnObjectListener;
import per.sue.gear2.utils.VerifyUtils;

/**
 * Created by zl on 2017/3/1.
 * 第三方绑定
 */

public class TposBindingActivity extends BiggarActivity {

    @BindView(R.id.tpos_binding_mobile_et)
    ClearEditText mMobileEt;
    @BindView(R.id.tpos_binding_code_et)
    ClearEditText mCodeEt;
    @BindView(R.id.get_tpos_binding_code_number_tv)
    TextView mNumberTv;
    @BindView(R.id.get_tpos_binding_code_tv)
    TextView mBindingCodeTv;
    @BindView(R.id.set_login_password_ev)
    ClearEditText mLoginPasswordEv;
    @BindView(R.id.complete_phone_tv)
    TextView mComplete;
    private CommonPresenter mCommonP;
    private int mCodeTime = 60;
    private boolean canGetCode = true;
    private UserPersenter mUserP;
    String  mMobile;
    private int mType;
    private Handler mHandler = new Handler();
    private Runnable mTimeRun = new Runnable() {
        @Override
        public void run() {
            if (mCodeTime != 0) {
                mCodeTime--;
                String timeFormat = getResources().getString(R.string.regist_label_time_code);
                String timeFormatText = getResources().getString(R.string.regist_label_time_code_text);
                mNumberTv.setText(String.format(timeFormat, mCodeTime));
                mBindingCodeTv.setText(timeFormatText);
                mHandler.postDelayed(mTimeRun, 1000);
            } else {
                enableCodeView();
            }
        }
    };
    public static Intent startIntent(Context context) {
        Intent intent = new Intent(context, TposBindingActivity.class);
        return intent;
    }
    public static Intent startIntent(Context context,int type) {
        Intent intent = new Intent(context, TposBindingActivity.class);
        intent.putExtra("type",type);
        return intent;
    }


    @Override
    public int getLayoutResId() {
        return R.layout.activity_tpos_binding;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        mMobileEt.addTextChangedListener(new DetectionEditView(mMobileEt));
        mCodeEt.addTextChangedListener(new DetectionEditView(mCodeEt));
        mLoginPasswordEv.addTextChangedListener(new DetectionEditView(mLoginPasswordEv));
        mCommonP = new CommonPresenter(this);
        mUserP=new UserPersenter(this);
        mType = getIntent().getIntExtra("type",0);
        if (mType == 1 ) {
            RootLayout.getInstance(this).setOnRightClick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }
    @OnClick({R.id.get_tpos_binding_code_tv, R.id.complete_phone_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_tpos_binding_code_tv:
                getCode();
                break;
            case R.id.complete_phone_tv:
                complete();
                break;
        }
    }

    private void getCode() {
        mMobile = mMobileEt.getText().toString();
        if (canGetCode) {
            showLoading();
            mCommonP.getCode(mMobile, "BindMobile", new OnObjectListener<String>() {
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
        }else {
            ToastUtils.showError("请稍等");
        }
    }

    /**
     * 完成
     */
    private void complete() {
        final String phoneNumber = mMobileEt.getText().toString();
        String phoneCode = mCodeEt.getText().toString();
        String userPassword = mLoginPasswordEv.getText().toString();
        if (vertifyInput(phoneNumber, userPassword, phoneCode)) {
            showLoading();
            if (verifyCode(phoneNumber,phoneCode)){
                showLoading();
                mUserP.topsBindingPhone(phoneCode, phoneNumber,Preferences.getUserBean(getActivity()).getId(), userPassword, new OnObjectListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        super.onSuccess(result);
                        updataSuccess(phoneNumber);
                        dismissLoading();
                        ToastUtils.showNormal(result);
                    }

                    @Override
                    public void onError(int code, String msg) {
                        super.onError(code, msg);
                        enableCodeView();
                        dismissLoading();
                        ToastUtils.showError(msg);
                    }
                });
            }
        }
    }
    /**
     * 修改成功
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
                Preferences.storeUserBean(getActivity(),bean);
                finish();
                startActivityForResult(BindingPhoneOldActivity.startIntent(getActivity()),2);
                EventBus.getDefault().post(phoneNumber);//通知 悬赏通告fragment 更新UI
            }
        },1000);
    }


    boolean isOk = true;

    /**
     * 验证验证码
     */
    private boolean verifyCode(String phone,String code) {
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
    private boolean vertifyInput(String account, String password, String code) {
        boolean canSubmit = true;
        isCorrectPassWord(password);
        if (!VerifyUtils.isMobileNo(account)) {
            ToastUtils.showError("手机格式错误");
            mMobileEt.requestFocus();
            canSubmit = false;
        } else if (!VerifyUtils.isCode(code)) {
            ToastUtils.showError("验证码不正确，请重新输入");
            mCodeEt.requestFocus();
            canSubmit = false;
        } else if (password.length() < 6) {
            ToastUtils.showError("密码不能少于6位");
            mLoginPasswordEv.requestFocus();
            canSubmit = false;
        } else if (password.length() >= 16) {
            ToastUtils.showError("密码不能大于16位");
            mLoginPasswordEv.requestFocus();
            canSubmit = false;
        } else if (!isDigit) {
            ToastUtils.showError("密码必须是6-16位英文字母、数字、字符组合(不能是纯数字）");
            canSubmit = false;

        } else if (!isLetter) {
            ToastUtils.showError("密码必须是6-16位英文字母、数字、字符组合(不能是纯字母）");
            canSubmit = false;
        }
        return canSubmit;
    }
    /**
     * 检测密码是否还有数字和字母
     *
     * @param password
     * @return
     */
    boolean isDigit = false;//用来表示是否包含数字
    boolean isLetter = false;//用来表示是否包含字母

    private void isCorrectPassWord(String password) {
        for (int i = 0; i < password.length(); i++) { //循环遍历字符串
            if (Character.isDigit(password.charAt(i))) {     //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            }
            if (Character.isLetter(password.charAt(i))) {   //用char包装类中的判断字母的方法判断每一个字符
                isLetter = true;
            }
        }
    }

    public class DetectionEditView implements TextWatcher {
        EditText editText;

        public DetectionEditView(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (mMobileEt.getText().length() > 0 && mCodeEt.getText().length() > 0 && mLoginPasswordEv.getText().length() > 0) {
                mComplete.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.shape_rounded_corners_red_big));
                mComplete.setTextColor(getActivity().getResources().getColor(R.color.whites));
                mComplete.setClickable(true);
            } else {
                mComplete.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.shape_rad_border_gray_white));
                mComplete.setTextColor(getActivity().getResources().getColor(R.color.app_text_color_g));
                mComplete.setClickable(false);
            }
        }
    }
    private void notEnableCodeView() {
        mHandler.postDelayed(mTimeRun, 1000);
        canGetCode = false;
        mCodeTime = 60;
        mBindingCodeTv.setEnabled(false);
    }
    /**
     * 获取验证码初始化
     */
    private void enableCodeView() {
        mHandler.removeCallbacks(mTimeRun);
        mBindingCodeTv.setText(getResources().getText(R.string.regist_label_get_code));
        mBindingCodeTv.setEnabled(true);
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
