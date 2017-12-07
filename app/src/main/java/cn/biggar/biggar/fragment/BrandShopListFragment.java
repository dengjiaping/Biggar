package cn.biggar.biggar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.activity.WebViewActivity;
import cn.biggar.biggar.adapter.BrandShopAdapter;
import cn.biggar.biggar.api.BaseUrl;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.base.BiggarFragment;
import cn.biggar.biggar.bean.GoodsBean;
import cn.biggar.biggar.presenter.GoodsListPresenter;
import cn.biggar.biggar.view.MultiStateView;
import cn.biggar.biggar.view.pullableview.PullToRefreshLayout;
import cn.biggar.biggar.view.pullableview.PullableGridView;
import cn.biggar.biggar.view.pullableview.PullableListView;

import java.util.ArrayList;

import butterknife.BindView;
import per.sue.gear2.presenter.ListPresenter;

/**
 * Created by SUE on 2016/7/21 0021.
 * 品牌空间 商店
 */
public class BrandShopListFragment extends BiggarFragment implements View.OnClickListener, ListPresenter.ListResultView<ArrayList<GoodsBean>> {

    public BrandShopAdapter mAdapterList;
    public BrandShopAdapter mAdapterGrid;
    @BindView(R.id.pull_view_list)
    PullableListView mPullViewList;
    @BindView(R.id.refresh_view_list)
    PullToRefreshLayout mRefreshViewList;
    @BindView(R.id.pull_view_grid)
    PullableGridView mPullViewGrid;
    @BindView(R.id.refresh_view_grid)
    PullToRefreshLayout mRefreshViewGrid;
    @BindView(R.id.multiStateView_detail)
    MultiStateView mMSV;
    public String mType;
    private String mBrandID;
    private GoodsListPresenter mGoodsP;

    private boolean mIsList;//是否是 列表
    private boolean mIsMore=true;

    public static BrandShopListFragment getInstance(String type, String brandId) {
        BrandShopListFragment edManFragmentr = new BrandShopListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putString("brandId", brandId);
        edManFragmentr.setArguments(bundle);
        return edManFragmentr;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_brand_shop_list;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        mType = getArguments().getString("type");
        mBrandID = getArguments().getString("brandId");
        requstData(mType);
    }

    public View getScrollableView() {
        return mIsList ? mPullViewList : mPullViewGrid;
    }


    @Override
    public void onSuccessRefresh(ArrayList<GoodsBean> msg) {
        mRefreshViewList.refreshFinish(PullToRefreshLayout.SUCCEED);
        mRefreshViewGrid.refreshFinish(PullToRefreshLayout.SUCCEED);
        mIsMore=!(msg.size() < Constants.PAGE_SIZE);
        mAdapterList.setData(msg);
        mAdapterGrid.setData(msg);
        if(mAdapterGrid.getCount()==0){
            mMSV.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        }else{
            mMSV.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        }
    }

    @Override
    public void onSuccessLoadModre(ArrayList<GoodsBean> msg) {
        mRefreshViewList.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        mRefreshViewGrid.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        if (msg.size() < Constants.PAGE_SIZE) {
            mIsMore = false;
        }
        mAdapterList.addData(msg);
        mAdapterGrid.addData(msg);
    }

    @Override
    public void onClick(View v) {
    }


    public void switchState(boolean isList) {
        try {
            mIsList = isList;
            mRefreshViewList.setVisibility(isList ? View.VISIBLE : View.GONE);
            mRefreshViewGrid.setVisibility(isList ? View.GONE : View.VISIBLE);
        } catch (Exception e) {
        }
    }

    @Override
    public void onError(int code, String message) {
        super.onError(code, message);
        if(mAdapterList.getCount()==0){
            mMSV.setViewState(MultiStateView.VIEW_STATE_ERROR);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mGoodsP.cancelRequest();
    }
    public void requstData(String mType){
        mGoodsP = new GoodsListPresenter(getActivity(), this);
        mGoodsP.setType(mType);
        mGoodsP.setBrandID(mBrandID);

        mAdapterList = new BrandShopAdapter(getActivity(), R.layout.item_brand_shop);
        mAdapterGrid = new BrandShopAdapter(getActivity(), R.layout.item_brand_shop_grid);

        mPullViewList.setAdapter(mAdapterList);
        mPullViewGrid.setAdapter(mAdapterGrid);

        mRefreshViewList.setPullRefresh(false);
        mRefreshViewGrid.setPullRefresh(false);

        mRefreshViewList.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                mGoodsP.refresh();
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if(mIsMore){
                    mGoodsP.loadMore();
                }else {
                    mRefreshViewList.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
            }
        });

        mRefreshViewGrid.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                mGoodsP.refresh();
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if(mIsMore){
                    mGoodsP.loadMore();
                }else {
                    mRefreshViewGrid.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
            }
        });

        mMSV.setOnMultiStateViewClickListener(new MultiStateView.OnMultiStateViewClickListener() {
            @Override
            public void onRetry(int Type) {
                mGoodsP.refresh();
            }
        });

        mPullViewGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String goodsId=mAdapterGrid.getItem(position).getID();
                WebViewActivity.getInstance(getActivity(), BaseUrl.GET_GOODS_DETAILS_H5_URL + "?soure=g&version=" + Constants.WEB_VERSION +"&devices=android"+ "&ID=" + goodsId + "&BID=" + mBrandID);
            }
        });
        mPullViewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String goodsId=mAdapterList.getItem(position).getID();
                WebViewActivity.getInstance(getActivity(), BaseUrl.GET_GOODS_DETAILS_H5_URL + "?soure=g&version=" + Constants.WEB_VERSION +"&devices=android"+ "&ID=" + goodsId + "&BID=" + mBrandID);
            }
        });

        mGoodsP.refreshWithLoading();
    }
}
