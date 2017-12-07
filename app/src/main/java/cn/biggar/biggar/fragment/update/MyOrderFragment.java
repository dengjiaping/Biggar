package cn.biggar.biggar.fragment.update;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import cn.biggar.biggar.R;
import cn.biggar.biggar.activity.update.OrderDetailActivity;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.base.BasePresenter;
import cn.biggar.biggar.base.BiggarListFragment;
import cn.biggar.biggar.bean.update.MyOrderBean;

/**
 * Created by Chenwy on 2017/11/20.
 */

public class MyOrderFragment extends BiggarListFragment<BasePresenter, MyOrderBean> {


    public static MyOrderFragment newInstance(int orderState) {
        MyOrderFragment myOrderFragment = new MyOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("orderState", orderState);
        myOrderFragment.setArguments(bundle);
        return myOrderFragment;
    }

    private int orderState;

    @Override
    protected void initDataBefore() {
        orderState = getArguments().getInt("orderState");
    }

    @Override
    protected void initAfter() {
        setEmptyBgColor(R.color.app_f7);
    }

    @Override
    protected void refreshData(boolean isLoadMore) {
        List<MyOrderBean> myOrderBeen = new ArrayList<>();
        if (orderState == Constants.ORDER_ALL) {
            myOrderBeen = getAllData();
        } else if (orderState == Constants.ORDER_WAIT_PAY) {
            myOrderBeen = getWaitPayData();
        } else if (orderState == Constants.ORDER_WAIT_TAKE) {
            myOrderBeen = getWaitTakeData();
        } else if (orderState == Constants.ORDER_FINISH) {
            myOrderBeen = getFinishData();
        } else if (orderState == Constants.ORDER_CANCEL) {
            myOrderBeen = getCancelData();
        }

        if (isLoadMore) {
            finishLoadMore(new ArrayList<MyOrderBean>());
        } else {
            finishRefresh(myOrderBeen);
        }
    }

    private List<MyOrderBean> getAllData() {
        List<MyOrderBean> myOrderBeen = new ArrayList<>();
        myOrderBeen.add(new MyOrderBean(Constants.ORDER_WAIT_PAY));
        myOrderBeen.add(new MyOrderBean(Constants.ORDER_WAIT_TAKE));
        myOrderBeen.add(new MyOrderBean(Constants.ORDER_FINISH));
        myOrderBeen.add(new MyOrderBean(Constants.ORDER_CANCEL));
        return myOrderBeen;
    }

    private List<MyOrderBean> getWaitPayData() {
        List<MyOrderBean> myOrderBeen = new ArrayList<>();
        myOrderBeen.add(new MyOrderBean(Constants.ORDER_WAIT_PAY));
        myOrderBeen.add(new MyOrderBean(Constants.ORDER_WAIT_PAY));
        myOrderBeen.add(new MyOrderBean(Constants.ORDER_WAIT_PAY));
        return myOrderBeen;
    }

    private List<MyOrderBean> getWaitTakeData() {
        List<MyOrderBean> myOrderBeen = new ArrayList<>();
        myOrderBeen.add(new MyOrderBean(Constants.ORDER_WAIT_TAKE));
        myOrderBeen.add(new MyOrderBean(Constants.ORDER_WAIT_TAKE));
        myOrderBeen.add(new MyOrderBean(Constants.ORDER_WAIT_TAKE));
        myOrderBeen.add(new MyOrderBean(Constants.ORDER_WAIT_TAKE));
        myOrderBeen.add(new MyOrderBean(Constants.ORDER_WAIT_TAKE));
        return myOrderBeen;
    }

    private List<MyOrderBean> getFinishData() {
        List<MyOrderBean> myOrderBeen = new ArrayList<>();
        myOrderBeen.add(new MyOrderBean(Constants.ORDER_FINISH));
        myOrderBeen.add(new MyOrderBean(Constants.ORDER_FINISH));
        myOrderBeen.add(new MyOrderBean(Constants.ORDER_FINISH));
        myOrderBeen.add(new MyOrderBean(Constants.ORDER_FINISH));
        return myOrderBeen;
    }

    private List<MyOrderBean> getCancelData() {
        List<MyOrderBean> myOrderBeen = new ArrayList<>();
        myOrderBeen.add(new MyOrderBean(Constants.ORDER_CANCEL));
        myOrderBeen.add(new MyOrderBean(Constants.ORDER_CANCEL));
        myOrderBeen.add(new MyOrderBean(Constants.ORDER_CANCEL));
        return myOrderBeen;
    }

    @Override
    protected int itemLayout() {
        return R.layout.item_my_order;
    }

    @Override
    protected void myHolder(BaseViewHolder helper, MyOrderBean data) {
        TextView tvOrderState = helper.getView(R.id.tv_order_state);
        Button btnLeft = helper.getView(R.id.btn_left);
        Button btnRight = helper.getView(R.id.btn_right);
        int state = data.state;
        switch (state) {
            case Constants.ORDER_WAIT_PAY:
                tvOrderState.setText("待付款");
                btnLeft.setVisibility(View.GONE);
                btnRight.setText("去支付");
                break;
            case Constants.ORDER_WAIT_TAKE:
                tvOrderState.setText("待收货");
                btnLeft.setVisibility(View.VISIBLE);
                btnLeft.setText("查看物流");
                btnRight.setText("再次购买");
                break;
            case Constants.ORDER_FINISH:
                tvOrderState.setText("已完成");
                btnLeft.setVisibility(View.GONE);
                btnRight.setText("再次购买");
                break;
            case Constants.ORDER_CANCEL:
                tvOrderState.setText("已取消");
                btnLeft.setVisibility(View.GONE);
                btnRight.setText("再次购买");
                break;
        }
    }

    @Override
    protected void onListItemClick(MyOrderBean data, int position) {
        startActivity(new Intent(getActivity(), OrderDetailActivity.class).putExtra("orderBean", data));
    }

    @Override
    protected void onListItemChildClick(int viewId, MyOrderBean data, int position) {

    }
}
