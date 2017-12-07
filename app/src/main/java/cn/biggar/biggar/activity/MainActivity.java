package cn.biggar.biggar.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.SPUtils;
import com.duanqu.qupai.sdk.android.QupaiManager;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.biggar.biggar.R;
import cn.biggar.biggar.activity.update.PushActivity;
import cn.biggar.biggar.adapter.MainFragmentAdapter;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.base.RequestCode;
import cn.biggar.biggar.bean.RecordResult;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.contract.MainContract;
import cn.biggar.biggar.dialog.PushMenuDialog;
import cn.biggar.biggar.event.ReceiverMsgEvent;
import cn.biggar.biggar.fragment.fragmentnavigator.FragmentNavigator;
import cn.biggar.biggar.fragment.update.DiscoverFragment;
import cn.biggar.biggar.fragment.update.MapFragment;
import cn.biggar.biggar.fragment.update.MeFragment;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.helper.DownloadManager;
import cn.biggar.biggar.presenter.update.MainPresenter;
import cn.biggar.biggar.utils.AppConfig;
import cn.biggar.biggar.utils.AppGlobalSetting;
import cn.biggar.biggar.view.MainBottomBar;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;
import io.rong.push.RongPushClient;


/**
 * Update by Chenwy on 2017/10/10
 */
public class MainActivity extends BiggarActivity<MainPresenter> implements PushMenuDialog.OnPushWayChooseCallBck
        , MainBottomBar.OnItemChooseListener, MainContract.View {
    @BindView(R.id.main_bottom_bar)
    MainBottomBar mainBottomBar;
    @BindView(R.id.iv_plus)
    ImageView ivPlus;
    private FragmentNavigator fragmentNavigator;
    private PushMenuDialog mPushMenuDialog;

    /**
     * 用户信息
     */
    private UserBean userBean;

    public static Intent startIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        //检测版本更新
        DownloadManager.getInstance().checkUpdate(MainActivity.this);
        // + 按钮初始化
        mPushMenuDialog = new PushMenuDialog();
        mPushMenuDialog.setOnPushWayChooseCallBck(this);

        //底部tab初始化，默认显示第一个tab
        fragmentNavigator = new FragmentNavigator(getSupportFragmentManager(), new MainFragmentAdapter(), R.id.fl_container);
        mainBottomBar.setOnItemChooseListener(this);
        setTab(MainBottomBar.ITEM_MAP);

        //融云
        initRong();
    }

    @Override
    public boolean isLoadEventBus() {
        return true;
    }

    @Override
    public boolean isCanSwipeBack() {
        return false;
    }

    /**
     * 融云相关初始化
     */
    private void initRong() {
        String rongToken = SPUtils.getInstance().getString(Constants.RONG_TOKEN);
        userBean = Preferences.getUserBean(this);
        if (userBean != null) {
            if (TextUtils.isEmpty(rongToken)) {
                mPresenter.requestRongToken(userBean);
            } else {
                mPresenter.connectRong(rongToken);
            }
        }
    }

    public void setMineHint(boolean ishint) {
        mainBottomBar.setMineHint(ishint);
    }

    private void setTab(int position) {
        fragmentNavigator.showFragment(position);
        ivPlus.setVisibility(position == MainBottomBar.ITEM_ACTIVITY ? View.VISIBLE : View.GONE);
        if (position == MainBottomBar.ITEM_MINE && fragmentNavigator.getCurrentFragment() != null) {
            MeFragment meFragment = (MeFragment) fragmentNavigator.getCurrentFragment();
            meFragment.refresh();
        }
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    private void goBack() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        startActivity(setIntent);
    }

    //拍照
    private void takePhoto() {
        PictureSelector.create(this)
                .openCamera(PictureMimeType.ofImage())
                .theme(R.style.picture_Sina_style)
                .compress(true)
                .forResult(RequestCode.TAKE_PHOTO);
    }

    //相册
    private void openAblum() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofAll())
                .theme(R.style.picture_Sina_style)
                .isCamera(false)
                .maxSelectNum(9)
                .minSelectNum(1)
                .imageSpanCount(3)
                .compress(true)
                .selectionMode(PictureConfig.MULTIPLE)
                .previewImage(true)
                .previewVideo(true)
                .forResult(RequestCode.ABLUM);
    }

    //拍视频
    private void openRecord() {
        AppGlobalSetting sp = new AppGlobalSetting(getApplicationContext());
        Boolean isGuideShow = sp.getBooleanGlobalItem(AppConfig.PREF_VIDEO_EXIST_USER, true);
        QupaiManager.getQupaiService(getApplicationContext()).showRecordPage(this, RequestCode.RECORDE_SHOW, isGuideShow);
        sp.saveGlobalConfigItem(AppConfig.PREF_VIDEO_EXIST_USER, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //拍照回调
                case RequestCode.TAKE_PHOTO:
                    if (data != null) {
                        List<LocalMedia> localMedias = PictureSelector.obtainMultipleResult(data);
                        startActivity(PushActivity.startIntent(MainActivity.this, PushMenuDialog.WAY_TAKE_PHOTO, localMedias));
                    }
                    break;
                //相册回调
                case RequestCode.ABLUM:
                    if (data != null) {
                        List<LocalMedia> localMedias = PictureSelector.obtainMultipleResult(data);
                        startActivity(PushActivity.startIntent(MainActivity.this, PushMenuDialog.WAY_ABLUM, localMedias));
                    }
                    break;
                //录制回调
                case RequestCode.RECORDE_SHOW:
                    if (data != null) {
                        RecordResult recordResult = new RecordResult(data);
                        String videoPath = recordResult.getPath();
                        String[] videoPhumbnail = recordResult.getThumbnail();
                        startActivity(PushActivity.startIntent(MainActivity.this, PushMenuDialog.WAY_RECORD, videoPath, videoPhumbnail));
                    }
                    break;
            }
        }
    }

    /**
     * 选择发布方式
     */
    private void startPhotoVideo() {
        if (userBean == null) {
            startActivity(LoginActivity.startIntent(getActivity()));
        } else {
            mPushMenuDialog.show(getSupportFragmentManager());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        RongPushClient.clearAllNotifications(this.getApplicationContext());
        userBean = Preferences.getUserBean(this);
        if (userBean != null) {
            mPresenter.requestNoticeNum(userBean.getId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiverMsg(ReceiverMsgEvent receiverMsgEvent) {
        mPresenter.requestRongMsgNum();
    }

    @Override
    protected void onDestroy() {
        RongIM.getInstance().disconnect();
        super.onDestroy();
    }

    @OnClick({R.id.iv_plus})
    public void onViewClicked(View v) {
        if (v.getId() == R.id.iv_plus) {
            startPhotoVideo();
        }
    }

    @Override
    public void onPushWayChoose(int wayChoose) {
        switch (wayChoose) {
            case PushMenuDialog.WAY_RECORD:
                openRecord();
                break;
            case PushMenuDialog.WAY_ABLUM:
                openAblum();
                break;
            case PushMenuDialog.WAY_TAKE_PHOTO:
                takePhoto();
                break;
        }
    }

    @Override
    public void onItemChoose(int position) {
        int currentPosition = fragmentNavigator.getCurrentPosition();
        switch (position) {
            case MainBottomBar.ITEM_MAP:
                if (currentPosition != MainBottomBar.ITEM_MAP){
                    setTab(MainBottomBar.ITEM_MAP);
                }else {
                    MapFragment mf = (MapFragment) fragmentNavigator.getCurrentFragment();
                    mf.refreshCurLocation();
                }
                break;
            case MainBottomBar.ITEM_ACTIVITY:
                if (currentPosition != MainBottomBar.ITEM_ACTIVITY) {
                    setTab(MainBottomBar.ITEM_ACTIVITY);
                } else {
                    DiscoverFragment df = (DiscoverFragment) fragmentNavigator.getCurrentFragment();
                    df.refresh();
                }
                break;
            case MainBottomBar.ITEM_MINE:
                if (currentPosition != MainBottomBar.ITEM_MINE) {
                    setTab(MainBottomBar.ITEM_MINE);
                } else {
                    MeFragment mf = (MeFragment) fragmentNavigator.getCurrentFragment();
                    mf.goToFirst();
                }
                break;
        }
    }

    /**
     * 获取融云token成功
     *
     * @param rongToken
     */
    @Override
    public void requestRongTokenSuccess(String rongToken) {
        Logger.e("融云token获取成功...");
        SPUtils.getInstance().put(Constants.RONG_TOKEN, rongToken);
        mPresenter.connectRong(rongToken);
    }

    /**
     * 融云连接成功
     */
    @Override
    public void connectRongSuccess() {
        Logger.e("融云连接成功...");
        if (userBean != null) {
            RongIM.getInstance().setCurrentUserInfo(new UserInfo(userBean.getId(), userBean.geteName(), Uri.parse(userBean.getE_HeadImg())));
            RongIM.getInstance().setMessageAttachedUserInfo(true);
        }
    }

    /**
     * 重新获取融云token
     */
    @Override
    public void onTokenIncorrect() {
        if (userBean != null) {
            mPresenter.requestRongToken(userBean);
        }
    }

    /**
     * 通知未读消息数量
     *
     * @param msgCount
     */
    @Override
    public void requestNoticeNumSuccess(int msgCount) {
        if (msgCount > 0) {
            setMineHint(false);
        } else {
            mPresenter.requestRongMsgNum();
        }
    }

    /**
     * 获取融云未读消息成功
     *
     * @param msgCount
     */
    @Override
    public void requestRongMsgNumSuccess(int msgCount) {
        if (msgCount > 0) {
            setMineHint(false);
        } else {
            setMineHint(true);
        }
    }

    @Override
    public void connectRongError(RongIMClient.ErrorCode errorCode) {
    }

    @Override
    public void showError(String errMsg) {
    }

}
