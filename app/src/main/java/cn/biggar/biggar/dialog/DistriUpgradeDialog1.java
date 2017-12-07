package cn.biggar.biggar.dialog;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import com.allen.library.SuperButton;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.biggar.biggar.R;
import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.bean.BgResponse;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.bean.update.DistriGoodsBean;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.utils.NumberUtils;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.utils.Utils;
import me.shaohui.bottomdialog.BaseBottomDialog;

/**
 * Created by Chenwy on 2017/7/18.
 */

public class DistriUpgradeDialog1 extends BaseBottomDialog implements View.OnClickListener {
    @BindView(R.id.tv_use_money_progress)
    TextView tvUseMoneyProgress;
    @BindView(R.id.pb_use_money)
    ContentLoadingProgressBar pbUseMoney;
    @BindView(R.id.tv_order_progress)
    TextView tvOrderProgress;
    @BindView(R.id.pb_order)
    ContentLoadingProgressBar pbOrder;
    @BindView(R.id.tv_sell_progress)
    TextView tvSellProgress;
    @BindView(R.id.pb_sell)
    ContentLoadingProgressBar pbSell;
    @BindView(R.id.tv_max_money_progress)
    TextView tvMaxMoneyProgress;
    @BindView(R.id.pb_max_money)
    ContentLoadingProgressBar pbMaxMoney;
    @BindView(R.id.upgrade)
    SuperButton upgrade;
    @BindView(R.id.tv_cur_level)
    TextView tvCurLevel;
    @BindView(R.id.tv_next_level)
    TextView tvNextLevel;

    private DistriGoodsBean distriGoodsBean;
    private String uid;
    private String brandId;
    private String nextLevel;

    @Override
    public int getLayoutRes() {
        return R.layout.distri_upgrade_dialog1;
    }

    @Override
    public float getDimAmount() {
        return 0.3f;
    }

    @Override
    public void bindView(View v) {
        ButterKnife.bind(this, v);
        Bundle arguments = getArguments();
        distriGoodsBean = (DistriGoodsBean) arguments.getSerializable("distriGoodsBean");
        brandId = arguments.getString("brandId");
        upgrade.setOnClickListener(this);
        upgrade.setText(distriGoodsBean.E_State.equals("3") ? "审核中" : "升级代理");

        UserBean userBean = Preferences.getUserBean(getContext());
        if (userBean != null) {
            uid = userBean.getId();
        }

        tvCurLevel.setText(distriGoodsBean.E_Lv.equals("3") ? "C" : "B");
        tvNextLevel.setText(distriGoodsBean.E_Lv.equals("3") ? "B" : "A");
        nextLevel = distriGoodsBean.E_Lv.equals("3") ? "2" : "1";

        setUseMoneyProgress();
        setOutOrderProgress();
//        setSellProgress();
//        setMaxMoneyProgress();
    }

    /**
     * 设置消费金额进度
     */
    private void setUseMoneyProgress() {
        pbUseMoney.setMax(NumberUtils.parseInt(distriGoodsBean.E_OnePrice));
        pbUseMoney.setProgress(NumberUtils.parseInt(distriGoodsBean.E_Price));

        String p = TextUtils.isEmpty(distriGoodsBean.E_Price)?"0":distriGoodsBean.E_Price;
        String a = "/" + (TextUtils.isEmpty(distriGoodsBean.E_OnePrice)?"0":distriGoodsBean.E_OnePrice);
        SpannableString spannableString = new SpannableString(p + a);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        spannableString.setSpan(colorSpan, 0, p.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvUseMoneyProgress.setText(spannableString);

        final int pbWidth = Utils.getScreenWidth(getActivity()) - Utils.dip2px(getActivity(), 128);
        final int pbProgress = (int) (pbWidth / NumberUtils.parseFloat(distriGoodsBean.E_OnePrice) * NumberUtils.parseFloat(distriGoodsBean.E_Price));

        ViewTreeObserver treeUseMoneyProgress = tvUseMoneyProgress.getViewTreeObserver();
        treeUseMoneyProgress.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                tvUseMoneyProgress.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int result = pbProgress - (tvUseMoneyProgress.getWidth() / 2);
                if (result > pbWidth - tvUseMoneyProgress.getWidth()) {
                    result = pbWidth - tvUseMoneyProgress.getWidth();
                } else if (result < 0) {
                    result = 0;
                }
                tvUseMoneyProgress.setPadding(result, 0, 0, 0);
            }
        });
    }

    /**
     * 设置已出订单进度
     */
    private void setOutOrderProgress() {
        pbOrder.setMax(NumberUtils.parseInt(distriGoodsBean.E_Order));
        pbOrder.setProgress(NumberUtils.parseInt(distriGoodsBean.E_Count));

        String p = TextUtils.isEmpty(distriGoodsBean.E_Count)?"0":distriGoodsBean.E_Count;
        String a = "/" + (TextUtils.isEmpty(distriGoodsBean.E_Order)?"0":distriGoodsBean.E_Order);

        SpannableString spannableString = new SpannableString(p + a);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        spannableString.setSpan(colorSpan, 0, p.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvOrderProgress.setText(spannableString);

        final int pbWidth = Utils.getScreenWidth(getActivity()) - Utils.dip2px(getActivity(), 128);
        final int pbProgress = (int) (pbWidth / NumberUtils.parseFloat(distriGoodsBean.E_Order) * NumberUtils.parseFloat(distriGoodsBean.E_Count));

        ViewTreeObserver treeUseMoneyProgress = tvOrderProgress.getViewTreeObserver();
        treeUseMoneyProgress.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                tvOrderProgress.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int result = pbProgress - (tvOrderProgress.getWidth() / 2);
                if (result > pbWidth - tvOrderProgress.getWidth()) {
                    result = pbWidth - tvOrderProgress.getWidth();
                } else if (result < 0) {
                    result = 0;
                }
                tvOrderProgress.setPadding(result, 0, 0, 0);
            }
        });
    }

    /**
     * 设置推广销售额进度
     */
//    private void setSellProgress() {
//        pbSell.setMax(NumberUtils.parseInt(distriGoodsBean.E_tuiPrice));
//        pbSell.setProgress(NumberUtils.parseInt(distriGoodsBean.E_ExtensionPrice));
//
//        String p = distriGoodsBean.E_ExtensionPrice;
//        String a = "/" + distriGoodsBean.E_tuiPrice;
//        SpannableString spannableString = new SpannableString(p + a);
//        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.colorPrimary));
//        spannableString.setSpan(colorSpan, 0, p.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//        tvSellProgress.setText(spannableString);
//
//        final int pbWidth = Utils.getScreenWidth(getActivity()) - Utils.dip2px(getActivity(), 128);
//        final int pbProgress = (int) (pbWidth / NumberUtils.parseFloat(distriGoodsBean.E_tuiPrice) * NumberUtils.parseFloat(distriGoodsBean.E_ExtensionPrice));
//
//        ViewTreeObserver treeUseMoneyProgress = tvSellProgress.getViewTreeObserver();
//        treeUseMoneyProgress.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//
//            @Override
//            public void onGlobalLayout() {
//                tvSellProgress.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                int result = pbProgress - (tvSellProgress.getWidth() / 2);
//                if (result > pbWidth - tvSellProgress.getWidth()) {
//                    result = pbWidth - tvSellProgress.getWidth();
//                } else if (result < 0) {
//                    result = 0;
//                }
//                tvSellProgress.setPadding(result, 0, 0, 0);
//            }
//        });
//    }

    /**
     * 设置消费最大金额进度
     */
//    private void setMaxMoneyProgress() {
//        pbMaxMoney.setMax(NumberUtils.parseInt(distriGoodsBean.E_MaxSale));
//        pbMaxMoney.setProgress(NumberUtils.parseInt(distriGoodsBean.E_MaxPrice));
//
//        String p = distriGoodsBean.E_MaxPrice;
//        String a = "/" + distriGoodsBean.E_MaxSale;
//        SpannableString spannableString = new SpannableString(p + a);
//        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.colorPrimary));
//        spannableString.setSpan(colorSpan, 0, p.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//        tvMaxMoneyProgress.setText(spannableString);
//
//        final int pbWidth = Utils.getScreenWidth(getActivity()) - Utils.dip2px(getActivity(), 128);
//        final int pbProgress = (int) (pbWidth / NumberUtils.parseFloat(distriGoodsBean.E_MaxSale) * NumberUtils.parseFloat(distriGoodsBean.E_MaxPrice));
//
//        ViewTreeObserver treeUseMoneyProgress = tvMaxMoneyProgress.getViewTreeObserver();
//        treeUseMoneyProgress.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//
//            @Override
//            public void onGlobalLayout() {
//                tvMaxMoneyProgress.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                int result = pbProgress - (tvMaxMoneyProgress.getWidth() / 2);
//                if (result > pbWidth - tvMaxMoneyProgress.getWidth()) {
//                    result = pbWidth - tvMaxMoneyProgress.getWidth();
//                } else if (result < 0) {
//                    result = 0;
//                }
//                tvMaxMoneyProgress.setPadding(result, 0, 0, 0);
//            }
//        });
//    }
    private void upgrade() {
        String url = BaseAPI.BASE_URL + "Brand/upgrade";
        OkGo.<String>post(url)
                .params("uid", uid)
                .params("pro_id", distriGoodsBean.ID)
                .params("level", nextLevel)
                .params("brand_id", brandId)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String s = response.body();
                        BgResponse bgResponse = new Gson().fromJson(s, BgResponse.class);
                        int status = bgResponse.status;
                        String info = (String) bgResponse.info;
                        if (status == 1) {
                            upgrade.setText("审核中");
                            distriGoodsBean.E_State = "3";

                            if (onUpgradeCallBack != null) {
                                onUpgradeCallBack.onUpgrade();
                            }

                            dismiss();
                        }
                        ToastUtils.showNormal(info);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ToastUtils.showError("请求失败");
                    }
                });
    }

    private OnUpgradeCallBack onUpgradeCallBack;

    public void setOnUpgradeCallBack(OnUpgradeCallBack onUpgradeCallBack) {
        this.onUpgradeCallBack = onUpgradeCallBack;
    }

    public interface OnUpgradeCallBack {
        void onUpgrade();
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.upgrade) {
            //审核中
            if (distriGoodsBean.E_State.equals("3")) {
                ToastUtils.showNormal("您的申请正在审核中");
            }
            //升级代理
            else {
                if (NumberUtils.parseInt(distriGoodsBean.E_Price) >= NumberUtils.parseInt(distriGoodsBean.E_OnePrice)
                        && NumberUtils.parseInt(distriGoodsBean.E_Count) >= NumberUtils.parseInt(distriGoodsBean.E_Order)
//                        && NumberUtils.parseInt(distriGoodsBean.E_ExtensionPrice) >= NumberUtils.parseInt(distriGoodsBean.E_tuiPrice)
//                        && NumberUtils.parseInt(distriGoodsBean.E_MaxPrice) >= NumberUtils.parseInt(distriGoodsBean.E_MaxSale)
                        ) {
                    upgrade();
                } else {
                    ToastUtils.showError("不满足升级条件");
                }
            }
        }
    }
}
