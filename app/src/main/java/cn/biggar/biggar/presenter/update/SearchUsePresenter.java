package cn.biggar.biggar.presenter.update;

import java.util.List;
import cn.biggar.biggar.bean.BgResponse;
import cn.biggar.biggar.bean.FansListBean;
import cn.biggar.biggar.contract.SearchUserContract;
import cn.biggar.biggar.http.BaseArrayObserver;
import cn.biggar.biggar.http.BaseObserver;
import cn.biggar.biggar.http.HttpMethods;
import cn.biggar.biggar.http.ParamsUtils;
import cn.biggar.biggar.http.RxSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by Chenwy on 2017/9/20.
 */

public class SearchUsePresenter extends SearchUserContract.Presenter {
    @Override
    public void requestUser(String typeId, String uid, int pages, int p, String name) {
        HttpMethods.getInstance().getApiService()
                .requestSearchUser(ParamsUtils.params("type_id", typeId)
                        .params("uid", uid)
                        .params("pages", pages)
                        .params("p", p)
                        .params("name", name).commitParams())
                .compose(RxSchedulers.<List<FansListBean>>compose())
                .subscribe(new BaseArrayObserver<FansListBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    protected void onHandleSuccess(List<FansListBean> t) {
                        mView.showUsers(t);
                    }

                    @Override
                    protected void onHandleError(int code, String msg) {
                        mView.showError(msg);
                    }
                });
    }

    @Override
    public void loadMoreUser(String typeId, String uid, int pages, int p, String name) {
        HttpMethods.getInstance().getApiService()
                .requestSearchUser(ParamsUtils.params("type_id", typeId)
                        .params("uid", uid)
                        .params("pages", pages)
                        .params("p", p)
                        .params("name", name).commitParams())
                .compose(RxSchedulers.<List<FansListBean>>compose())
                .subscribe(new BaseArrayObserver<FansListBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    protected void onHandleSuccess(List<FansListBean> t) {
                        mView.showMoreUsers(t);
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
