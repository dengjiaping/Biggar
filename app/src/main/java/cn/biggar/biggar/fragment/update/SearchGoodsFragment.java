package cn.biggar.biggar.fragment.update;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseViewHolder;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cn.biggar.biggar.R;
import cn.biggar.biggar.activity.WebViewActivity;
import cn.biggar.biggar.api.BaseUrl;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.base.BasePresenter;
import cn.biggar.biggar.base.BiggarListFragment;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.bean.update.SearchGoodsBean;
import cn.biggar.biggar.contract.SearchGoodsContract;
import cn.biggar.biggar.event.SearchEvent;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.presenter.update.SearchGoodsPresenter;
import cn.biggar.biggar.utils.Utils;

/**
 * Created by Chenwy on 2017/9/7.
 */

public class SearchGoodsFragment extends BiggarListFragment<SearchGoodsPresenter, SearchGoodsBean> implements SearchGoodsContract.View {
    private String uid;
    private String name;

    public static SearchGoodsFragment newInstance(String searchName) {
        SearchGoodsFragment searchGoodsFragment = new SearchGoodsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("searchName", searchName);
        searchGoodsFragment.setArguments(bundle);
        return searchGoodsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = getArguments().getString("searchName");
    }

    @Override
    protected void initDataBefore() {
        UserBean userBean = Preferences.getUserBean(getContext());
        if (userBean != null) {
            uid = userBean.getId();
        }
    }

    @Override
    protected void initAfter() {
    }

    @Override
    public boolean isOpenEventBus() {
        return true;
    }

    @Subscribe
    public void search(SearchEvent searchEvent) {
        name = searchEvent.searchName;
        if (searchEvent.currentItem == 2) {
            showLoading();
        }
        curPage = 1;
        mPresenter.requestGoods("3", uid, Constants.PAGE_SIZE, curPage, name);
    }

    public void setSearchName(String searchName) {
        name = searchName;
    }

    @Override
    protected void refreshData(final boolean isLoadMore) {
        if (!isLoadMore) {
            mPresenter.requestGoods("3", uid, Constants.PAGE_SIZE, curPage, name);
        } else {
            mPresenter.loadMoreGoods("3", uid, Constants.PAGE_SIZE, curPage, name);
        }
    }

    @Override
    protected int itemLayout() {
        return R.layout.item_search_goods;
    }

    @Override
    protected void myHolder(BaseViewHolder helper, SearchGoodsBean data) {
        Glide.with(mContext).load(Utils.getRealUrl(data.E_Img1))
                .apply(new RequestOptions().centerCrop()).into((ImageView) helper.getView(R.id.iv_link_goods));
        helper.setText(R.id.tv_link_goods_name, data.E_Name)
                .setText(R.id.tv_link_goods_brand, data.E_BrandCnName)
                .setText(R.id.tv_link_goods_price, "￥" + data.E_SellPrice);
        if (helper.getLayoutPosition() == 0) {
            helper.getView(R.id.ll_root_view).setPadding(0, Utils.dip2px(mContext, 22), 0, 0);
        } else {
            helper.getView(R.id.ll_root_view).setPadding(0, 0, 0, 0);
        }
    }

    @Override
    protected void onListItemClick(SearchGoodsBean data, int position) {
        WebViewActivity.getInstance(getActivity(), BaseUrl.GET_GOODS_DETAILS_H5_URL
                + "?soure=g&version=" + Constants.WEB_VERSION
                + "&devices=android" + "&ID=" + data.ID + "&BID=" + data.E_CompanyID);
    }

    @Override
    protected void onListItemChildClick(int viewId, SearchGoodsBean data, int position) {

    }

    @Override
    public void showError(String errMsg) {
        handleError(errMsg);
    }

    @Override
    public void showGoodses(List<SearchGoodsBean> searchGoodsBeen) {
        finishRefresh(searchGoodsBeen);
    }

    @Override
    public void showMoreGoodses(List<SearchGoodsBean> searchGoodsBeen) {
        finishLoadMore(searchGoodsBeen);
    }
}
