package cn.biggar.biggar.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.orhanobut.logger.Logger;
import com.swochina.videoView.AdResValue;
import com.swochina.videoView.AdVideoView;
import com.swochina.videoView.Parameter;
import com.swochina.videoView.Style;
import com.swochina.videoView.SwoAdVideo;
import com.swochina.videoView.SwoRequstClient;

import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import cn.biggar.biggar.R;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.bean.update.OptionsBean;
import cn.biggar.biggar.contract.SplashContract;
import cn.biggar.biggar.helper.InitHelper;
import cn.biggar.biggar.preference.AppPrefrences;
import cn.biggar.biggar.presenter.update.SplashPresenter;
import cn.biggar.biggar.utils.NumberUtils;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.utils.Utils;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Chenwy
 */
public class SplashActivity extends BiggarActivity<SplashPresenter> implements EasyPermissions.PermissionCallbacks, SplashContract.View {
    @BindView(R.id.ad_view)
    AdVideoView videoView;
    @BindView(R.id.tv_jump)
    TextView tvJump;
    @BindView(R.id.iv_start_image)
    ImageView ivStartImage;

    private boolean isRequestJZHAdvFileSuccess = false;
    private boolean isRequestBiggarAdvSuccess = false;
    private boolean isShowBiggarAdv = false;
    private String biggarAdv;
    private String biggarAdvUrl;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (isRequestJZHAdvFileSuccess) {
                        showJZHAdv();
                        return;
                    }

                    if (isRequestBiggarAdvSuccess) {
                        showBiggarAdv();
                        return;
                    }

                    handJump();
                    break;
                //倒计时
                case 1:
                    advDuration -= 1;
                    if (advDuration < 0) {
                        advDuration = 0;
                    }
                    setDurationText();
                    if (advDuration == 0) {
                        advDuration = 0;
                        handJump();
                    } else {
                        mHandler.sendEmptyMessageDelayed(1, 1000);
                    }
                    break;
            }
        }
    };

    /**
     * 广告位 ID
     */
    private String advSpaceId;

    /**
     * 广告位所属 App 标识字段
     */
    private String advAppId;

    /**
     * 广告位所属 App 媒体类型(具体分类值由 SSP 告知)
     */
    private String advCategory;

    private String advLocation;
    private int advStyle;
    private AdResValue adResValue;
    private int advDuration;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initImmersionBar() {
    }

    @Override
    public boolean isCanSwipeBack() {
        return false;
    }

    @Override
    public void onInitialize(Bundle savedInstanceState) {
        InitHelper.getInstance().init(SplashActivity.this);
        requestPermission();
    }

    /**
     * 广告初始化
     */
    private void initAdv() {
        int screenWidth = Utils.getScreenWidth(this);
        int videoViewHeight = (int) (1280f * screenWidth / 720f);

        videoView.getLayoutParams().width = screenWidth;
        videoView.getLayoutParams().height = videoViewHeight;

        SwoAdVideo.getInstance().init(getApplicationContext());
        //正式
        advSpaceId = "RUWas9D8";
        advAppId = "f35e249abeb511e78cf7fa163e6bf3e2";
        advCategory = "2501";

        //测试
//        advSpaceId = "teNIVKQm";
//        advAppId = "66dd066a62c311e78cf7fa163e6bf3e2";
//        advCategory = "3701";

        advLocation = "1";
        //Style.PEACOCK开屏  Style.INFORMATION_FLOW信息流
        advStyle = Style.PEACOCK;
        adResValue = new AdResValue();

        if (AppPrefrences.getInstance(getApplicationContext()).isFirst()) {
            videoView.setIntentActivity(this, GuideActivity.class);
        } else {
            videoView.setIntentActivity(this, MainActivity.class);
        }

        mPresenter.requestOptions();
        mHandler.sendEmptyMessageDelayed(0, 3000);
    }


    private void setDurationText() {
        if (tvJump.getVisibility() == View.VISIBLE) {
            tvJump.setText("跳过 " + advDuration + "s");
        }
    }

    /**
     * 手动跳转
     */
    private void handJump() {
        if (videoView != null && videoView.isPlaying()) {
            videoView.pause();
        }
        if (AppPrefrences.getInstance(getApplicationContext()).isFirst()) {
            startActivity(new Intent(SplashActivity.this, GuideActivity.class));
        } else {
            startActivity(MainActivity.startIntent(SplashActivity.this));
        }
        finish();
    }

    /**
     * 显示极智汇广告
     */
    private void showJZHAdv() {
        ivStartImage.setVisibility(View.GONE);
        videoView.setVisibility(View.VISIBLE);
        tvJump.setVisibility(View.GONE);
        setDurationText();

        videoView.play(adResValue, AdVideoView.JUMP_INSIDE, true);
    }

    private void showBiggarAdv() {
        videoView.setVisibility(View.GONE);
        ivStartImage.setVisibility(View.VISIBLE);
        tvJump.setVisibility(View.VISIBLE);
        setDurationText();

        Glide.with(this).load(biggarAdv)
                .apply(new RequestOptions().centerCrop()
                        .skipMemoryCache(false)
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(ivStartImage);

        isShowBiggarAdv = true;
        mHandler.sendEmptyMessageDelayed(1, 1000);
    }

    private void requesJZHtAdv() {
        new Thread() {
            @Override
            public void run() {
                int result = SwoRequstClient.post(adResValue, advStyle, advSpaceId, advAppId, advCategory, advLocation, null, null, null, null);
                Logger.e("advResult : " + result);
                if (result == Parameter.OK) {
                    isRequestJZHAdvFileSuccess = true;
                }
            }
        }.start();
    }

    @AfterPermissionGranted(123)
    public void requestPermission() {
        String[] pers = {Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA
                , Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE
                , Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (!EasyPermissions.hasPermissions(this, pers)) {
            EasyPermissions.requestPermissions(this
                    , "需要如下权限：\n1、读写文件权限\n2、相机权限\n3、录音权限\n4、使用电话权限\n5、定位权限"
                    , 123, pers);
        } else {
            initAdv();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).setTitle("需要权限")
                    .setRationale("需要如下权限：\n1、读写文件权限\n2、相机权限\n3、录音权限\n4、使用电话权限\n5、定位权限\n请到设置界面开启。")
                    .build().show();
            return;
        }
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            requestPermission();
        }
    }

    @Override
    protected void onDestroy() {
        mHandler.removeMessages(0);
        mHandler.removeMessages(1);
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    //不让返回
    @Override
    public void onBackPressed() {
    }

    @Override
    public void showError(String errMsg) {
        isRequestJZHAdvFileSuccess = false;
        isRequestBiggarAdvSuccess = false;
        if (!TextUtils.isEmpty(errMsg)) {
            ToastUtils.showError(errMsg);
        }
    }

    @Override
    public void showOptions(OptionsBean optionsBean) {
        advDuration = NumberUtils.parseInt(optionsBean.E_Seconds);
        biggarAdv = Utils.getRealUrl(optionsBean.E_Img1);
        biggarAdvUrl = optionsBean.E_URL;
        int state = NumberUtils.parseInt(optionsBean.E_State);
        switch (state) {
            //审核
            case 0:
                isRequestBiggarAdvSuccess = false;
                isRequestJZHAdvFileSuccess = false;
                break;
            //比格
            case 1:
                isRequestBiggarAdvSuccess = true;
                isRequestJZHAdvFileSuccess =false;
                break;
            //关闭
            case 2:
                isRequestBiggarAdvSuccess = false;
                isRequestJZHAdvFileSuccess = false;
                break;
            //极智汇
            case 3:
                requesJZHtAdv();
                break;
        }
    }

    @OnClick({R.id.tv_jump, R.id.iv_start_image})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.tv_jump:
                handJump();
                break;
            case R.id.iv_start_image:
                if (isShowBiggarAdv) {
                    WebViewActivity.jumpFromSplash(SplashActivity.this, biggarAdvUrl);
                    finish();
                }
                break;
        }
    }
}
