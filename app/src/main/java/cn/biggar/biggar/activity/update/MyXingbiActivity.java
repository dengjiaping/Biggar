package cn.biggar.biggar.activity.update;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.adapter.update.ListDropDownAdapter;
import cn.biggar.biggar.adapter.update.XbAdapter;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.bean.update.XbBean;
import cn.biggar.biggar.contract.StarRecordContract;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.presenter.update.StarRecordPresenter;
import cn.biggar.biggar.utils.ToastUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.biggar.biggar.view.WhiteLoadMoreView;

/**
 * Created by Chenwy on 2017/5/31.
 */

public class MyXingbiActivity extends BiggarActivity<StarRecordPresenter> implements StarRecordContract.View {


    @BindView(R.id.rv_filter)
    RecyclerView rvFilter;
    @BindView(R.id.rv_filter2)
    RecyclerView rvFilter2;
    @BindView(R.id.tv_type1)
    TextView tvType1;
    @BindView(R.id.iv_type1)
    ImageView ivType1;
    @BindView(R.id.tv_type2)
    TextView tvType2;
    @BindView(R.id.iv_type2)
    ImageView ivType2;
    @BindView(R.id.ll_filter_content)
    LinearLayout llFilterContent;
    @BindView(R.id.rl_type1)
    RelativeLayout rlType1;
    @BindView(R.id.rl_type2)
    RelativeLayout rlType2;
    @BindView(R.id.rv_content)
    RecyclerView rvContent;
    @BindView(R.id.tv_xb)
    TextView tvXb;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private boolean isOpen1;
    private boolean isOpen2;
    private ListDropDownAdapter mListDropDownAdapter;
    private ListDropDownAdapter mListDropDownAdapter2;
    private String[] datas = new String[]{"全部", "获得", "消耗"};
    private String[] requestDatas = new String[]{"充值", "活动", "签到", "抽奖", "互动"};
    private String[] outDatas = new String[]{"送礼", "兑换", "抽奖"};

    private XbAdapter mXbAdapter;
    private String mCurXb = "0";

    private static final String C_TYPE = "1";
    private int curPage = 1;
    private String id;
    private String type = "1";
    private String getType = "";

    @Override
    public int getLayoutResId() {
        return R.layout.activity_my_xingbi;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        mCurXb = getIntent().getStringExtra("xb");
        tvXb.setText("当前星币：" + mCurXb);
        showLoading();

        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                curPage = 1;
                mPresenter.requestDatas(id, type, C_TYPE, getType, curPage, Constants.PAGE_SIZE);
            }
        });

        mListDropDownAdapter = new ListDropDownAdapter(Arrays.asList(datas));
        rvFilter.setLayoutManager(new LinearLayoutManager(this));
        rvFilter.setAdapter(mListDropDownAdapter);

        mListDropDownAdapter2 = new ListDropDownAdapter(new ArrayList<String>());
        rvFilter2.setLayoutManager(new LinearLayoutManager(this));
        rvFilter2.setAdapter(mListDropDownAdapter2);

        rvFilter.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                isOpen1 = false;
                rvFilter.setVisibility(View.GONE);
                ivType1.setImageResource(R.mipmap.pull_down);
                tvType1.setTextColor(Color.parseColor("#666666"));

                switch (position) {
                    case 0:
                        isOpen2 = false;
                        rlType2.setVisibility(View.GONE);
                        rvFilter2.setVisibility(View.GONE);
                        llFilterContent.setVisibility(View.GONE);
                        ivType2.setImageResource(R.mipmap.pull_down);
                        tvType2.setTextColor(Color.parseColor("#666666"));
                        tvType1.setText("全部");
                        type = "1";
                        getType = "";
                        break;
                    case 1:
                        isOpen2 = true;
                        rlType2.setVisibility(View.VISIBLE);
                        rvFilter2.setVisibility(View.GONE);
                        llFilterContent.setVisibility(View.GONE);
                        ivType2.setImageResource(R.mipmap.pull_down);
                        tvType2.setTextColor(Color.parseColor("#666666"));
                        tvType1.setText("获得");
                        tvType2.setText("获得方式");
                        mListDropDownAdapter2.setNewData(Arrays.asList(requestDatas));
                        type = "2";
                        getType = "";
                        break;
                    case 2:
                        isOpen2 = true;
                        rlType2.setVisibility(View.VISIBLE);
                        rvFilter2.setVisibility(View.GONE);
                        llFilterContent.setVisibility(View.GONE);
                        ivType2.setImageResource(R.mipmap.pull_down);
                        tvType2.setTextColor(Color.parseColor("#666666"));
                        tvType1.setText("消耗");
                        tvType2.setText("消耗方式");
                        mListDropDownAdapter2.setNewData(Arrays.asList(outDatas));
                        type = "3";
                        getType = "";
                        break;
                }

                curPage = 1;
                mPresenter.requestDatas(id, type, C_TYPE, getType, curPage, Constants.PAGE_SIZE);
            }
        });

        rvFilter2.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                isOpen2 = false;
                ivType1.setImageResource(R.mipmap.pull_down);
                tvType1.setTextColor(Color.parseColor("#666666"));
                ivType2.setImageResource(R.mipmap.pull_down);
                tvType2.setTextColor(Color.parseColor("#666666"));
                rvFilter.setVisibility(View.GONE);
                rvFilter2.setVisibility(View.GONE);
                llFilterContent.setVisibility(View.GONE);
                String item = mListDropDownAdapter2.getItem(position);
                tvType2.setText(item);

                if (type.equals("2")) {
                    switch (position) {
                        case 0:
                            getType = "8";
                            break;
                        case 1:
                            getType = "4";
                            break;
                        case 2:
                            getType = "6";
                            break;
                        case 3:
                            getType = "3";
                            break;
                        case 4:
                            getType = "9";
                            break;
                    }
                } else if (type.equals("3")) {
                    switch (position) {
                        case 0:
                            getType = "5";
                            break;
                        case 1:
                            getType = "2";
                            break;
                        case 2:
                            getType = "3";
                            break;
                    }
                }


                curPage = 1;
                mPresenter.requestDatas(id, type, C_TYPE, getType, curPage, Constants.PAGE_SIZE);
            }
        });


        mXbAdapter = new XbAdapter(new ArrayList<XbBean>(), "xb");
        rvContent.setLayoutManager(new LinearLayoutManager(this));
        rvContent.setAdapter(mXbAdapter);
        mXbAdapter.setEnableLoadMore(true);
        mXbAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                curPage += 1;
                mPresenter.requestMoreDatas(id, type, C_TYPE, getType, curPage, Constants.PAGE_SIZE);
            }
        }, rvContent);
        mXbAdapter.setEmptyView(R.layout.empty_view);
        mXbAdapter.setLoadMoreView(new WhiteLoadMoreView());


        id = Preferences.getUserBean(this).getId();
        mPresenter.requestDatas(id, type, C_TYPE, getType, curPage, Constants.PAGE_SIZE);
    }

    @OnClick({R.id.rl_type1, R.id.rl_type2, R.id.view_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_type1:
                if (!isOpen1) {
                    ivType1.setImageResource(R.mipmap.star_up);
                    tvType1.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    llFilterContent.setVisibility(View.VISIBLE);
                    rvFilter.setVisibility(View.VISIBLE);
                } else {
                    ivType1.setImageResource(R.mipmap.pull_down);
                    tvType1.setTextColor(Color.parseColor("#666666"));
                    llFilterContent.setVisibility(View.GONE);
                    rvFilter.setVisibility(View.GONE);
                }
                rvFilter2.setVisibility(View.GONE);
                ivType2.setImageResource(R.mipmap.pull_down);
                tvType2.setTextColor(Color.parseColor("#666666"));
                isOpen1 = !isOpen1;
                break;
            case R.id.rl_type2:
                if (!isOpen2) {
                    ivType2.setImageResource(R.mipmap.star_up);
                    tvType2.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    llFilterContent.setVisibility(View.VISIBLE);
                    rvFilter2.setVisibility(View.VISIBLE);
                } else {
                    ivType2.setImageResource(R.mipmap.pull_down);
                    tvType2.setTextColor(Color.parseColor("#666666"));
                    llFilterContent.setVisibility(View.GONE);
                    rvFilter2.setVisibility(View.GONE);
                }
                rvFilter.setVisibility(View.GONE);
                ivType1.setImageResource(R.mipmap.pull_down);
                tvType1.setTextColor(Color.parseColor("#666666"));
                isOpen2 = !isOpen2;
                break;
            case R.id.view_cancel:
                isOpen1 = false;
                isOpen2 = false;
                ivType1.setImageResource(R.mipmap.pull_down);
                tvType1.setTextColor(Color.parseColor("#666666"));
                ivType2.setImageResource(R.mipmap.pull_down);
                tvType2.setTextColor(Color.parseColor("#666666"));
                rvFilter.setVisibility(View.GONE);
                rvFilter2.setVisibility(View.GONE);
                llFilterContent.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void showError(String errMsg) {
        dismissLoading();
        refreshLayout.setRefreshing(false);
        if (curPage == 1){
            if (mXbAdapter.getData().size() > 0){
                ToastUtils.showError("请求失败，请检查网络");
            }else {
                setErrorView(mXbAdapter.getEmptyView());
            }
        }else {
            mXbAdapter.loadMoreFail();
        }
    }

    @Override
    public void showResults(List<XbBean> xbBeens) {
        dismissLoading();
        refreshLayout.setRefreshing(false);
        if (xbBeens.size() <= 0){
            setEmptyView(mXbAdapter.getEmptyView());
        }
        mXbAdapter.setNewData(xbBeens);
        mXbAdapter.disableLoadMoreIfNotFullPage();
        if (xbBeens.size() < Constants.PAGE_SIZE) {
            mXbAdapter.loadMoreEnd(true);
        }
    }

    @Override
    public void showMoreResults(List<XbBean> xbBeens) {
        dismissLoading();
        refreshLayout.setRefreshing(false);
        mXbAdapter.addData(xbBeens);
        if (xbBeens.size() > 0) {
            mXbAdapter.loadMoreComplete();
        } else {
            mXbAdapter.loadMoreEnd(true);
        }
    }
}
