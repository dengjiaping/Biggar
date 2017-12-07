package cn.biggar.biggar.presenter.update;

import java.util.List;

import cn.biggar.biggar.bean.BrandBean;
import cn.biggar.biggar.contract.BrandListContract;
import cn.biggar.biggar.http.BaseArrayObserver;
import cn.biggar.biggar.http.HttpMethods;
import cn.biggar.biggar.http.ParamsUtils;
import cn.biggar.biggar.http.RxSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by Chenwy on 2017/10/27.
 */

public class BrandListPresenter extends BrandListContract.Presenter {
    @Override
    public void requestRecommendBrandList(final boolean isLoadMore, String searchName, int p, int pages) {

        HttpMethods.getInstance().getMallApiService()
                .requestRecommendBrandList(ParamsUtils
                        .params("name", searchName)
                        .params("p", p)
                        .params("pages", pages)
                        .commitParams())
                .compose(RxSchedulers.<List<BrandBean>>compose())
                .subscribe(new BaseArrayObserver<BrandBean>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    protected void onHandleSuccess(List<BrandBean> t) {
                        if (isLoadMore) {
                            mView.loadMoreRecommendBrandList(t);
                        } else {
                            mView.showRecommendBrandList(t);
                        }
                    }

                    @Override
                    protected void onHandleError(int code, String msg) {
                        mView.showError(msg);
                    }
                });
    }

    @Override
    public void requestFollowBrandList(final boolean isLoadMore, String searchName, String MID, int flag, int p, int pages) {
        HttpMethods.getInstance().getMallApiService()
                .requestFollowBrandList(ParamsUtils
                        .params("name", searchName)
                        .params("MID",MID)
                        .params("flag",flag)
                        .params("p", p)
                        .params("pages", pages)
                        .commitParams())
                .compose(RxSchedulers.<List<BrandBean>>compose())
                .subscribe(new BaseArrayObserver<BrandBean>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    protected void onHandleSuccess(List<BrandBean> t) {
                        if (isLoadMore) {
                            mView.loadMoewFollowBrandList(t);
                        } else {
                            mView.showFollowBrandList(t);
                        }
                    }

                    @Override
                    protected void onHandleError(int code, String msg) {
                        mView.showError(msg);
                    }
                });
    }
}
