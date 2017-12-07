package cn.biggar.biggar.activity.update;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.gyf.barlibrary.KeyboardPatch;
import com.orhanobut.logger.Logger;
import com.yinglan.keyboard.HideUtil;

import cn.biggar.biggar.R;
import cn.biggar.biggar.activity.MainActivity;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.bean.update.RongTokenBean;
import cn.biggar.biggar.helper.InitHelper;
import cn.biggar.biggar.http.BaseObjectObserver;
import cn.biggar.biggar.http.HttpMethods;
import cn.biggar.biggar.http.ParamsUtils;
import cn.biggar.biggar.http.RxSchedulers;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.utils.SHA1;
import cn.biggar.biggar.view.RootLayout;
import io.reactivex.disposables.Disposable;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.push.RongPushClient;

/**
 * Created by Chenwy on 2017/6/19.
 */

public class ChatActivity extends BiggarActivity {

    private String targetId;
    private String nickName;
    private String rongToken;
    private UserBean userBean;

    /**
     * 是否 不是从通知栏来的
     */
    private boolean isNotFromNotification;
    private Disposable disposable;

    @Override
    public int getLayoutResId() {
        return R.layout.conversation;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        HideUtil.init(this);
        targetId = getIntent().getData().getQueryParameter("targetId");
        nickName = getIntent().getData().getQueryParameter("title");
        String isNotFromNotificationStr = TextUtils.isEmpty(getIntent().getData().getQueryParameter("isNotFromNotification"))
                ? "0" : getIntent().getData().getQueryParameter("isNotFromNotification");
        isNotFromNotification = isNotFromNotificationStr.equals("0") ? false : true;
        Logger.e("isNotFromNotification : " + isNotFromNotification);

        if (!InitHelper.getInstance().isInit()) {
            InitHelper.getInstance().init(this);
        }

        rongToken = SPUtils.getInstance().getString(Constants.RONG_TOKEN);
        userBean = Preferences.getUserBean(this);

        RootLayout.getInstance(this)
                .setTitle(nickName)
                .setOnLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                });
        KeyboardPatch.patch(this, RootLayout.getInstance(this)).enable();
        RongPushClient.clearAllNotifications(this.getApplicationContext());
        checkRongTokenAndConnect();
    }

    @Override
    public void onBackPressed() {
        if (!isNotFromNotification) {
            startActivity(MainActivity.startIntent(this));
        }
        super.onBackPressed();
    }

    private void checkRongTokenAndConnect() {
        if (userBean != null) {
            if (TextUtils.isEmpty(rongToken)) {
                requestRongToken();
            } else {
                connectRong();
            }
        }
    }

    private void requestRongToken() {
        String appKey = Constants.RONG_APP_KEY;
        String appSecret = Constants.RONG_APP_SECRET;
        int nonce = (int) (Math.random() * 100);
        String timeStamp = String.valueOf(System.currentTimeMillis());

        byte[] sign = (appSecret + nonce + timeStamp).getBytes();
        String signature = new SHA1().getDigestOfString(sign);

        HttpMethods.getInstance().getApiService()
                .requestRongToken(
                        ParamsUtils
                                .header("App-Key", appKey)
                                .header("Nonce", String.valueOf(nonce))
                                .header("Timestamp", String.valueOf(timeStamp))
                                .header("Signature", signature)
                                .commitHeaders(),
                        ParamsUtils
                                .params("userId", userBean.getId())
                                .params("name", userBean.geteName())
                                .params("portraitUri", userBean.getE_HeadImg())
                                .commitParams())
                .compose(RxSchedulers.<RongTokenBean>compose())
                .subscribe(new BaseObjectObserver<RongTokenBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    protected void onHandleSuccess(RongTokenBean rongTokenBean) {
                        if (rongTokenBean.code == 200) {
                            rongToken = rongTokenBean.token;
                            SPUtils.getInstance().put(Constants.RONG_TOKEN, rongToken);
                            connectRong();
                        }
                    }

                    @Override
                    protected void onHandleError(int code, String msg) {

                    }
                });
    }

    private void connectRong() {
        RongIM.connect(rongToken, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                requestRongToken();
            }

            @Override
            public void onSuccess(String userid) {
                Logger.e("融云连接成功");
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        super.onDestroy();
    }
}
