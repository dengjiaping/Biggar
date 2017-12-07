package cn.biggar.biggar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.activity.WebViewActivity;
import cn.biggar.biggar.adapter.ActivityListAdapter;
import cn.biggar.biggar.api.BaseUrl;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.bean.ActivityBean;
import cn.biggar.biggar.presenter.ActivityListPersenter;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.view.MultiStateView;
import cn.biggar.biggar.view.pullableview.PullToRefreshLayout;
import cn.biggar.biggar.view.pullableview.PullableListView;

import java.util.ArrayList;

import butterknife.BindView;
import per.sue.gear2.presenter.ListPresenter;

/**
 * Created by mx on 2016/8/12.
 * 品牌空间 活动
 */
public class BrandActivityFragment extends HeaderViewPagerFragment implements ListPresenter.ListResultView<ArrayList<ActivityBean>> {
    @BindView(R.id.refresh_view)
    PullToRefreshLayout mRefreshLayout;
    @BindView(R.id.pull_view)
    PullableListView mListView;
    @BindView(R.id.multiStateView_detail)
    MultiStateView mMSV;
    private ActivityListPersenter mListPersenter;
    private ActivityListAdapter mAdapter;
    private String mBrandId;
    private boolean mIsMore=true;

    public static BrandActivityFragment getInstance(String id) {
        BrandActivityFragment brandActivityFragment = new BrandActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        brandActivityFragment.setArguments(bundle);
        return brandActivityFragment;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_brand_activity;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        mBrandId = getArguments().getString("id");

        mRefreshLayout.setPullRefresh(false);

        mListPersenter = new ActivityListPersenter(getActivity(), this);
        mListPersenter.setType(0);
        mListPersenter.setBrandId(mBrandId);
        mListPersenter.refreshWithLoading();

        mAdapter = new ActivityListAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                mRefreshLayout.setLoadMore(true);
                mListPersenter.refresh();

            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if (mIsMore) {
                    mListPersenter.loadMore();
                } else {
                    mRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    ActivityBean bean= (ActivityBean) parent.getAdapter().getItem(position);
                    if (bean!=null){
                        WebViewActivity.getInstance(getContext(), BaseUrl.GET_ACTIVITY_DETAILS_H5_URL+"?soure=activity_details&version="+Constants.WEB_VERSION+"&ID="+bean.getID());
                    }else{
                        ToastUtils.showError("数据错误");
                    }
                }catch (Exception e){
                }
            }
        });
        mMSV.setOnMultiStateViewClickListener(new MultiStateView.OnMultiStateViewClickListener() {
            @Override
            public void onRetry(int Type) {

                mMSV.setViewState(MultiStateView.VIEW_STATE_LOADING);
                mListPersenter.refresh();
            }
        });
    }

    @Override
    public View getScrollableView() {
        return mListView;
    }

    @Override
    public void onSuccessRefresh(ArrayList<ActivityBean> msg) {
        mRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        mIsMore=!(msg.size() < Constants.PAGE_SIZE);
        mAdapter.setData(msg);
        if(mAdapter.getCount()==0){
            mMSV.setViewState(MultiStateView.VIEW_STATE_LOADING);
        }else{
            mMSV.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        }
    }

    @Override
    public void onSuccessLoadModre(ArrayList<ActivityBean> msg) {
        mRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        if (msg.size() < Constants.PAGE_SIZE) {
            mIsMore = false;
        }
        mAdapter.addData(msg);
        mAdapter.addData(msg);
    }
    @Override
    public void onError(int code, String message) {
        super.onError(code, message);
        if(mAdapter.getCount()==0){
            mMSV.setViewState(MultiStateView.VIEW_STATE_ERROR);
            mMSV.setHintTextColorByState(MultiStateView.VIEW_STATE_ERROR,R.color.app_text_color_z,R.mipmap.activity_empty_page,"");
        }

    }
    @Override
    public void onDestroyView() {
        mListPersenter.cancelRequest();
        super.onDestroyView();
    }


}
