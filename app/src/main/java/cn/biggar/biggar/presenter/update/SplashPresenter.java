package cn.biggar.biggar.presenter.update;

import cn.biggar.biggar.bean.BgResponse;
import cn.biggar.biggar.bean.update.OptionsBean;
import cn.biggar.biggar.contract.SplashContract;
import cn.biggar.biggar.http.BaseObserver;
import cn.biggar.biggar.http.HttpMethods;
import cn.biggar.biggar.http.RxSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by Chenwy on 2017/10/17.
 */

public class SplashPresenter extends SplashContract.Presenter {
    @Override
    public void requestOptions() {
        HttpMethods.getInstance().getApiService()
                .requestOptions()
                .compose(RxSchedulers.<BgResponse<OptionsBean>>compose())
                .subscribe(new BaseObserver<OptionsBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    protected void onHandleSuccess(OptionsBean optionsBean) {
                        mView.showOptions(optionsBean);
                    }

                    @Override
                    protected void onHandleError(int code, String msg) {
                        mView.showError(msg);
                    }
                });
    }
}
