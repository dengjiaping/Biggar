package cn.biggar.biggar.activity.update;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.biggar.biggar.R;
import cn.biggar.biggar.adapter.update.DistriEarningAdapter;
import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.bean.BgResponse;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.bean.update.DistriEarningsBean;
import cn.biggar.biggar.dialog.DialogDate;
import cn.biggar.biggar.http.JsonCallBack;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.view.WhiteLoadMoreView;
import cn.biggar.biggar.view.RootLayout;

/**
 * Created by Chenwy on 2017/7/20.
 */

public class DistriEarningsDateActivity extends BiggarActivity implements DialogDate.OnDateChooseListener {
    @BindView(R.id.rv_distri_earnings)
    RecyclerView rvDistriEarnings;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.btn_total_price)
    Button btnTotalPrice;

    private String startDate;
    private String endDate;

    private DialogDate dialogDate;
    private DistriEarningAdapter distriEarningAdapter;
    private String uid;
    private int curPage = 1;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_distri_date_earning;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        showLoading();
        startDate = getIntent().getStringExtra("startDate");
        endDate = getIntent().getStringExtra("endDate");
        dialogDate = new DialogDate(getActivity(), this);

        tvDate.setText(startDate.replace("-", ".") + "-" + endDate.replace("-", "."));

        UserBean userBean = Preferences.getUserBean(this);
        if (userBean != null) {
            uid = userBean.getId();
        }

        RootLayout.getInstance(this).setOnRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDate.showPopupWindow();
            }
        });

        distriEarningAdapter = new DistriEarningAdapter(uid, new ArrayList<DistriEarningsBean.DistriEarnings>());
        rvDistriEarnings.setLayoutManager(new LinearLayoutManager(this));
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

        requestData();
    }

    private void requestData() {
        String url = BaseAPI.BASE_URL + "Brand/myEarnings";
        OkGo.<BgResponse<DistriEarningsBean>>get(url)
                .params("uid", uid)
                .params("fromtime", startDate)
                .params("totime", endDate)
                .params("p", curPage)
                .params("pages", Constants.PAGE_SIZE)
                .execute(new JsonCallBack<BgResponse<DistriEarningsBean>>(JsonCallBack.TYPE_SUPER) {
                    @Override
                    public void onSuccess(Response<BgResponse<DistriEarningsBean>> response) {
                        dismissLoading();
                        BgResponse<DistriEarningsBean> distriEarningsBeanBgResponse = response.body();
                        List<DistriEarningsBean.DistriEarnings> distriEarningses = distriEarningsBeanBgResponse.info.list;
                        refreshLayout.setRefreshing(false);
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

    @OnClick({R.id.iv_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finish();
                break;
        }
    }

    @Override
    public void onDateChoose(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        tvDate.setText(startDate.replace("-", ".") + "-" + endDate.replace("-", "."));
        curPage = 1;
        requestData();
    }
}
