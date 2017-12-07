package cn.biggar.biggar.dialog;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allen.library.SuperButton;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import cn.biggar.biggar.R;
import cn.biggar.biggar.activity.BrandSpaceActivity;
import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.bean.CardBean;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.utils.NumberUtils;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.utils.Utils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Chenwy on 2017/7/14 0014.
 */

public class RedPacketDialog implements View.OnClickListener {
    private AnimationDrawable animationDrawable;
    private Dialog dialog;
    private RelativeLayout rlContainer;
    private static String CARD_TYPE1 = "1";
    private static String CARD_TYPE2 = "2";
    private static String CARD_TYPE3 = "3";
    private static String CARD_TYPE4 = "4";

    private String uid;
    private ImageView bofangView;
    private CardBean mBean;
    private ImageView ivCancel;
    private ImageView ivCancelBg;

    private RelativeLayout bgChai;
    private LinearLayout bgBgb;
    private LinearLayout bgQuan;

    private TextView tvBgb;
    private Button btnBgbSure;
    private Button btnQuanSure;
    private TextView tvQuanTile;
    private TextView tvQuanEndTime;
    private TextView tvQuanType;
    private TextView tvQuanMoney;
    private TextView tvSpending;
    private LinearLayout llMoney;
    private LinearLayout llDefault;
    private LinearLayout llDuihuan;
    private CircleImageView ivBrandLogo;
    private TextView tvDiscount;
    private TextView tvGoodsName;
    private TextView tvBrandName;
    private SuperButton tvQuanNumber;

    private Context mContext;
    private boolean isStartGainRedpacket = false;
    private boolean isGet = false;

    public RedPacketDialog(Context context) {
        this.mContext = context;
        dialog = new Dialog(context, R.style.DialogFullScreen);
        dialog.setContentView(R.layout.pop_red_packet);
        rlContainer = (RelativeLayout) dialog.findViewById(R.id.rl_container);
        tvBrandName = (TextView) dialog.findViewById(R.id.tv_brand_name);
        tvQuanNumber = (SuperButton) dialog.findViewById(R.id.tv_quan_number);
        llDefault = (LinearLayout) dialog.findViewById(R.id.ll_default);
        llDuihuan = (LinearLayout) dialog.findViewById(R.id.ll_duihuan);
        tvGoodsName = (TextView) dialog.findViewById(R.id.tv_goods_name);
        llMoney = (LinearLayout) dialog.findViewById(R.id.ll_money);
        bgChai = (RelativeLayout) dialog.findViewById(R.id.bg_chai);
        bgBgb = (LinearLayout) dialog.findViewById(R.id.bg_bgb);
        bgQuan = (LinearLayout) dialog.findViewById(R.id.bg_quan);
        btnBgbSure = (Button) dialog.findViewById(R.id.btn_bgb_sure);
        btnQuanSure = (Button) dialog.findViewById(R.id.btn_quan_sure);
        tvBgb = (TextView) dialog.findViewById(R.id.tv_bgb);
        tvQuanTile = (TextView) dialog.findViewById(R.id.tv_quan_title);
        tvQuanEndTime = (TextView) dialog.findViewById(R.id.tv_quan_end_time);
        tvQuanType = (TextView) dialog.findViewById(R.id.tv_quan_type);
        tvQuanMoney = (TextView) dialog.findViewById(R.id.tv_quan_money);
        tvSpending = (TextView) dialog.findViewById(R.id.tv_spending);
        tvDiscount = (TextView) dialog.findViewById(R.id.tv_discount);
        bofangView = (ImageView) dialog.findViewById(R.id.iv_open);
        ivCancel = (ImageView) dialog.findViewById(R.id.iv_cancel);
        ivCancelBg = (ImageView) dialog.findViewById(R.id.iv_cancel_bg);
        ivBrandLogo = (CircleImageView) dialog.findViewById(R.id.iv_brand_logo);
        ivCancel.setOnClickListener(this);
        ivCancelBg.setOnClickListener(this);
        animationDrawable = (AnimationDrawable) bofangView.getBackground();
        bofangView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isStartGainRedpacket) {
                    animationDrawable.start();//启动动画
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            handlerResult();
                        }
                    }, 1000);
                    isStartGainRedpacket = true;
                }
            }
        });

        btnBgbSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnQuanSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBean.getE_CardType().equals(CARD_TYPE1)) {
                    mContext.startActivity(BrandSpaceActivity.startIntent(mContext, mBean.getE_BrandID()));
                } else {
                    if (!mBean.getE_CardType2().equals(CARD_TYPE1)) {
                        ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                        String content = tvQuanNumber.getText().toString().trim();
                        ClipData clipData = ClipData.newPlainText("Label", content);
                        cmb.setPrimaryClip(clipData);
                        ToastUtils.showNormal("内容已复制到剪贴板");
                    }
                }
                dialog.dismiss();
            }
        });

        tvQuanNumber.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                String content = tvQuanNumber.getText().toString().trim();
                ClipData clipData = ClipData.newPlainText("Label", content);
                cmb.setPrimaryClip(clipData);
                ToastUtils.showNormal("内容已复制到剪贴板");
                return false;
            }
        });
    }

    public RedPacketDialog setCardBean(CardBean cardBean) {
        this.mBean = cardBean;
        return this;
    }

    public RedPacketDialog setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public RedPacketDialog showDailog() {
        dialog.show();
        startAnim();
        return this;
    }

    private void startAnim() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.0F, 1.0F, 0.0F, 1.0F, 1, 0.5F, 1, 0.5F);
        scaleAnimation.setDuration(500);
        scaleAnimation.setInterpolator(new OvershootInterpolator());
        scaleAnimation.setFillEnabled(true);
        scaleAnimation.setFillAfter(true);
        rlContainer.clearAnimation();
        rlContainer.startAnimation(scaleAnimation);
    }

    /**
     * 拆红包
     */
    private void handlerResult() {
        String url = BaseAPI.BASE_URL + "Card/saveBonus";
        OkGo.<String>post(url)
                .tag(mContext)
                .params("uid", uid)
                .params("id", mBean.getID())
                .params("E_CardType", mBean.getE_CardType())
                .params("E_CardType2", mBean.getE_CardType2())
                .params("money", mBean.getE_Money())
                .params("Keys", mBean.getE_RateKeys())
                .params("CardNo", mBean.getE_CardNo())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        isGet = true;
                        animationDrawable.stop();
                        bgChai.setVisibility(View.GONE);
                        judgeDate();
                        if (onPackGetListener != null) {
                            onPackGetListener.OnPackGet(mBean.getID());
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        animationDrawable.stop();
                        ToastUtils.showError("连接失败，请检查网络");
                    }
                });
    }

    public RedPacketDialog openDissmissListener() {
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (onPackGetListener != null) {
                    onPackGetListener.onDissmiss(isGet);
                }
                OkGo.getInstance().cancelTag(mContext);
            }
        });
        return this;
    }

    private OnPackGetListener onPackGetListener;

    public RedPacketDialog setOnPackGetListener(OnPackGetListener onPackGetListener) {
        this.onPackGetListener = onPackGetListener;
        return this;
    }

    public interface OnPackGetListener {
        void OnPackGet(String cardId);

        void onDissmiss(boolean isGet);
    }

    private void judgeDate() {
        //优惠券
        if (mBean.getE_CardType().equals(CARD_TYPE1)) {
            llDefault.setVisibility(View.VISIBLE);
            llDuihuan.setVisibility(View.GONE);

            //满减券
            if (mBean.getE_CardType2().equals(CARD_TYPE1)) {
                bgBgb.setVisibility(View.GONE);
                bgQuan.setVisibility(View.VISIBLE);
                llMoney.setVisibility(View.GONE);
                tvDiscount.setVisibility(View.GONE);

                tvSpending.setVisibility(View.VISIBLE);
                //满多少减多少
                tvSpending.setText("满" + mBean.getE_Spending() + "减" + mBean.getE_Money());
                //券类型
                tvQuanType.setText("获得一张满减券");
            }
            //代金券
            else if (mBean.getE_CardType2().equals(CARD_TYPE2)) {
                bgBgb.setVisibility(View.GONE);
                bgQuan.setVisibility(View.VISIBLE);
                tvSpending.setVisibility(View.GONE);
                tvDiscount.setVisibility(View.GONE);

                llMoney.setVisibility(View.VISIBLE);
                //钱
                tvQuanMoney.setText(mBean.getE_Money());
                //券类型
                tvQuanType.setText("获得一张代金券");
            }
            //折扣券
            else if (mBean.getE_CardType2().equals(CARD_TYPE3)) {
                bgBgb.setVisibility(View.GONE);
                bgQuan.setVisibility(View.VISIBLE);
                tvSpending.setVisibility(View.GONE);
                llMoney.setVisibility(View.GONE);

                tvDiscount.setVisibility(View.VISIBLE);
                //几折
                tvDiscount.setText(mBean.getE_Money() + "折");
                //券类型
                tvQuanType.setText("获得一张折扣券");
            }

            //品牌logo
            Glide.with(mContext).load(Utils.getRealUrl(mBean.getE_Logo()))
                    .apply(new RequestOptions().centerCrop()).into(ivBrandLogo);
            //品牌名
            tvQuanTile.setText(mBean.getE_BrandCnName());
            tvGoodsName.setText(TextUtils.isEmpty(mBean.getE_GoodsName()) ? "" : "指定商品：" + mBean.getE_GoodsName());
            //有效日期
            tvQuanEndTime.setText("有效日期：" + mBean.getE_ExDate());
        }
        //兑换券
        else {
            //比格币
            if (mBean.getE_CardType2().equals(CARD_TYPE1)) {
                updateUserPoints(NumberUtils.parseInt(mBean.getE_Money()));
                bgQuan.setVisibility(View.GONE);
                bgBgb.setVisibility(View.VISIBLE);
                tvBgb.setText(mBean.getE_Money());
            } else {
                bgQuan.setVisibility(View.VISIBLE);
                bgBgb.setVisibility(View.GONE);
                llDefault.setVisibility(View.GONE);
                llDuihuan.setVisibility(View.VISIBLE);
                tvQuanNumber.setText(mBean.getE_CardNo() == null ? "" : mBean.getE_CardNo());
                tvBrandName.setText(mBean.getE_Title());
                tvQuanType.setText("获得一张兑换券");
            }

            //兑换券
//            else if (mBean.getE_CardType2().equals(CARD_TYPE4)) {
//                bgQuan.setVisibility(View.VISIBLE);
//                bgBgb.setVisibility(View.GONE);
//                llDefault.setVisibility(View.GONE);
//                llDuihuan.setVisibility(View.VISIBLE);
//                tvQuanNumber.setText("123456789");
//                tvBrandName.setText(mBean.getE_BrandCnName());
//                tvQuanType.setText("获得一张兑换券");
//            }
            //零钱券
//            else if (mBean.getE_CardType2().equals(CARD_TYPE2)) {
//            }
        }
    }

    private void updateUserPoints(int bgb) {
        UserBean userBean = Preferences.getUserBean(mContext);
        String ePoints = userBean.getE_Points();
        int curPoints = NumberUtils.parseInt(ePoints);
        int result = curPoints + bgb;
        userBean.setE_Points(result + "");
        Preferences.storeUserBean(mContext, userBean);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cancel:
                animationDrawable.stop();
                dialog.dismiss();
                break;
            case R.id.iv_cancel_bg:
                animationDrawable.stop();
                dialog.dismiss();
                break;
        }
    }
}
