package cn.biggar.biggar.fragment.update;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.biggar.biggar.R;
import cn.biggar.biggar.adapter.update.DistriEarningAdapter;
import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.base.LazyFragment;
import cn.biggar.biggar.bean.BgResponse;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.bean.update.DistriEarningsBean;
import cn.biggar.biggar.http.JsonCallBack;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.view.WhiteLoadMoreView;

/**
 * Created by Chenwy on 2017/7/18.
 */

public class DistriEarningFragment extends LazyFragment {
    @BindView(R.id.rv_distri_earnings)
    RecyclerView rvDistriEarnings;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.btn_total_price)
    Button btnTotalPrice;

    private int position;

    private DistriEarningAdapter distriEarningAdapter;


    private String type;
    private String uid;
    private int curPage = 1;

    public static DistriEarningFragment newInstance(int position) {
        DistriEarningFragment distriEarningFragment = new DistriEarningFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        distriEarningFragment.setArguments(bundle);
        return distriEarningFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("position");
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.frg_distri_earning;
    }

    @Override
    protected void lazyLoadData(Bundle savedInstanceState) {
        showLoading();
        UserBean userBean = Preferences.getUserBean(getContext());
        if (userBean != null) {
            uid = userBean.getId();
        }

        distriEarningAdapter = new DistriEarningAdapter(uid, new ArrayList<DistriEarningsBean.DistriEarnings>());
        rvDistriEarnings.setLayoutManager(new LinearLayoutManager(getContext()));
        rvDistriEarnings.setAdapter(distriEarningAdapter);
        distriEarningAdapter.setEnableLoadMore(true);

        distriEarningAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                curPage += 1;
                requestData();
            }
        }, rvDistriEarnings);
        distriEarningAdapter.setEmptyView(R.layout.empty_view);
        distriEarningAdapter.setLoadMoreView(new WhiteLoadMoreView());


        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                curPage = 1;
                requestData();
            }
        });

        switch (position) {
            case 0:
                type = "1";
                break;
            case 1:
                type = "3";
                break;
            case 2:
                type = "7";
                break;
        }
        requestData();
    }

    private void requestData() {
        String url = BaseAPI.BASE_URL + "Brand/myEarnings";
        OkGo.<BgResponse<DistriEarningsBean>>get(url)
                .params("uid", uid)
                .params("type", type)
                .params("p", curPage)
                .params("pages", Constants.PAGE_SIZE)
                .execute(new JsonCallBack<BgResponse<DistriEarningsBean>>(JsonCallBack.TYPE_SUPER) {
                    @Override
                    public void onSuccess(Response<BgResponse<DistriEarningsBean>> response) {
                        dismissLoading();
                        BgResponse<DistriEarningsBean> distriEarningsBeanBgResponse = response.body();
                        refreshLayout.setRefreshing(false);
                        List<DistriEarningsBean.DistriEarnings> distriEarningses = distriEarningsBeanBgResponse.info.list;
                        if (curPage == 1) {
                            if (distriEarningses.size() <= 0){
                                setEmptyView(distriEarningAdapter.getEmptyView());
                            }
                            distriEarningAdapter.setNewData(distriEarningses);
                            if (distriEarningses.size() < Constants.PAGE_SIZE) {
                                distriEarningAdapter.loadMoreEnd(true);
                            } else {
                                distriEarningAdapter.loadMoreComplete();
                            }
                            String totalPrice = distriEarningsBeanBgResponse.info.total_price;
                            String totalMsg = "总收入：￥" + (TextUtils.isEmpty(totalPrice) ? "0" : totalPrice);
                            btnTotalPrice.setText(totalMsg);
                        } else {
                            distriEarningAdapter.addData(distriEarningses);
                            if (distriEarningses.size() < Constants.PAGE_SIZE) {
                                distriEarningAdapter.loadMoreEnd(true);
                            } else {
                                distriEarningAdapter.loadMoreComplete();
                            }
                        }
                    }

                    @Override
                    public void onError(Response<BgResponse<DistriEarningsBean>> response) {
                        super.onError(response);
                        dismissLoading();
                        refreshLayout.setRefreshing(false);
                        if (curPage == 1){
                            if (distriEarningAdapter.getData().size() > 0){
                                ToastUtils.showError("请求失败，请检查网络");
                            }else {
                                setErrorView(distriEarningAdapter.getEmptyView());
                            }
                        }else {
                            distriEarningAdapter.loadMoreFail();
                        }
                    }
                });
    }
}
