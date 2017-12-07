package cn.biggar.biggar.fragment.update;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.yinglan.keyboard.HideUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.biggar.biggar.R;
import cn.biggar.biggar.adapter.update.LinkGoodsShopAdapter;
import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.base.LazyFragment;
import cn.biggar.biggar.bean.BgResponse;
import cn.biggar.biggar.bean.BrandBean;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.http.JsonCallBack;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.view.LinkGoodsPop;
import cn.biggar.biggar.view.WhiteLoadMoreView;

/**
 * Created by Chenwy on 2017/7/17.
 */

public class LinkGoodsFragment extends LazyFragment {
    @BindView(R.id.rv_link_shop)
    RecyclerView rvLinkGoodsShop;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private int mPosition;
    private LinkGoodsShopAdapter linkGoodsShopAdapter;
    private String clickShopName = "";
    private int curPage = 1;
    private LinkGoodsPop linkGoodsPop;
    private String searchName;
    private String userId;

    public static LinkGoodsFragment getInstance(int position) {
        LinkGoodsFragment linkGoodsFragment = new LinkGoodsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        linkGoodsFragment.setArguments(bundle);
        return linkGoodsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt("position");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        HideUtil.init(getActivity());
    }

    public void setSearchName(String searchName) {
        Logger.e("searchName ===> " + searchName);
        this.searchName = searchName;
    }

    public void startSearch(String searchName) {
        Logger.e("startSearch...");
        this.searchName = searchName;
        curPage = 1;
        requestShop();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.frg_link_goods;
    }

    @Override
    protected void lazyLoadData(Bundle savedInstanceState) {
        showLoading();
        UserBean userBean = Preferences.getUserBean(getContext());
        if (userBean != null) {
            userId = userBean.getId();
        }
        linkGoodsPop = new LinkGoodsPop(getActivity());
        linkGoodsShopAdapter = new LinkGoodsShopAdapter(mPosition == 1, new ArrayList<BrandBean>());
        linkGoodsShopAdapter.setEnableLoadMore(true);
        rvLinkGoodsShop.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rvLinkGoodsShop.setAdapter(linkGoodsShopAdapter);


        linkGoodsPop.setOnLinkGoodsChooseLinstener(new LinkGoodsPop.OnLinkGoodsChooseLinstener() {
            @Override
            public void onLinkGoodsChoose(String goodsId, String goodsImage, String goodsPrice, String goodsName,String rate) {
                Intent intent = new Intent();
                intent.putExtra("shop_name", clickShopName);
                intent.putExtra("goods_id", goodsId);
                intent.putExtra("goods_image", goodsImage);
                intent.putExtra("goods_price", goodsPrice);
                intent.putExtra("goods_name", goodsName);
                intent.putExtra("rate",rate);
                getActivity().setResult(getActivity().RESULT_OK, intent);
                getActivity().finish();
            }
        });

        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                curPage = 1;
                requestShop();
            }
        });

        linkGoodsShopAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                curPage += 1;
                requestShop();
            }
        }, rvLinkGoodsShop);
        linkGoodsShopAdapter.setEmptyView(R.layout.empty_view);
        linkGoodsShopAdapter.setLoadMoreView(new WhiteLoadMoreView());

        rvLinkGoodsShop.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                clickShopName = linkGoodsShopAdapter.getItem(position).getE_BrandCnName();
                linkGoodsPop.setData(linkGoodsShopAdapter.getItem(position).getE_BrandID(), String.valueOf(mPosition), userId);
                linkGoodsPop.showPopupWindow();
            }
        });
        requestShop();
    }

    private void requestShop() {
        String url = BaseAPI.BASE_URL + "Brand/brand?type_id=" + mPosition + "&member_id=" + userId + "&p=" + curPage + "&pages=" + Constants.PAGE_SIZE;
        if (!TextUtils.isEmpty(searchName)) {
            url += "&name=" + searchName;
        }

        OkGo.<BgResponse<List<BrandBean>>>get(url)
                .execute(new JsonCallBack<BgResponse<List<BrandBean>>>(JsonCallBack.TYPE_SUPER) {


                    @Override
                    public void onSuccess(Response<BgResponse<List<BrandBean>>> response) {
                        dismissLoading();
                        BgResponse<List<BrandBean>> listBgResponse = response.body();
                        refreshLayout.setRefreshing(false);
                        List<BrandBean> brandBeen = listBgResponse.info;
                        if (curPage == 1) {
                            if (brandBeen.size() <= 0){
                                setEmptyView(linkGoodsShopAdapter.getEmptyView());
                            }
                            linkGoodsShopAdapter.setNewData(brandBeen);
                            if (brandBeen.size() < Constants.PAGE_SIZE) {
                                linkGoodsShopAdapter.loadMoreEnd(true);
                            }
                        } else {
                            linkGoodsShopAdapter.addData(brandBeen);
                            if (brandBeen.size() < Constants.PAGE_SIZE) {
                                linkGoodsShopAdapter.loadMoreEnd(true);
                            } else {
                                linkGoodsShopAdapter.loadMoreComplete();
                            }
                        }
                    }

                    @Override
                    public void onError(Response<BgResponse<List<BrandBean>>> response) {
                        super.onError(response);
                        dismissLoading();
                        refreshLayout.setRefreshing(false);
                        if (curPage == 1){
                            if (linkGoodsShopAdapter.getData().size() > 0){
                                ToastUtils.showError("请求失败，请检查网络");
                            }else {
                                setErrorView(linkGoodsShopAdapter.getEmptyView());
                            }
                        }else {
                            linkGoodsShopAdapter.loadMoreFail();
                        }
                    }
                });
    }
}
