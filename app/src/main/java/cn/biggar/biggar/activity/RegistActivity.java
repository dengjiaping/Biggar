package cn.biggar.biggar.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.biggar.biggar.R;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.presenter.CommonPresenter;
import cn.biggar.biggar.presenter.RegistPresenter;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.view.ClearEditText;


import butterknife.BindView;
import butterknife.OnClick;
import per.sue.gear2.presenter.OnObjectListener;
import per.sue.gear2.utils.VerifyUtils;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;


/*
* 文件名：
* 描 述：
* 作 者：苏昭强
* 时 间：2016/4/27
*/
public class RegistActivity extends BiggarActivity implements RegistPresenter.RegistView {


    @BindView(R.id.mobile_et)
    ClearEditText mobileEt;
    @BindView(R.id.code_et)
    EditText codeEt;
    @BindView(R.id.get_code_tv)
    TextView getCodeTv;
    @BindView(R.id.get_code_number_tv)
    TextView getCodeNumberTv;

    @BindView(R.id.password_et)
    ClearEditText passwordEt;

    RegistPresenter registPresenter;


    @BindView(R.id.regist_view)
    TextView mButton;
    public static RegistActivity mFlag;
    private int mCodeTime = 60;
    private boolean canGetCode = true;
    private CommonPresenter mCommonP;
    private Handler mHandler = new Handler();
    private Runnable mTimeRun = new Runnable() {
        @Override
        public void run() {
            if (mCodeTime != 1) {
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

    public static Intent startIntent(Context context) {
        Intent intent = new Intent(context, RegistActivity.class);
        return intent;
    }


    @Override
    public int getLayoutResId() {
        return R.layout.activity_register;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        registPresenter = new RegistPresenter(getActivity(), this);
        mFlag = this;
        editListener();
        mButton.setClickable(false);

    }


    private void editListener() {
        passwordEt.addTextChangedListener(new DetectionEditView(mobileEt));
        mobileEt.addTextChangedListener(new DetectionEditView(mobileEt));
        codeEt.addTextChangedListener(new DetectionEditView(codeEt));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @OnClick({R.id.get_code_tv, R.id.regist_view})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_code_tv:
                getCode();
                break;
            case R.id.regist_view:
                hideKeyboard();
                regist();
                break;

        }
    }

    private void regist() {
        final String phoneNumber = mobileEt.getText().toString();
        final String phoneCode = codeEt.getText().toString();
        final String userPassword = passwordEt.getText().toString();

        if (vertifyInput(phoneNumber, userPassword, phoneCode)) {
            mCommonP = new CommonPresenter(getActivity());
            mCommonP.verifyCode(phoneNumber, phoneCode, new OnObjectListener<String>() {
                @Override
                public void onSuccess(String result) {
                    super.onSuccess(result);
                    getActivity().startActivity(RegisterSucceedActivity.startActivity(getActivity(), phoneNumber, phoneCode, userPassword));
                    notEnableCodeView();
                }

                @Override
                public void onError(int code, String msg) {
                    super.onError(code, msg);
                    ToastUtils.showError(msg);
                }
            });
        }

    }


    @Override
    public void onRegistSucess(UserBean userBean) {

    }

    @Override
    public void onError(int code, String message) {
        dismissLoading();
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onCode(String code) {
        dismissLoading();
        notEnableCodeView();
    }

    /**
     * 获取验证码
     */
    private void getCode() {
        String phoneNumber = mobileEt.getText().toString();
        if (VerifyUtils.isMobileNo(phoneNumber)) {
            if (canGetCode) {
                showLoading();
                registPresenter.getCode(phoneNumber,"userreg");

            } else {
                ToastUtils.showNormal("请稍等");
            }
        } else {
            ToastUtils.showError("手机格式错误");

            mobileEt.requestFocus();
        }
    }

    /**
     * 获取验证码初始化
     */
    private void enableCodeView() {
        mHandler.removeCallbacks(mTimeRun);
        getCodeTv.setText(getResources().getText(R.string.regist_label_again_get_code));
        getCodeNumberTv.setText("");
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

    private void notEnableCodeView() {
        mHandler.postDelayed(mTimeRun, 1000);
        canGetCode = false;
        mCodeTime = 60;
        getCodeTv.setEnabled(false);
    }


    public boolean vertifyInput(String account, String password, String code) {
        boolean canSubmit = true;
        isCorrectPassWord(password);
         if (!VerifyUtils.isMobileNo(account)) {
            ToastUtils.showError("手机格式错误");
            mobileEt.requestFocus();
            canSubmit = false;
        } else if (!VerifyUtils.isCode(code)) {
            ToastUtils.showError("验证码不正确，请重新输入");
            codeEt.requestFocus();
            canSubmit = false;
        } else if (password.length() < 6) {
            ToastUtils.showError("密码不能少于6位");
            passwordEt.requestFocus();
            canSubmit = false;
        } else if (password.length() >= 16) {
            ToastUtils.showError("密码不能大于16位");
            passwordEt.requestFocus();
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

    public void isCorrectPassWord(String password) {
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
            if (mobileEt.getText().length() > 0 && codeEt.getText().length() > 0 && passwordEt.getText().length() > 0) {
                mButton.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.shape_rounded_corners_red_big));
                mButton.setTextColor(getActivity().getResources().getColor(R.color.whites));
                mButton.setClickable(true);
            } else {
                mButton.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.shape_rad_border_gray_white));
                mButton.setTextColor(getActivity().getResources().getColor(R.color.app_text_color_g));
                mButton.setClickable(false);
            }
        }
    }
}
