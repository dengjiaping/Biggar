package cn.biggar.biggar.presenter;

import cn.biggar.biggar.bean.BgResponse;
import cn.biggar.biggar.bean.VideoImageBean;
import cn.biggar.biggar.bean.update.DiscoverBean;
import cn.biggar.biggar.contract.BiggarShowListContract;
import cn.biggar.biggar.http.BaseArrayObserver;
import cn.biggar.biggar.http.BaseObserver;
import cn.biggar.biggar.http.HttpMethods;
import cn.biggar.biggar.http.RxSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import java.util.List;


/**
 * Created by Chenwy on 2017/4/18.
 */

public class BiggarShowListPresenter extends BiggarShowListContract.Presenter {
    /**
     * 获取热门
     *
     * @param page
     * @param pages
     */
    @Override
    public void requestHotList(int page, int pages) {
        HttpMethods.getInstance().getContentApi()
                .requestHotContentList(page, pages)
                .compose(RxSchedulers.<BgResponse<DiscoverBean>>compose())
                .subscribe(new BaseObserver<DiscoverBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    protected void onHandleSuccess(DiscoverBean discoverBean) {
                        mView.showBanners(discoverBean.message);
                        mView.showResultList(discoverBean.list);
                    }

                    @Override
                    protected void onHandleError(int code, String msg) {
                        mView.showError(msg);
                    }
                });
    }

    /**
     * 获取更多热门
     *
     * @param page
     * @param pages
     */
    @Override
    public void loadMoreHotList(int page, int pages) {
        HttpMethods.getInstance().getContentApi()
                .requestHotContentList(page, pages)
                .compose(RxSchedulers.<BgResponse<DiscoverBean>>compose())
                .subscribe(new BaseObserver<DiscoverBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    protected void onHandleSuccess(DiscoverBean discoverBean) {
                        mView.showMoreResultList(discoverBean.list);
                    }

                    @Override
                    protected void onHandleError(int code, String msg) {
                        mView.showError(msg);
                    }
                });
    }

    /**
     * 获取关注
     *
     * @param page
     * @param pages
     * @param uid
     */
    @Override
    public void requestFollowList(final int page, int pages, String uid) {
        HttpMethods.getInstance().getContentApi().requestFollowContentList(page, pages, uid)
                .compose(RxSchedulers.<List<VideoImageBean>>compose())
                .subscribe(new BaseArrayObserver<VideoImageBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    protected void onHandleSuccess(List<VideoImageBean> t) {
                        mView.showResultList(t);
                    }

                    @Override
                    protected void onHandleError(int code, String msg) {
                        mView.showError(msg);
                    }
                });
    }

    /**
     * 获取更多关注
     *
     * @param page
     * @param pages
     * @param uid
     */
    @Override
    public void loadMoreFollowList(final int page, int pages, String uid) {
        HttpMethods.getInstance().getContentApi().requestFollowContentList(page, pages, uid)
                .compose(RxSchedulers.<List<VideoImageBean>>compose())
                .subscribe(new BaseArrayObserver<VideoImageBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    protected void onHandleSuccess(List<VideoImageBean> t) {
                        mView.showMoreResultList(t);
                    }

                    @Override
                    protected void onHandleError(int code, String msg) {
                        mView.showError(msg);
                    }
                });
    }
}
