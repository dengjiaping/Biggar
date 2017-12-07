package cn.biggar.biggar.preference;

import cn.biggar.biggar.bean.BgResponse;
import cn.biggar.biggar.bean.update.DistriManagerBean;
import cn.biggar.biggar.contract.DistributionManagerContract;
import cn.biggar.biggar.http.BaseObserver;
import cn.biggar.biggar.http.HttpMethods;
import cn.biggar.biggar.http.RxSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by Chenwy on 2017/11/13.
 */

public class DistributionManagerPresenter extends DistributionManagerContract.Presenter {
    @Override
    public void requestData(String uid) {
        HttpMethods.getInstance().getDistributionService()
                .requestDatas(uid)
                .compose(RxSchedulers.<BgResponse<DistriManagerBean>>compose())
                .subscribe(new BaseObserver<DistriManagerBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    protected void onHandleSuccess(DistriManagerBean distriManagerBean) {
                        mView.showDatas(distriManagerBean);
                    }

                    @Override
                    protected void onHandleError(int code, String msg) {
                        mView.showError(msg);
                    }
                });
    }
}
