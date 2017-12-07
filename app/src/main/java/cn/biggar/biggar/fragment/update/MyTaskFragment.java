package cn.biggar.biggar.fragment.update;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;
import android.widget.TextView;

import com.allen.library.SuperButton;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import org.greenrobot.eventbus.EventBus;
import java.util.List;
import cn.biggar.biggar.R;
import cn.biggar.biggar.activity.update.MyTaskDetailsActivity;
import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.base.BasePresenter;
import cn.biggar.biggar.base.BiggarListFragment;
import cn.biggar.biggar.bean.BgResponse;
import cn.biggar.biggar.bean.update.MyTaskBean;
import cn.biggar.biggar.event.GetTaskEvent;
import cn.biggar.biggar.http.JsonCallBack;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.utils.Utils;

/**
 * Created by Chenwy on 2017/8/3.
 */

public class MyTaskFragment extends BiggarListFragment<BasePresenter,MyTaskBean> {
    private int position;
    private String uid;
    private int clickPosition = -1;
    private boolean isShowed;

    public static MyTaskFragment newInstance(int position) {
        MyTaskFragment myTaskFragment = new MyTaskFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        myTaskFragment.setArguments(bundle);
        return myTaskFragment;
    }

    @Override
    protected void initDataBefore() {
        position = getArguments().getInt("position");
        uid = Preferences.getUserBean(getContext()).getId();
        isShowed = true;
    }

    @Override
    protected void initAfter() {

    }

    @Override
    public void refreshData(final boolean isLoadMore) {
        int status = position + 1;
        String url = BaseAPI.BASE_URL + "Activity/myTask";
        OkGo.<BgResponse<List<MyTaskBean>>>get(url)
                .params("uid", uid)
                .params("status", status)
                .params("p", curPage)
                .params("pages", Constants.PAGE_SIZE)
                .execute(new JsonCallBack<BgResponse<List<MyTaskBean>>>(JsonCallBack.TYPE_SUPER) {
                    @Override
                    public void onSuccess(Response<BgResponse<List<MyTaskBean>>> response) {
                        BgResponse<List<MyTaskBean>> listBgResponse = response.body();
                        List<MyTaskBean> myTaskBeens = listBgResponse.info;
                        if (isLoadMore) {
                            finishLoadMore(myTaskBeens);
                        } else {
                            finishRefresh(myTaskBeens);
                        }
                    }

                    @Override
                    public void onError(Response<BgResponse<List<MyTaskBean>>> response) {
                        super.onError(response);
                        handleError("请求失败，请检查网络");
                    }
                });
    }

    public void refreshToFirst() {
        if (isShowed){
            curPage = 1;
            refreshData(false);
        }
    }

    @Override
    public int itemLayout() {
        return R.layout.item_my_task;
    }

    @Override
    protected void myHolder(BaseViewHolder baseViewHolder, MyTaskBean myTaskBean) {
        SuperButton tvTaskState = baseViewHolder.getView(R.id.tv_task_state);
        switch (position) {
            case 0:
                tvTaskState.setText("领取任务");
                tvTaskState.setShapeSolidColor(ContextCompat.getColor(mContext,R.color.colorPrimary)).setUseShape();
                break;
            case 1:
                tvTaskState.setText("进行中");
                tvTaskState.setShapeSolidColor(ContextCompat.getColor(mContext,R.color.app_1a83d6)).setUseShape();
                break;
            case 2:
                tvTaskState.setText("已完结");
                tvTaskState.setShapeSolidColor(ContextCompat.getColor(mContext,R.color.app_9)).setUseShape();
                break;
        }

        Glide.with(mContext).load(Utils.getRealUrl(myTaskBean.E_Logo))
                .apply(new RequestOptions().centerCrop())
                .into((ImageView) baseViewHolder.getView(R.id.iv_logo));

        String startTime = myTaskBean.E_StartTime;
        String endTime = myTaskBean.E_OverTime;
        baseViewHolder.setText(R.id.tv_shop_name, myTaskBean.E_BrandCnName)
                .setText(R.id.tv_time, startTime + " - " + endTime);


        TextView tvMoney = baseViewHolder.getView(R.id.tv_use_money_progress);
        //总酬劳
        String sumMoney = myTaskBean.E_SumMoney;

        String ePrice = myTaskBean.E_Price;

        SpannableString spannableString = new SpannableString(ePrice + "/" + sumMoney);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        spannableString.setSpan(colorSpan, 0, ePrice.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        tvMoney.setText(spannableString);

        ContentLoadingProgressBar pb = baseViewHolder.getView(R.id.pb_task);
        pb.setMax(Integer.parseInt(TextUtils.isEmpty(sumMoney)?"0":sumMoney));
        pb.setProgress(Integer.parseInt(TextUtils.isEmpty(ePrice)?"0":ePrice));

        baseViewHolder.setText(R.id.tv_title, myTaskBean.E_Name);

        baseViewHolder.addOnClickListener(R.id.tv_task_state);
    }

    @Override
    protected void onListItemClick(MyTaskBean data, int position) {
        clickPosition = position;
        int state = this.position + 1;
        startActivity(new Intent(getActivity(), MyTaskDetailsActivity.class).putExtra("state", state).putExtra("taskBean", data));
    }

    @Override
    protected void onListItemChildClick(int viewId, MyTaskBean data, int position) {
        switch (viewId) {
            case R.id.tv_task_state:
                if (this.position == 0){
                    showLoading();
                    getTask(data.E_TaskID, position);
                }
                break;
        }
    }

    public void removeItem() {
        if (clickPosition > -1) {
            remove(clickPosition);
        }
    }

    /**
     * 领取任务
     */
    private void getTask(String taskId, final int position) {
        String url = BaseAPI.BASE_URL + "Activity/receiveTask";
        OkGo.<String>post(url)
                .params("uid", uid)
                .params("task_id", taskId)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        dismissLoading();
                        String s = response.body();
                        BgResponse bgResponse = new Gson().fromJson(s, BgResponse.class);
                        if (bgResponse.status == 1) {
                            EventBus.getDefault().post(new GetTaskEvent(true));
                            remove(position);
                            ToastUtils.showNormal("领取成功");
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
}
