package cn.biggar.biggar.presenter.update;

import java.util.List;

import cn.biggar.biggar.bean.BgResponse;
import cn.biggar.biggar.bean.BuyStarMoneyBean;
import cn.biggar.biggar.contract.BuyStarContract;
import cn.biggar.biggar.http.BaseArrayObserver;
import cn.biggar.biggar.http.BaseObserver;
import cn.biggar.biggar.http.HttpMethods;
import cn.biggar.biggar.http.RxSchedulers;
import cn.biggar.biggar.wxapi.WXPayInfo;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by Chenwy on 2017/11/10.
 */

public class BuyStarPresenter extends BuyStarContract.Presenter {
    @Override
    public void requestStarDatas(int p, int pages, final boolean isLoadMore) {
        HttpMethods.getInstance().getApiService()
                .requestStarDatas(p, pages)
                .compose(RxSchedulers.<List<BuyStarMoneyBean>>compose())
                .subscribe(new BaseArrayObserver<BuyStarMoneyBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    protected void onHandleSuccess(List<BuyStarMoneyBean> t) {
                        if (!isLoadMore) {
                            mView.showStarDatas(t);
                        } else {
                            mView.showMoreStarDatas(t);
                        }
                    }

                    @Override
                    protected void onHandleError(int code, String msg) {
                        mView.showError(msg);
                    }
                });
    }

    @Override
    public void payBuyStar(String uid, String payMoney) {
        HttpMethods.getInstance().getApiService()
                .payBuyStar(uid, payMoney)
                .compose(RxSchedulers.<BgResponse<WXPayInfo>>compose())
                .subscribe(new BaseObserver<WXPayInfo>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    protected void onHandleSuccess(WXPayInfo wxPayInfo) {
                        mView.buyStarSuccess(wxPayInfo);
                    }

                    @Override
                    protected void onHandleError(int code, String msg) {
                        mView.buyStarError(code, msg);
                    }
                });
    }
}
