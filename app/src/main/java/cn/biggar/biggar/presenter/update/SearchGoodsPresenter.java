package cn.biggar.biggar.presenter.update;

import java.util.List;
import cn.biggar.biggar.bean.update.SearchGoodsBean;
import cn.biggar.biggar.contract.SearchGoodsContract;
import cn.biggar.biggar.http.BaseArrayObserver;
import cn.biggar.biggar.http.HttpMethods;
import cn.biggar.biggar.http.ParamsUtils;
import cn.biggar.biggar.http.RxSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by Chenwy on 2017/9/20.
 */

public class SearchGoodsPresenter extends SearchGoodsContract.Presenter {
    @Override
    public void requestGoods(String typeId, String uid, int pages, int p, String name) {
        HttpMethods.getInstance().getMallApiService()
                .requestSearchGoods(ParamsUtils.params("type_id", typeId)
                        .params("uid", uid)
                        .params("pages", pages)
                        .params("p", p)
                        .params("name", name)
                        .commitParams())
                .compose(RxSchedulers.<List<SearchGoodsBean>>compose())
                .subscribe(new BaseArrayObserver<SearchGoodsBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    protected void onHandleSuccess(List<SearchGoodsBean> t) {
                        mView.showGoodses(t);
                    }

                    @Override
                    protected void onHandleError(int code, String msg) {
                        mView.showError(msg);
                    }
                });
    }

    @Override
    public void loadMoreGoods(String typeId, String uid, int pages, int p, String name) {
        HttpMethods.getInstance().getMallApiService()
                .requestSearchGoods(ParamsUtils.params("type_id", typeId)
                        .params("uid", uid)
                        .params("pages", pages)
                        .params("p", p)
                        .params("name", name)
                        .commitParams())
                .compose(RxSchedulers.<List<SearchGoodsBean>>compose())
                .subscribe(new BaseArrayObserver<SearchGoodsBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    protected void onHandleSuccess(List<SearchGoodsBean> t) {
                        mView.showMoreGoodses(t);
                    }

                    @Override
                    protected void onHandleError(int code, String msg) {
                        mView.showError(msg);
                    }
                });
    }
}
