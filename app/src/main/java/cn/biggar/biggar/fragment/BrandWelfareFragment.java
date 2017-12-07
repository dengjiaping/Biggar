package cn.biggar.biggar.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ScrollView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.activity.LoginActivity;
import cn.biggar.biggar.activity.WebViewActivity;
import cn.biggar.biggar.activity.update.MyCardActivity;
import cn.biggar.biggar.api.BaseUrl;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.bean.CardBean;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.dialog.DrawRedPacketStateDialog;
import cn.biggar.biggar.dialog.WelfareDialog;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.presenter.CommonPresenter;
import cn.biggar.biggar.presenter.UserDetailsPresenter;
import cn.biggar.biggar.utils.ToastUtils;
import butterknife.BindView;
import butterknife.OnClick;
import per.sue.gear2.presenter.OnObjectListener;


/**
 * Created by mx on 2016/12/22.
 * 品牌 福利
 */

public class BrandWelfareFragment extends HeaderViewPagerFragment {
    @BindView(R.id.brand_welfare_sv)
    ScrollView mScrollView;
    private static String LOOK_WALLET = "查看钱包";
    private static String LOOK_CARD_BAG = "查看卡包";

    private UserBean userBean;
    private String mBrandId;
    private WelfareDialog dialog;
    private UserDetailsPresenter mUserPresenter;

    public static BrandWelfareFragment getInstance(String id) {
        BrandWelfareFragment fragment = new BrandWelfareFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_brand_welfare;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        mBrandId = getArguments().getString("id");
        mUserPresenter = new UserDetailsPresenter(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        userBean = Preferences.getUserBean(getActivity());
    }

    @OnClick({R.id.brand_welfare_red_packet_iv})
    public void OnClick(View view) {
        if (userBean == null) {
            startActivity(LoginActivity.startIntent(getActivity()));
            return;
        }
        queryWelfare();
    }

    @Override
    public View getScrollableView() {
        return mScrollView;
    }

    /**
     * 查询 福利
     */
    private void queryWelfare() {
        showLoading();
        mUserPresenter.getBrandCard("brand", mBrandId, userBean.getId(), new OnObjectListener<CardBean>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onSuccess(CardBean result) {
                super.onSuccess(result);
                dismissLoading();
                dialog = new WelfareDialog(getActivity(), result, new WelfareDialog.OnRedPacketListener() {
                    @Override
                    public void onClickDraw(String mCardType1, String mCardType2, String key) {

                        drawRedPacket(key, mCardType1, mCardType2);

                    }
                });
                dialog.showDialog();

            }

            private void drawRedPacket(String key, final String mCardType1, final String mCardType2) {
                UserBean userBean = Preferences.getUserBean(getContext());
                if (userBean == null) {
                    startActivity(LoginActivity.startIntent(getContext()));
                    return;
                }
                CommonPresenter commonPresenter = new CommonPresenter(getContext());
                commonPresenter.drawRedPacket(key, userBean.getId(), 2, new OnObjectListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        super.onSuccess(result);
                        dismissLoading();
                        String text = "";
                        String mButtonText = "";
                        if (mCardType1.equals("1") && mCardType2.equals("2")) {
                            text = "成功兑换星币！\n 星币已放入您的钱包";
                            mButtonText = LOOK_WALLET;
                        } else {
                            text = "成功领取卡券！\n 卡券已放入您的卡包";
                            mButtonText = LOOK_CARD_BAG;
                        }

                        drawSuccess(text, mButtonText);
                        dialog.dississDialog();
                    }

                    @Override
                    public void onError(int code, String msg) {
                        super.onError(code, msg);
                        dismissLoading();
                        drawSuccess(msg, "确认");
                        dialog.dississDialog();
                    }
                });
            }

            @Override
            public void onError(int code, String msg) {
                super.onError(code, msg);
                dismissLoading();

                ToastUtils.showError("暂无福利礼券");
            }
        });
    }

    /**
     * 领取红包后弹出的对话框
     *
     * @param text
     */
    private void drawSuccess(String text, final String buttonText) {
        DrawRedPacketStateDialog dialog = new DrawRedPacketStateDialog(getContext(), text, new DrawRedPacketStateDialog.OnExamineLinstener() {
            @Override
            public void onClickButton() {
                if (!buttonText.equals(LOOK_WALLET) && !buttonText.equals(LOOK_CARD_BAG)) {
                    return;
                }
                if (buttonText.equals(LOOK_WALLET)) {
                    //跳转到钱包
                    String url = BaseUrl.GET_MY_INCOME_H5_URL + "?soure=mycollect&version=" + Constants.WEB_VERSION;
                    WebViewActivity.getInstance(getActivity(), url);
                } else if (buttonText.equals(LOOK_CARD_BAG)) {
                    //跳转到卡包
                    startActivity(new Intent(getActivity(),MyCardActivity.class));
                }
            }
        });

        dialog.setButtonText(buttonText);

        dialog.showDialog();
    }
}
