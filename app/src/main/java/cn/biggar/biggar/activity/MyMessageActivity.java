package cn.biggar.biggar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.activity.update.MyMessageDetailActivity;
import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.bean.BgResponse;
import cn.biggar.biggar.bean.MessageBean;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.http.JsonCallBack;
import cn.biggar.biggar.preference.AppPrefrences;
import cn.biggar.biggar.preference.Preferences;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mx on 2016/11/2.
 * 我的消息
 */
public class MyMessageActivity extends BiggarActivity {

    @BindView(R.id.tv_gg_date)
    TextView tvGgDate;
    @BindView(R.id.tv_gg_content)
    TextView tvGgContent;
    @BindView(R.id.tv_tz_date)
    TextView tvTzDate;
    @BindView(R.id.tv_tz_content)
    TextView tvTzContent;
    @BindView(R.id.tv_gh_num)
    TextView tvGhNum;
    @BindView(R.id.tv_gh_date)
    TextView tvGhDate;
    @BindView(R.id.tv_gg_num)
    TextView tvGgNum;
    @BindView(R.id.tv_tz_num)
    TextView tvTzNum;
    @BindView(R.id.tv_gh_content)
    TextView tvGhContent;
    private String mUserID;

    private void getUserInfo() {
        if (AppPrefrences.getInstance(getActivity()).isLogined()) {
            UserBean userBean = Preferences.getUserBean(getApplication());
            mUserID = userBean.getId();
        } else {
            mUserID = null;
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_my_message;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
        queryNewMessage();
    }

    /**
     * 获取最新消息
     */
    private void queryNewMessage() {
        String url = BaseAPI.BASE_URL + "Mynews/sysMessage";
        OkGo.<BgResponse<List<MessageBean>>>post(url)
                .params("uid", mUserID)
                .execute(new JsonCallBack<BgResponse<List<MessageBean>>>(JsonCallBack.TYPE_ARRAY) {
                    @Override
                    public void onSuccess(Response<BgResponse<List<MessageBean>>> response) {
                        BgResponse<List<MessageBean>> listBgResponse = response.body();
                        List<MessageBean> messageBeanList = listBgResponse.info;
                        if (messageBeanList != null) {
                            for (int i = 0; i < messageBeanList.size(); i++) {
                                MessageBean messageBean = messageBeanList.get(i);
                                //系统通知
                                if (i == 0) {
                                    tvTzDate.setText(messageBean.getE_CreateDate());
                                    if (!TextUtils.isEmpty(messageBean.getE_Number())) {
                                        int num = Integer.parseInt(messageBean.getE_Number());
                                        tvTzNum.setVisibility(num > 0 ? View.VISIBLE : View.GONE);
                                        tvTzNum.setText(messageBean.getE_Number());
                                    }

                                    String relaType = messageBean.getE_RelaType();
                                    if (relaType.equals("15")) {
                                        String agentState = messageBean.getE_AgentState();
                                        //0就是等待同意或拒绝
                                        if (agentState.equals("0")) {
                                            String msg = messageBean.getE_Message() + "邀请你成为Ta的代理商，是否同意？";
                                            tvTzContent.setText(msg);
                                        }
                                        //1就是同意了对方的代理
                                        else if (agentState.equals("1")) {
                                            String msg = "你同意了" + messageBean.getE_Message() + "的代理邀请";
                                            tvTzContent.setText(msg);
                                        }
                                        //2就是拒绝
                                        else if (agentState.equals("2")) {
                                            String msg = "你拒绝了" + messageBean.getE_Message() + "的代理邀请";
                                            tvTzContent.setText(msg);
                                        }
                                        //3就是对方同意了我给他的代理
                                        else if (agentState.equals("3")) {
                                            String msg = messageBean.getE_Message() + "同意了你的代理邀请";
                                            tvTzContent.setText(msg);
                                        }
                                        //4就是拒绝我给他的代理
                                        else if (agentState.equals("4")) {
                                            String msg = messageBean.getE_Message() + "拒绝了你的代理邀请";
                                            tvTzContent.setText(msg);
                                        }
                                    }else {
                                        tvTzContent.setText(messageBean.getE_Message());
                                    }
                                }
                                //官方公告
                                else if (i == 1) {
                                    tvGgDate.setText(messageBean.getE_CreateDate());
                                    tvGgContent.setText(messageBean.getE_Message());
                                    if (!TextUtils.isEmpty(messageBean.getE_Number())) {
                                        int num = Integer.parseInt(messageBean.getE_Number());
                                        tvGgNum.setVisibility(num > 0 ? View.VISIBLE : View.GONE);
                                        tvGgNum.setText(messageBean.getE_Number());
                                    }
                                } else if (i == 2) {
                                    tvGhDate.setText(messageBean.getE_CreateDate());
                                    tvGhContent.setText(messageBean.getE_Message());
                                    if (!TextUtils.isEmpty(messageBean.getE_Number())) {
                                        int num = Integer.parseInt(messageBean.getE_Number());
                                        tvGhNum.setVisibility(num > 0 ? View.VISIBLE : View.GONE);
                                        tvGhNum.setText(messageBean.getE_Number());
                                    }
                                }
                            }
                        }
                    }
                });
    }

    @OnClick({R.id.ll_gg, R.id.ll_tz, R.id.ll_gh})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_gg:
                intent = new Intent();
                intent.setClass(this, MyMessageDetailActivity.class);
                intent.putExtra("tid", "2");
                intent.putExtra("title", "官方公告");
                startActivity(intent);
                break;
            case R.id.ll_tz:
                intent = new Intent();
                intent.setClass(this, MyMessageDetailActivity.class);
                intent.putExtra("tid", "1");
                intent.putExtra("title", "系统通知");
                startActivity(intent);
                break;
            case R.id.ll_gh:
                intent = new Intent();
                intent.setClass(this, MyMessageDetailActivity.class);
                intent.putExtra("tid", "3");
                intent.putExtra("title", "公会信息");
                startActivity(intent);
                break;
        }
    }
}