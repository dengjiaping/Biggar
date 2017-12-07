package cn.biggar.biggar.activity.update;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.biggar.biggar.R;
import cn.biggar.biggar.activity.ImageViewerActivity;
import cn.biggar.biggar.activity.ReleaseSuccessActivity;
import cn.biggar.biggar.adapter.ImagePublicAdapter;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.base.RequestCode;
import cn.biggar.biggar.bean.VideoBean;
import cn.biggar.biggar.dialog.PushMenuDialog;
import cn.biggar.biggar.dialog.TipsDialog;
import cn.biggar.biggar.event.PublishVideoSucessEvent;
import cn.biggar.biggar.presenter.ImagePublicPresenter;
import cn.biggar.biggar.presenter.VideoPublishPresenter;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.utils.Utils;
import per.sue.gear2.dialog.GearCustomDialog;
import per.sue.gear2.utils.BitmapUtils;
import per.sue.gear2.utils.FileUtils;
import per.sue.gear2.widget.NoScrollGridView;

/**
 * Created by Chenwy on 2017/7/13.
 */

public class PushActivity extends BiggarActivity implements ImagePublicPresenter.ImagePublishView, VideoPublishPresenter.VideoPublishSubmitView {
    @BindView(R.id.pic_gv)
    NoScrollGridView picGv;
    @BindView(R.id.rl_video_preview)
    RelativeLayout rlVideoPreView;
    @BindView(R.id.iv_video_preview)
    ImageView ivVidePreview;
    @BindView(R.id.iv_cover)
    ImageView ivCover;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.tv_send)
    TextView tvSend;
    @BindView(R.id.ll_link_goods_content)
    LinearLayout llLinkGoodsContent;
    @BindView(R.id.ll_link_goods_tips)
    LinearLayout llLinkGoodsTips;
    @BindView(R.id.iv_link_goods)
    ImageView ivLinkGoods;
    @BindView(R.id.tv_link_goods_name)
    TextView tvLinkGoodsName;
    @BindView(R.id.tv_link_goods_price)
    TextView tvLinkGoodsPrice;
    @BindView(R.id.tv_link_shop_name)
    TextView tvLinkShopName;
    @BindView(R.id.tv_link_goods_welfare)
    TextView tvLinkGoodsWelfare;

    private final int IMAGE_MAX_SIZE = 9;
    private ImagePublicAdapter imagePublicAdapter;

    private int way;
    private List<LocalMedia> localMedias;

    private ImagePublicPresenter imagePublicPresenter;
    private VideoPublishPresenter videoPublishPresenter;
    private NumberProgressBar numberProgressBar;
    private Dialog progressDialog;
    private TextView progressDialogDescribeTV;

    /**
     * 图片集合
     */
    private ArrayList<String> mPaths;

    /**
     * 视频
     */
    private String videoPath;

    /**
     * 视频缩略图数据
     */
    private String[] videoPhumbnail;
    /**
     * 封面1
     */
    private String coverPath;

    /**
     * 封面2
     */
    private String firstPath;

    /**
     * 关联商品id
     */
    private String linkGoodsId = "";


    //相册
    public static Intent startIntent(Context context, int way, List<LocalMedia> localMedias) {
        Intent intent = new Intent();
        intent.setClass(context, PushActivity.class);
        intent.putExtra("way", way);
        intent.putExtra("data", (Serializable) localMedias);
        return intent;
    }

    //趣拍
    public static Intent startIntent(Context context, int way, String videoPath, String[] videoPhumbnail) {
        Intent intent = new Intent();
        intent.setClass(context, PushActivity.class);
        intent.putExtra("way", way);
        intent.putExtra("videoPath", videoPath);
        intent.putExtra("videoPhumbnail", videoPhumbnail);
        return intent;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_push;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        way = getIntent().getIntExtra("way", 0);
        mPaths = new ArrayList<>();

        imagePublicPresenter = new ImagePublicPresenter(this, this);
        videoPublishPresenter = new VideoPublishPresenter(this, this);

        if (way == PushMenuDialog.WAY_TAKE_PHOTO || way == PushMenuDialog.WAY_ABLUM) {
            localMedias = (List<LocalMedia>) getIntent().getSerializableExtra("data");
        } else {
            videoPath = getIntent().getStringExtra("videoPath");
            videoPhumbnail = getIntent().getStringArrayExtra("videoPhumbnail");
        }

        imagePublicAdapter = new ImagePublicAdapter(this, mPaths);
        picGv.setAdapter(imagePublicAdapter);
        imagePublicAdapter.setOnRemoveListener(new ImagePublicAdapter.OnRemoveListener() {
            @Override
            public void onRemove(int position) {
                if (localMedias != null && localMedias.size() > 0) {
                    localMedias.remove(position);
                }
                mPaths.remove(position);
                imagePublicAdapter.notifyDataSetChanged();
            }
        });
        picGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mPaths.size() < IMAGE_MAX_SIZE && i == imagePublicAdapter.getCount() - 1) {
                    openAblum(mPaths.size() > 0);
                } else {
                    startActivity(ImageViewerActivity.startIntent(getActivity(), mPaths
                            , i, false, true));
                    overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
                }
            }
        });

        switch (way) {
            //拍照过来的
            case PushMenuDialog.WAY_TAKE_PHOTO:
                fromTakePhoto();
                break;
            //相册选择过来的
            case PushMenuDialog.WAY_ABLUM:
                fromAblum();
                break;
            //趣拍拍视频过来的
            case PushMenuDialog.WAY_RECORD:
                fromRecord();
                break;
        }

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(" ")) {
                    String[] str = s.toString().split(" ");
                    String str1 = "";
                    for (int i = 0; i < str.length; i++) {
                        str1 += str[i];
                    }
                    etContent.setText(str1);
                    etContent.setSelection(start);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkCanPush();
            }
        });
    }

    private void checkCanPush() {
        String content = etContent.getText().toString().trim();
        if (!TextUtils.isEmpty(content) && (mPaths.size() > 0 || !TextUtils.isEmpty(videoPath))) {
            tvSend.setTextColor(Color.parseColor("#F23365"));
            tvSend.setEnabled(true);
        } else {
            tvSend.setTextColor(Color.parseColor("#999999"));
            tvSend.setEnabled(false);
        }
    }

    /**
     * 打开相册
     */
    private void openAblum(boolean isOnlyImage) {
        int count = IMAGE_MAX_SIZE - mPaths.size();
        if (count <= 0) {
            ToastUtils.showError("抱歉，最多只能上传9张");
            return;
        }

        PictureSelector.create(PushActivity.this)
                .openGallery(isOnlyImage ? PictureMimeType.ofImage() : PictureMimeType.ofAll())
                .theme(R.style.picture_Sina_style)
                .maxSelectNum(count)
                .minSelectNum(1)
                .imageSpanCount(3)
                .compress(true)
                .selectionMode(PictureConfig.MULTIPLE)
                .previewImage(true)
                .forResult(RequestCode.ABLUM);
    }


    /**
     * 拍照过来的
     */
    private void fromTakePhoto() {
        Logger.e("拍照过来的...");
        coverPath = localMedias.get(0).getCompressPath();
        picGv.setVisibility(View.VISIBLE);
        rlVideoPreView.setVisibility(View.GONE);
        if (localMedias != null && localMedias.size() > 0) {
            for (LocalMedia localMedia : localMedias) {
                mPaths.add(localMedia.getCompressPath());
                imagePublicAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 相册过来的
     */
    private void fromAblum() {
        Logger.e("相册过来的...");
        //需要判断是图片还是视频
        if (localMedias != null && localMedias.size() > 0) {
            String pictureType = localMedias.get(0).getPictureType();
            int mediaType = PictureMimeType.pictureToVideo(pictureType);
            //视频
            if (mediaType == PictureConfig.TYPE_VIDEO) {
                picGv.setVisibility(View.GONE);
                rlVideoPreView.setVisibility(View.VISIBLE);
                videoPath = localMedias.get(0).getPath();
                //得到封面图
                Bitmap localVideoThumbnail = getLocalVideoThumbnail(videoPath);
                String newCoverPath = FileUtils.newOutgoingCoverFilePath();
                coverPath = firstPath = BitmapUtils.bitmapTofile(new File(newCoverPath), localVideoThumbnail).getAbsolutePath();
                Glide.with(this).load(videoPath).apply(new RequestOptions().centerCrop()).into(ivVidePreview);
            }
            //图片
            else {
                coverPath = localMedias.get(0).getCompressPath();
                picGv.setVisibility(View.VISIBLE);
                rlVideoPreView.setVisibility(View.GONE);
                mPaths.clear();
                for (LocalMedia localMedia : localMedias) {
                    mPaths.add(localMedia.getCompressPath());
                    imagePublicAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    /**
     * 趣拍录制视频过来的
     */
    private void fromRecord() {
        Logger.e("趣拍录制视频过来的...");
        coverPath = videoPhumbnail[0];
        firstPath = videoPhumbnail[1];
        picGv.setVisibility(View.GONE);
        rlVideoPreView.setVisibility(View.VISIBLE);
        Glide.with(this).load(videoPath).apply(new RequestOptions().centerCrop()).into(ivVidePreview);
    }

    /**
     * 获取本地视频的第一帧
     *
     * @param filePath
     * @return
     */
    private Bitmap getLocalVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据文件路径获取缩略图
            retriever.setDataSource(filePath);
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return bitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //相册
                case RequestCode.ABLUM:
                    if (data != null) {
                        List<LocalMedia> localMedias = PictureSelector.obtainMultipleResult(data);
                        String pictureType = localMedias.get(0).getPictureType();
                        int mediaType = PictureMimeType.pictureToVideo(pictureType);
                        //视频
                        if (mediaType == PictureConfig.TYPE_VIDEO) {
                            this.localMedias.clear();
                        }
                        this.localMedias.addAll(localMedias);
                        fromAblum();
                        checkCanPush();
                    }
                    break;
                //封面图
                case RequestCode.REQUEST_COVER:
                    if (data != null) {
                        List<LocalMedia> localMedias = PictureSelector.obtainMultipleResult(data);
                        if (localMedias.size() > 0) {
                            coverPath = localMedias.get(0).getCompressPath();
                            Glide.with(this).load(coverPath).apply(new RequestOptions().centerCrop()).into(ivCover);
                        }
                    }
                    break;
                //关联商品
                case 666:
                    if (data != null) {
                        String goodsId = data.getStringExtra("goods_id");
                        String goodsImage = data.getStringExtra("goods_image");
                        String goodsPrice = data.getStringExtra("goods_price");
                        String goodsName = data.getStringExtra("goods_name");
                        String shopName = data.getStringExtra("shop_name");
                        String rate = data.getStringExtra("rate");
                        if (TextUtils.isEmpty(rate)) {
                            rate = "0";
                        }
                        updateLinkGoods(goodsId, goodsImage, goodsPrice, goodsName, shopName, rate);
                    }
                    break;
            }
        }
    }

    private void updateLinkGoods(String goodsId, String goodsImage, String goodsPrice, String goodsName, String shopName, String rate) {
        llLinkGoodsContent.setVisibility(View.VISIBLE);
        llLinkGoodsTips.setVisibility(View.GONE);
        linkGoodsId = goodsId;
        Glide.with(this).load(Utils.getRealUrl(goodsImage)).apply(new RequestOptions().centerCrop()).into(ivLinkGoods);
        tvLinkGoodsName.setText(goodsName);
        tvLinkGoodsPrice.setText("￥" + goodsPrice);
        tvLinkShopName.setText(shopName);
        tvLinkGoodsWelfare.setText("佣金" + rate + "%");
        tvLinkGoodsWelfare.setVisibility(rate.equals("0") ? View.INVISIBLE : View.VISIBLE);
    }

    @OnClick({R.id.iv_video_preview, R.id.iv_remove_video, R.id.iv_cover, R.id.tv_send, R.id.fl_link_goods, R.id.tv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_video_preview:
                if (!TextUtils.isEmpty(videoPath)) {
                    Intent it = new Intent(Intent.ACTION_VIEW);
                    Uri uri;
                    if (Build.VERSION.SDK_INT >= 24) {
                        uri = FileProvider.getUriForFile(this, "cn.biggar.biggar.provider", new File(videoPath));
                        it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    } else {
                        String bpath = "file://" + videoPath;
                        uri = Uri.parse(bpath);
                    }
                    it.setDataAndType(uri, "video/avi");
                    startActivity(it);
                }
                break;
            case R.id.iv_remove_video:
                picGv.setVisibility(View.VISIBLE);
                rlVideoPreView.setVisibility(View.GONE);
                ivVidePreview.setImageResource(R.color.app_bg_color);
                this.localMedias.clear();
                videoPath = null;
                coverPath = null;
                firstPath = null;
                break;
            case R.id.iv_cover:
                PictureSelector.create(PushActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .theme(R.style.picture_Sina_style)
                        .maxSelectNum(1)
                        .minSelectNum(1)
                        .imageSpanCount(3)
                        .compress(true)
                        .selectionMode(PictureConfig.SINGLE)
                        .previewImage(true)
                        .forResult(RequestCode.REQUEST_COVER);
                break;
            case R.id.tv_send:
                startSend();
                break;
            case R.id.fl_link_goods:
                Intent intent = new Intent(PushActivity.this, LinkGoodsActivity.class);
                startActivityForResult(intent, 666);
                break;
            case R.id.tv_cancel:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        TipsDialog.newInstance("您确定放弃发布吗？").setOnTipsOnClickListener(new TipsDialog.OnTipsOnClickListener() {
            @Override
            public void onSure() {
                finish();
            }

            @Override
            public void onCancel() {
            }
        }).setMargin(52).show(getSupportFragmentManager());
    }

    /**
     * 开始发布
     */
    private void startSend() {
        String content = etContent.getText().toString().trim();
        //图片
        if (mPaths.size() > 0 && !TextUtils.isEmpty(coverPath)) {
            imagePublicPresenter.publishImage(content, content, mPaths, coverPath, linkGoodsId);
        }
        //视频
        else if (!TextUtils.isEmpty(videoPath) && !TextUtils.isEmpty(coverPath)) {
            videoPublishPresenter.init(videoPath, content, coverPath, firstPath, content, "", linkGoodsId);
            videoPublishPresenter.startTask();
        }
    }

    @Override
    protected void dismissProgressDialog() {
        if (null != progressDialog) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void showProgressDialog(String tip) {
        if (null == progressDialog) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_progress, null);
            progressDialogDescribeTV = (TextView) view.findViewById(R.id.dialog_describe_tv);
            numberProgressBar = (NumberProgressBar) view.findViewById(R.id.number_progress_bar);
            numberProgressBar.setMax(100);
            progressDialog = new GearCustomDialog.Builder(getActivity()).setContentView(view).create();
            progressDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        }
        progressDialogDescribeTV.setText(tip);
        progressDialog.show();
    }

    @Override
    public void onSuccess(VideoBean msg) {
        PictureFileUtils.deleteCacheDirFile(PushActivity.this);
        dismissProgressDialog();
        EventBus.getDefault().post(new PublishVideoSucessEvent(null, msg));
        ToastUtils.showNormal(getString(R.string.app_tip_submit_success));
        startActivity(ReleaseSuccessActivity.getInstance(this, msg));
        finish();
    }

    @Override
    public void uploadProgress(double percent) {
        if (null != numberProgressBar) {
            int progress = (int) (percent * 100);
            numberProgressBar.setProgress(progress);
        }
    }

    @Override
    public void onError(int code, String message) {
        super.onError(code, message);
        dismissProgressDialog();
        ToastUtils.showError(message);
    }

    @Override
    public void prepareUpload(String message) {
        showProgressDialog(message);
        numberProgressBar.setProgress(0);
    }
}
