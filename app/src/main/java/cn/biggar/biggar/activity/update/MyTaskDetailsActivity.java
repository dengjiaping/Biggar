package cn.biggar.biggar.activity.update;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import cn.biggar.biggar.R;
import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.bean.BgResponse;
import cn.biggar.biggar.bean.update.MyTaskBean;
import cn.biggar.biggar.event.GetTaskEvent;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.utils.Utils;

/**
 * Created by Chenwy on 2017/8/3.
 */

public class MyTaskDetailsActivity extends BiggarActivity {
    @BindView(R.id.btn_state)
    Button btnState;
    @BindView(R.id.iv_brand_logo)
    ImageView ivLogo;
    @BindView(R.id.iv_bg)
    ImageView ivBg;
    @BindView(R.id.tv_brand_name)
    TextView tvBrandName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_task_title)
    TextView tvTaskTitle;
    @BindView(R.id.tv_use_money_progress)
    TextView tvUseMoneyProgress;
    @BindView(R.id.tv_one_money)
    TextView tvOneMoney;
    @BindView(R.id.tv_add_counts)
    TextView tvAddCounts;
    @BindView(R.id.tv_task_content)
    TextView tvTaskContent;
    @BindView(R.id.pb_task)
    ContentLoadingProgressBar pbTask;

    private int state;
    private MyTaskBean myTaskBean;
    private String uid;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_my_task_details;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        state = getIntent().getIntExtra("state", 0);
        myTaskBean = (MyTaskBean) getIntent().getSerializableExtra("taskBean");
        uid = Preferences.getUserBean(this).getId();
        switch (state) {
            case 1:
                btnState.setText("领取任务");
                btnState.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
                break;
            case 2:
                btnState.setText("收入：￥" + myTaskBean.E_GetMoney);
                btnState.setBackgroundColor(ContextCompat.getColor(this, R.color.app_1a83d6));
                break;
            case 3:
                btnState.setText("已完结（总收入：￥" + myTaskBean.E_GetMoney + "）");
                btnState.setBackgroundColor(ContextCompat.getColor(this, R.color.app_9));
                break;
        }

        //品牌名称
        tvBrandName.setText(myTaskBean.E_BrandCnName);

        //品牌logo
        Glide.with(this).load(Utils.getRealUrl(myTaskBean.E_Logo)).apply(new RequestOptions().centerCrop()).into(ivLogo);

        //背景
        Glide.with(this).load(Utils.getRealUrl(myTaskBean.E_Img3)).apply(new RequestOptions().centerCrop()).into(ivBg);

        //有效期
        tvTime.setText(myTaskBean.E_StartTime + " - " + myTaskBean.E_OverTime);

        String sumMoney = myTaskBean.E_SumMoney;
        String oneMoney = myTaskBean.E_OneMoney;
        String ePrice = myTaskBean.E_Price;

        SpannableString spannableString = new SpannableString(ePrice + "/" + sumMoney);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        spannableString.setSpan(colorSpan, 0, ePrice.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        tvUseMoneyProgress.setText(spannableString);

        pbTask.setMax(Integer.parseInt(TextUtils.isEmpty(sumMoney) ? "0" : sumMoney));
        pbTask.setProgress(Integer.parseInt(TextUtils.isEmpty(ePrice) ? "0" : ePrice));

        tvTaskContent.setText(myTaskBean.E_Content);

        tvTaskTitle.setText(myTaskBean.E_Name);

        tvOneMoney.setText("每卖出一件商品可获得" + oneMoney + "元");

        tvAddCounts.setText("发布内容的浏览量达到" + myTaskBean.E_AddCounts + "后可获得" + myTaskBean.E_AddMoney + "元（仅限前100条）");
    }


    /**
     * 领取任务
     */
    private void getTask() {
        String url = BaseAPI.BASE_URL + "Activity/receiveTask";
        OkGo.<String>post(url)
                .params("uid", uid)
                .params("task_id", myTaskBean.E_TaskID)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        dismissLoading();
                        String s = response.body();
                        BgResponse bgResponse = new Gson().fromJson(s, BgResponse.class);
                        if (bgResponse.status == 1) {
                            state = 2;
                            btnState.setText("收入：￥" + myTaskBean.E_GetMoney);
                            btnState.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.app_1a83d6));
                            ToastUtils.showNormal("领取成功");
                            EventBus.getDefault().post(new GetTaskEvent(false));
                        } else {
                            ToastUtils.showError((String) bgResponse.info);
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        dismissLoading();
                        ToastUtils.showError("请求失败");
                    }
                });
    }

    @OnClick(R.id.btn_state)
    public void onViewClicked() {
        if (state == 1) {
            showLoading();
            getTask();
        }
    }
}
