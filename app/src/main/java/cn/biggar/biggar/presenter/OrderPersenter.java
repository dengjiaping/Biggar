package cn.biggar.biggar.presenter;

import android.content.Context;

import cn.biggar.biggar.api.DataApiFactory;
import cn.biggar.biggar.api.order.IOrderAPI;

import per.sue.gear2.net.exception.ResultException;
import per.sue.gear2.presenter.AbsPresenter;
import per.sue.gear2.presenter.OnObjectListener;
import rx.Subscriber;

/**
 * Created by mx on 2016/12/20.
 * 订单
 */

public class OrderPersenter extends AbsPresenter {

    private Context context;
    private IOrderAPI iOrderAPI;

    public OrderPersenter(Context context) {
        this.context = context;
        iOrderAPI = DataApiFactory.getInstance().createIOrderAPI(context);
    }


    /**
     * 微信账户查看购买  - 微信
     * @param uid
     * @param listener
     */
    public void payWeChatAccountByWeChat(String uid, String payMoney, int type, final OnObjectListener<String> listener){
        iOrderAPI.payWeChatAccountByWeChat(uid,payMoney,type).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                listener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                ResultException exception=ResultException.createResultException(e);
                listener.onError(exception.getCode(),exception.getMessage());
            }

            @Override
            public void onNext(String s) {
                listener.onSuccess(s);
            }
        });
    }

}
