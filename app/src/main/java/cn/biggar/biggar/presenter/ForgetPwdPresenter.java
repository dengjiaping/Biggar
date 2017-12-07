package cn.biggar.biggar.presenter;

import android.content.Context;

import cn.biggar.biggar.api.DataApiFactory;
import cn.biggar.biggar.api.account.IUserAPI;

import per.sue.gear2.presenter.AbsPresenter;
import rx.functions.Action1;

/*
* 文件名：
* 描 述：
* 作 者：苏昭强
* 时 间：2016/4/27
* 版 权：比格科技有限公司
*/
public class ForgetPwdPresenter extends AbsPresenter {

    private Context context;
    private ForgetView forgetView;
    private IUserAPI iUserAPI;

    public ForgetPwdPresenter(Context context, ForgetView forgetView) {
        this.context = context;
        this.forgetView = forgetView;
        iUserAPI = DataApiFactory.getInstance().createIUserAPI(context);
    }

    public void forgetPwd(String mobile, String pasword,  String code){
        iUserAPI.forgetPwd(mobile, pasword, code)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        forgetView.onSucess(s);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        forgetView.onError(-1, throwable.getMessage());
                    }
                });
    }

    public void getCode(String mobile){
        iUserAPI.getCode(mobile, "ForgetPassword")
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        forgetView.onCode(s);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        forgetView.onError(0, throwable.getMessage());
                    }
                });
    }

    public interface ForgetView{
        void onSucess(String msg);
        void onError(int code, String message);
        void onCode(String code);
    }
}
