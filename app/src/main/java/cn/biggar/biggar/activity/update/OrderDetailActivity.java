package cn.biggar.biggar.activity.update;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yinglan.keyboard.HideUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.biggar.biggar.R;
import cn.biggar.biggar.adapter.update.OrderDetailAdapter;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.bean.update.AddressListBean;
import cn.biggar.biggar.bean.update.MyOrderBean;
import cn.biggar.biggar.bean.update.OrderDetailGoodsBean;
import cn.biggar.biggar.dialog.DialogAddressList;
import cn.biggar.biggar.view.CenterAlignImageSpan;
import cn.biggar.biggar.view.CenterImageSpan;

/**
 * Created by Chenwy on 2017/11/20.
 */

public class OrderDetailActivity extends BiggarActivity implements View.OnClickListener {
    @BindView(R.id.rv_order_detail)
    RecyclerView rvOrderDetail;
    @BindView(R.id.bottom_default)
    LinearLayout llBottomDefault;
    @BindView(R.id.bottom_wait_pay)
    LinearLayout llBottomWaitPay;
    @BindView(R.id.bottom_wait_take)
    LinearLayout llBottomWaitTake;
    @BindView(R.id.bottom_succed_cancel)
    LinearLayout llBottomSuccedCancel;

    private TextView tvAddress;
    private View headerView;
    private View footView;
    private LinearLayout llHeaderTip;
    private ImageView ivTip;
    private TextView tvTip;
    private LinearLayout llFootDefault;
    private LinearLayout llFootOther;
    private LinearLayout llExpress;
    private LinearLayout llFootPrice;

    /**
     * 商品价格
     */
    private TextView tvGoodsPrice;

    /**
     * 运费
     */
    private TextView tvFreight;

    /**
     * 优惠
     */
    private TextView tvFavourable;

    private OrderDetailAdapter orderDetailAdapter;
    private MyOrderBean orderBean;
    private boolean isDefault = false;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected int getKeyboardMode() {
        return WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        HideUtil.init(this);
        orderBean = (MyOrderBean) getIntent().getSerializableExtra("orderBean");
        orderDetailAdapter = new OrderDetailAdapter(isDefault, new ArrayList<OrderDetailGoodsBean>());
        rvOrderDetail.setLayoutManager(new LinearLayoutManager(this));
        rvOrderDetail.setAdapter(orderDetailAdapter);
        initHeaderView();
        initFootView();
        initData();
        if (orderBean != null) {
            int state = orderBean.state;
            switch (state) {
                case Constants.ORDER_WAIT_PAY:
                    initWaitPay();
                    break;
                case Constants.ORDER_WAIT_TAKE:
                    initWaitTake();
                    break;
                case Constants.ORDER_FINISH:
                    initFinish();
                    break;
                case Constants.ORDER_CANCEL:
                    initCancel();
                    break;
            }
        }
    }

    private void initData() {
        List<OrderDetailGoodsBean> orderDetailGoodsBeen = new ArrayList<>();
        orderDetailGoodsBeen.add(new OrderDetailGoodsBean());
        orderDetailGoodsBeen.add(new OrderDetailGoodsBean());
        orderDetailAdapter.setNewData(orderDetailGoodsBeen);
    }

    private void initWaitPay() {
        llFootDefault.setVisibility(View.GONE);
        llFootOther.setVisibility(View.VISIBLE);
        llHeaderTip.setVisibility(View.VISIBLE);
        llExpress.setVisibility(View.GONE);
        llBottomDefault.setVisibility(View.GONE);
        llBottomWaitPay.setVisibility(View.VISIBLE);
        llBottomWaitTake.setVisibility(View.GONE);
        llBottomSuccedCancel.setVisibility(View.GONE);
        llFootPrice.setVisibility(View.VISIBLE);

        ivTip.setImageResource(R.mipmap.pay);
        tvTip.setText("等待付款");
    }

    private void initWaitTake() {
        llFootDefault.setVisibility(View.GONE);
        llFootOther.setVisibility(View.VISIBLE);
        llHeaderTip.setVisibility(View.VISIBLE);
        llExpress.setVisibility(View.VISIBLE);
        llBottomDefault.setVisibility(View.GONE);
        llBottomWaitPay.setVisibility(View.GONE);
        llBottomWaitTake.setVisibility(View.VISIBLE);
        llBottomSuccedCancel.setVisibility(View.GONE);
        llFootPrice.setVisibility(View.VISIBLE);

        ivTip.setImageResource(R.mipmap.shipping);
        tvTip.setText("等待收货");
    }

    private void initFinish() {
        llFootDefault.setVisibility(View.GONE);
        llFootOther.setVisibility(View.VISIBLE);
        llHeaderTip.setVisibility(View.VISIBLE);
        llExpress.setVisibility(View.VISIBLE);
        llBottomDefault.setVisibility(View.GONE);
        llBottomWaitPay.setVisibility(View.GONE);
        llBottomWaitTake.setVisibility(View.GONE);
        llBottomSuccedCancel.setVisibility(View.VISIBLE);
        llFootPrice.setVisibility(View.VISIBLE);

        ivTip.setImageResource(R.mipmap.succeed_icon);
        tvTip.setText("交易成功");
    }

    private void initCancel() {
        llFootDefault.setVisibility(View.GONE);
        llFootOther.setVisibility(View.VISIBLE);
        llHeaderTip.setVisibility(View.VISIBLE);
        llExpress.setVisibility(View.VISIBLE);
        llBottomDefault.setVisibility(View.GONE);
        llBottomWaitPay.setVisibility(View.GONE);
        llBottomWaitTake.setVisibility(View.GONE);
        llBottomSuccedCancel.setVisibility(View.VISIBLE);
        llFootPrice.setVisibility(View.VISIBLE);

        ivTip.setImageResource(R.mipmap.cancel);
        tvTip.setText("已取消");
    }

    private void initHeaderView() {
        headerView = getLayoutInflater().inflate(R.layout.header_order_detail, (ViewGroup) rvOrderDetail.getParent(), false);
        orderDetailAdapter.addHeaderView(headerView);
        tvAddress = (TextView) headerView.findViewById(R.id.tv_address);
        llHeaderTip = (LinearLayout) headerView.findViewById(R.id.ll_tip);
        ivTip = (ImageView) headerView.findViewById(R.id.iv_tip);
        tvTip = (TextView) headerView.findViewById(R.id.tv_tip);
        SpannableString sp = new SpannableString("占位 " + "广州市天河区林和西路 3-15 号耀中广场B座 4209-4210 室");
        //获取一张图片
        CenterImageSpan centerImageSpan = new CenterImageSpan(getApplicationContext(), R.mipmap.address_icon, ImageSpan.ALIGN_BASELINE);
        sp.setSpan(centerImageSpan, 0, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvAddress.setText(sp);

        headerView.findViewById(R.id.ll_address).setOnClickListener(this);
    }

    private void initFootView() {
        footView = getLayoutInflater().inflate(R.layout.foot_order_detail, (ViewGroup) rvOrderDetail.getParent(), false);
        orderDetailAdapter.addFooterView(footView);
        llFootDefault = (LinearLayout) footView.findViewById(R.id.foot_default);
        llFootOther = (LinearLayout) footView.findViewById(R.id.foot_other);
        llExpress = (LinearLayout) footView.findViewById(R.id.ll_detail_express);
        tvGoodsPrice = (TextView) footView.findViewById(R.id.tv_goods_price);
        tvFreight = (TextView) footView.findViewById(R.id.tv_freight);
        tvFavourable = (TextView) footView.findViewById(R.id.tv_favourable);
        llFootPrice = (LinearLayout) footView.findViewById(R.id.ll_foot_price);
        tvGoodsPrice.setTextColor(isDefault ? ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary)
                : ContextCompat.getColor(getApplicationContext(), R.color.app_6));
        tvFreight.setTextColor(isDefault ? ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary)
                : ContextCompat.getColor(getApplicationContext(), R.color.app_6));
        tvFavourable.setTextColor(isDefault ? ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary)
                : ContextCompat.getColor(getApplicationContext(), R.color.app_6));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_address:
                DialogAddressList.newInstance(getAddressDatas()).show(getSupportFragmentManager());
                break;
        }
    }

    private List<AddressListBean> getAddressDatas() {
        List<AddressListBean> addressListBeen = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            AddressListBean addressListBean = new AddressListBean();
            addressListBean.isCheck = i == 0 ? true : false;
            addressListBean.id = String.valueOf(i);
            addressListBeen.add(addressListBean);
        }
        return addressListBeen;
    }
}
