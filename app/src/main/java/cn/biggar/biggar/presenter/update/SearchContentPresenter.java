package cn.biggar.biggar.presenter.update;

import java.util.List;
import cn.biggar.biggar.bean.VideoImageBean;
import cn.biggar.biggar.contract.SearchContentContract;
import cn.biggar.biggar.http.BaseArrayObserver;
import cn.biggar.biggar.http.HttpMethods;
import cn.biggar.biggar.http.ParamsUtils;
import cn.biggar.biggar.http.RxSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by Chenwy on 2017/9/20.
 */

public class SearchContentPresenter extends SearchContentContract.Presenter {
    @Override
    public void requestContent(String typeId, String uid, int pages, int p, String name) {
        HttpMethods.getInstance().getContentApi()
                .requestSearchContent(ParamsUtils
                        .params("type_id", typeId)
                        .params("uid", uid)
                        .params("pages", pages)
                        .params("p", p)
                        .params("name", name).commitParams())
                .compose(RxSchedulers.<List<VideoImageBean>>compose())
                .subscribe(new BaseArrayObserver<VideoImageBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    protected void onHandleSuccess(List<VideoImageBean> t) {
                        mView.showContents(t);
                    }

                    @Override
                    protected void onHandleError(int code, String msg) {
                        mView.showError(msg);
                    }
                });
    }

    @Override
    public void loadMoreContent(String typeId, String uid, int pages, int p, String name) {
        HttpMethods.getInstance().getContentApi()
                .requestSearchContent(ParamsUtils
                        .params("type_id", typeId)
                        .params("uid", uid)
                        .params("pages", pages)
                        .params("p", p)
                        .params("name", name).commitParams())
                .compose(RxSchedulers.<List<VideoImageBean>>compose())
                .subscribe(new BaseArrayObserver<VideoImageBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    protected void onHandleSuccess(List<VideoImageBean> t) {
                        mView.showMoreContents(t);
                    }

                    @Override
                    protected void onHandleError(int code, String msg) {
                        mView.showError(msg);
                    }
                });
    }
}
