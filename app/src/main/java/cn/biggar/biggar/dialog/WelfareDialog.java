package cn.biggar.biggar.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import cn.biggar.biggar.R;
import cn.biggar.biggar.bean.CardBean;

import cn.biggar.biggar.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mx on 2016/9/5.
 * 品牌 福利 dialog
 */
public class WelfareDialog extends Dialog implements View.OnClickListener {
    Context context;

    @BindView(R.id.dialog_card_close_iv)
    ImageView mClose;
    @BindView(R.id.red_packet_gift_certificate_iv)
    ImageView mGiftCertificateimage;//
    @BindView(R.id.red_packet_gift_certificate_title_tv)
    TextView mGiftCertificateTitle;//
    @BindView(R.id.red_packet_gift_certificate_tv)
    TextView mGiftCertificate;//
    @BindView(R.id.red_packet_draw_tv)
    TextView mDraw;//
    @BindView(R.id.dialog_card_head_img_iv)
    CircleImageView mCardHeadImgIv;
    @BindView(R.id.dialog_card_title_tv)
    TextView mCardTitleTv;
    @BindView(R.id.dialog_card_time_tv)
    TextView mCardTimeTv;
    @BindView(R.id.dialog_card_hint_tv)
    TextView mCardHintTv;
    @BindView(R.id.red_packet_gift_certificate_lv)
    LinearLayout mGiftCertificateLv;
    @BindView(R.id.dialog_card_bg_lv)
    LinearLayout mBgLv;
    @BindView(R.id.dialog_card_image_iv)
    ImageView mCardImageIv;
    private OnRedPacketListener listener;
    Dialog mDia;
    private View view;
    CardBean mBean;
    private static String CARD_TYPE1 = "1";
    private static String CARD_TYPE2 = "2";
    private static String CARD_TYPE3 = "3";

    public WelfareDialog(@NonNull Context context,CardBean bean, OnRedPacketListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
        this.mBean = bean;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void showDialog() {
        mDia = new AlertDialog.Builder(context, R.style.UploadImgDiaLog).create();
        view = LayoutInflater.from(context).inflate(R.layout.dialog_show_welfare, null);
        ButterKnife.bind(this, view);
        mDia.show();
        mDia.setContentView(view);
        mDia.setCancelable(true);
        mDia.setCanceledOnTouchOutside(true);

        Glide.with(context).load(Utils.getRealUrl(mBean.getE_Logo()))
                .apply(new RequestOptions().centerCrop())
                .into(mCardHeadImgIv);


        Glide.with(context).load(Utils.getRealUrl(mBean.getE_Img()))
                .apply(new RequestOptions().centerCrop())
                .into(mGiftCertificateimage);

        if (TextUtils.isEmpty(mBean.getE_Img1())){
            mBgLv.setBackground(context.getResources().getDrawable(R.drawable.shape_oval_white));
            mCardImageIv.setVisibility(View.GONE);
        }else {
            Glide.with(context).load(Utils.getRealUrl(mBean.getE_Img1()))
                    .apply(new RequestOptions().centerCrop())
                    .into(mCardImageIv);
        }
        mGiftCertificateTitle.setText(TextUtils.isEmpty(mBean.getE_Title()) ? "" : mBean.getE_Title());

//        mGiftCertificate.setText(TextUtils.isEmpty(mBean.getE_Title()) ? "" : mBean.getE_Money());
        mCardTitleTv.setText(TextUtils.isEmpty(mBean.getE_BrandCnName()) ? "" : mBean.getE_BrandCnName());
        if (!TextUtils.isEmpty(mBean.getE_FormTime())&&!TextUtils.isEmpty(mBean.getE_ExDate())){
            mCardTimeTv.setText("可兑换时间："+mBean.getE_FormTime()+"-"+mBean.getE_ExDate());
        }else if (!TextUtils.isEmpty(mBean.getE_FormTime())&&TextUtils.isEmpty(mBean.getE_ExDate())){
            mCardTimeTv.setText("可兑换时间："+mBean.getE_FormTime());
        }
        mGiftCertificate.setText((TextUtils.isEmpty(mBean.getE_Money()) ? "" : mBean.getE_Money()+judgeDate()));
    }

    public void dississDialog() {
        mDia.dismiss();
    }

    @OnClick({R.id.dialog_card_close_iv, R.id.red_packet_draw_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_card_close_iv:
                mDia.dismiss();
                break;
            case R.id.red_packet_draw_tv:
                listener.onClickDraw(mBean.getE_CardType(),mBean.getE_CardType2(),mBean.getE_RateKeys());
                break;
        }
    }

    public interface OnRedPacketListener {
        void onClickDraw(String mCardType1, String mCardType2, String key);
    }
    private String judgeDate() {
        String mTicketText = "";
        if (mBean.getE_CardType().equals(CARD_TYPE1)){
            if (mBean.getE_CardType2().equals(CARD_TYPE1)){
                mTicketText = "元优惠券";
                if (TextUtils.isEmpty(mBean.getE_Spending())){
                    mCardHintTv.setVisibility(View.GONE);
                }else {
                    mCardHintTv.setVisibility(View.VISIBLE);
                    mCardHintTv.setText("(满"+(TextUtils.isEmpty(mBean.getE_Spending())?"":mBean.getE_Spending())+"元可用)");
                }
            }else if (mBean.getE_CardType2().equals(CARD_TYPE2)){
                mTicketText = "元优惠券";
            }else if (mBean.getE_CardType2().equals(CARD_TYPE3)){
                mTicketText = "元折扣券";
            }
        }else {
            if (mBean.getE_CardType2().equals(CARD_TYPE1)){
                mTicketText = "元星币兑换券";
            }else if (mBean.getE_CardType2().equals(CARD_TYPE2)){
                mTicketText = "元兑换券";
            }else if(mBean.getE_CardType2().equals(CARD_TYPE3)){
                mTicketText = "元商品兑换券";
            }
        }
        return mTicketText;
    }
}

