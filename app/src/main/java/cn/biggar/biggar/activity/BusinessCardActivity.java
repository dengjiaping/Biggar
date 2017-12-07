package cn.biggar.biggar.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.api.BaseUrl;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.bean.BrandDetails;
import cn.biggar.biggar.dialog.ShareDialog;
import cn.biggar.biggar.presenter.BrandPersenter;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.utils.Utils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.biggar.biggar.view.RootLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import per.sue.gear2.adapter.ImageSimpleAdapter;
import per.sue.gear2.presenter.OnObjectListener;
import per.sue.gear2.utils.UnitUtils;
import per.sue.gear2.widget.flow.CircleFlowIndicator;
import per.sue.gear2.widget.flow.ViewFlowFixViewPager;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by zl on 2017/3/21.
 * 商家名片
 */

public class BusinessCardActivity extends BiggarActivity {
    @BindView(R.id.business_card_recommend_view_flow)
    ViewFlowFixViewPager mRecommendViewFlow;
    @BindView(R.id.business_card_recommend_circle_indicator)
    CircleFlowIndicator mIndicator;
    @BindView(R.id.business_card_logo1_iv)
    CircleImageView mLogoIv;
    @BindView(R.id.business_card_name)
    TextView mCardName;
    @BindView(R.id.business_card_rq_code)
    ImageView mCardRqCode;
    @BindView(R.id.business_card_service_time)
    TextView mServiceTime;
    @BindView(R.id.business_card_service_time_ly)
    LinearLayout mServiceTimeLy;
    @BindView(R.id.business_card_weixin_tv)
    TextView mWeixinTv;
    @BindView(R.id.business_card_weixin_ly)
    LinearLayout mWeixinLy;
    @BindView(R.id.line_01)
    View line01;
    @BindView(R.id.business_card_qq_tv)
    TextView mCardQqTv;
    @BindView(R.id.business_card_qq_ly)
    LinearLayout mCardQqLy;
    @BindView(R.id.line_02)
    View line02;
    @BindView(R.id.business_card_phone_tv)
    TextView mCardPhoneTv;
    @BindView(R.id.business_card_phone_ly)
    LinearLayout mPhoneLy;
    @BindView(R.id.business_card_ly)
    LinearLayout mLayout;
    @BindView(R.id.line_03)
    View line03;

    private BrandPersenter mBrandP;
    BrandDetails bean;
    public String shareUrl;
    private ShareDialog shareDialog;

    public static void startIntent(Context context, String bId) {
        Intent intent = new Intent(context, BusinessCardActivity.class);
        intent.putExtra("bId", bId);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_business_card;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {

        initViews();
        initData();
        shareDialog = new ShareDialog();
    }

    private void initData() {
        mBrandP = new BrandPersenter(this);
        String bid = getIntent().getStringExtra("bId");
        showLoading();
        mBrandP.queryBrandDetails(bid, new OnObjectListener<BrandDetails>() {
            @Override
            public void onSuccess(BrandDetails result) {
                bean = result;
                loadData(result);
                dismissLoading();
            }

            @Override
            public void onError(int code, String msg) {

                ToastUtils.showError(msg);
                dismissLoading();
            }
        });
    }

    /**
     * 加载数据
     *
     * @param result
     */
    private void loadData(BrandDetails result) {

        Glide.with(this).load(BaseAPI.BASE_RES_URL + result.getE_Logo())
                .apply(new RequestOptions().centerCrop())
                .into(mLogoIv);

        mCardName.setText(result.getE_BrandCnName());
        loadShufflingImgs((ArrayList<String>) result.getE_Img2());
        if (!TextUtils.isEmpty(result.getE_Qrcode())) {
            Glide.with(this).load(Utils.getRealUrl(result.getE_Qrcode()))
                    .apply(new RequestOptions().centerCrop())
                    .into(mCardRqCode);
        } else {
            shareUrl = BaseUrl.SHARE_BRAND_URL + "ID=" + result.getID();
            createQrcode(shareUrl);
        }
        if (!TextUtils.isEmpty(result.getE_Service())) {
            mServiceTime.setText(result.getE_Service());
        } else {
            goneView(mServiceTime);
        }
        if (!TextUtils.isEmpty(result.getE_WxID())) {
            mWeixinTv.setText(result.getE_WxID());
        } else {
            goneView(mWeixinTv);
        }
        if (!TextUtils.isEmpty(result.getE_QQMsn())) {
            mCardQqTv.setText(result.getE_QQMsn());
        } else {
            goneView(mCardQqTv);
        }
        if (!TextUtils.isEmpty(result.getE_ServiceMobile())) {
            mCardPhoneTv.setText(result.getE_ServiceMobile());
        } else {
            goneView(mCardPhoneTv);
        }

        if (TextUtils.isEmpty(result.getE_ServiceMobile()) &&
                TextUtils.isEmpty(result.getE_QQMsn()) &&
                TextUtils.isEmpty(result.getE_WxID()) &&
                TextUtils.isEmpty(result.getE_Service())) {
            mLayout.setVisibility(View.GONE);
        }

    }

    private void createQrcode(String path) {
        int wh = UnitUtils.dip2px_new(this, 184);
        Bitmap bitmap = CodeUtils.createImage(path, wh, wh, null);
        mCardRqCode.setImageBitmap(bitmap);
    }

    public void goneView(TextView layout) {
        layout.setText("");
    }

    private void loadShufflingImgs(ArrayList<String> imagslist) {
        try {

            if (imagslist == null || (imagslist != null && imagslist.isEmpty())) {
                imagslist.add("");
            }
            for (int i = 0; i < imagslist.size(); i++) {
                imagslist.set(i, Utils.getRealUrl(imagslist.get(i)));
            }
            ImageSimpleAdapter imageSimpleAdapter = new ImageSimpleAdapter(getActivity());
            imageSimpleAdapter.setOnAdapterItemClickLiener(new ImageSimpleAdapter.OnAdapterItemClickLiener() {
                @Override
                public void OnImageViewClick(int position, String url) {
                    // Toast.makeText(getActivity().getApplicationContext(), "position=" + position + "  url=" + url, Toast.LENGTH_SHORT).show();
                }
            });
            imageSimpleAdapter.setDefultImageResId(R.drawable.gear_image_default);
            imageSimpleAdapter.setList(imagslist);
            imageSimpleAdapter.setIsLoob(true);
            mRecommendViewFlow.setAdapter(imageSimpleAdapter);
            mRecommendViewFlow.setmSideBuffer(imagslist.size()); // 实际图片张数， 我的ImageAdapter实际图片张数为3
            mRecommendViewFlow.setFlowIndicator(mIndicator);
            mRecommendViewFlow.setTimeSpan(4500);
            //viewFlow.setFillColor(getResources().getColor(r.color.app_green_l));
            mRecommendViewFlow.setSelection(imagslist.size()); // 设置初始位置
            mRecommendViewFlow.startAutoFlowTimer(); // 启动自动播放
            mRecommendViewFlow.requestLayout();// 重新计算该控制在父布局中的位置
        } catch (Exception e) {
        }
    }


    private void initViews() {
        RootLayout.getInstance(this)
                .setOnRightClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String targetUrl = BaseUrl.BUSINESS_CARD + "ID=" + bean.getID();
                        Logger.e("targetUrl------" + targetUrl);
                        share(BaseAPI.BASE_RES_URL + bean.getE_Logo(), targetUrl);
                    }
                });
    }

    public void share(String imageUrl, String targetUrl) {
        String content = TextUtils.isEmpty(bean.getE_BrandCnName()) ? getString(R.string.share_default_title) : bean.getE_BrandCnName();
        shareDialog.show(getSupportFragmentManager(), content, content, imageUrl, targetUrl, ShareDialog.SHARE_TYPE_4, "", new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                ToastUtils.showNormal("分享成功");
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                ToastUtils.showError("分享失败");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                ToastUtils.showNormal("取消分享");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
