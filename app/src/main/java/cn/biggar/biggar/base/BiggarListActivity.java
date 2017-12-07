package cn.biggar.biggar.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.loadmore.LoadMoreView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.biggar.biggar.R;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.view.RootLayout;
import cn.biggar.biggar.view.WhiteLoadMoreView;

/**
 * Created by Chenwy on 2017/8/3.
 */

public abstract class BiggarListActivity<T extends BasePresenter, D> extends BiggarActivity<T> implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.rv)
    RecyclerView rv;

    private ListAdapter listAdapter;
    protected int curPage;
    private boolean isLoadMoreFail;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_commom_list;
    }

    @Override
    public void onInitialize(Bundle savedInstanceState) {
        showLoading();

        initDataBefore();

        initRefresh();

        rv.setLayoutManager(getLayoutManager());
        listAdapter = new ListAdapter(new ArrayList<D>());
        rv.setAdapter(listAdapter);

        listAdapter.setEnableLoadMore(isEnableLoadMore());
        listAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (!isLoadMoreFail) {
                    curPage += 1;
                }
                refreshData(true);
            }
        }, rv);
        if (isShowEmptyView()){
            listAdapter.setEmptyView(R.layout.empty_view);
        }
        listAdapter.setLoadMoreView(getLoadView());

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

        initDataAfter();

        curPage = 1;
        refreshData(false);
    }

    protected RecyclerView getRv(){
        return rv;
    }

    protected abstract void initDataBefore();

    protected abstract void initDataAfter();

    protected abstract void refreshData(boolean isLoadMore);

    protected abstract int itemLayout();

    protected abstract void myHolder(Context context, BaseViewHolder helper, D t);

    protected abstract void onListItemClick(D data, int position);

    protected abstract void onListItemChildClick(int viewId, D data, int position);

    protected void setCenterTitle(String title) {
        RootLayout.getInstance(this).setTitle(title);
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

    protected D getItemData(int position) {
        if (listAdapter != null) {
            return listAdapter.getItem(position);
        }
        return null;
    }

    private void initRefresh() {
        refreshLayout.setEnabled(isEnableRefresh());
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(this);
    }

    public void finishRefresh(List<D> datas) {
        dismissLoading();
        refreshLayout.setRefreshing(false);
        isLoadMoreFail = false;
        if (datas.size() <= 0 && isShowEmptyView()) {
            setEmptyView(listAdapter.getEmptyView());
        }
        listAdapter.setNewData(datas);
        listAdapter.disableLoadMoreIfNotFullPage();
        if (datas.size() < cn.biggar.biggar.app.Constants.PAGE_SIZE) {
            listAdapter.loadMoreEnd(true);
        } else {
            listAdapter.loadMoreComplete();
        }
    }

    public void finishLoadMore(List<D> datas) {
        dismissLoading();
        refreshLayout.setRefreshing(false);
        isLoadMoreFail = false;
        listAdapter.addData(datas);
        if (datas.size() < cn.biggar.biggar.app.Constants.PAGE_SIZE) {
            listAdapter.loadMoreEnd(true);
        } else {
            listAdapter.loadMoreComplete();
        }
    }

    public void setListBackGroundColor(int colorRec) {
        rv.setBackgroundColor(ContextCompat.getColor(this, colorRec));
    }

    public LoadMoreView getLoadView() {
        return new WhiteLoadMoreView();
    }

    public boolean isEnableLoadMore(){
        return true;
    }

    public boolean isEnableRefresh(){
        return true;
    }

    public boolean isShowEmptyView(){
        return true;
    }


    public View getHeaderView(int headerViewRes) {
        View header = getLayoutInflater().inflate(headerViewRes, (ViewGroup) rv.getParent(), false);
        listAdapter.addHeaderView(header);
        return header;
    }


    public void handleError() {
        handleError("请求失败，请检查网络");
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
        return new LinearLayoutManager(getApplicationContext());
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
        protected void convert(BaseViewHolder helper, D t) {
            myHolder(mContext, helper, t);
        }
    }
}
