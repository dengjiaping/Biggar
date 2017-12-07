package cn.biggar.biggar.activity.update;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.biggar.biggar.R;
import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.base.BasePresenter;
import cn.biggar.biggar.base.BiggarListActivity;
import cn.biggar.biggar.bean.BgResponse;
import cn.biggar.biggar.bean.MessageBean;
import cn.biggar.biggar.http.JsonCallBack;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.utils.DensityUtil;
import cn.biggar.biggar.utils.ToastUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.List;

import cn.biggar.biggar.utils.Utils;
import cn.biggar.biggar.view.GrayLoadMoreView;
import cn.biggar.biggar.view.RootLayout;

/**
 * Created by Chenwy on 2017/6/22.
 */

public class MyMessageDetailActivity extends BiggarListActivity<BasePresenter, MessageBean> {
    private String tid;
    private String uid;

    @Override
    protected void initDataBefore() {
        String title = getIntent().getStringExtra("title");
        tid = getIntent().getStringExtra("tid");
        uid = Preferences.getUserBean(this).getId();
        RootLayout.getInstance(this).setTitle(title);
    }

    @Override
    protected void initDataAfter() {
        setListBackGroundColor(R.color.app_e5e5e5);
    }

    @Override
    public LoadMoreView getLoadView() {
        return new GrayLoadMoreView();
    }

    @Override
    protected void refreshData(final boolean isLoadMore) {
        String url = BaseAPI.BASE_URL + "Mynews/userMessage";
        OkGo.<BgResponse<List<MessageBean>>>get(url)
                .params("tid", tid)
                .params("uid", uid)
                .params("p", curPage)
                .params("pages", Constants.PAGE_SIZE)
                .execute(new JsonCallBack<BgResponse<List<MessageBean>>>(JsonCallBack.TYPE_ARRAY) {

                    @Override
                    public void onSuccess(Response<BgResponse<List<MessageBean>>> response) {
                        BgResponse<List<MessageBean>> listBgResponse = response.body();
                        if (isLoadMore) {
                            finishLoadMore(listBgResponse.info);
                        } else {
                            finishRefresh(listBgResponse.info);
                        }
                    }

                    @Override
                    public void onError(Response<BgResponse<List<MessageBean>>> response) {
                        super.onError(response);
                        handleError();
                    }
                });
    }

    @Override
    protected int itemLayout() {
        return R.layout.item_message_detail;
    }

    @Override
    protected void myHolder(Context context, BaseViewHolder helper, MessageBean item) {

        if (helper.getLayoutPosition() == 0) {
            helper.getView(R.id.fl_date).setPadding(0, DensityUtil.dp2px(getApplicationContext(), 28), 0, 0);
        } else {
            helper.getView(R.id.fl_date).setPadding(0, 0, 0, 0);
        }

        helper.setText(R.id.tv_date, item.getE_CreateDate())
                .addOnClickListener(R.id.tv_agree)
                .addOnClickListener(R.id.tv_refuse);

        if (!TextUtils.isEmpty(tid)) {
            if (tid.equals("1")) {
                helper.setImageResource(R.id.iv_logo, R.mipmap.system_news_3x);
            } else if (tid.equals("2")) {
                helper.setImageResource(R.id.iv_logo, R.mipmap.official_news_3x);
            }
//            else if (tid.equals("3")) {
//                helper.setImageResource(R.id.iv_logo, R.mipmap.forum_news_3x);
//            }
        }


        LinearLayout llContent = helper.getView(R.id.ll_content);
        if ((item.getE_RelaType().equals("15") && item.getE_AgentState().equals("0")) || item.getE_RelaType().equals("5")) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) llContent.getLayoutParams();
            params.width = LinearLayout.LayoutParams.MATCH_PARENT;
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            params.rightMargin = Utils.dip2px(getApplicationContext(), 20);
            llContent.setLayoutParams(params);
        } else {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) llContent.getLayoutParams();
            params.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            params.rightMargin = Utils.dip2px(getApplicationContext(), 20);
            llContent.setLayoutParams(params);
        }

        helper.setGone(R.id.ll_apply, item.getE_RelaType().equals("15") && item.getE_AgentState().equals("0"));

        //代理邀请
        if (item.getE_RelaType().equals("15")) {
            helper.setGone(R.id.iv_goods, false);
            String agentState = item.getE_AgentState();

            //0就是等待同意或拒绝
            if (agentState.equals("0")) {
                String name = item.getE_Message();
                String a = "邀请你成为Ta的代理商，是否同意？";
                SpannableString spannableString = new SpannableString(name + a);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                spannableString.setSpan(colorSpan, 0, name.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                helper.setText(R.id.tv_content, spannableString);
            }
            //1就是同意了对方的代理
            else if (agentState.equals("1")) {
                String a = "你同意了";
                String name = item.getE_Message();
                String b = "的代理邀请";
                SpannableString spannableString = new SpannableString(a + name + b);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                spannableString.setSpan(colorSpan, a.length(), a.length() + name.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                helper.setText(R.id.tv_content, spannableString);
            }
            //2就是拒绝
            else if (agentState.equals("2")) {
                String a = "你拒绝了";
                String name = item.getE_Message();
                String b = "的代理邀请";
                SpannableString spannableString = new SpannableString(a + name + b);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                spannableString.setSpan(colorSpan, a.length(), a.length() + name.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                helper.setText(R.id.tv_content, spannableString);
            }
            //3就是对方同意了我给他的代理
            else if (agentState.equals("3")) {
                String name = item.getE_Message();
                String a = "同意了你的代理邀请";
                SpannableString spannableString = new SpannableString(name + a);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                spannableString.setSpan(colorSpan, 0, name.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                helper.setText(R.id.tv_content, spannableString);
            }
            //4就是拒绝我给他的代理
            else if (agentState.equals("4")) {
                String name = item.getE_Message();
                String a = "拒绝了你的代理邀请";
                SpannableString spannableString = new SpannableString(name + a);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                spannableString.setSpan(colorSpan, 0, name.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                helper.setText(R.id.tv_content, spannableString);
            }
        }
        //收到礼物
        else if (item.getE_RelaType().equals("5")) {
            if (Integer.parseInt(item.getE_ObjID()) > 0) {
                helper.setGone(R.id.iv_goods, true);
                Glide.with(this).load(Utils.getRealUrl(item.getE_VideoImg()))
                        .apply(new RequestOptions().centerCrop())
                        .into((ImageView) helper.getView(R.id.iv_goods));
            } else {
                helper.setGone(R.id.iv_goods, false);
            }
            helper.setText(R.id.tv_content, item.getE_Message());
        } else {
            helper.setGone(R.id.iv_goods, false);
            helper.setText(R.id.tv_content, item.getE_Message());
        }
    }

    @Override
    protected void onListItemClick(MessageBean data, int position) {
        if (data.getE_RelaType().equals("5") && Integer.parseInt(data.getE_ObjID()) > 0) {
            startActivity(ContentDetailActivity.startIntent(MyMessageDetailActivity.this, data.getE_ObjID(), data.getE_TypeVal()));
        }
    }

    @Override
    protected void onListItemChildClick(int viewId, MessageBean data, int position) {
        switch (viewId) {
            case R.id.tv_agree:
                messageRead(position, data, "1");
                break;
            case R.id.tv_refuse:
                messageRead(position, data, "0");
                break;
        }
    }

    private void messageRead(final int position, final MessageBean data, final String state) {
        String url = BaseAPI.BASE_URL + "Mynews/messageRead";
        OkGo.<String>post(url)
                .params("obj_id", data.getE_RelaID())
                .params("uid", uid)
                .params("cuid", data.getE_ToMemberID())
                .params("state", state)
                .execute(new StringCallback() {

                    @Override
                    public void onSuccess(Response<String> response) {
                        String s = response.body();
                        BgResponse bgResponse = new Gson().fromJson(s, BgResponse.class);
                        if (bgResponse.status == 1) {
                            if (state.equals("1")) {
                                data.setE_AgentState("1");
                            } else {
                                data.setE_AgentState("2");
                            }
                            notifyItemChanged(position);
                        } else {
                            ToastUtils.showError((String) bgResponse.info);
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ToastUtils.showError("请求失败");
                    }
                });
    }
}
