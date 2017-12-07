package cn.biggar.biggar.fragment.update;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.activity.ApplyForTalentActivity;
import cn.biggar.biggar.activity.LoginActivity;
import cn.biggar.biggar.activity.MainActivity;
import cn.biggar.biggar.activity.MyMessageActivity;
import cn.biggar.biggar.activity.SettingActivity;
import cn.biggar.biggar.activity.WebViewActivity;
import cn.biggar.biggar.activity.update.BrandListActivity;
import cn.biggar.biggar.activity.update.MyCardActivity;
import cn.biggar.biggar.activity.update.MyHomeActivity;
import cn.biggar.biggar.activity.update.MyLingqianActivity;
import cn.biggar.biggar.activity.update.MyOrderActivity;
import cn.biggar.biggar.activity.update.MyStoreActivity;
import cn.biggar.biggar.activity.update.MyTaskActivity;
import cn.biggar.biggar.activity.update.MyXingbiActivity;
import cn.biggar.biggar.activity.update.MyXingzhiActivity;
import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.api.BaseUrl;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.base.BiggarFragment;
import cn.biggar.biggar.bean.BgResponse;
import cn.biggar.biggar.bean.MessageBean;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.contract.MeContract;
import cn.biggar.biggar.event.LoginOutEvent;
import cn.biggar.biggar.event.ReceiverMsgEvent;
import cn.biggar.biggar.helper.RongManager;
import cn.biggar.biggar.http.JsonCallBack;
import cn.biggar.biggar.preference.AppPrefrences;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.presenter.update.MePresenter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by Chenwy on 2017/5/29 0029.
 */

public class MeFragment extends BiggarFragment<MePresenter> implements MeContract.View {
    public static final String TAG = MeFragment.class.getSimpleName();
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_id)
    TextView tvId;
    @BindView(R.id.tv_sign)
    TextView tvSign;
    @BindView(R.id.tv_xb_num)
    TextView tvXbNum;
    @BindView(R.id.tv_xz_num)
    TextView tvXzNum;
    @BindView(R.id.tv_lq_num)
    TextView tvLqNum;
    @BindView(R.id.sv_container)
    ScrollView sv;
    @BindView(R.id.iv_sex)
    ImageView ivSex;
    @BindView(R.id.tv_msg_num)
    TextView tvMsgNum;
    @BindView(R.id.tv_notice_num)
    TextView tvNoticeNum;
    @BindView(R.id.ll_task)
    LinearLayout llTask;
    @BindView(R.id.ll_mall)
    LinearLayout llMall;
    @BindView(R.id.iv_new_task)
    ImageView ivNewTask;

    private String mUserID = "";
    private UserBean mUserData;
    private String points = "0";
    private String jyz = "0";
    private String lq = "0";

    public static MeFragment getInstance() {
        MeFragment meFragment = new MeFragment();
        return meFragment;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.frg_me;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
    }


    /**
     * 是否认证
     *
     * @return
     */
    private boolean isCertification() {
        if (mUserData == null) return false;
        if (!TextUtils.isEmpty(mUserData.getE_VerWorker())) {
            if (mUserData.getE_VerWorker().equals("1")) {
                return true;
            }
        }
        return false;
    }

    @OnClick({R.id.ll_my_info, R.id.ll_xb, R.id.ll_xz, R.id.ll_lq, R.id.ll_order, R.id.ll_card, R.id.ll_weaf
            , R.id.ll_mall, R.id.ll_msg, R.id.ll_official, R.id.ll_setting, R.id.ll_chat, R.id.ll_store
            , R.id.ll_task})
    public void onViewClicked(View view) {

        //下面都需要  判断是否登录
        if (!AppPrefrences.getInstance(getActivity()).isLogined()) {
            startActivity(LoginActivity.startIntent(getContext()));
            return;
        }

        switch (view.getId()) {
            //个人信息
            case R.id.ll_my_info:
                startActivity(MyHomeActivity.startIntent(getContext(), mUserID));
                break;
            //星币
            case R.id.ll_xb:
                startActivity(new Intent(getActivity(), MyXingbiActivity.class).putExtra("xb", points == null ? "0" : points));
                break;
            //星值
            case R.id.ll_xz:
                startActivity(new Intent(getActivity(), MyXingzhiActivity.class).putExtra("xz", jyz == null ? "0" : jyz));
                break;
            //零钱
            case R.id.ll_lq:
                startActivity(new Intent(getActivity(), MyLingqianActivity.class).putExtra("lq", lq == null ? "0" : lq));
                break;
            //订单
            case R.id.ll_order:
                WebViewActivity.getInstance(getContext(), BaseUrl.GET_MY_ORDER_H5_URL + "?soure=myorder&version=" + Constants.WEB_VERSION);
//                startActivity(new Intent(getActivity(), MyOrderActivity.class));
                break;
            //卡包
            case R.id.ll_card:
                startActivity(new Intent(getActivity(), MyCardActivity.class));
                break;
            //比格乐园
            case R.id.ll_weaf:
                WebViewActivity.getInstance(getContext(), BaseUrl.GET_RIZ_ZOAWD_H5_URL + "?soure=eden&version=" + Constants.WEB_VERSION, false);
                break;
            //商城品牌
            case R.id.ll_mall:
                startActivity(new Intent(getActivity(), BrandListActivity.class));
                break;
            //消息
            case R.id.ll_msg:
                startActivity(new Intent(getActivity(), MyMessageActivity.class));
                break;
            //认证
            case R.id.ll_official:
                if (isCertification()) {//已经认证成功
                    startActivity(ApplyForTalentActivity.getInstance(getActivity(), ApplyForTalentActivity.MODE_AFT_3, mUserID));
                } else {//申请认证须知
                    startActivityForResult(ApplyForTalentActivity.getInstance(getActivity(), ApplyForTalentActivity.MODE_AFT_4, mUserID), 1);
                }
                break;
            //设置
            case R.id.ll_setting:
                startActivity(SettingActivity.startIntent(getActivity()));
                break;
            //聊天
            case R.id.ll_chat:
                Map<String, Boolean> datas = new HashMap<>();
                datas.put(Conversation.ConversationType.PRIVATE.getName(), false);
                RongManager.getInstance().startConversationList(datas, "1");
                break;
            //我的比格店
            case R.id.ll_store:
                startActivity(new Intent(getActivity(), MyStoreActivity.class));
                break;
            //我的任务
            case R.id.ll_task:
                startActivity(new Intent(getActivity(), MyTaskActivity.class));
                break;
        }
    }

    private void requestNoticeNum() {
        String url = BaseAPI.BASE_URL + "Mynews/sysMessage";
        OkGo.<BgResponse<List<MessageBean>>>post(url)
                .params("uid", mUserID)
                .execute(new JsonCallBack<BgResponse<List<MessageBean>>>(JsonCallBack.TYPE_ARRAY) {
                    @Override
                    public void onSuccess(Response<BgResponse<List<MessageBean>>> response) {
                        BgResponse<List<MessageBean>> listBgResponse = response.body();
                        List<MessageBean> messageBeanList = listBgResponse.info;
                        if (messageBeanList != null) {
                            int msgCount = 0;
                            for (int i = 0; i < messageBeanList.size(); i++) {
                                MessageBean messageBean = messageBeanList.get(i);
                                if (!TextUtils.isEmpty(messageBean.getE_Number())) {
                                    int num = Integer.parseInt(messageBean.getE_Number());
                                    if (num > 0) {
                                        msgCount += num;
                                    }
                                }
                            }

                            if (msgCount > 0) {
                                tvNoticeNum.setText(msgCount + "");
                                tvNoticeNum.setVisibility(View.VISIBLE);
                            } else {
                                Logger.e("没有新通知...");
                                tvNoticeNum.setText("");
                                tvNoticeNum.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == -2 && data != null) {
            //检测已认证
            String label = data.getStringExtra("label");
            mUserData.setE_VerWorker("1");
            mUserData.setE_Worker(label);
            Preferences.storeUserBean(getActivity(), mUserData);
        }
        if (requestCode == 1) {
            Logger.d("resultCode = " + resultCode);
        }
    }

    @Override
    public void showError(String errMsg) {

    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public boolean isLoadEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiverMsg(ReceiverMsgEvent receiverMsgEvent) {
        String curNumStr = tvMsgNum.getText().toString().trim();

        if (!TextUtils.isEmpty(curNumStr)) {
            int num = Integer.parseInt(curNumStr) + 1;
            if (num > 99) {
                tvMsgNum.setText("99");
            } else {
                tvMsgNum.setText(num + "");
            }
        } else {
            tvMsgNum.setText("1");
        }

        tvMsgNum.setVisibility(View.VISIBLE);
    }

    private void updateMsgNum() {
        RongIM.getInstance().getTotalUnreadCount(new RongIMClient.ResultCallback<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                if (integer > 0) {
                    tvMsgNum.setVisibility(View.VISIBLE);
                    if (integer > 99) {
                        tvMsgNum.setText("99");
                    } else {
                        tvMsgNum.setText(integer + "");
                    }
                } else {
                    tvMsgNum.setText("");
                    tvMsgNum.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    public void refresh() {
        if (!AppPrefrences.getInstance(getActivity()).isLogined()) {
            mUserID = "";
            tvName.setText("未登录");
            ivAvatar.setImageResource(R.mipmap.defaul_avatar_3x);
            tvId.setText("");
            tvSign.setText("登录比格用视频记录生活");
            tvXbNum.setText("0");
            tvXzNum.setText("0");
            tvLqNum.setText("0");

            //商城开关
            llMall.setVisibility(View.GONE);
            //任务开关
            llTask.setVisibility(View.GONE);
            //是否有任务
            ivNewTask.setVisibility(View.GONE);
        } else {
            mUserID = Preferences.getUserBean(getActivity()).getId();
            mUserData = Preferences.getUserBean(getContext());
            mPresenter.requestMeData(mUserID, "");
            requestNoticeNum();
            updateMsgNum();
        }
    }

    public void goToFirst() {
        sv.scrollTo(0, 0);
    }

    @Subscribe
    public void loginOut(LoginOutEvent loginOutEvent) {
        tvMsgNum.setText("");
        tvMsgNum.setVisibility(View.GONE);
        tvNoticeNum.setText("");
        tvNoticeNum.setVisibility(View.GONE);
        ((MainActivity) getActivity()).setMineHint(true);
    }

    @Override
    public void showMeData(UserBean userBean) {
        Preferences.storeUserBean(getActivity(), userBean);

        //头像
        if (!TextUtils.isEmpty(userBean.getE_HeadImg())) {
            RequestOptions options = new RequestOptions();
            options.centerCrop();
            Glide.with(getActivity()).asBitmap().load(userBean.getE_HeadImg()).apply(options).into(ivAvatar);
        }

        //id
        tvId.setText("@" + userBean.getId());

        //昵称
        tvName.setText(userBean.geteName());

        //签名
        if (!TextUtils.isEmpty(userBean.getE_Signature())) {
            tvSign.setText(userBean.getE_Signature());
        } else {
            tvSign.setText("Ta 好像忘记写签名了 ...");
        }

        //星币
        points = userBean.getE_Points();
        tvXbNum.setText(points == null ? "" : points);

        //星值
        jyz = userBean.getE_Experience();
        tvXzNum.setText(jyz == null ? "" : jyz);
        //零钱
        lq = userBean.getE_ComMoney();
        tvLqNum.setText(lq == null ? "" : lq);

        //性别
        String eSex = userBean.getE_Sex();
        if (!TextUtils.isEmpty(eSex)) {
            if (eSex.equals("男")) {
                ivSex.setVisibility(View.VISIBLE);
                ivSex.setImageResource(R.mipmap.hof_man);
            } else if (eSex.equals("女")) {
                ivSex.setVisibility(View.VISIBLE);
                ivSex.setImageResource(R.mipmap.hof_woman);
            } else {
                ivSex.setVisibility(View.GONE);
            }
        }

        //商城开关
        llMall.setVisibility(userBean.E_Shop != null && userBean.E_Shop.equals("1") ? View.VISIBLE : View.GONE);
        //任务开关
        llTask.setVisibility(userBean.E_Task != null && userBean.E_Task.equals("1") ? View.VISIBLE : View.GONE);
        //是否有任务
        ivNewTask.setVisibility(userBean.E_Read != null && userBean.E_Read.equals("1") ? View.VISIBLE : View.GONE);
    }
}
