package cn.biggar.biggar.presenter.update;

import java.util.List;

import cn.biggar.biggar.bean.BgResponse;
import cn.biggar.biggar.bean.FansListBean;
import cn.biggar.biggar.contract.FansContract;
import cn.biggar.biggar.http.BaseArrayObserver;
import cn.biggar.biggar.http.BaseObserver;
import cn.biggar.biggar.http.HttpMethods;
import cn.biggar.biggar.http.ParamsUtils;
import cn.biggar.biggar.http.RxSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by Chenwy on 2017/11/10.
 */

public class FansPresenter extends FansContract.Presenter {
    @Override
    public void requestFans(String uid, String cid, int p, int pages, final boolean isLoadMore) {
        HttpMethods.getInstance().getApiService()
                .requestFans(ParamsUtils.params("uid", uid)
                        .params("cid", cid)
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
                        if (isLoadMore) {
                            mView.showMoreFans(t);
                        } else {
                            mView.showFans(t);
                        }
                    }

                    @Override
                    protected void onHandleError(int code, String msg) {
                        mView.showError(msg);
                    }
                });
    }

    @Override
    public void addFollow(String id, String nickname, int type, String uid, String usernick) {
        HttpMethods.getInstance().getApiService()
                .addFollow(ParamsUtils.params("ID", id)
                        .params("TIT", nickname)
                        .params("TYPE", "1")
                        .params("E_UID", uid)
                        .params("E_NAME", usernick)
                        .commitParams())
                .compose(RxSchedulers.<BgResponse<String>>compose())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    protected void onHandleSuccess(String s) {
                        mView.addFollowSuccess();
                    }

                    @Override
                    protected void onHandleError(int code, String msg) {
                        mView.addFollowError("关注失败：" + msg);
                    }
                });
    }

    @Override
    public void cancelFollow(String uid, String oid) {
        HttpMethods.getInstance().getApiService()
                .cancelFollow(ParamsUtils.params("uid", uid)
                        .params("conid", oid)
                        .params("typeid", "1")
                        .commitParams())
                .compose(RxSchedulers.<BgResponse<String>>compose())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    protected void onHandleSuccess(String s) {
                        mView.canccelFollowSuccess();
                    }

                    @Override
                    protected void onHandleError(int code, String msg) {
                        mView.canccelFollowError("取消关注失败：" + msg);
                    }
                });
    }
}
