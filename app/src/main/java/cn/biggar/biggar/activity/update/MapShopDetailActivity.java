package cn.biggar.biggar.activity.update;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseViewHolder;

import butterknife.BindView;
import butterknife.OnClick;
import cn.biggar.biggar.R;
import cn.biggar.biggar.activity.BrandSpaceActivity;
import cn.biggar.biggar.base.BasePresenter;
import cn.biggar.biggar.base.BiggarListActivity;
import cn.biggar.biggar.bean.update.MapLocationBean;
import cn.biggar.biggar.utils.Utils;
import cn.biggar.biggar.view.RootLayout;

/**
 * Created by Chenwy on 2017/11/17.
 */

public class MapShopDetailActivity extends BiggarListActivity<BasePresenter, MapLocationBean.CardData> {
    @BindView(R.id.ll_coupon)
    LinearLayout llCoupon;
    private LinearLayout llheaderCoupon;
    private MapLocationBean.Arr mMapLocationBean;
    private double curLati;
    private double curLongi;
    private View headerView;

    @Override
    protected void initDataBefore() {
        mMapLocationBean = (MapLocationBean.Arr) getIntent().getSerializableExtra("mapLocationBean");
        curLati = getIntent().getDoubleExtra("curLati", 0);
        curLongi = getIntent().getDoubleExtra("curLongi", 0);
        RootLayout.getInstance(this)
                .setTitle(mMapLocationBean == null ? "" : mMapLocationBean.E_Name);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_map_shop_detail;
    }

    @Override
    protected void initDataAfter() {
        headerView = getHeaderView(R.layout.header_map_shop_detail);
        llheaderCoupon = (LinearLayout) headerView.findViewById(R.id.ll_coupon);
        ImageView ivBrandBg = (ImageView) headerView.findViewById(R.id.iv_brand_bg);
        TextView tvStoreName = (TextView) headerView.findViewById(R.id.tv_store_name);
        TextView tvAddress = (TextView) headerView.findViewById(R.id.tv_address);
        SuperTextView tvTel = (SuperTextView) headerView.findViewById(R.id.tv_tel);
        if (mMapLocationBean != null) {
            Glide.with(this).load(Utils.getRealUrl(mMapLocationBean.E_Img2))
                    .apply(new RequestOptions().centerCrop()).into(ivBrandBg);
            tvStoreName.setText(mMapLocationBean.E_Name);
            tvAddress.setText(TextUtils.isEmpty(mMapLocationBean.E_Address) ? "" : mMapLocationBean.E_Address);
            tvTel.setLeftString(TextUtils.isEmpty(mMapLocationBean.E_Tel) ? "" : mMapLocationBean.E_Tel);
        }

        getRv().addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int position = layoutManager.findFirstVisibleItemPosition();
                llCoupon.setVisibility(position > 0 ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public boolean isEnableRefresh() {
        return false;
    }

    @Override
    public boolean isEnableLoadMore() {
        return false;
    }

    @Override
    public boolean isShowEmptyView() {
        return false;
    }

    @Override
    protected void refreshData(boolean isLoadMore) {
        llheaderCoupon.setVisibility(mMapLocationBean.Card_data != null && mMapLocationBean.Card_data.size() > 0
                ? View.VISIBLE : View.GONE);
        finishRefresh(mMapLocationBean.Card_data);
    }

    @Override
    protected int itemLayout() {
        return R.layout.item_map_shop_detail;
    }

    @Override
    protected void myHolder(Context context, BaseViewHolder helper, MapLocationBean.CardData cardData) {
        helper.setText(R.id.tv_quan, cardData.E_Title);

//        //优惠券
//        if (cardData.E_CardType.equals(Constants.CARD_TYPE1)) {
//            //满减券
//            if (cardData.E_CardType2.equals(Constants.CARD_TYPE1)) {
//            }
//            //代金券
//            else if (cardData.E_CardType2.equals(Constants.CARD_TYPE2)) {
//            }
//            //折扣券
//            else if (cardData.E_CardType2.equals(Constants.CARD_TYPE3)) {
//            }
//        }
//        //兑换券
//        else {
//            //比格币
//            if (cardData.E_CardType2.equals(Constants.CARD_TYPE1)) {
//
//            }
//            //兑换券
//            else if (cardData.E_CardType2.equals(Constants.CARD_TYPE4)) {
//            }
//            //零钱券
//            else if (cardData.E_CardType2.equals(Constants.CARD_TYPE2)) {
//            }
//        }
    }

    @Override
    protected void onListItemClick(MapLocationBean.CardData data, int position) {

    }

    @Override
    protected void onListItemChildClick(int viewId, MapLocationBean.CardData data, int position) {

    }

    @OnClick({R.id.btn_navigation, R.id.btn_to_shop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_navigation:
                Intent intent = new Intent(this, MapNavigationActivity.class);
                intent.putExtra("mapLocationBean", mMapLocationBean);
                intent.putExtra("curLati", curLati);
                intent.putExtra("curLongi", curLongi);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_to_shop:
                startActivity(BrandSpaceActivity.startIntent(this, mMapLocationBean.ID));
                break;
        }
    }
}
