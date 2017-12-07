package cn.biggar.biggar.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.dialog.ShareDialog;
import cn.biggar.biggar.utils.StorageManager;
import cn.biggar.biggar.view.RootLayout;
import cn.biggar.biggar.view.SmartImageView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import cn.biggar.biggar.adapter.RoundImageAdapter;

import per.sue.gear2.utils.BitmapUtils;
import per.sue.gear2.utils.ToastUtils;
import per.sue.gear2.utils.UnitUtils;
import per.sue.gear2.widget.flow.CircleFlowIndicator;
import per.sue.gear2.widget.flow.ViewFlowFixViewPager;

/**
 * Created by zl on 2016/12/20.
 * 视频或者图片的卡片分享
 */

public class ShareCardActivity extends BiggarActivity {
    Context mContext;
    @BindView(R.id.share_card_content)
    TextView shareCardContent;
    @BindView(R.id.share_card_tag)
    TextView shareCardTag;
    @BindView(R.id.share_card_qr_code_image)
    SmartImageView shareCardQrCodeImage;
    @BindView(R.id.share_card_layout)
    LinearLayout shareCardLy;
    @BindView(R.id.recommend_view_flow)
    ViewFlowFixViewPager recommendViewFlow;
    @BindView(R.id.recommend_circle_indicator)
    CircleFlowIndicator recommendCircleIndicator;

    private String mType;
    private String mQrCodePath;//缓存 分享二维码的 路径
    private boolean mIsSave;
    private String mContent, mShareUrl, mTitle, mLabel;
    private ArrayList<String> mImages;

    public static Intent startIntent(Context context, ArrayList<String> images, String content, String shareUrl, String type, String label) {
        Intent intent = new Intent(context, ShareCardActivity.class);
        intent.putExtra("type", type);
        intent.putStringArrayListExtra("images", images);
        intent.putExtra("content", content);
        intent.putExtra("shareUrl", shareUrl);
        intent.putExtra("label", label);
        return intent;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_card_share;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        mContext = this;
        mImages = getIntent().getStringArrayListExtra("images");
        mContent = getIntent().getStringExtra("content");

        mShareUrl = getIntent().getStringExtra("shareUrl");
        mType = getIntent().getStringExtra("type");
        mTitle = getIntent().getStringExtra("title");
        mLabel = getIntent().getStringExtra("label");

        initData();
    }

    private void initData() {
        loadData();
        createQrcode();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(mQrCodePath)) {
            File file = new File(mQrCodePath);
            if (!file.exists()) {
                mQrCodePath = null;
            }
        }
    }

    private void loadData() {

        RoundImageAdapter imageSimpleAdapter = new RoundImageAdapter(getActivity());
        imageSimpleAdapter.setDefultImageResId(R.drawable.gear_image_default);
        imageSimpleAdapter.setList(mImages);
        imageSimpleAdapter.setIsLoob(mImages.size() > 1);
        recommendCircleIndicator.setVisibility(mImages.size() > 1 ? View.VISIBLE : View.GONE);
        recommendCircleIndicator.setFillColor(getResources().getColor(R.color.colorPrimary));
        recommendCircleIndicator.setStrokeColor(Color.WHITE);
        recommendViewFlow.setAdapter(imageSimpleAdapter);
        recommendViewFlow.setFlowIndicator(recommendCircleIndicator);
        recommendViewFlow.setmSideBuffer(mImages.size());

        RootLayout.getInstance(this).setTitle(TextUtils.isEmpty(mTitle) ? "" : mTitle);
        if (mType.equals("0")) {
            RootLayout.getInstance(this).setTitle("视频卡片分享");
        } else if (mType.equals("1")) {
            RootLayout.getInstance(this).setTitle("图集卡片分享");
        } else if (mType.equals(ShareDialog.SHARE_TYPE_3)) {
            RootLayout.getInstance(this).setTitle("活动卡片分享");
        } else if (mType.equals(ShareDialog.SHARE_TYPE_2)) {
            RootLayout.getInstance(this).setTitle("个人主页卡片分享");
        } else if (mType.equals(ShareDialog.SHARE_TYPE_4)) {
            RootLayout.getInstance(this).setTitle("品牌卡片分享");
        }
        shareCardContent.setText(mContent);
        shareCardTag.setText(TextUtils.isEmpty(mLabel) ? "" : "——" + mLabel);

    }

    /**
     * 创建二维码
     */
    private void createQrcode() {
        int wh = UnitUtils.dip2px_new(this, 48);
        Bitmap bitmap = CodeUtils.createImage(mShareUrl, wh, wh, null);
        shareCardQrCodeImage.setImageBitmap(bitmap);
    }

    SHARE_MEDIA share_media;

    @OnClick({R.id.share_card_qq, R.id.share_card_weixin, R.id.share_card_pyq, R.id.share_card_weibo, R.id.share_card_save})
    public void onClick(View view) {

        if (TextUtils.isEmpty(mQrCodePath)) {
            saveLocal(false);
        }

        switch (view.getId()) {
            case R.id.share_card_qq:
                share_media = SHARE_MEDIA.QQ;
                break;
            case R.id.share_card_weixin:
                share_media = SHARE_MEDIA.WEIXIN;
                break;
            case R.id.share_card_pyq:
                share_media = SHARE_MEDIA.WEIXIN_CIRCLE;
                break;
            case R.id.share_card_weibo:
                share_media = SHARE_MEDIA.SINA;
                break;
            case R.id.share_card_save:
                if (!mIsSave) {
                    saveLocal(true);
                    mIsSave = true;
                }
                ToastUtils.showShortMessage("保存成功!", this);
                break;
        }
        share(mQrCodePath, share_media);
    }

    private void share(String mQrCodePath, SHARE_MEDIA share_media) {
        Bitmap bitmap = BitmapFactory.decodeFile(mQrCodePath);
        sharePlatform(ShareCardActivity.this, "", share_media, "", bitmap, "", umShareListener);
    }

    private void sharePlatform(Activity activity, String title, SHARE_MEDIA plateform, String text, Bitmap bitmap, String targetUrl, UMShareListener umShareListener) {
        if (TextUtils.isEmpty(title)) {
            title = "比格:一个有逼格的app";
        }
        if (bitmap == null) {
            UMWeb web = new UMWeb(targetUrl);
            web.setTitle(title);//标题
            web.setDescription(text);//描述
            new ShareAction(activity).setPlatform(plateform).setCallback(umShareListener)
                    .withMedia(web)
                    .share();
        } else {
            UMImage image = new UMImage(activity, bitmap);
            new ShareAction(activity).setPlatform(plateform).setCallback(umShareListener)
                    .withMedia(image)
                    .share();
        }
    }

    /**
     * 分享回调
     */
    UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
           ToastUtils.showError("分享成功", getApplication());

            if (!mIsSave) {
                //删除
                File file = new File(mQrCodePath);
                if (file.exists()) {
                    file.delete();
                    mQrCodePath = null;
                }
            }
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            ToastUtils.showError("分享失败", getApplication());
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
           ToastUtils.showError("取消分享", getApplication());
        }
    };

    /**
     * 保存到本地
     */
    private void saveLocal(boolean isNotice) {
        File file;
        if (isNotice) {//保存本地
            file = StorageManager.getInstance().createQrCodeImgFile(this);
        } else { //临时保存
            file = StorageManager.getInstance().createImgFile(this);
        }
        shot(shareCardLy, file);

        if (isNotice) {
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            sendBroadcast(intent);//这个广播的目的就是更新图库，发了这个广播进入相册
        }
    }

    /**
     * 截图
     *
     * @param view
     * @return
     */
    private Bitmap shot(View view, File file) {
        view.setBackgroundColor(Color.WHITE);
        //View是你需要截图的View
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        mQrCodePath = file.getAbsolutePath();
        BitmapUtils.bitmapTofile(file, b1);

        view.destroyDrawingCache();
        view.setBackgroundColor(Color.TRANSPARENT);
        return b1;
    }
}
