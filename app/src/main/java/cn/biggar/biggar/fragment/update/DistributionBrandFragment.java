package cn.biggar.biggar.fragment.update;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.yinglan.keyboard.HideUtil;

import java.util.List;

import cn.biggar.biggar.R;
import cn.biggar.biggar.activity.update.DistributionGoodsActivity;
import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.base.BasePresenter;
import cn.biggar.biggar.base.BiggarListFragment;
import cn.biggar.biggar.bean.BgResponse;
import cn.biggar.biggar.bean.BrandBean;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.http.JsonCallBack;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.utils.Utils;

/**
 * Created by Chenwy on 2017/7/17.
 * 分销品牌
 */

public class DistributionBrandFragment extends BiggarListFragment<BasePresenter, BrandBean> {
    private String searchName;
    private String userId = "";

    public static DistributionBrandFragment newInstance() {
        return new DistributionBrandFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        HideUtil.init(getActivity());
    }

    @Override
    protected void initDataBefore() {
        UserBean userBean = Preferences.getUserBean(getContext());
        if (userBean != null) {
            userId = userBean.getId();
        }
        setListBackGroundColor(R.color.app_e5e5e5);
    }

    @Override
    protected void initAfter() {

    }

    @Override
    protected void refreshData(boolean isLoadMore) {
        requestBrand(isLoadMore);
    }

    @Override
    protected int itemLayout() {
        return R.layout.item_link_shop;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(getContext(), 2);
    }

    @Override
    protected void myHolder(BaseViewHolder helper, BrandBean brandBean) {
        ImageView logo = helper.getView(R.id.iv_logo);
        Glide.with(mContext)
                .load(Utils.getRealUrl(brandBean.getE_Logo()))
                .apply(new RequestOptions().centerCrop())
                .into(logo);
        helper.setText(R.id.tv_brand_name, brandBean.getE_BrandCnName());
        helper.setGone(R.id.right_line, helper.getLayoutPosition() % 2 == 0).setGone(R.id.ll_can_sell, true);
        helper.setText(R.id.tv_num, brandBean.getE_Num() + "件");
    }

    @Override
    protected void onListItemClick(BrandBean data, int position) {
        startActivity(new Intent(getActivity(), DistributionGoodsActivity.class)
                .putExtra("brand_id", data.getE_BrandID())
                .putExtra("brand_name", data.getE_BrandCnName())
                .putExtra("type_id", "1"));
    }

    @Override
    protected void onListItemChildClick(int viewId, BrandBean data, int position) {

    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public void startSearch(String searchName) {
        this.searchName = searchName;
        curPage = 1;
        requestBrand(false);
    }

    private void requestBrand(final boolean isLoadMore) {
        String url = BaseAPI.BASE_URL + "Brand/brand?type_id=1&member_id=" + userId + "&p=" + curPage + "&pages=" + Constants.PAGE_SIZE;
        if (!TextUtils.isEmpty(searchName)) {
            url += "&name=" + searchName;
        }

        OkGo.<BgResponse<List<BrandBean>>>get(url)
                .tag(this)
                .execute(new JsonCallBack<BgResponse<List<BrandBean>>>(JsonCallBack.TYPE_SUPER) {
                    @Override
                    public void onSuccess(Response<BgResponse<List<BrandBean>>> response) {
                        dismissLoading();
                        BgResponse<List<BrandBean>> listBgResponse = response.body();
                        if (isLoadMore) {
                            finishLoadMore(listBgResponse.info);
                        } else {
                            finishRefresh(listBgResponse.info);
                        }
                    }

                    @Override
                    public void onError(Response<BgResponse<List<BrandBean>>> response) {
                        super.onError(response);
                        handleError("请求失败，请检查网络");
                    }
                });
    }

    @Override
    public void onDestroy() {
        OkGo.getInstance().cancelTag(this);
        super.onDestroy();
    }
}
