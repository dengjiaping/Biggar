package cn.biggar.biggar.fragment.update;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.biggar.biggar.R;
import cn.biggar.biggar.activity.update.DistriEarningsActivity;
import cn.biggar.biggar.adapter.update.DistriManagerAdapter;
import cn.biggar.biggar.base.LazyFragment;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.bean.update.DistriManagerBean;
import cn.biggar.biggar.contract.DistributionManagerContract;
import cn.biggar.biggar.preference.DistributionManagerPresenter;
import cn.biggar.biggar.preference.Preferences;

/**
 * Created by Chenwy on 2017/7/17.
 * 分销管理
 */

public class DistributionManagerFragment extends LazyFragment<DistributionManagerPresenter> implements DistributionManagerContract.View {
    @BindView(R.id.rv_distri_manager)
    RecyclerView rvLinkGoodsShop;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private DistriManagerAdapter distriManagerAdapter;
    private String uid;
    private List<DistriManagerBean> distriManagerBeens;

    public static DistributionManagerFragment newInstance() {
        return new DistributionManagerFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.frg_distribution_manager;
    }

    @Override
    protected void lazyLoadData(Bundle savedInstanceState) {
        distriManagerBeens = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            DistriManagerBean distriManagerBean = new DistriManagerBean();
            distriManagerBean.E_Num = "0";
            distriManagerBean.E_Money = "0";
            distriManagerBeens.add(distriManagerBean);
        }

        UserBean userBean = Preferences.getUserBean(getContext());
        if (userBean != null) {
            uid = userBean.getId();
        }

        distriManagerAdapter = new DistriManagerAdapter(distriManagerBeens);
        rvLinkGoodsShop.setLayoutManager(new LinearLayoutManager(getContext()));
        rvLinkGoodsShop.setAdapter(distriManagerAdapter);
        distriManagerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(getActivity(), DistriEarningsActivity.class));
                        break;
                }
            }
        });

        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData();
            }
        });

        requestData();
    }

    private void requestData() {
        mPresenter.requestData(uid);
    }

    @Override
    public void showError(String errMsg) {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void showDatas(DistriManagerBean distriManagerBean) {
        refreshLayout.setRefreshing(false);
        for (int i = 0; i < distriManagerBeens.size(); i++) {
            DistriManagerBean dmb = distriManagerBeens.get(i);
            dmb.E_Money = distriManagerBean.E_Money;
            dmb.E_Num = distriManagerBean.E_Num;
        }
        distriManagerAdapter.setNewData(distriManagerBeens);
    }
}
