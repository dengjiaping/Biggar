package cn.biggar.biggar.api.order;


import cn.biggar.biggar.bean.WeChatRecordBean;

import java.util.ArrayList;

import rx.Observable;

/**
 * Created by mx on 2016/12/16.
 * 订单
 */

public interface IOrderAPI {

    /**
     * 查询 买家购买微信号订单
     * @param userId
     * @param type
     * @param p
     * @param pages
     * @return
     */
    Observable<ArrayList<WeChatRecordBean>> queryBuyersWechatOrderList(String userId, String type, int p, int pages);

    /**
     * 查询 我购买微信号订单
     * @param userId
     * @param p
     * @param pages
     * @return
     */
    Observable<ArrayList<WeChatRecordBean>> queryMyWechatOrderList(String userId, int p, int pages);


    /**
     * 购买查看微信账户  - 微信
     * @param uid
     * @return
     */
    Observable<String> payWeChatAccountByWeChat(String uid, String payMoney, int type);

}
