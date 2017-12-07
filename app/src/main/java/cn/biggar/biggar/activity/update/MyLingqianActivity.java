package cn.biggar.biggar.activity.update;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.biggar.biggar.R;
import cn.biggar.biggar.adapter.update.MoneyAdapter;
import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.bean.BgResponse;
import cn.biggar.biggar.bean.update.CashTipsBean;
import cn.biggar.biggar.bean.update.MoneyBean;
import cn.biggar.biggar.dialog.CashTipsDialog;
import cn.biggar.biggar.http.JsonCallBack;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.utils.Utils;

/**
 * Created by Chenwy on 2017/5/31.
 */

public class MyLingqianActivity extends BiggarActivity {
    @BindView(R.id.rv_lq)
    RecyclerView rvLq;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.rl_cash)
    RelativeLayout rlCash;
    @BindView(R.id.ll_detail_tip)
    LinearLayout llDetailTips;
    private MoneyAdapter moneyAdapter;
    private String lq;
    private String mUserId;
    private int curpage = 1;

    private String mContent;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_my_lingqian;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        rlCash.setPadding(0, Utils.getStatusBarHeight(this), 0, 0);
        mUserId = Preferences.getUserBean(this).getId();
        lq = getIntent().getExtras().getString("lq");
        if (!TextUtils.isEmpty(lq)) {
            tvMoney.setText(lq);
        }
        moneyAdapter = new MoneyAdapter(new ArrayList<MoneyBean.Detail>(), mUserId);
        rvLq.setLayoutManager(new LinearLayoutManager(this));
        rvLq.setAdapter(moneyAdapter);
        moneyAdapter.setEnableLoadMore(true);
        moneyAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                curpage += 1;
                updateData();
            }
        }, rvLq);

        updateData();
    }

    @Override
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarColor(R.color.transparent)
                .statusBarDarkFont(false)
                .flymeOSStatusBarFontColor(R.color.whites)
                .init();
    }

    private void updateData() {
        String url = BaseAPI.BASE_URL + "User/userIncome?p=" + curpage + "&pages=" + Constants.PAGE_SIZE;


        OkGo.<BgResponse<MoneyBean>>post(url)
                //test: 12551
                .params("uid", mUserId)
                .execute(new JsonCallBack<BgResponse<MoneyBean>>(JsonCallBack.TYPE_SUPER) {
                    @Override
                    public void onSuccess(Response<BgResponse<MoneyBean>> response) {
                        BgResponse<MoneyBean> moneyBeanBgResponse = response.body();
                        List<MoneyBean.Detail> details = moneyBeanBgResponse.info.detail;
                        if (curpage == 1) {
                            mContent = moneyBeanBgResponse.info.content;
                            llDetailTips.setVisibility(details.size() > 0 ? View.VISIBLE : View.GONE);
                            moneyAdapter.setNewData(details);
                            moneyAdapter.disableLoadMoreIfNotFullPage();
                            if (details.size() < Constants.PAGE_SIZE) {
                                moneyAdapter.loadMoreEnd(true);
                            }
                        } else {
                            if (details.size() < Constants.PAGE_SIZE) {
                                moneyAdapter.loadMoreEnd(true);
                            } else {
                                moneyAdapter.loadMoreComplete();
                            }
                        }
                    }

                    @Override
                    public void onError(Response<BgResponse<MoneyBean>> response) {
                        super.onError(response);
                        ToastUtils.showError("加载失败");
                        moneyAdapter.loadMoreEnd(true);
                    }
                });
    }

    /**
     * 提现
     */
    private void requestCrash() {
//        String url = BaseAPI.BASE_URL + "User/userReflect";
//        OkGo.<String>get(url)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(Response<String> response) {
//                        dismissLoading();
//                        CashTipsBean cashTipsBean = new Gson().fromJson(response.body(), CashTipsBean.class);
//                        CashTipsDialog.newInstance(cashTipsBean.content.replace("\\n","\n"))
//                                .setMargin(52).show(getSupportFragmentManager());
//                    }
//
//                    @Override
//                    public void onError(Response<String> response) {
//                        super.onError(response);
//                        dismissLoading();
//                        ToastUtils.showError("提现失败", getApplicationContext());
//                    }
//                });

        if (TextUtils.isEmpty(mContent)) {
            return;
        }

        mContent = mContent.replace("\\n", "\n");
        CashTipsDialog.newInstance(mContent)
                .setMargin(52).show(getSupportFragmentManager());
    }

    @OnClick({R.id.iv_back, R.id.ll_submit_money})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_submit_money:
                requestCrash();
                break;
        }
    }
}
