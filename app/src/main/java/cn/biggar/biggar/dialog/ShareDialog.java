package cn.biggar.biggar.dialog;

import android.support.v4.app.FragmentManager;
import android.view.View;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.ArrayList;

import cn.biggar.biggar.R;
import cn.biggar.biggar.activity.ShareCardActivity;
import cn.biggar.biggar.utils.ToastUtils;
import me.shaohui.bottomdialog.BaseBottomDialog;

/**
 * Created by Chenwy on 2017/9/14.
 */

public class ShareDialog extends BaseBottomDialog implements View.OnClickListener {
    public static final String SHARE_TYPE_3 = "3";//点击活动页面的分享
    public static final String SHARE_TYPE_2 = "2";//点击个人主页的分享
    public static final String SHARE_TYPE_4 = "4";//点击品牌空间的分享

    private String title;
    private String content;
    private String thumbImg;
    private String targetUrl;
    private String type;
    private String label;
    private ArrayList<String> images;
    private UMShareListener umShareListener;

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_share;
    }

    @Override
    public void bindView(View v) {
        v.findViewById(R.id.ll_wx).setOnClickListener(this);
        v.findViewById(R.id.ll_wx_circle).setOnClickListener(this);
        v.findViewById(R.id.ll_qq).setOnClickListener(this);
        v.findViewById(R.id.ll_wb).setOnClickListener(this);
        v.findViewById(R.id.ll_card).setOnClickListener(this);
    }

    public void show(FragmentManager fragmentManager, String title, String content, String thumbImg
            , String targetUrl, String type, String label, UMShareListener umShareListener) {
        this.title = title;
        this.content = content;
        this.thumbImg = thumbImg;
        this.targetUrl = targetUrl;
        this.type = type;
        this.label = label;
        this.umShareListener = umShareListener;
        super.show(fragmentManager);
    }

    public void show(FragmentManager fragmentManager, String title, String content, String thumbImg
            , ArrayList<String> images, String targetUrl, String type, String label, UMShareListener umShareListener) {
        this.title = title;
        this.content = content;
        this.thumbImg = thumbImg;
        this.images = images;
        this.targetUrl = targetUrl;
        this.type = type;
        this.label = label;
        this.umShareListener = umShareListener;
        super.show(fragmentManager);
    }

    @Override
    public void onClick(View view) {
        dismiss();
        switch (view.getId()) {
            case R.id.ll_wx:
                share(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.ll_wx_circle:
                share(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.ll_qq:
                share(SHARE_MEDIA.QQ);
                break;
            case R.id.ll_wb:
                share(SHARE_MEDIA.SINA);
                break;
            case R.id.ll_card:
                if (images == null) {
                    ArrayList<String> imgs = new ArrayList<>();
                    imgs.add(thumbImg);
                    images = imgs;
                }
                startActivity(ShareCardActivity.startIntent(
                        getContext(), images,
                        content,
                        targetUrl,
                        type, label));
                break;
        }
    }

    private void share(SHARE_MEDIA plateform) {
        UMImage image = new UMImage(getContext(), thumbImg);
        UMWeb web = new UMWeb(targetUrl);
        web.setTitle(title);
        web.setDescription(content);
        web.setThumb(image);
        new ShareAction(getActivity()).setPlatform(plateform).setCallback(umShareListener).withMedia(web).share();
    }

    @Override
    public float getDimAmount() {
        return 0.5f;
    }
}
