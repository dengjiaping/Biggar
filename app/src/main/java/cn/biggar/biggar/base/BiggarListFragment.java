package cn.biggar.biggar.base;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.biggar.biggar.R;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.view.AutoSwipeRefreshLayout;
import cn.biggar.biggar.view.WhiteLoadMoreView;

/**
 * Created by Chenwy on 2017/8/3.
 */

public abstract class BiggarListFragment<T extends BasePresenter, D> extends LazyFragment<T> implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.refreshLayout)
    AutoSwipeRefreshLayout refreshLayout;
    @BindView(R.id.rv)
    RecyclerView rv;

    private ListAdapter listAdapter;
    protected int curPage;
    private boolean isLoadMoreFail;

    @Override
    public int getLayoutResId() {
        return R.layout.frg_commom_list;
    }

    @Override
    public void lazyLoadData(Bundle savedInstanceState) {
        showLoading();

        initDataBefore();

        initRefresh();

        rv.setLayoutManager(getLayoutManager());
        listAdapter = new ListAdapter(new ArrayList<D>());
        rv.setAdapter(listAdapter);

        listAdapter.setEnableLoadMore(true);
        listAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (!isLoadMoreFail) {
                    curPage += 1;
                }
                refreshData(true);
            }
        }, rv);
        listAdapter.setEmptyView(R.layout.empty_view);
        listAdapter.setLoadMoreView(new WhiteLoadMoreView());

        initAfter();

        listAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                onListItemClick(listAdapter.getItem(position), position);
            }
        });

        listAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                onListItemChildClick(view.getId(), listAdapter.getItem(position), position);
            }
        });

        curPage = 1;
        refreshData(false);
    }


    protected abstract void initDataBefore();

    protected abstract void initAfter();

    protected abstract void refreshData(boolean isLoadMore);

    protected abstract int itemLayout();

    protected abstract void myHolder(BaseViewHolder helper, D data);

    protected abstract void onListItemClick(D data, int position);

    protected abstract void onListItemChildClick(int viewId, D data, int position);

    private void initRefresh() {
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(this);
    }

    protected void notifyItemChanged(int position) {
        if (listAdapter != null) {
            listAdapter.notifyItemChanged(position);
        }
    }

    protected void remove(int position) {
        if (listAdapter != null) {
            listAdapter.remove(position);
        }
    }

    protected D getItem(int position) {
        D item = listAdapter.getItem(position);
        return item;
    }

    protected void clearDatas() {
        listAdapter.setNewData(new ArrayList<D>());
    }

    protected void toTopWithRefresh() {
        rv.scrollToPosition(0);
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.autoRefresh();
//                curPage = 1;
//                refreshData(false);
            }
        });
    }

    protected View getHeaderView(int headerLayout) {
        View headerBanner = mActivity.getLayoutInflater().inflate(headerLayout, (ViewGroup) rv.getParent(), false);
        listAdapter.addHeaderView(headerBanner);
        return headerBanner;
    }

    public void finishRefresh(List<D> datas) {
        dismissLoading();
        refreshLayout.setRefreshing(false);
        isLoadMoreFail = false;
        if (datas.size() <= 0) {
            setEmptyView(listAdapter.getEmptyView());
        }
        listAdapter.setNewData(datas);
        listAdapter.disableLoadMoreIfNotFullPage();
        if (datas.size() < Constants.PAGE_SIZE) {
            listAdapter.loadMoreEnd(true);
        } else {
            listAdapter.loadMoreComplete();
        }

    }

    public void setEmptyBgColor(int color) {
        if (listAdapter != null && listAdapter.getEmptyView() != null) {
            listAdapter.getEmptyView().findViewById(R.id.rl_bg).setBackgroundColor(ContextCompat.getColor(getContext(), color));
        }
    }

    public void finishLoadMore(List<D> datas) {
        dismissLoading();
        refreshLayout.setRefreshing(false);
        isLoadMoreFail = false;
        listAdapter.addData(datas);
        if (datas.size() < Constants.PAGE_SIZE) {
            listAdapter.loadMoreEnd(true);
        } else {
            listAdapter.loadMoreComplete();
        }
    }

    public void setListBackGroundColor(int colorRec) {
        rv.setBackgroundResource(colorRec);
    }


    /**
     * 处理错误
     *
     * @param msg
     */
    public void handleError(String msg) {
        dismissLoading();
        refreshLayout.setRefreshing(false);
        if (curPage == 1) {
            if (listAdapter.getData().size() > 0) {
                ToastUtils.showError(msg);
            } else {
                setErrorView(listAdapter.getEmptyView(), msg);
            }
        } else {
            isLoadMoreFail = true;
            listAdapter.loadMoreFail();
        }
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    @Override
    public void onRefresh() {
        curPage = 1;
        refreshData(false);
    }

    public class ListAdapter extends BaseQuickAdapter<D, BaseViewHolder> {

        public ListAdapter(List<D> data) {
            super(itemLayout(), data);
        }

        @Override
        protected void convert(BaseViewHolder helper, D data) {
            myHolder(helper, data);
        }
    }
}
