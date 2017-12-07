package cn.biggar.biggar.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.TextView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.api.DataApiFactory;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.bean.AddressBean;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.dialog.TipsDialog;
import cn.biggar.biggar.event.LoginOutEvent;
import cn.biggar.biggar.preference.AppPrefrences;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.presenter.AddressPersenter;
import cn.biggar.biggar.presenter.CommonPresenter;
import cn.biggar.biggar.utils.StorageManager;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.utils.Utils;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import per.sue.gear2.presenter.OnObjectListener;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Subscriber;

/**
 * 设置界面
 * Created by SUE on 2016/8/15 0015.
 */
public class SettingActivity extends BiggarActivity implements EasyPermissions.PermissionCallbacks {

    @BindView(R.id.cache_size_tv)
    TextView mTvCacheSize;
    @BindView(R.id.address_tv)
    TextView addressTv;
    @BindView(R.id.bind_weibo_tv)
    TextView bindWeiboTv;
    @BindView(R.id.bind_QQ_tv)
    TextView bindQQTv;
    @BindView(R.id.bind_WX_tv)
    TextView bindWXTv;
    @BindView(R.id.edit_phone_tv)
    TextView mTvPhone;
    Context mContext;
    UserBean bean;
    private String userId = "";
    AddressPersenter persenter;
    CommonPresenter commonPresenter;
    List<String> bindList;

    public static Intent startIntent(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        return intent;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_settting;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        mContext = this;
        bean = Preferences.getUserBean(mContext);
        bindList = new ArrayList<>();
        if (bean != null) {
            init();
            userId = bean.getId();
        } else {
            mContext.startActivity(LoginActivity.startIntent(mContext));
        }
        persenter = new AddressPersenter(mContext);
        getDefaultAddress();
        commonPresenter = new CommonPresenter(getActivity());

    }

    @Override
    protected void onResume() {
        super.onResume();
        getDefaultAddress();
        record();
    }

    private void loadCacheSize() {
        try {
            String size = StorageManager.getInstance().getImageCacheSize(this);
            mTvCacheSize.setText(size);
            mTvCacheSize.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            mTvCacheSize.setVisibility(View.GONE);
        }
    }

    private void getDefaultAddress() {

        persenter.getDefaultAddress(bean.getId(), new OnObjectListener<AddressBean>() {
            @Override
            public void onSuccess(AddressBean result) {
                super.onSuccess(result);
                String mAddress = Utils.getAreaText(result.getE_prov(), result.getE_city(), result.getE_dist()) + result.getE_Adress();
                addressTv.setText(mAddress);
            }

            @Override
            public void onError(int code, String msg) {
                super.onError(code, msg);
            }
        });
    }

    private void init() {
        if (!TextUtils.isEmpty((String) bean.getE_RelationQQ())) {
            setViewState(bindQQTv);
            bindList.add(Constants.QQ_LOGIN);
        }
        if (!TextUtils.isEmpty((String) bean.getE_RelationWx())) {
            setViewState(bindWXTv);
            bindList.add(Constants.WEIXIN_LOGIN);
        }
        if (!TextUtils.isEmpty((String) bean.getE_RelationWb())) {
            setViewState(bindWeiboTv);
            bindList.add(Constants.SINA_LOGIN);
        }
        if (!TextUtils.isEmpty(bean.getE_Mobile())) {
            setViewState(mTvPhone);
            bindList.add(Constants.BIGGAR_LOGIN);
        }
    }


    /**
     * 设置绑定的状态显示相对应的视图
     *
     * @param view
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setViewState(TextView view) {
        view.setBackground(mContext.getResources().getDrawable(R.drawable.shape_squ_app_grey));
        view.setTextColor(mContext.getResources().getColor(R.color.app_text_color_z));
        view.setText("已绑定");

    }

    /**
     * 设置未绑定的状态显示相对应的视图
     *
     * @param view
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setUnViewState(TextView view, String platform) {
        view.setBackground(mContext.getResources().getDrawable(R.drawable.shape_squ_app_red));
        view.setTextColor(mContext.getResources().getColor(R.color.whites));
        view.setText("绑定");
        if (platform.equals("QQ")) {
            bean.setE_RelationQQ("");

        } else if (platform.equals("WEIXIN")) {
            bean.setE_Weixin("");

        } else if (platform.equals("SINA")) {
            bean.setE_RelationWb("");
        }
        bindList.remove(platform);
        Preferences.storeUserBean(getActivity(), bean);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    @OnClick({R.id.login_out_btn, R.id.user_info_tv, R.id.set_password_ll, R.id.address_ll,
            R.id.bind_weibo_tv, R.id.bind_QQ_tv, R.id.bind_WX_tv, R.id.feedback_ll,
            R.id.about_ll, R.id.fwipecache_ll, R.id.edit_phone_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_info_tv:
                startActivity(EditInfoActivity.startIntent(this));
                break;
            case R.id.set_password_ll:
                String mTitle = getActivity().getResources().getString(R.string.modify_password);
                ForgetPwdActivity.startIntent(getActivity(), mTitle);
                break;
            case R.id.address_ll:
                AddressManageActivity.startIntent(mContext);
                break;
            case R.id.bind_weibo_tv:
                if (bindWeiboTv.getText().toString().equals("绑定")) {
                    bindSina();
                } else {
                    removeBind("SINA", bindWeiboTv);
                }

                break;
            case R.id.bind_QQ_tv:
                if (bindQQTv.getText().toString().equals("绑定")) {
                    bindQQ();
                } else {
                    removeBind("QQ", bindQQTv);
                }

                break;
            case R.id.bind_WX_tv:
                if (bindWXTv.getText().toString().equals("绑定")) {
                    bindWeixin();
                } else {
                    removeBind("WEIXIN", bindWXTv);
                }
                break;

            case R.id.feedback_ll:

                FeedbackActivity.startIntent(mContext);
                break;
            case R.id.about_ll:

                AboutActivity.statrIntent(mContext);
                break;
            case R.id.login_out_btn:
                loginOutCheck();
                break;
            //清理缓存
            case R.id.fwipecache_ll:
                clearGlideCache();
                break;
            case R.id.edit_phone_ll:
                if (!TextUtils.isEmpty(bean.getE_Mobile())) {
                    startActivityForResult(BindingPhoneOldActivity.startIntent(this), 2);
                } else {
                    String mLoginSource = AppPrefrences.getInstance(getApplication()).getmLoginSource();
                    if (!TextUtils.isEmpty(mLoginSource)) {//考虑到用户直接更新  所以需重新登录
                        if (mLoginSource.equals(Constants.QQ_LOGIN) ||
                                mLoginSource.equals(Constants.WEIXIN_LOGIN) ||
                                mLoginSource.equals(Constants.SINA_LOGIN)) {
                            startActivity(TposBindingActivity.startIntent(getActivity()));
                        } else {
                            startActivityForResult(VerifyPasswordActivity.startIntent(this), 2);
                        }
                    } else {
                        startActivity(LoginActivity.startIntent(getActivity()));
                    }
                }
                break;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                boolean result = StorageManager.getInstance().delImageCache(SettingActivity.this);
                if (result) {
                    ToastUtils.showNormal("清理缓存成功");
                    loadCacheSize();
                }
            }
        }
    };

    private void clearGlideCache() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                Glide.get(SettingActivity.this).clearDiskCache();
                mHandler.sendEmptyMessage(1);
            }
        }.start();
    }

    /**
     * 解除绑定
     */
    private void removeBind(final String platform, final TextView view) {
        if (bindList.size() == 1) {
            ToastUtils.showError("唯一账号不能解绑");
            return;
        }

        for (int i = 0; i < bindList.size(); i++) {
            if (bindList.get(i).equals(platform)) {
                TipsDialog.newInstance("您确定要解除绑定吗？").setOnTipsOnClickListener(new TipsDialog.OnTipsOnClickListener() {
                    @Override
                    public void onSure() {
                        showLoading();
                        commonPresenter.unbindThird(bean.getId(), platform, new OnObjectListener<String>() {
                            @Override
                            public void onSuccess(String result) {
                                super.onSuccess(result);
                                dismissLoading();
                                ToastUtils.showNormal(result);
                                setUnViewState(view, platform);
                            }

                            @Override
                            public void onError(int code, String msg) {
                                super.onError(code, msg);
                                dismissLoading();
                                ToastUtils.showError(msg);
                            }
                        });
                    }

                    @Override
                    public void onCancel() {
                    }
                }).setMargin(52).show(getSupportFragmentManager());
            }

            continue;

        }

    }

    /**
     * 绑定QQ
     */
    public void bindQQ() {
        platform = SHARE_MEDIA.QQ;
        UMShareAPI.get(this).getPlatformInfo(getActivity(), platform, umAuthListener);
    }

    /**
     * 绑定微信
     */
    public void bindWeixin() {
        platform = SHARE_MEDIA.WEIXIN;
        UMShareAPI.get(this).getPlatformInfo(getActivity(), platform, umAuthListener);
    }

    /**
     * 绑定微博
     */
    public void bindSina() {
        platform = SHARE_MEDIA.SINA;
        UMShareAPI.get(this).getPlatformInfo(getActivity(), platform, umAuthListener);
    }

    private void loginOutCheck() {
        boolean authorizeQQ = UMShareAPI.get(mContext).isAuthorize(this, SHARE_MEDIA.QQ);
        boolean authorizeWX = UMShareAPI.get(mContext).isAuthorize(this, SHARE_MEDIA.WEIXIN);
        boolean authorizeWB = UMShareAPI.get(mContext).isAuthorize(this, SHARE_MEDIA.SINA);


        if (authorizeQQ) {
            Logger.e("授权了QQ...");
            UMShareAPI.get(this).deleteOauth(this, SHARE_MEDIA.QQ, deleteOauthListener);
        } else if (authorizeWX) {
            Logger.e("授权了微信...");
            UMShareAPI.get(this).deleteOauth(this, SHARE_MEDIA.WEIXIN, deleteOauthListener);
        } else if (authorizeWB) {
            Logger.e("授权了微博...");
            UMShareAPI.get(this).deleteOauth(this, SHARE_MEDIA.SINA, deleteOauthListener);
        } else {
            loginOut();
        }
    }

    private void loginOut() {
        RongIM.getInstance().logout();
        AppPrefrences.getInstance(getApplication()).setAccountType(1);
        //商家token
        AppPrefrences.getInstance(getApplication()).setMerchantToken("");
        AppPrefrences.getInstance(getApplication()).setToken("");
        Preferences.storeUserBean(getApplication(), null);
        MobclickAgent.onProfileSignOff();
        new Thread(new Runnable() {
            @Override
            public void run() {
                MobclickAgent.onKillProcess(SettingActivity.this);//用来保存统计数据
                CookieManager.getInstance().removeAllCookie();
                getActivity().deleteDatabase("webview.db");
                getActivity().deleteDatabase("webviewCache.db");
            }
        }).start();
        SPUtils.getInstance().put(Constants.CHOOSE_GIFT_ID, "");
        SPUtils.getInstance().put(Constants.CHOOSE_GIFT_ITEM, 0);
        EventBus.getDefault().post(new LoginOutEvent());
        ToastUtils.showNormal("退出成功");
        finish();
    }

    private UMAuthListener deleteOauthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            showLoading();
        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            Logger.e("授权登录解除成功");
            dismissLoading();
            loginOut();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            dismissLoading();
            ToastUtils.showError("授权登录解除失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            dismissLoading();
            ToastUtils.showNormal("取消授权登录解除");
        }
    };

    private SHARE_MEDIA platform;
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
        }

        @Override
        public void onComplete(final SHARE_MEDIA share_media, int i, Map<String, String> map) {

            if (null == map) {
                Logger.e("map参数为空，授权失败!");
                return;
            }

            showProgressDialog("正在绑定...");

            String openId = map.get("openid");
            if (openId == null) {
                openId = map.get("uid");
                if (openId == null) {
                    openId = map.get("unionid");
                }
            }
            final String accessToken = map.get("access_token");

            DataApiFactory.getInstance().createIUserAPI(mContext).bindThird(userId, share_media.name(), openId, accessToken)
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            dismissProgressDialog();
                            ToastUtils.showError(e.getMessage());
                        }

                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onNext(String s) {
                            dismissProgressDialog();
                            bindList.add(platform.toString());
                            ToastUtils.showNormal(s);
                            if (share_media.equals(SHARE_MEDIA.QQ)) {
                                setViewState(bindQQTv);
                                bean.setE_RelationQQ(accessToken);
                                Preferences.storeUserBean(mContext, bean);
                            } else if (share_media.equals(SHARE_MEDIA.WEIXIN)) {
                                setViewState(bindWXTv);
                                bean.setE_RelationWx(accessToken);
                                Preferences.storeUserBean(mContext, bean);
                            } else if (share_media.equals(SHARE_MEDIA.SINA)) {
                                setViewState(bindWeiboTv);
                                bean.setE_RelationWb(accessToken);
                                Preferences.storeUserBean(mContext, bean);
                            }
                        }
                    });
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            ToastUtils.showError(throwable.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {

        }
    };

    /**
     * 向服务器发送绑定请求
     */
//    private void bindToServer(String platform, String openId, String token) {
//        String url = BaseAPI.API_ACCOUNT_BIND_THIRD;
//        Map<String, String> params = new HashMap<>();
//        params.put("platform", platform);
//        params.put("openId", openId);
//        params.put("accessToken", token);
//        params.put("userId", userId);
//        OkGo.post(url)
//                .params(params)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(String s, Call call, Response response) {
//                        Logger.e(s);
//                    }
//                });
//    }

    protected static final int RC_CAMERA_PERM = 123;

    @AfterPermissionGranted(RC_CAMERA_PERM)
    public void record() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (EasyPermissions.hasPermissions(this, perms)) {
            loadCacheSize();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_storage),
                    RC_CAMERA_PERM, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    /**
     * 接受 消息数量的改变
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(String event) {
        setViewState(mTvPhone);
    }
}
