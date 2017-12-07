package cn.biggar.biggar.contract;

import java.util.List;

import cn.biggar.biggar.base.BasePresenter;
import cn.biggar.biggar.base.BaseView;
import cn.biggar.biggar.bean.BuyStarMoneyBean;
import cn.biggar.biggar.wxapi.WXPayInfo;

/**
 * Created by Chenwy on 2017/11/10.
 */

public class BuyStarContract {
    public interface View extends BaseView {
        void showStarDatas(List<BuyStarMoneyBean> buyStarMoneyBeen);

        void showMoreStarDatas(List<BuyStarMoneyBean> buyStarMoneyBeen);

        void buyStarSuccess(WXPayInfo wxPayInfo);

        void buyStarError(int code, String errMsg);
    }

    public abstract static class Presenter extends BasePresenter<View> {
        public abstract void requestStarDatas(int p, int pages, boolean isLoadMore);

        public abstract void payBuyStar(String uid, String payMoney);
    }
}
