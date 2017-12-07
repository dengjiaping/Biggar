package cn.biggar.biggar.activity.update;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import cn.biggar.biggar.bean.UserBean;
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
import cn.biggar.biggar.view.RootLayout;

/**
 * Created by Chenwy on 2017/5/31.
 */

public class MyXingzhiActivity extends BiggarActivity<StarRecordPresenter> implements StarRecordContract.View {
    @BindView(R.id.tv_type1)
    TextView tvType1;
    @BindView(R.id.iv_type1)
    ImageView ivType1;
    @BindView(R.id.rl_type1)
    RelativeLayout rlType1;
    @BindView(R.id.tv_xb)
    TextView tvXb;
    @BindView(R.id.rv_content)
    RecyclerView rvContent;
    @BindView(R.id.rv_filter)
    RecyclerView rvFilter;
    @BindView(R.id.view_cancel)
    View viewCancel;
    @BindView(R.id.ll_filter_content)
    LinearLayout llFilterContent;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private boolean isOpen;
    private String[] types = new String[]{"销售", "活动", "签到", "礼物", "互动"};
    private ListDropDownAdapter mListDropDownAdapter;

    private XbAdapter mXbAdapter;
    private static final String C_TYPE = "2";
    private static final String TYPE = "";
    private int curPage = 1;
    private String id;
    private String getType = "";
    private String mCurJyz = "0";

    @Override
    public int getLayoutResId() {
        return R.layout.activity_my_xingzhi;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        showLoading();

        String uid = getIntent().getStringExtra("uid");
        UserBean userBean = Preferences.getUserBean(this);
        //从我的界面过来的
        if(TextUtils.isEmpty(uid)){
            this.id = userBean.getId();
        }
        //从个人中心过来的
        else {
            this.id = uid;
            //没登录
            if (userBean == null){
                RootLayout.getInstance(this).setTitle("Ta的经验值");
            }
            //已经登录
            else {
                if (!this.id.equals(userBean.getId())){
                    RootLayout.getInstance(this).setTitle("Ta的经验值");
                }
            }
        }

        mCurJyz = getIntent().getExtras().getString("xz");
        tvXb.setText("当前经验值:" + mCurJyz);

        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                curPage = 1;
                mPresenter.requestDatas(MyXingzhiActivity.this.id, TYPE, C_TYPE, getType, curPage, Constants.PAGE_SIZE);
            }
        });

        mListDropDownAdapter = new ListDropDownAdapter(Arrays.asList(types));
        rvFilter.setLayoutManager(new LinearLayoutManager(this));
        rvFilter.setAdapter(mListDropDownAdapter);

        rvFilter.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                ivType1.setImageResource(R.mipmap.pull_down);
                tvType1.setTextColor(Color.parseColor("#666666"));
                llFilterContent.setVisibility(View.GONE);
                isOpen = false;
                String item = mListDropDownAdapter.getItem(position);
                tvType1.setText(item);

                switch (position) {
                    case 0:
                        getType = "1";
                        break;
                    case 1:
                        getType = "4";
                        break;
                    case 2:
                        getType = "6";
                        break;
                    case 3:
                        getType = "5";
                        break;
                    case 4:
                        getType = "9";
                        break;
                }

                curPage = 1;
                mPresenter.requestDatas(MyXingzhiActivity.this.id, TYPE, C_TYPE, getType, curPage, Constants.PAGE_SIZE);
            }
        });

        mXbAdapter = new XbAdapter(new ArrayList<XbBean>(), "xz");
        rvContent.setLayoutManager(new LinearLayoutManager(this));
        rvContent.setAdapter(mXbAdapter);
        mXbAdapter.setEnableLoadMore(true);
        mXbAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                curPage += 1;
                mPresenter.requestMoreDatas(MyXingzhiActivity.this.id, TYPE, C_TYPE, getType, curPage, Constants.PAGE_SIZE);
            }
        }, rvContent);
        mXbAdapter.setEmptyView(R.layout.empty_view);
        mXbAdapter.setLoadMoreView(new WhiteLoadMoreView());

        mPresenter.requestDatas(this.id, TYPE, C_TYPE, getType, curPage, Constants.PAGE_SIZE);
    }

    @OnClick({R.id.rl_type1, R.id.view_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_type1:
                if (!isOpen) {
                    ivType1.setImageResource(R.mipmap.value_up);
                    tvType1.setTextColor(ContextCompat.getColor(this, R.color.app_1a83d6));
                    llFilterContent.setVisibility(View.VISIBLE);
                } else {
                    ivType1.setImageResource(R.mipmap.pull_down);
                    tvType1.setTextColor(Color.parseColor("#666666"));
                    llFilterContent.setVisibility(View.GONE);
                }
                isOpen = !isOpen;
                break;
            case R.id.view_cancel:
                ivType1.setImageResource(R.mipmap.pull_down);
                tvType1.setTextColor(Color.parseColor("#666666"));
                llFilterContent.setVisibility(View.GONE);
                isOpen = false;
                break;
        }
    }

    @Override
    public void showError(String errMsg) {
        dismissLoading();
        refreshLayout.setRefreshing(false);
        if (curPage == 1) {
            if (mXbAdapter.getData().size() > 0) {
                ToastUtils.showError("请求失败，请检查网络");
            } else {
                setErrorView(mXbAdapter.getEmptyView());
            }
        } else {
            mXbAdapter.loadMoreFail();
        }
    }

    @Override
    public void showResults(List<XbBean> xbBeens) {
        dismissLoading();
        refreshLayout.setRefreshing(false);
        if (xbBeens.size() <= 0) {
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
