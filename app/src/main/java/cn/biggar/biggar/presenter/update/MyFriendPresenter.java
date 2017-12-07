package cn.biggar.biggar.presenter.update;


import java.util.List;
import cn.biggar.biggar.bean.FansListBean;
import cn.biggar.biggar.contract.MyFriendContract;
import cn.biggar.biggar.http.BaseArrayObserver;
import cn.biggar.biggar.http.HttpMethods;
import cn.biggar.biggar.http.ParamsUtils;
import cn.biggar.biggar.http.RxSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by Chenwy on 2017/9/6.
 */

public class MyFriendPresenter extends MyFriendContract.Presenter {
    @Override
    public void requestFriend(String uid, String cid, String keyName, int pages, int p) {
        HttpMethods.getInstance().getApiService()
                .requestFollows(ParamsUtils.params("uid", uid)
                        .params("cid", cid)
                        .params("name", keyName)
                        .params("p", p)
                        .params("pages", pages)
                        .commitParams())
                .compose(RxSchedulers.<List<FansListBean>>compose())
                .subscribe(new BaseArrayObserver<FansListBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    protected void onHandleSuccess(List<FansListBean> t) {
                        mView.requestFriendFinish(t);
                    }

                    @Override
                    protected void onHandleError(int code, String msg) {
                        mView.showError(msg);
                    }
                });
    }

    @Override
    public void loadMoreFriend(String uid, String cid, String keyName, int pages, int p) {
        HttpMethods.getInstance().getApiService()
                .requestFollows(ParamsUtils.params("uid", uid)
                        .params("cid", cid)
                        .params("name", keyName)
                        .params("p", p)
                        .params("pages", pages)
                        .commitParams())
                .compose(RxSchedulers.<List<FansListBean>>compose())
                .subscribe(new BaseArrayObserver<FansListBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    protected void onHandleSuccess(List<FansListBean> t) {
                        mView.loadMoreFriendFinish(t);
                    }

                    @Override
                    protected void onHandleError(int code, String msg) {
                        mView.showError(msg);
                    }
                });
    }
}
