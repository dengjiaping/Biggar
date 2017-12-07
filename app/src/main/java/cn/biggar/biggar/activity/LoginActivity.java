package cn.biggar.biggar.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.contract.LoginContract;
import cn.biggar.biggar.event.LoginSucessEvent;
import cn.biggar.biggar.preference.AppPrefrences;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.presenter.update.LoginPresenter;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.utils.Utils;
import cn.biggar.biggar.view.ClearEditText;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import butterknife.OnClick;
import cn.biggar.biggar.view.RootLayout;
import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;


public class LoginActivity extends BiggarActivity<LoginPresenter> implements LoginContract.View {
    public static int IS_BUSINESS = 1;//是从商家进来的
    private TextView mLoginButton;
    ClearEditText nameEditText, passwordEditText;
    private String mRegistrationID;
    int mState;
    private String userName = "";
    private String userPassword = "";
    private UserBean mUserBean;
    private String mPlatformStr;

    public static Intent startIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    public static Intent startIntent(Context context, int state) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("state", state);
        return intent;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        mRegistrationID = JPushInterface.getRegistrationID(this);
        initView();
    }

    private void initView() {
        mState = getIntent().getIntExtra("state", mState);
        mLoginButton = (TextView) findViewById(R.id.login_finish_button);
        nameEditText = (ClearEditText) findViewById(R.id.login_name_edit);
        passwordEditText = (ClearEditText) findViewById(R.id.login_password_edit);
        String account = AppPrefrences.getInstance(getApplication()).getLastAccount("");
        if (!TextUtils.isEmpty(account)) {
            nameEditText.setText(account);
        }

        RootLayout.getInstance(this).setOnRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(RegistActivity.startIntent(getActivity()));
            }
        });
        nameEditText.addTextChangedListener(new DetectionEditView(nameEditText));
        passwordEditText.addTextChangedListener(new DetectionEditView(passwordEditText));
        mLoginButton.setClickable(false);
    }

    @OnClick({R.id.login_finish_button, R.id.view_qq, R.id.view_sina, R.id.view_weixin, R.id.login_forget_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_finish_button:
                hideKeyboard();
                submit();
                break;
            case R.id.view_qq:
                loginByQQ();
                break;
            case R.id.view_sina:
                loginBySina();
                break;
            case R.id.view_weixin:
                loginByWeixin();
                break;
            case R.id.login_forget_button:
                ForgetPwdActivity.startIntent(getActivity(), getActivity().getString(R.string.forget_password));
                break;
            default:
                break;
        }
    }

    private void submit() {
        loginSource = Constants.BIGGAR_LOGIN;
        userName = nameEditText.getText().toString();
        userPassword = passwordEditText.getText().toString();
        showLoading();
        mPresenter.login(userName, userPassword, mRegistrationID);
    }

    @Override
    public void onError(int code, String message) {
        dismissLoading();
        ToastUtils.showError(message);
    }

    private SHARE_MEDIA platform;
    private MyUMAuthListener umAuthListener = new MyUMAuthListener();
    String loginSource;//登录来源

    private void loginByQQ() {
        loginSource = Constants.QQ_LOGIN;
        platform = SHARE_MEDIA.QQ;
        UMShareAPI.get(this).getPlatformInfo(this, platform, umAuthListener);
    }


    private void loginByWeixin() {
        loginSource = Constants.WEIXIN_LOGIN;
        platform = SHARE_MEDIA.WEIXIN;
        UMShareAPI.get(this).getPlatformInfo(this, platform, umAuthListener);
    }

    private void loginBySina() {
        loginSource = Constants.SINA_LOGIN;
        platform = SHARE_MEDIA.SINA;
        UMShareAPI.get(this).getPlatformInfo(this, platform, umAuthListener);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showError(String errMsg) {
        dismissLoading();
        ToastUtils.showError(errMsg);
    }

    @Override
    public void requestRongTokenSuccess(String rongToken) {
        Logger.e("融云token获取成功...");
        mPresenter.connectRong(rongToken);
    }

    @Override
    public void onTokenIncorrect() {
        mPresenter.requestRongToken(mUserBean.getId(), mUserBean.geteName(), Utils.getRealUrl(mUserBean.getE_HeadImg()));
    }

    @Override
    public void connectRongSuccess() {
        Logger.e("融云连接成功...");
        dismissLoading();
        RongIM.getInstance().setCurrentUserInfo(new UserInfo(mUserBean.getId(), mUserBean.geteName(), Uri.parse(mUserBean.getE_HeadImg())));
        RongIM.getInstance().setMessageAttachedUserInfo(true);

        String mHint;
        if (TextUtils.isEmpty(mPlatformStr)) {
            MobclickAgent.onProfileSignIn(mUserBean.getId());
            mHint = "登录成功";
        } else {
            MobclickAgent.onProfileSignIn(mPlatformStr, mUserBean.getId());
            if (TextUtils.isEmpty(mUserBean.getE_Mobile())) {
                startActivity(TposBindingActivity.startIntent(getActivity(), 1));
                mHint = "登录成功，请绑定手机号";
            } else {
                mHint = "登录成功";
            }
        }

        ToastUtils.showNormal(mHint);

        AppPrefrences.getInstance(getActivity()).setmLoginSource(loginSource);
        Preferences.storeUserBean(getApplication(), mUserBean);
        AppPrefrences.getInstance(getApplication()).setToken(mUserBean.getId());
        AppPrefrences.getInstance(getApplication()).setLastAccount(nameEditText.getText().toString());


        EventBus.getDefault().post(new LoginSucessEvent(mUserBean));
        finish();
    }

    @Override
    public void connectRongError(RongIMClient.ErrorCode errorCode) {
        dismissLoading();
        Logger.e(errorCode.getValue() + "\n" + errorCode.getMessage());
        ToastUtils.showError("聊天连接失败");
    }

    @Override
    public void loginSuccess(String platformStr, UserBean userBean) {
        mUserBean = userBean;
        mPlatformStr = platformStr;
        mPresenter.requestRongToken(userBean.getId(), userBean.geteName(), Utils.getRealUrl(userBean.getE_HeadImg()));
    }

    public class MyUMAuthListener implements UMAuthListener {

        @Override
        public void onStart(SHARE_MEDIA share_media) {
            showLoading();
            Logger.e("第三方授权登录开始...");
        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            dismissLoading();

            if (null == map) {
                Logger.e("map参数为空，登录错误!");
                return;
            }

            Logger.e("第三方授权登录成功，拿到参数后继续进行我们自己的登录...");

            showProgressDialog("正在登录...");
            String info = new Gson().toJson(map);

            Logger.e("map参数解析后数据为 ===> " + info);

            String openId = map.get("openid");
            if (openId == null) {
                openId = map.get("uid");
                if (openId == null) {
                    openId = map.get("unionid");
                }
            }
            String accessToken = map.get("access_token");
            afterTirdLoginSuccess(share_media, openId, accessToken, info);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            dismissLoading();
            Logger.e("第三方授权登录出错 ===> " + throwable.getMessage());
            LoginActivity.this.onError(-1, throwable.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            dismissLoading();
            Logger.e("取消第三方授权登录...");
        }
    }

    /**
     * 登录授权成功后
     */
    private void afterTirdLoginSuccess(final SHARE_MEDIA share_media, String openId, String accessToken, String info) {
        mPresenter.loginByThird(share_media.name(), openId, accessToken, info, mRegistrationID);
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
            if (nameEditText.getText().length() > 0 && passwordEditText.getText().length() > 0) {
                mLoginButton.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.shape_rounded_corners_red_big));
                mLoginButton.setTextColor(getActivity().getResources().getColor(R.color.whites));
                mLoginButton.setClickable(true);
            } else {
                mLoginButton.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.shape_rad_border_gray_white));
                mLoginButton.setTextColor(getActivity().getResources().getColor(R.color.app_text_color_g));
                mLoginButton.setClickable(false);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }
}
