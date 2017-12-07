package cn.biggar.biggar.activity.update;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import org.greenrobot.eventbus.Subscribe;
import java.util.List;
import cn.biggar.biggar.R;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.base.BiggarListActivity;
import cn.biggar.biggar.bean.BuyStarMoneyBean;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.contract.BuyStarContract;
import cn.biggar.biggar.event.PaySuccessEvent;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.presenter.update.BuyStarPresenter;
import cn.biggar.biggar.wxapi.WXPayInfo;
import cn.biggar.biggar.wxapi.WXPayManager;
import cn.biggar.biggar.utils.ToastUtils;

/**
 * Created by Chenwy on 2017/8/4.
 * 购买星币
 */

public class BuyStarActivity extends BiggarListActivity<BuyStarPresenter, BuyStarMoneyBean> implements BuyStarContract.View {
    private String chooseMoney;
    private TextView mBalance;

    @Override
    protected void initDataBefore() {
        setCenterTitle("购买星币");
    }

    @Override
    protected void initDataAfter() {
        View headerView = getHeaderView(R.layout.item_buy_star_money_head);
        mBalance = (TextView) headerView.findViewById(R.id.star_money_balance);
        mBalance.setText(Preferences.getUserBean(getActivity()).getE_Points());
    }

    @Override
    protected void refreshData(final boolean isLoadMore) {
        mPresenter.requestStarDatas(curPage, Constants.PAGE_SIZE, isLoadMore);
    }

    @Override
    protected int itemLayout() {
        return R.layout.item_buy_star;
    }

    /**
     * 支付
     */
    private void WXPayRmb(String price) {
        showLoading();
        mPresenter.payBuyStar(Preferences.getUserBean(this).getId(), price);
    }

    @Override
    protected void myHolder(Context context, BaseViewHolder helper, BuyStarMoneyBean buyStarMoneyBean) {
        helper.addOnClickListener(R.id.star_money_price_tv);
        helper.setText(R.id.star_money_num_tv, TextUtils.isEmpty(buyStarMoneyBean.getE_Points()) ? "" : buyStarMoneyBean.getE_Points() + "星币");
        helper.setText(R.id.star_money_price_tv, TextUtils.isEmpty(buyStarMoneyBean.getE_Money()) ? "" : buyStarMoneyBean.getE_Money() + "元");
    }

    @Override
    protected void onListItemClick(BuyStarMoneyBean data, int position) {

    }

    @Override
    protected void onListItemChildClick(int viewId, BuyStarMoneyBean data, int position) {
        if (viewId == R.id.star_money_price_tv) {
            chooseMoney = data.getE_Money();
            WXPayRmb(data.getE_Money());
        }
    }

    @Subscribe
    public void onPaySuccess(PaySuccessEvent event) {
        if (event.paySucess) {
            if (!TextUtils.isEmpty(chooseMoney)) {
                UserBean bean = Preferences.getUserBean(getActivity());
                if (!TextUtils.isEmpty(bean.getE_Points())) {
                    int pre = Integer.parseInt(bean.getE_Points());
                    int add = Integer.parseInt(chooseMoney);
                    int result = pre + add;
                    bean.setE_Points(String.valueOf(result));
                    mBalance.setText(String.valueOf(result));
                    Preferences.storeUserBean(getActivity(), bean);
                }
            }
        }
    }

    @Override
    public boolean isLoadEventBus() {
        return true;
    }

    @Override
    public void showError(String errMsg) {
        handleError(errMsg);
    }

    @Override
    public void showStarDatas(List<BuyStarMoneyBean> buyStarMoneyBeen) {
        finishRefresh(buyStarMoneyBeen);
    }

    @Override
    public void showMoreStarDatas(List<BuyStarMoneyBean> buyStarMoneyBeen) {
        finishLoadMore(buyStarMoneyBeen);
    }

    @Override
    public void buyStarSuccess(WXPayInfo wxPayInfo) {
        dismissLoading();
        WXPayManager.getInstance().payByWX(getActivity(), wxPayInfo);
    }

    @Override
    public void buyStarError(int code, String errMsg) {
        dismissLoading();
        if (code == 0 || code == Constants.CODE_NETWORK_ERROR) {
            ToastUtils.showError(errMsg);
        } else {
            ToastUtils.showError("支付失败，请稍后再试！");
        }
    }
}
