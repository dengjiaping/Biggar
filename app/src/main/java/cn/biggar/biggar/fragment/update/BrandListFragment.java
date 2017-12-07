package cn.biggar.biggar.fragment.update;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yinglan.keyboard.HideUtil;
import org.greenrobot.eventbus.Subscribe;
import java.util.List;
import cn.biggar.biggar.R;
import cn.biggar.biggar.activity.BrandSpaceActivity;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.base.BiggarListFragment;
import cn.biggar.biggar.bean.BrandBean;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.contract.BrandListContract;
import cn.biggar.biggar.event.SearchBrandEvent;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.presenter.update.BrandListPresenter;
import cn.biggar.biggar.utils.Utils;

/**
 * Created by Chenwy on 2017/7/26.
 * 品牌列表
 */

public class BrandListFragment extends BiggarListFragment<BrandListPresenter, BrandBean> implements BrandListContract.View {
    private int mFlag = 0;//会员和品牌已相互关注（1）、会员关注的所有品牌（0）、默认为：0
    private int mMode; // 0   推荐  1 关注

    private String mUserID;
    private String searchName;

    public static BrandListFragment newInstance(int mode) {
        BrandListFragment brandListFragment = new BrandListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("mode", mode);
        brandListFragment.setArguments(bundle);
        return brandListFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        HideUtil.init(getActivity());
    }

    @Override
    protected void initDataBefore() {
        searchName = "";
        mMode = getArguments().getInt("mode");
        getUserInfo();
    }

    @Override
    protected void initAfter() {

    }

    @Override
    protected void refreshData(boolean isLoadMore) {
        requestData(isLoadMore);
    }

    @Override
    protected int itemLayout() {
        return R.layout.item_brand_list;
    }

    @Override
    protected void myHolder(BaseViewHolder helper, BrandBean brandBean) {
        ImageView imageViewlogo = helper.getView(R.id.brand_logo_iv);
        Glide.with(mContext).load(Utils.getRealUrl(brandBean.getE_Logo()))
                .apply(new RequestOptions().centerCrop().transform(new RoundedCorners(8)))
                .into(imageViewlogo);


        ImageView imageView = helper.getView(R.id.brand_iv);
        Glide.with(mContext).load(Utils.getRealUrl(brandBean.getE_Img3()))
                .apply(new RequestOptions().centerCrop())
                .into(imageView);


        helper.setText(R.id.brand_title1_tv, brandBean.getE_CompanyTitle());
        helper.setText(R.id.brand_title2_tv, brandBean.getE_BrandCnName());
        helper.setText(R.id.brand_content_tv, brandBean.getE_BrandCnName());
    }

    @Override
    protected void onListItemClick(BrandBean data, int position) {
        startActivity(BrandSpaceActivity.startIntent(getActivity(), data.getID()));
    }

    @Override
    protected void onListItemChildClick(int viewId, BrandBean data, int position) {

    }

    private void getUserInfo() {
        if (mMode == 0) {
            return;
        }
        UserBean userBean = Preferences.getUserBean(getActivity());
        if (userBean != null) {
            mUserID = userBean.getId();
        }
    }

    @Override
    public boolean isOpenEventBus() {
        return true;
    }

    @Subscribe
    public void onSearch(SearchBrandEvent event) {
        if (event != null && event.type == mMode) {
            setSearchKey(event.searchKey);
        }
    }

    /**
     * 设置搜索 关键字
     *
     * @param key
     */
    private void setSearchKey(String key) {
        searchName = key;
        curPage = 1;
        requestData(false);
    }

    private void requestData(boolean isLoadMore) {
        if (mMode == 0) {
            requestRecommendList(isLoadMore);
        } else {
            requestFollowList(isLoadMore);
        }
    }

    /**
     * 推荐列表
     */
    private void requestRecommendList(final boolean isLoadMore) {
        mPresenter.requestRecommendBrandList(isLoadMore, searchName, curPage, Constants.PAGE_SIZE);
    }

    /**
     * 关注列表
     */
    private void requestFollowList(final boolean isLoadMore) {
        mPresenter.requestFollowBrandList(isLoadMore, searchName, mUserID, mFlag, curPage, Constants.PAGE_SIZE);
    }

    @Override
    public void showError(String errMsg) {
        handleError(errMsg);
    }

    @Override
    public void showRecommendBrandList(List<BrandBean> brandBeen) {
        dismissLoading();
        finishRefresh(brandBeen);
    }

    @Override
    public void loadMoreRecommendBrandList(List<BrandBean> brandBeen) {
        dismissLoading();
        finishLoadMore(brandBeen);
    }

    @Override
    public void showFollowBrandList(List<BrandBean> brandBeen) {
        dismissLoading();
        finishRefresh(brandBeen);
    }

    @Override
    public void loadMoewFollowBrandList(List<BrandBean> brandBeen) {
        dismissLoading();
        finishLoadMore(brandBeen);
    }
}
