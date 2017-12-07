package cn.biggar.biggar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.activity.update.ContentDetailActivity;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.bean.VideoBean;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.utils.Utils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mx on 2016/10/29.
 * 发布成功
 */
public class ReleaseSuccessActivity extends BiggarActivity {
    @BindView(R.id.iv_release_success)
    ImageView mIvImage;
    @BindView(R.id.tv_release_success_title)
    TextView mTvTitle;
    private VideoBean mData;


    public static Intent getInstance(Context context, VideoBean data) {
        Intent intent = new Intent(context, ReleaseSuccessActivity.class);
        intent.putExtra("data", data);
        return intent;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_release_succeed;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        mData = (VideoBean) getIntent().getSerializableExtra("data");
        initView();
        initData();
    }

    private void initView() {

    }

    private void initData() {
        if (mData == null) return;
        mTvTitle.setText(TextUtils.isEmpty(mData.getE_Content()) ? (TextUtils.isEmpty(mData.getE_Name()) ? "" : mData.getE_Name()) : mData.getE_Content());
        Glide.with(this).load(Utils.getRealUrl(mData.getE_Img1())).apply(new RequestOptions().centerCrop()).into(mIvImage);
        initTitle();
    }

    private void initTitle() {
        String title = mTvTitle.getText().toString().trim();
        String tag;
        String regex = "#.*?#";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(title);
        StringBuilder builder = new StringBuilder();
        while (matcher.find()) {
            builder.append(matcher.group()).append(" ");
            title = title.replace(matcher.group(), "");
        }
        tag = builder.toString();
        String content = title + " " + tag;
        mTvTitle.setText(content);

        SpannableStringBuilder builderSpan = new SpannableStringBuilder(mTvTitle.getText().toString());

        ForegroundColorSpan aSpan = new ForegroundColorSpan(getResources().getColor(R.color.app_text_color_q));
        ForegroundColorSpan bSpan = new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary));

        builderSpan.setSpan(aSpan, 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builderSpan.setSpan(bSpan, title.length(), content.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        mTvTitle.setText(builderSpan);
    }

    @OnClick({R.id.tv_share_qq, R.id.tv_share_weixin, R.id.tv_share_pyq, R.id.tv_share_weibo, R.id.iv_release_success_details, R.id.iv_release_success_back})
    public void OnClick(View view) {
        SHARE_MEDIA share_media = SHARE_MEDIA.QQ;
        switch (view.getId()) {
            case R.id.tv_share_qq:
                share_media = SHARE_MEDIA.QQ;
                break;
            case R.id.tv_share_weixin:
                share_media = SHARE_MEDIA.WEIXIN;
                break;
            case R.id.tv_share_pyq:
                share_media = SHARE_MEDIA.WEIXIN_CIRCLE;
                break;
            case R.id.tv_share_weibo:
                share_media = SHARE_MEDIA.SINA;
                break;
            case R.id.iv_release_success_details:
                startActivity(ContentDetailActivity.startIntent(ReleaseSuccessActivity.this, mData.getID(), mData.getE_TypeVal()));
                finish();
                return;
            case R.id.iv_release_success_back:
                finish();
                return;
        }

        share(share_media, mData.getE_Img1(), mData.getE_RATE_SHAREURL(), mData.getE_ShareTitle(),mData.getE_ShareContent());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private void share(SHARE_MEDIA plateform, String thumbImg, String targetUrl, String title, String content) {
        UMImage image = new UMImage(this, thumbImg);
        UMWeb web = new UMWeb(targetUrl);
        web.setTitle(title);
        web.setDescription(content);
        web.setThumb(image);
        new ShareAction(getActivity()).setPlatform(plateform).setCallback(new UMShareListener() {
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
        }).withMedia(web).share();
    }

}
