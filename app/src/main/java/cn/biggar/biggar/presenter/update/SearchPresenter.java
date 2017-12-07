package cn.biggar.biggar.presenter.update;

import cn.biggar.biggar.bean.update.SeachBean;
import cn.biggar.biggar.contract.SearchContract;
import cn.biggar.biggar.http.BaseObjectObserver;
import cn.biggar.biggar.http.HttpMethods;
import cn.biggar.biggar.http.RxSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by Chenwy on 2017/9/6.
 */

public class SearchPresenter extends SearchContract.Presenter {
    @Override
    public void requestSearch() {
        HttpMethods.getInstance().getApiService()
                .requestSearchIndex()
                .compose(RxSchedulers.<SeachBean>compose())
                .subscribe(new BaseObjectObserver<SeachBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    protected void onHandleSuccess(SeachBean seachBean) {
                        mView.showSearch(seachBean);
                    }

                    @Override
                    protected void onHandleError(int code, String msg) {
                        mView.showError(msg);
                    }
                });
    }
}
