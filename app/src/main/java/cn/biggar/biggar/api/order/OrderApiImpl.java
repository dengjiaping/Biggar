package cn.biggar.biggar.api.order;

import android.content.Context;

import cn.biggar.biggar.api.APIObserver;
import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.api.BaseParser;
import cn.biggar.biggar.bean.WeChatRecordBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import per.sue.gear2.net.ApiConnectionFactory;
import rx.Observable;

/**
 * Created by mx on 2016/12/16.
 * 订单
 */

public class OrderApiImpl implements IOrderAPI {
    private Context context;

    public OrderApiImpl(Context context) {
        this.context = context;
    }

    @Override
    public Observable<ArrayList<WeChatRecordBean>> queryBuyersWechatOrderList(String userId, String type, int p, int pages) {
        String url = BaseAPI.API_WECHAT_BUYERS_ORDER_LIST;
        Map<String, String> params = new HashMap<>();
        params.put("uid", userId);
        params.put("state", type);
        params.put("p", p+"");
        params.put("pages", pages+"");
        return new APIObserver(context, ApiConnectionFactory.createPOST(url, params))
                .observeOnMainThread(new BaseParser<WeChatRecordBean>(WeChatRecordBean.class));
    }

    @Override
    public Observable<ArrayList<WeChatRecordBean>> queryMyWechatOrderList(String userId, int p, int pages) {
        String url = BaseAPI.API_WECHAT_MY_ORDER_LIST;
        Map<String, String> params = new HashMap<>();
        params.put("uid", userId);
        params.put("p", p+"");
        params.put("pages", pages+"");
        return new APIObserver(context, ApiConnectionFactory.createPOST(url, params))
                .observeOnMainThread(new BaseParser<WeChatRecordBean>(WeChatRecordBean.class));
    }

    @Override
    public Observable<String> payWeChatAccountByWeChat(String uid,String payMoney,int type) {
        String url = null;
        switch (type){
            case 1:
                url = BaseAPI.API_WECHAT_ACCOUNT_WECHAT_PAY;
                break;
            case 2:
                url = BaseAPI.API_STAR_MONEY_WECHAT_PAY;
                break;
        }
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("PayMoney", payMoney);
        return new APIObserver(context, ApiConnectionFactory.createPOST(url, params))
                .observeOnMainThread(new BaseParser<String>(String.class));
    }


}
