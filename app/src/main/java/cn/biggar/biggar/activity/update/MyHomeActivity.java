package cn.biggar.biggar.activity.update;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.activity.EditInfoActivity;
import cn.biggar.biggar.activity.ImageViewerActivity;
import cn.biggar.biggar.activity.LoginActivity;
import cn.biggar.biggar.adapter.PersonPhotoAdapter;
import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.bean.BgResponse;
import cn.biggar.biggar.bean.LabelBean;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.dialog.ShareDialog;
import cn.biggar.biggar.dialog.TipsDialog;
import cn.biggar.biggar.event.FollowEvent;
import cn.biggar.biggar.fragment.BiggarShowFragment;
import cn.biggar.biggar.helper.RongManager;
import cn.biggar.biggar.preference.AppPrefrences;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.presenter.CommonPresenter;
import cn.biggar.biggar.presenter.PersonUploadPhotoPerenter;
import cn.biggar.biggar.presenter.UpdataHeadImgPresenter;
import cn.biggar.biggar.presenter.UserDetailsPresenter;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.utils.Utils;
import cn.biggar.biggar.view.BottomView;
import cn.biggar.biggar.view.MyHorizontalListView;
import cn.biggar.biggar.view.NoScrollViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import cn.biggar.biggar.view.RootLayout;
import cn.biggar.biggar.view.scrollable.ScrollableLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;
import per.sue.gear2.dialog.GearCustomDialog;
import per.sue.gear2.presenter.OnObjectListener;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * update by Chenwy on 2017-06-01.
 * 个人主页
 */
public class MyHomeActivity extends BiggarActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks, UserDetailsPresenter.UserDetailsData, PersonUploadPhotoPerenter.PersonImagePublishView {
    Context mContext;
    @BindView(R.id.user_details_image)
    CircleImageView mUserImage;
    @BindView(R.id.person_home_viewpager)
    NoScrollViewPager mViewpager;
    @BindView(R.id.person_scrollable_vp)
    ScrollableLayout mPersonScrollable;
    @BindView(R.id.user_name_tv)
    TextView mUserName;
    @BindView(R.id.person_focus_num_tv)
    TextView mFocusNum;
    @BindView(R.id.tv_jyz)
    TextView tvJyz;
    @BindView(R.id.person_fans_num_tv)
    TextView mFansNum;
    @BindView(R.id.person_intro_tv)
    TextView mIntro;
    @BindView(R.id.buttomBar_01)
    BottomView buttomBar01;
    @BindView(R.id.buttomBar_02)
    BottomView mGuanZhu;
    @BindView(R.id.person_add_img_iv)
    ImageView mAddImg;
    @BindView(R.id.person_bg_iv)
    ImageView mPersonBg;
    @BindView(R.id.person_hlist_view)
    MyHorizontalListView mRecyclerView;
    @BindView(R.id.line1)
    View line1;
    @BindView(R.id.iv_v)
    ImageView ivV;
    @BindView(R.id.tv_tag)
    TextView tvTag;
    @BindView(R.id.ll_image_list)
    LinearLayout llImageList;

    private final int REQUEST_EDIT_DATA = 1000;
    private final int REQUEST_EDIT_BG = 1001;
    private final int REQUEST_EDIT_HEAD = 1002;
    private final int REQUEST_EDIT_IMAGE_LIST = 1003;

    private List<Fragment> fragments;
    private UserBean bean;
    private UserBean mUserBean;
    private String userId;
    private UserDetailsPresenter userDetailsPresenter;
    private String shareUrl;
    private CommonPresenter commonP;
    private PersonPhotoAdapter mAdapter;
    private NumberProgressBar numberProgressBar;
    private Dialog progressDialog;
    private PersonUploadPhotoPerenter personUploadPhotoPerenter;
    private UploadManager uploadManager;
    private UpdataHeadImgPresenter mHeadPresenter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_my_home;
    }

    /**
     * @param context
     * @param userId  userId
     * @return
     */
    public static Intent startIntent(Context context, String userId) {
        Intent intent = new Intent(context, MyHomeActivity.class);
        intent.putExtra("userid", userId);
        return intent;
    }


    public static Intent startIntent(Context context, String userId, int state) {
        Intent intent = new Intent(context, MyHomeActivity.class);
        intent.putExtra("userid", userId);
        intent.putExtra("state", state);//
        return intent;
    }

    int mState = 2;

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        line1.setAlpha(0.8f);
        viewInit();
        requestInit();
        initViewPager();
        initEvents();

    }


    private void requestInit() {
        showLoading();
        userId = getIntent().getStringExtra("userid");
        mState = getIntent().getIntExtra("state", 0);//“2”表情从商家端进来的
        userDetailsPresenter = new UserDetailsPresenter(getActivity(), this);
        mHeadPresenter = new UpdataHeadImgPresenter(getActivity());
        commonP = new CommonPresenter(this);
        UserBean userBean = Preferences.getUserBean(getActivity());
        if (userBean != null
                && userBean.getId() != null
                && userBean.getId().equals(userId)) {
            userDetailsPresenter.queryUserDetails(userBean.getId(), "");
        } else {
            userDetailsPresenter.queryUserDetails(userId, "");
        }
        mAdapter = new PersonPhotoAdapter(mContext);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<String> data = (ArrayList<String>) mAdapter.getData();
                startActivity(ImageViewerActivity.startIntent(mContext, data, i, mIsOwn));
                overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
            }
        });
        personUploadPhotoPerenter = new PersonUploadPhotoPerenter(mContext, this);
    }

    private void dataInit(UserBean bean) {
        RootLayout.getInstance(this).setTitle(TextUtils.isEmpty(bean.geteName()) ? "比格用户" : bean
                .geteName());
        shareUrl = bean.getE_SharePersonal() + bean.getId();
        mUserName.setText(TextUtils.isEmpty(bean.geteName()) ? "" : bean.geteName());
        mFocusNum.setText(TextUtils.isEmpty(bean.getE_Concern_ta()) ? "0" : bean.getE_Concern_ta());
        tvJyz.setText(bean.getE_Experience() == null ? "0" : bean.getE_Experience());
        mFansNum.setText(TextUtils.isEmpty(bean.getE_Concern_me()) ? "0" : bean.getE_Concern_me());
        mIntro.setText(TextUtils.isEmpty(bean.getE_Signature()) ? "ta很神秘，什么都还没有说！" : bean.getE_Signature());//简介
        tvTag.setText(TextUtils.isEmpty(bean.getE_Worker()) ? "" : bean.getE_Worker());
        Glide.with(mContext).load(Utils.getRealUrl(bean.getE_HeadImg())).apply(new RequestOptions().centerCrop()).into(mUserImage);
        mAdapter.setData(bean.getE_Albumimg());
        mPathSize = bean.getE_Albumimg().size();
        if (!mIsOwn && mPathSize == 0) {
            llImageList.setVisibility(View.GONE);
        } else {
            llImageList.setVisibility(View.VISIBLE);
        }
        Glide.with(mContext).load(Utils.getRealUrl(bean.getE_BGImg())).apply(new RequestOptions().centerCrop()).into(mPersonBg);
        //没有认证
        if (bean.getE_VerWorker().equals("0")) {
            ivV.setVisibility(View.GONE);
        }
    }

    private void initEvents() {
        mViewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mPersonScrollable.getHelper().setCurrentScrollableContainer((BiggarShowFragment) fragments.get(position));
            }
        });

        RootLayout.getInstance(this).setOnRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bean != null) {
                    share(bean.geteName(), TextUtils.isEmpty(bean.getPrice()) ? "0" : bean.getPrice(),
                            TextUtils.isEmpty(bean.getCount()) ? "0" : bean.getCount(), bean.getE_HeadImg(), shareUrl);
                }
            }
        });
    }

    /**
     * fragment初始化
     */
    private void initViewPager() {
        fragments = new ArrayList<>();
        fragments.add(BiggarShowFragment.getInstance(userId));
        mViewpager.setAdapter(new PersonPagerAdapter(getSupportFragmentManager()));
        mPersonScrollable.getHelper().setCurrentScrollableContainer((BiggarShowFragment) fragments.get(0));
    }

    /**
     * 试图初始化
     */
    private void viewInit() {
        mContext = this;
        buttomBar01.setItemSelect(false);
        mGuanZhu.setItemSelect(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        registerBoradcastReceiver();
    }

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Constants.KEY_DELETE_IMGE);
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    boolean mIsOwn = false;

    private void initData() {
        uploadManager = new UploadManager();
        mUserBean = Preferences.getUserBean(getApplication());
        if (mUserBean != null) {
            if (userId.equals(mUserBean.getId())) {
                mIsOwn = true;
                if (mUserBean.getE_Albumimg().size() < IMAGE_MAX_SIZE) {
                    mAddImg.setVisibility(View.VISIBLE);
                } else {
                    mAddImg.setVisibility(View.GONE);
                }
                buttomBar01.setmIconDefault(R.mipmap.return_white);
                buttomBar01.setmIconSelect(R.mipmap.return_white);
                mGuanZhu.setmIconDefault(R.mipmap.data_3x);
                mGuanZhu.setmIconSelect(R.mipmap.data_3x);
                buttomBar01.setItemSelect(false);
                mGuanZhu.setItemSelect(false);
                buttomBar01.setText("分享主页");
                mGuanZhu.setText("我的资料");
                mFocusNum.setText(TextUtils.isEmpty(mUserBean.getE_Concern_ta()) ? "0" : mUserBean.getE_Concern_ta());
            } else {
                mIsOwn = false;
                checkConcern(userId, 1, mUserBean.getId(), "ME");
                mAddImg.setVisibility(View.GONE);
            }
        } else {
            mAddImg.setVisibility(View.GONE);
        }
    }

    boolean concernSuccess = false;//是否关注成功

    /**
     * 检查是否关注
     */
    private void checkConcern(String id, int type, String userId, String checkType) {
        commonP.checkConcern(id, type, userId, checkType, new OnObjectListener<String>() {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                mGuanZhu.setText("取消关注");
                mGuanZhu.setTag(true);
                mGuanZhu.setItemSelect(true);
                concernSuccess = true;
            }
        });
    }

    /**
     * 关注
     */
    private void concern(String id, String nick, int type, String userId, String username) {
        commonP.concern(id, nick, type, userId, username, new OnObjectListener<String>() {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                ToastUtils.showNormal(result);
                mGuanZhu.setText("取消关注");
                concernSuccess = true;
                mGuanZhu.setTag(true);
                mGuanZhu.setItemSelect(true);
                updateConcernNumber(true);
                EventBus.getDefault().post(new FollowEvent(true));
            }

            @Override
            public void onError(int code, String msg) {
                super.onError(code, msg);
                ToastUtils.showError(getString(R.string.str_operation_error));
            }
        });
    }

    /**
     * 更新 关注人数
     *
     * @param state ture+  false -
     */
    private void updateConcernNumber(boolean state) {
        if (AppPrefrences.getInstance(getActivity()).isLogined()) {
            UserBean mUserData = Preferences.getUserBean(this);
            int count = Utils.parseInt(mUserData.getE_Concern_ta());
            if (state) {
                count++;
            } else {
                count--;
                count = count < 0 ? 0 : count;
            }
            mUserData.setE_Concern_ta(count + "");
            Preferences.storeUserBean(getActivity(), mUserData);
        }
    }

    /**
     * 分享
     *
     * @param title
     * @param text
     * @param imageUrl
     * @param targetUrl
     */
    public void share(String name, String title, String text, String imageUrl, String targetUrl) {
        String mName;
        if (mUserBean != null && mUserBean.getId().equals(bean.getId())) {
            mName = "我";
        } else {
            mName = name;
        }
        String mTitle = mName + "在比格秀的达人价值是" + title + "元，关注" + mName + "，一起秀出你的价值";
        new ShareDialog().show(getSupportFragmentManager(),mTitle, mTitle, bean.getE_BGImg(), targetUrl, ShareDialog.SHARE_TYPE_2, "",new UMShareListener() {
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

    /**
     * 取消关注
     *
     * @param uid
     * @param oid
     * @param type
     */
    public void cancelConcern(String uid, String oid, final int type) {
        commonP.cancelConcern(uid, oid, type, new OnObjectListener<String>() {
            @Override
            public void onSuccessRequest(int request, String result) {
                super.onSuccessRequest(request, result);
                ToastUtils.showNormal("取消成功");
                mGuanZhu.setText("关注");
                mGuanZhu.setItemSelect(false);
                concernSuccess = false;
                updateConcernNumber(false);
                EventBus.getDefault().post(new FollowEvent(false));
            }

            @Override
            public void onError(int code, String msg) {
                super.onError(code, msg);
                ToastUtils.showError(type == 1 ? "取消关注失败" : "取消收藏失败");
            }
        });
    }

    @OnClick({R.id.user_details_image, R.id.buttomBar_01, R.id.buttomBar_02, R.id.person_add_img_iv,
            R.id.business_home_new_order_lv, R.id.business_home_new_fen_lv, R.id.ll_jyz, R.id.person_bg_iv})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //头像
            case R.id.user_details_image:
                if (mIsOwn) {
                    mOnClickImagestate = 0;
                    record();
                } else {
                    mContext.startActivity(EditInfoActivity.startIntent(mContext, EditInfoActivity.MODE_SEE_INFO, userId));
                }

                break;
            case R.id.buttomBar_01:

                if (mIsOwn) {
                    if (bean != null) {
                        share(bean.geteName(), TextUtils.isEmpty(bean.getPrice()) ? "0" : bean.getPrice(),
                                TextUtils.isEmpty(bean.getCount()) ? "0" : bean.getCount(), bean.getE_HeadImg(), shareUrl);
                    }
                }
                //聊天
                else {
                    if (mUserBean == null) {
                        startActivity(LoginActivity.startIntent(this));
                        return;
                    }
                    RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                        @Override
                        public UserInfo getUserInfo(String s) {
                            return new UserInfo(bean.getId(), bean.geteName(), Uri.parse(bean.getE_HeadImg()));
                        }
                    }, true);
                    RongManager.getInstance().startPrivateChat(userId, bean.geteName(),"1");
                }
                break;
            case R.id.buttomBar_02:
                if (mIsOwn) {
                    startActivityForResult(EditInfoActivity.startIntent(mContext), REQUEST_EDIT_DATA);
                } else {
                    if (mUserBean == null) {
                        startActivity(LoginActivity.startIntent(getActivity()));
                    } else {
                        if (bean == null) {
                            ToastUtils.showError("请检查一下网络");
                        } else {
                            if (concernSuccess) {
                                TipsDialog.newInstance("您确定不再关注" + bean.geteName() + "了吗？").setOnTipsOnClickListener(new TipsDialog.OnTipsOnClickListener() {
                                    @Override
                                    public void onSure() {
                                        cancelConcern(mUserBean.getId(), bean.getId(), 1);
                                    }

                                    @Override
                                    public void onCancel() {
                                    }
                                }).setMargin(52).show(getSupportFragmentManager());
                            } else {
                                concern(bean.getId(), bean.geteName(), 1, mUserBean.getId(), mUserBean.geteName());
                            }
                        }
                    }
                }
                break;
            //图片集
            case R.id.person_add_img_iv:
                mOnClickImagestate = 1;
                record();
                break;
            //关注
            case R.id.business_home_new_order_lv:
                if (bean != null) {
//                    startActivity(MyFocusActivity.startIntent(this, bean.getId()));
                    startActivity(FollowActivity.startIntent(this, bean.getId()));
                }
                break;
            //粉丝
            case R.id.business_home_new_fen_lv:
                if (bean != null) {
                    startActivity(FansActivity.startIntent(this, bean.getId()));
                }
                break;
            //经验值
            case R.id.ll_jyz:
                if (bean != null) {
                    startActivity(new Intent(this, MyXingzhiActivity.class)
                            .putExtra("uid", bean.getId())
                            .putExtra("xz", bean.getE_Experience()));
                }
                break;
            //背景图片
            case R.id.person_bg_iv:
                if (mIsOwn) {
                    mOnClickImagestate = 2;
                    record();
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void UserDetailsInfo(ArrayList<UserBean> data) {
        dismissLoading();
        for (UserBean userDetailsBean : data) {
            bean = userDetailsBean;
            dataInit(bean);
        }
    }


    @Override
    public void UserLebelInfo(ArrayList<LabelBean> data) {

    }

    protected static final int RC_CAMERA_PERM = 123;
    private int mOnClickImagestate;


    @AfterPermissionGranted(RC_CAMERA_PERM)
    public void record() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            //头像
            if (mOnClickImagestate == 0) {
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())
                        .theme(R.style.picture_Sina_style)
                        .imageSpanCount(3)
                        .enableCrop(true)
                        .withAspectRatio(1, 1)
                        .circleDimmedLayer(true)
                        .showCropFrame(false)
                        .showCropGrid(false)
                        .compress(true)
                        .selectionMode(PictureConfig.SINGLE)
                        .previewImage(true)
                        .forResult(REQUEST_EDIT_HEAD);
            }
            //图片集
            else if (mOnClickImagestate == 1) {
                openImageList();
            }
            //背景
            else if (mOnClickImagestate == 2) {
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())
                        .theme(R.style.picture_Sina_style)
                        .imageSpanCount(3)
                        .compress(true)
                        .selectionMode(PictureConfig.SINGLE)
                        .previewImage(true)
                        .forResult(REQUEST_EDIT_BG);
            }
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_camera),
                    RC_CAMERA_PERM, perms);
        }
    }

    private final int IMAGE_MAX_SIZE = 20;
    private int mPathSize;

    /**
     * 图片集
     */
    private void openImageList() {
        int count = IMAGE_MAX_SIZE - mPathSize;
        if (count <= 0) {
            ToastUtils.showError("抱歉，最多只能上传20张");
            return;
        }

        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .theme(R.style.picture_Sina_style)
                .maxSelectNum(count)
                .minSelectNum(1)
                .imageSpanCount(3)
                .compress(true)
                .selectionMode(PictureConfig.MULTIPLE)
                .previewImage(true)
                .forResult(REQUEST_EDIT_IMAGE_LIST);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void uploadProgress(double percent) {
        if (null != numberProgressBar) {
            int progress = (int) (percent * 100);
            numberProgressBar.setProgress(progress);
        }
    }

    private TextView progressDialogDescribeTV;

    @Override
    public void showProgressDialog(String tip) {
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
    protected void dismissProgressDialog() {
        if (null != progressDialog) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void prepareUpload(String message) {
        showProgressDialog(message);
        numberProgressBar.setProgress(0);
    }

    @Override
    public void returnPath(List<String> paths) {
        List<String> returnPath = mUserBean.getE_Albumimg();
        for (int i = 0; i < paths.size(); i++) {
            returnPath.add(paths.get(i));
        }
        mUserBean.setE_Albumimg(returnPath);
        Preferences.storeUserBean(mContext, mUserBean);
        if (mUserBean.getE_Albumimg().size() >= 20) {
            mAddImg.setVisibility(View.GONE);
        }
        mAdapter.addData(paths);
        mAdapter.notifyDataSetChanged();
        mPathSize = mAdapter.getCount();
    }

    @Override
    public void onCompleted() {
        super.onCompleted();
        dismissProgressDialog();
    }

    @Override
    public void onSuccess(String msg) {
        ToastUtils.showNormal(msg);
    }

    public class PersonPagerAdapter extends FragmentStatePagerAdapter {
        public PersonPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    ArrayList<String> uploadPath;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //编辑资料返回
            if (requestCode == REQUEST_EDIT_DATA) {
                UserBean bean = (UserBean) data.getSerializableExtra("user");
                dataInit(bean);
            }
            //图片集合
            else if (requestCode == REQUEST_EDIT_IMAGE_LIST) {
                uploadPath = new ArrayList<>();
                if (data != null) {
                    List<LocalMedia> localMedias = PictureSelector.obtainMultipleResult(data);
                    for (LocalMedia localMedia : localMedias) {
                        uploadPath.add(0, localMedia.getCompressPath());
                    }
                    personUploadPhotoPerenter.publishImage(userId, uploadPath);
                }
            }
            //背景
            else if (requestCode == REQUEST_EDIT_BG) {
                List<LocalMedia> localMedias = PictureSelector.obtainMultipleResult(data);
                if (localMedias.size() > 0) {
                    showLoading();
                    String photo = localMedias.get(0).getCompressPath();
                    File file = new File(photo);
                    uploadBg(file);
                }
            }
            //头像
            else {
                List<LocalMedia> localMedias = PictureSelector.obtainMultipleResult(data);
                if (localMedias.size() > 0) {
                    showLoading();
                    mHeadPresenter.uploadHeadFiles(new File(localMedias.get(0).getCompressPath()), false, new UpdataHeadImgPresenter.UpdataHeadImg() {
                        @Override
                        public void onUpDataSucess(String url, String s) {
                            try {
                                ToastUtils.showNormal(s);
                                UserBean bean = Preferences.getUserBean(mContext);
                                bean.setE_HeadImg(url);
                                Preferences.storeUserBean(mContext, bean);
                                Glide.with(mContext).load(Utils.getRealUrl(url)).apply(new RequestOptions().centerCrop()).into(mUserImage);
                            } catch (Exception e) {
                            }
                            dismissLoading();
                        }

                        @Override
                        public void onError(int code, String message) {
                            ToastUtils.showError(message);
                            dismissLoading();
                        }
                    }, Constants.KEY_USER_HEAD);
                }
            }
        }
    }


    /**
     * 上传背景图片
     */
    private void uploadBg(final File path) {
        if (!path.exists()) {
            dismissLoading();
            ToastUtils.showError("文件不存在");
            return;
        }

        OkGo.<String>get(BaseAPI.QINIU_TOKEN)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String s = response.body();
                        try {
                            JSONObject j = new JSONObject(s);
                            String token = j.getString("token");
                            String expectKey = UUID.randomUUID().toString();
                            uploadManager.put(path, expectKey, token, new UpCompletionHandler() {
                                @Override
                                public void complete(String key, ResponseInfo info, JSONObject response) {
                                    try {
                                        dismissLoading();
                                        if (!info.isOK()) {
                                            ToastUtils.showError("上传失败");
                                            return;
                                        }
                                        String path = Utils.getQiniuUrl(response.getString("key"));
                                        submitBgToServer(path);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, null);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 提交背景图片到服务器
     *
     * @param bgUrl
     */
    private void submitBgToServer(final String bgUrl) {
        final String url = BaseAPI.API_ACCOUNT_UPDATE_AVART;
        OkGo.<String>post(url)
                .params("MemberID", userId)
                .params("HeadImgUrl", bgUrl)
                .params("type", "BG")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        dismissLoading();
                        String s = response.body();
                        BgResponse bgResponse = new Gson().fromJson(s, BgResponse.class);
                        int status = bgResponse.status;
                        if (status == 1) {
                            Glide.with(mContext).load(Utils.getRealUrl(bgUrl)).apply(new RequestOptions().centerCrop()).into(mPersonBg);
                            UserBean bean = Preferences.getUserBean(mContext);
                            bean.setE_BGImg(url);
                            Preferences.storeUserBean(mContext, bean);
                        }
                        String info = (String) bgResponse.info;
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        dismissLoading();
                        ToastUtils.showError("背景更新失败,请检查网络");
                    }
                });
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.KEY_DELETE_IMGE)) {
                ArrayList<String> paths = intent.getStringArrayListExtra("urls");

                List<String> returnPath = mUserBean.getE_Albumimg();
                returnPath.clear();
                for (int i = 0; i < paths.size(); i++) {
                    returnPath.add(paths.get(i));
                }
                mUserBean.setE_Albumimg(returnPath);
                Preferences.storeUserBean(mContext, mUserBean);

                mAdapter.setData(paths);
                mAdapter.notifyDataSetChanged();
                mPathSize = mAdapter.getCount();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }
}
