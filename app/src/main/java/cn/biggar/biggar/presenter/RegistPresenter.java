package cn.biggar.biggar.presenter;

import android.content.Context;

import cn.biggar.biggar.api.DataApiFactory;
import cn.biggar.biggar.api.account.IUserAPI;
import cn.biggar.biggar.bean.UserBean;

import per.sue.gear2.presenter.AbsPresenter;
import rx.functions.Action1;

/*
* 文件名：
* 描 述：
* 作 者：苏昭强
* 时 间：2016/4/27
* 版 权：比格科技有限公司
*/
public class RegistPresenter extends AbsPresenter {

    private Context context;
    private RegistView registView;
    private IUserAPI iUserAPI;

    public RegistPresenter(Context context, RegistView registView) {
        this.context = context;
        this.registView = registView;
        iUserAPI = DataApiFactory.getInstance().createIUserAPI(context);
    }

    public void regist(String mobile, String headImage, String nick, String pasword){

        String imei = "";
        String system = "";
        iUserAPI.regist(mobile, nick, pasword, headImage, imei, system)
                .subscribe(new Action1<UserBean>() {
                    @Override
                    public void call(UserBean userBean) {
                        registView.onRegistSucess(userBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        registView.onError(-1, throwable.getMessage());
                    }
                });
    }

    public void getCode(String mobile,String type){
        iUserAPI.getCode(mobile, type)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        registView.onCode(s);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        registView.onError(0, throwable.getMessage());
                    }
                });
    }

    public interface RegistView{
        void onRegistSucess(UserBean userBean);
        void onError(int code, String message);
        void onCode(String code);
    }
}
