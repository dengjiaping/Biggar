package cn.biggar.biggar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.biggar.biggar.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import per.sue.gear2.widget.nav.TableHostBean;

/**
 * Created by mx on 2016/8/15.
 * 商店
 */
public class BrandShopFragment extends HeaderViewPagerFragment implements View.OnClickListener {

   @BindView(R.id.brand_shop_recommend_rl)
    RelativeLayout mRlRecommend;
   @BindView(R.id.brand_shop_sales_rl)
    RelativeLayout mRlSales;
   @BindView(R.id.brand_shop_new_product_rl)
    RelativeLayout mRlNewProduct;
   @BindView(R.id.brand_shop_price_rl)
    RelativeLayout mRlPrice;
   @BindView(R.id.brand_shop_switch_rl)
    RelativeLayout mRLSwitch;
   @BindView(R.id.brand_shop_state_iv)
    ImageView mIVState;
   @BindView(R.id.brand_shop_tab_a)
   TextView mTvTabA;
   @BindView(R.id.brand_shop_tab_b)
    TextView mTvTabB;
   @BindView(R.id.brand_shop_tab_c)
    TextView mTvTabC;
   @BindView(R.id.brand_shop_tab_d)
    TextView mTvTabD;
   @BindView(R.id.brand_shop_price_rise)
    ImageView mPriceRise;
   @BindView(R.id.brand_shop_price_fall)
    ImageView mPriceFall;

    private String mBrandId;
    private List<BrandShopListFragment> mFragments;
    private boolean mSwitchState;//当前 显示状态
    private int mTabIndex;//当前 tab 位置
    private String type = "price";
    public static BrandShopFragment getInstance(String id) {
        BrandShopFragment edManFragmentr = new BrandShopFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        edManFragmentr.setArguments(bundle);
        return edManFragmentr;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_brand_shop;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        mBrandId = getArguments().getString("id");
        initViews();
        initData();
        initEvents();

    }

    private void initViews() {
        mFragments = new ArrayList<>();
        mFragments.add(BrandShopListFragment.getInstance("", mBrandId));
        mFragments.add(BrandShopListFragment.getInstance("sales", mBrandId));
        mFragments.add(BrandShopListFragment.getInstance("news", mBrandId));
        mFragments.add(BrandShopListFragment.getInstance(type, mBrandId));
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.brand_shop_fragment, mFragments.get(0))
                .commit();
        this.mTabIndex = 0;

        mTvTabA.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    private ArrayList<TableHostBean> getTabHost() {

        ArrayList<TableHostBean> list = new ArrayList<>();
        list.add(new TableHostBean(getString(R.string.tv_recommend), 0));
        list.add(new TableHostBean(getString(R.string.tv_sales), 0));
        list.add(new TableHostBean(getString(R.string.tv_new_product), 0));
        list.add(new TableHostBean(getString(R.string.tv_price), 0));
        return list;

    }

    private void initData() {

    }

    private void initEvents() {
        mRlRecommend.setOnClickListener(this);
        mRlSales.setOnClickListener(this);
        mRlNewProduct.setOnClickListener(this);
        mRlPrice.setOnClickListener(this);
        mRLSwitch.setOnClickListener(this);
    }


    @Override
    public View getScrollableView() {
        return mFragments.get(mTabIndex).getScrollableView();
    }


    /**
     * 切换
     */

    private void switchTab(int tabIndex,TextView textView) {
        if (tabIndex == 3){
            mPriceRise.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.rise_red));
            mPriceFall.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.fall));
        }else {
            mPriceFall.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.fall));
            mPriceRise.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.rise));
        }
        if (!mFragments.get(tabIndex).isAdded()) {
            getActivity().getSupportFragmentManager().beginTransaction().hide(mFragments.get(mTabIndex)).add(R.id.brand_shop_fragment, mFragments.get(tabIndex)).commit();

        } else {
            getActivity().getSupportFragmentManager().beginTransaction().hide(mFragments.get(mTabIndex)).show(mFragments.get(tabIndex)).commit();

            if (tabIndex ==3){

                if (type.equals("price")){
                    mFragments.get(tabIndex).requstData("priceDesc");
                    type = "priceDesc";
                    mPriceFall.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.fall_red));
                    mPriceRise.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.rise));
                }else {
                    mFragments.get(tabIndex).requstData("price");
                    type = "price";
                    mPriceRise.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.rise_red));
                    mPriceFall.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.fall));
                }

            }else {
                mPriceFall.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.fall));
                mPriceRise.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.rise));
                type = "priceDesc";
            }

        }

        mTvTabA.setTextColor(getResources().getColor(R.color.app_text_color_z));
        mTvTabB.setTextColor(getResources().getColor(R.color.app_text_color_z));
        mTvTabC.setTextColor(getResources().getColor(R.color.app_text_color_z));
        mTvTabD.setTextColor(getResources().getColor(R.color.app_text_color_z));
        textView.setTextColor(getResources().getColor(R.color.colorPrimary));
        this.mTabIndex = tabIndex;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.brand_shop_new_product_rl:
                switchTab(2,mTvTabC);
                break;
            case R.id.brand_shop_sales_rl:
                switchTab(1,mTvTabB);
                break;
            case R.id.brand_shop_price_rl:
                switchTab(3,mTvTabD);
                break;
            case R.id.brand_shop_recommend_rl:
                switchTab(0,mTvTabA);
                break;
            case R.id.brand_shop_switch_rl:
                mSwitchState = !mSwitchState;
                mIVState.setImageResource(mSwitchState ? R.mipmap.list_style : R.mipmap.style);
                for (BrandShopListFragment fragment : mFragments) {
                    fragment.switchState(!mSwitchState);
                }
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

}

