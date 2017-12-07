package cn.biggar.biggar.presenter.update;


import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.bean.BgResponse;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.bean.update.RongTokenBean;
import cn.biggar.biggar.contract.LoginContract;
import cn.biggar.biggar.http.BaseObjectObserver;
import cn.biggar.biggar.http.BaseObserver;
import cn.biggar.biggar.http.HttpMethods;
import cn.biggar.biggar.http.ParamsUtils;
import cn.biggar.biggar.http.RxSchedulers;
import cn.biggar.biggar.utils.SHA1;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by Chenwy on 2017/10/20.
 */

public class LoginPresenter extends LoginContract.Presenter {
    @Override
    public void login(String userName, String password, String registrationID) {
        HttpMethods.getInstance().getApiService()
                .login(userName, password, registrationID, "android")
                .compose(RxSchedulers.<BgResponse<UserBean>>compose())
                .subscribe(new BaseObserver<UserBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    protected void onHandleSuccess(UserBean userBean) {
                        mView.loginSuccess(null, userBean);
                    }

                    @Override
                    protected void onHandleError(int code, String msg) {
                        mView.showError(msg);
                    }
                });
    }

    @Override
    public void loginByThird(final String platform, String openId, String taken, String infoJson, String registrationID) {
        HttpMethods.getInstance().getApiService()
                .loginByThird(platform, openId, taken, infoJson, registrationID, "android")
                .compose(RxSchedulers.<BgResponse<UserBean>>compose())
                .subscribe(new BaseObserver<UserBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    protected void onHandleSuccess(UserBean userBean) {
                        mView.loginSuccess(platform, userBean);
                    }

                    @Override
                    protected void onHandleError(int code, String msg) {
                        mView.showError(msg);
                    }
                });
    }

    @Override
    public void requestRongToken(String userId, String name, String portraitUri) {
        String appKey = Constants.RONG_APP_KEY;
        String appSecret = Constants.RONG_APP_SECRET;
        int nonce = (int) (Math.random() * 100);
        long timeStamp = System.currentTimeMillis();
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
                                .params("userId", userId)
                                .params("name", name)
                                .params("portraitUri", portraitUri)
                                .commitParams())
                .compose(RxSchedulers.<RongTokenBean>compose())
                .subscribe(new BaseObjectObserver<RongTokenBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    protected void onHandleSuccess(RongTokenBean rongTokenBean) {
                        if (rongTokenBean.code == 200) {
                            mView.requestRongTokenSuccess(rongTokenBean.token);
                        }
                    }

                    @Override
                    protected void onHandleError(int code, String msg) {

                    }
                });
    }

    @Override
    public void connectRong(String rongToken) {
        RongIM.connect(rongToken, new RongIMClient.ConnectCallback() {

            /**
             * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
             *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
             */
            @Override
            public void onTokenIncorrect() {
                mView.onTokenIncorrect();
            }

            /**
             * 连接融云成功
             * @param userid 当前 token 对应的用户 id
             */
            @Override
            public void onSuccess(String userid) {
                mView.connectRongSuccess();
            }

            /**
             * 连接融云失败
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                mView.connectRongError(errorCode);
            }
        });
    }
}
