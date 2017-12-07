package cn.biggar.biggar.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.activity.update.MyHomeActivity;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.bean.LabelBean;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.dialog.DateSelectorDialog;
import cn.biggar.biggar.dialog.EditInputDialog;
import cn.biggar.biggar.dialog.SelectorAddressDialog;
import cn.biggar.biggar.dialog.SingleRollerSelectorDialog;
import cn.biggar.biggar.preference.AppPrefrences;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.presenter.UpdataHeadImgPresenter;
import cn.biggar.biggar.presenter.UserDetailsPresenter;
import cn.biggar.biggar.presenter.UserPersenter;
import cn.biggar.biggar.utils.TimeTransform;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.utils.Utils;
import cn.biggar.biggar.view.MultiStateView;

import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.biggar.biggar.view.RootLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import per.sue.gear2.presenter.OnObjectListener;
import per.sue.gear2.utils.UnitUtils;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by mx on 2016/8/29.
 * 个人资料 编辑
 */
public class EditInfoActivity extends BiggarActivity implements UserDetailsPresenter.UserDetailsData, EasyPermissions.PermissionCallbacks {

    public static final int MODE_EDIT_INFO = 0;//修改资料
    public static final int MODE_SEE_INFO = 1;//查看资料
    public static final int MODE_TALENT = 2;//认证达人


    @BindView(R.id.edit_v_iv)
    ImageView mIvV;
    @BindView(R.id.edit_msv)
    MultiStateView mMSV;
    @BindView(R.id.edit_header_iv)
    CircleImageView mIvHeader;
    @BindView(R.id.edit_nick_tv)
    TextView mTvNick;
    @BindView(R.id.edit_idiograph_tv)
    TextView mTvIdiograph;
    @BindView(R.id.edit_gender_tv)
    TextView mTvGender;

    @BindView(R.id.edit_birthday_tv)
    TextView mTvBirthday;
    @BindView(R.id.edit_emotional_tv)
    TextView mTvEmotional;
    @BindView(R.id.edit_height_tv)
    TextView mTvHeight;
    @BindView(R.id.edit_diploma_tv)
    TextView mTvDiploma;
    @BindView(R.id.edit_hometown_tv)
    TextView mTvHometown;
    @BindView(R.id.edit_address_tv)
    TextView mTvAddress;
    @BindView(R.id.edit_constellation_tv)
    TextView mTvConstellation;
    @BindView(R.id.edit_wechat_tv2)
    TextView mTvWechat;
    @BindView(R.id.edit_weibo_tv)
    TextView mTvWeibo;
    @BindView(R.id.edit_live_platform_tv)
    TextView mTvLivePlatform;
    @BindView(R.id.edit_certification_tv)
    TextView mTvCI;
    @BindView(R.id.edit_next_step_tv)
    TextView mTvNextStep;
    @BindView(R.id.rl_next_step)
    RelativeLayout rlNextStep;
    @BindView(R.id.edit_wechat_left_iv)
    ImageView mIvWeChatLeft;


    private HashMap<String, String> mParameter;
    private UserBean mUser, mUserSource;//mUserSource用于 修改手机号本地缓存的更新
    private int mSelectIndex;
    private int mMode;//模式
    private String mUserId;

    private SingleRollerSelectorDialog mDialogSRS;//选择器
    private DateSelectorDialog mDialogDate;//时间选择器

    private UserPersenter mUserP;
    private UserDetailsPresenter mUserPresenter;
    private UpdataHeadImgPresenter mHeaderP;
    private TimePickerView pvTime;
    private SelectorAddressDialog dialog;

    private final int REQUEST_EDIT_HEAD = 0x263;
    private final int RC_CAMERA_PERM = 123;

    public static Intent startIntent(Context context) {
        return EditInfoActivity.startIntent(context, MODE_EDIT_INFO);
    }

    /**
     * mode==MODE_EDIT_INFO||mode==MODE_TALENT  可以不传 userID
     *
     * @param context
     * @param mode
     * @return
     */
    public static Intent startIntent(Context context, int mode) {
        String userId = null;
        if (mode == MODE_EDIT_INFO || mode == MODE_TALENT) {
            if (!AppPrefrences.getInstance(context).isLogined()) {
                return LoginActivity.startIntent(context);
            }
            UserBean user = AppPrefrences.getUserBean(context);
            userId = user == null ? "" : user.getId();
        }
        return EditInfoActivity.startIntent(context, mode, userId);
    }

    public static Intent startIntent(Context context, int mode, String userId) {
        Intent intent;
        if ((mode == MODE_EDIT_INFO || mode == MODE_TALENT) && !AppPrefrences.getInstance(context).isLogined()) {
            return LoginActivity.startIntent(context);
        }
        intent = new Intent(context, EditInfoActivity.class);
        intent.putExtra("mode", mode);
        intent.putExtra("userId", userId);
        return intent;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_edit_info;
    }


    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        if (getIntent() != null) {
            mMode = getIntent().getIntExtra("mode", MODE_EDIT_INFO);
            mUserId = getIntent().getStringExtra("userId");
        }

        if (TextUtils.isEmpty(mUserId)) {
            ToastUtils.showError("数据异常！");
            finish();
            return;
        }

        initView();
        initData();
        initEvent();

    }

    private void initView() {
        switch (mMode) {
            case MODE_EDIT_INFO:
                initMode1();
                break;
            case MODE_SEE_INFO:
                initMode2();
                break;
            case MODE_TALENT:
                initMode3();
                break;
        }
    }

    private void initData() {
        dialog = new SelectorAddressDialog(this);
        mParameter = new HashMap<>();

        mUserPresenter = new UserDetailsPresenter(this, this);
        mUserP = new UserPersenter(this);

        mHeaderP = new UpdataHeadImgPresenter(getActivity());

        if (mMode == MODE_EDIT_INFO || mMode == MODE_TALENT) {
            if (AppPrefrences.getInstance(getActivity()).isLogined()) {
                mUser = AppPrefrences.getUserBean(this);
                mUserSource = (UserBean) mUser.clone();
                loadData();
            } else {
                ToastUtils.showError("数据异常！");
                finish();
            }
        } else {
            mUserPresenter.queryUserDetails(mUserId, "");
        }
    }

    private void initEvent() {
        mMSV.setOnMultiStateViewClickListener(new MultiStateView.OnMultiStateViewClickListener() {
            @Override
            public void onRetry(int Type) {
                mUserPresenter.queryUserDetails(mUserId, "");
            }
        });
    }

    /**
     * 修改自己资料
     */
    private void initMode1() {
        RootLayout.getInstance(this).setTitle("我的资料").setRightTitle("完成").setOnRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit(true);
            }
        });
        findViewById(R.id.edit_talent_progress_layout).setVisibility(View.GONE);
        findViewById(R.id.ll_top_part).setVisibility(View.GONE);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) findViewById(R.id.edit_header_layout).getLayoutParams();
        layoutParams.height = UnitUtils.dip2px_new(this, 97);
        mTvNextStep.setVisibility(View.GONE);
        rlNextStep.setVisibility(View.GONE);

        mIvWeChatLeft.setImageResource(R.mipmap.sale);
    }

    /**
     * 查看别人资料
     */
    private void initMode2() {
        RootLayout.getInstance(this).setTitle("个人资料");
        mTvNextStep.setVisibility(View.GONE);
        rlNextStep.setVisibility(View.GONE);
        mMSV.setViewState(MultiStateView.VIEW_STATE_LOADING);
        findViewById(R.id.ll_top_part).setVisibility(View.GONE);
        findViewById(R.id.edit_talent_progress_layout).setVisibility(View.GONE);
        findViewById(R.id.edit_userinfo_1_layout).setVisibility(View.GONE);

        for (int i = 0; i <= 15; i++) {
            int resId = getResources().getIdentifier("edit_arrow_" + i + "_iv", "id", getPackageName());
            try {
                findViewById(resId).setVisibility(View.GONE);
            } catch (Exception e) {
            }
        }
    }

    /**
     * 申请比格达人认证
     */
    private void initMode3() {

        RootLayout.getInstance(this).setTitle("申请认证比格达人").setRightTitle("完成").setOnRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit(false);
            }
        });

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) findViewById(R.id.edit_header_layout).getLayoutParams();
        layoutParams.height = UnitUtils.dip2px_new(this, 127);
        mTvNextStep.setVisibility(View.VISIBLE);
        rlNextStep.setVisibility(View.VISIBLE);

        findViewById(R.id.edit_talent_progress_layout).setVisibility(View.VISIBLE);
        mIvWeChatLeft.setImageResource(R.mipmap.sale);
    }


    private void loadData() {
        if (mUser == null) return;
        initDatePicker();
        mTvHometown.setText(TextUtils.isEmpty(mUser.getE_FamilyAdd()) ? "" : mUser.getE_FamilyAdd());
        mTvAddress.setText(TextUtils.isEmpty(mUser.getE_Adress()) ? "" : mUser.getE_Adress());
        mTvNick.setText(TextUtils.isEmpty(mUser.geteName()) ? "" : mUser.geteName());

        mTvGender.setText(TextUtils.isEmpty(mUser.getE_Sex()) ? "" : mUser.getE_Sex());
        try {
            Date date = TimeTransform.ConverToDateByYMDHMS(TextUtils.isEmpty(mUser.getE_Birthday()) ? "" : mUser.getE_Birthday());
            mTvBirthday.setText(TextUtils.isEmpty(mUser.getE_Birthday()) ? "" : TimeTransform.ConverToStringForYMD(date));
        } catch (Exception e) {
            mTvBirthday.setText(TextUtils.isEmpty(mUser.getE_Birthday()) ? "" : mUser.getE_Birthday());
        }
        mTvIdiograph.setText(TextUtils.isEmpty(mUser.getE_Signature()) ? "" : mUser.getE_Signature());
        mTvEmotional.setText(TextUtils.isEmpty(mUser.getE_Marriage()) ? "" : mUser.getE_Marriage());
        mTvDiploma.setText(TextUtils.isEmpty(mUser.getE_Education()) ? "" : mUser.getE_Education());
        mTvHeight.setText(TextUtils.isEmpty(mUser.getE_Height()) ? "" : mUser.getE_Height());
        Glide.with(getActivity()).load(Utils.getRealUrl(mUser.getE_HeadImg()))
                .apply(new RequestOptions().centerCrop())
                .into(mIvHeader);
        mTvWeibo.setText(TextUtils.isEmpty(mUser.getE_blog()) ? "" : mUser.getE_blog());
        mTvWechat.setText(TextUtils.isEmpty(mUser.getE_Weixin()) ? "" : mUser.getE_Weixin());
        mTvLivePlatform.setText(TextUtils.isEmpty(mUser.getE_platform()) ? "" : mUser.getE_platform());
        mTvConstellation.setText(TextUtils.isEmpty(mUser.getE_Constellation()) ? "" : mUser.getE_Constellation());
        if (isCertification()) {
            mTvCI.setText(TextUtils.isEmpty(mUser.getE_Worker()) ? "" : mUser.getE_Worker());
            mIvV.setVisibility(View.VISIBLE);
        } else {
            if (mMode != MODE_TALENT) {
                mTvCI.setText("*尚未认证");
                mIvV.setVisibility(View.GONE);
            } else {
                mTvCI.setText("");
            }
        }
    }

    /**
     * 是否认证
     *
     * @return
     */
    private boolean isCertification() {
        if (mUser == null) return false;
        if (!TextUtils.isEmpty(mUser.getE_VerWorker())) {
            if (mUser.getE_VerWorker().equals("1")) {
                return true;
            }
        }
        return false;
    }

    /**
     * show 选择器
     */
    private void showSelectorDialog(List<String> caches, String rightStr) {
        if (mDialogSRS == null) {
            mDialogSRS = new SingleRollerSelectorDialog(this);
            mDialogSRS.setOnCallbcak(new SingleRollerSelectorDialog.Callback() {
                @Override
                public void callback(String str) {
                    switch (mSelectIndex) {
                        case 1:
                            mParameter.put("E_Education", str);
                            mUser.setE_Education(str);
                            mTvDiploma.setText(str);
                            break;
                        case 2:
                            mParameter.put("E_Marriage", str);
                            mUser.setE_Marriage(str);
                            mTvEmotional.setText(str);
                            break;
                        case 3:
                            mParameter.put("E_Sex", str);
                            mUser.setE_Sex(str);
                            mTvGender.setText(str);
                            break;
                        case 4:
                            mParameter.put("E_Height", str);
                            mUser.setE_Height(str);
                            mTvHeight.setText(str);
                            break;
                        case 5:
                            mParameter.put("E_Constellation", str);
                            mUser.setE_Constellation(str);
                            mTvConstellation.setText(str);
                            break;
                    }
                    checkHasEmpty(false);
                }
            });
        }
        mDialogSRS.setDate(caches);
        mDialogSRS.setRightText(rightStr);
        mDialogSRS.show();
    }

    /**
     * 生日选择
     */
    private void showDateSelectorDialog() {
        if (mDialogDate == null) {
            mDialogDate = new DateSelectorDialog(this);
            mDialogDate.setOnCallbcak(new DateSelectorDialog.Callback() {
                @Override
                public void callback(Date data, String str) {
                    mTvBirthday.setText(str);
                    mParameter.put("E_Birthday", str);
                    mUser.setE_Birthday(str);
                    checkHasEmpty(false);
                }
            });
        }
        mDialogDate.setInitDate(TextUtils.isEmpty(mUser.getE_Birthday()) ? "" : mUser.getE_Birthday());
        mDialogDate.show();
    }

    /**
     * 初始化日期选择器
     */
    private void initDatePicker() {
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
//        Calendar selectedDate = Calendar.getInstance();
//        Calendar startDate = Calendar.getInstance();
//        startDate.set(1970,1,1);
//        Calendar endDate = Calendar.getInstance();
//        endDate.set(2100,1,1);

        Date date = null;
        if (mUser != null && mUser.getE_Birthday() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                date = sdf.parse(mUser.getE_Birthday());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            date = new Date();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        //时间选择器
        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String dateStr = format.format(date);
                mTvBirthday.setText(dateStr);
                mParameter.put("E_Birthday", dateStr);
                mUser.setE_Birthday(dateStr);
                checkHasEmpty(false);
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("", "", "", "", "", "") //设置空字符串以隐藏单位提示   hide label
                .setDividerColor(Color.DKGRAY)
                .setContentSize(20)
                .setDate(calendar)
                .setSubmitColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setCancelColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setTextColorCenter(0xffff8e3b)
                .setDividerColor(0xffd3d3d3)
                .build();
    }

    /**
     * 弹出日期选择器
     *
     * @param v
     */
    private void showDateSelectorDialog2(View v) {
        if (pvTime != null) {
            pvTime.show(v);
        }
    }


    /**
     * 地址
     *
     * @param type true: 家乡 false 现住址
     */
    private void showAddressDialog(final boolean type) {

        dialog.setListener(new SelectorAddressDialog.CallBackListener() {
            @Override
            public void callBack(String data, String prov, String city, String dist) {
                if (type) {
                    mTvHometown.setText(data);
                    mParameter.put("E_FamilyAdd", data);
                    mUser.setE_FamilyAdd(data);
                } else {
                    mTvAddress.setText(data);
                    mParameter.put("E_Adress", data);
                    mUser.setE_Adress(data);
                }
                checkHasEmpty(false);
            }
        });

        dialog.showDialog();
    }

    /**
     * 文本 输入dialog
     *
     * @param title
     * @param def
     * @param hint
     */
    private void showEditInputDialog(String title, String def, String hint, final int type, int maxLength) {
        final EditInputDialog dialog = new EditInputDialog(this);
        dialog.setOnCallbcak(new EditInputDialog.Callback() {
            @Override
            public void callback(String str) {
                switch (type) {
                    case 1://昵称
                        mTvNick.setText(str);
                        mParameter.put("E_Name", str);
                        mUser.setE_Name(str);
                        break;
                    case 2://微博
                        mTvWeibo.setText(str);
                        mParameter.put("E_blog", str);
                        mUser.setE_blog(str);
                        break;
                    case 3://直播平台
                        mTvLivePlatform.setText(str);
                        mParameter.put("E_platform", str);
                        mUser.setE_platform(str);
                        break;
                    case 4://微信
                        mTvWechat.setText(str);
                        mParameter.put("E_Weixin", str);
                        mUser.setE_Weixin(str);
                        break;
                }
                checkHasEmpty(false);
                dialog.dismiss();
            }
        });
        dialog.setMaxLength(maxLength);
        dialog.setTitle(title);
        dialog.setDefaultData(def, hint);
        dialog.show();
    }

    /**
     * 提交
     */
    private void submit(final boolean isShowToast) {
        if (mParameter.isEmpty()) {

            if (mMode == MODE_EDIT_INFO) {

                if (mUrl != null) {
                    mUser.setE_HeadImg(mUrl);
                    startResult();
                }
                finish();
            } else if (mMode == MODE_TALENT) {
                startActivityForResult(ApplyForTalentActivity.getInstance(EditInfoActivity.this, ApplyForTalentActivity.MODE_AFT_2, mUserId, mTvCI.getText().toString()), 6);
            }
            return;
        }

        showLoading();
        mUserP.updateUserInfo(mUser.getId(), mParameter, new OnObjectListener<String>() {
            @Override
            public void onError(int code, String msg) {
                super.onError(code, msg);
                ToastUtils.showError("修改失败");
                dismissLoading();
            }

            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                if (isShowToast) {
                    ToastUtils.showNormal("修改成功");
                }

                startResult();
                mParameter.clear();
                dismissLoading();

                if (mMode == MODE_EDIT_INFO) {
                    finish();
                } else if (mMode == MODE_TALENT) {
                    startActivityForResult(ApplyForTalentActivity.getInstance(EditInfoActivity.this, ApplyForTalentActivity.MODE_AFT_2, mUserId, mTvCI.getText().toString()), 6);
                }
            }
        });
    }

    public void startResult() {
        Preferences.storeUserBean(getApplication(), mUser);
        Intent intent = new Intent(getActivity(), MyHomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", mUser);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
    }

    String mUrl;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case 1:
                    //个性签名
                    mTvIdiograph.setText(data.getStringExtra("result"));
                    mParameter.put("E_Signature", data.getStringExtra("result"));
                    mUser.setE_Signature(data.getStringExtra("result"));
                    break;
                case 2:
                    //手机号码
                    String phone = data.getStringExtra("phone");
                    if (!TextUtils.isEmpty(phone)) {
                        mUser.setE_Mobile(phone);
                        mUserSource.setE_Mobile(phone);
                        Preferences.storeUserBean(getActivity(), mUserSource);
                    }
                    break;
                case 5:
                    //认证标签选取
                    String label = data.getStringExtra("label");
                    mTvCI.setText(label);
                    break;
                case 6://申请成功
                    mTvCI.setText("");
                    mMode = MODE_EDIT_INFO;
                    initView();
                    break;
                case 7://编辑微信
                    String account = data.getStringExtra("account");
                    String price = data.getStringExtra("price");
                    mParameter.put("E_Weixin", account);
                    mParameter.put("E_Wechatprice", price);
                    mUser.setE_Weixin(account);
                    mUser.setE_Wechatprice(price);
                    mTvWechat.setText(account);
                    break;
                case 8://添加微信
                    boolean result = data.getBooleanExtra("payResult", false);
                    break;
                case REQUEST_EDIT_HEAD:
                    List<LocalMedia> localMedias = PictureSelector.obtainMultipleResult(data);
                    if (localMedias.size() > 0) {
                        showLoading();
                        String compressPath = localMedias.get(0).getCompressPath();
                        mHeaderP.uploadHeadFiles(new File(compressPath), false, new UpdataHeadImgPresenter.UpdataHeadImg() {
                            @Override
                            public void onUpDataSucess(String url, String s) {
                                try {
                                    ToastUtils.showNormal(s);
                                    UserBean bean = Preferences.getUserBean(getActivity());
                                    bean.setE_HeadImg(url);
                                    mUrl = url;

                                    Preferences.storeUserBean(getActivity(), bean);
                                    Glide.with(getActivity()).load(url).apply(new RequestOptions().centerCrop()).into(mIvHeader);
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
                    break;
            }
        }
        if (requestCode == 4 && data != null) {
            //认证标签
            if (resultCode == RESULT_OK) {
                //没有申请过  --》申请认证达人
                mMode = MODE_TALENT;
                initMode3();
            } else if (resultCode == -2) {
                //已通告审核了
                String label = data.getStringExtra("label");
                mTvCI.setText(label);
                mIvV.setVisibility(View.VISIBLE);
                mUser.setE_VerWorker("1");
                mUser.setE_Worker(label);
                mUserSource.setE_VerWorker("1");
                mUserSource.setE_Worker(label);
                Preferences.storeUserBean(getActivity(), mUserSource);
            }
        }
        checkHasEmpty(false);
    }

    @OnClick({R.id.edit_nick_ll, R.id.edit_address_ll, R.id.edit_birthday_ll,
            R.id.edit_diploma_ll, R.id.edit_emotional_ll, R.id.edit_gender_ll, R.id.edit_height_ll,
            R.id.edit_hometown_ll, R.id.edit_idiograph_ll, R.id.edit_header_iv,
            R.id.edit_certification_ll, R.id.edit_wechat_ll, R.id.edit_weibo_ll, R.id.edit_live_platform_ll
            , R.id.edit_next_step_tv, R.id.edit_constellation_ll})
    public void onClick(View view) {
        if (view.getId() == R.id.bar_back_view) {
            finish();
            return;
        }
        if (mMode == MODE_SEE_INFO) return;//如果 是查看别人资料
        if (mUser == null) return;
        switch (view.getId()) {
            case R.id.edit_nick_ll:
                showEditInputDialog("编辑昵称", mTvNick.getText().toString(), "请输入昵称..", 1, 20);
                break;
            case R.id.edit_address_ll:
                showAddressDialog(false);
                break;
            case R.id.edit_birthday_ll:
                showDateSelectorDialog2(view);
                break;
            case R.id.edit_diploma_ll:
                mSelectIndex = 1;
                String[] diploma = new String[]{"高中", "中专", "本科", "硕士", "博士", "其他"};
                showSelectorDialog(Arrays.asList(diploma), "");
                break;
            case R.id.edit_emotional_ll:
                mSelectIndex = 2;
                String[] emotional = new String[]{"未婚", "已婚", "保密"};
                showSelectorDialog(Arrays.asList(emotional), "");
                break;
            case R.id.edit_gender_ll:
                mSelectIndex = 3;
                String[] gender = new String[]{"男", "女", "保密"};
                showSelectorDialog(Arrays.asList(gender), "");
                break;
            case R.id.edit_height_ll:
                mSelectIndex = 4;
                List<String> height = new ArrayList<>();
                for (int i = 150; i <= 210; i++) {
                    height.add(i + "");
                }
                showSelectorDialog(height, "CM");
                break;
            case R.id.edit_hometown_ll:
                showAddressDialog(true);
                break;
            case R.id.edit_idiograph_ll:
                startActivityForResult(EditInputActivity.startIntent(this, mTvIdiograph.getText().toString(), "个性签名", "请输入您的个性签名..", 25), 1);
                break;

            case R.id.edit_header_iv:
                record();
                break;
            case R.id.edit_constellation_ll:
                mSelectIndex = 5;
                String[] constellation = new String[]{"白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座", "水瓶座", "双鱼座"};
                showSelectorDialog(Arrays.asList(constellation), "");
                break;
            case R.id.edit_certification_ll:

                if (mMode == MODE_TALENT) {//选取标签
                    startActivityForResult(CertificationInfoActivity.getInstance(EditInfoActivity.this), 5);
                } else if (mMode == MODE_EDIT_INFO) {
                    if (isCertification()) {//已经成功
                        startActivity(ApplyForTalentActivity.getInstance(EditInfoActivity.this, ApplyForTalentActivity.MODE_AFT_3, mUserId));
                    } else {//申请认证须知
                        startActivityForResult(ApplyForTalentActivity.getInstance(EditInfoActivity.this, ApplyForTalentActivity.MODE_AFT_1, mUserId), 4);
                    }
                }
                break;
            case R.id.edit_next_step_tv:
                if (!checkHasEmpty(true)) {
                    submit(false);//提交资料  --》 跳转 认证第二步
                }
                break;
            case R.id.edit_weibo_ll:
                showEditInputDialog("微博", mTvWeibo.getText().toString(), "请输入微博名称..", 2, 20);
                break;
            case R.id.edit_live_platform_ll:
                showEditInputDialog("直播平台", mTvLivePlatform.getText().toString(), "请输入直播平台名称..", 3, 20);
                break;
            case R.id.edit_wechat_ll:
                if (mMode != MODE_SEE_INFO) {
                    //编辑微信
                    showEditInputDialog("微信", mTvWechat.getText().toString(), "请输入微信号..", 4, 20);
                }
                break;
        }
    }

    @AfterPermissionGranted(RC_CAMERA_PERM)
    public void record() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            //头像
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
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_camera),
                    RC_CAMERA_PERM, perms);
        }
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

    /**
     * 判断是否 有null值
     *
     * @return
     */
    private boolean checkHasEmpty(boolean isShowHint) {
        if (mMode != MODE_TALENT) {
            return false;
        }
        boolean result;
        if (result = checkItemsHasEmpty(isShowHint)) {
            mTvNextStep.setSelected(true);
        } else {
            mTvNextStep.setSelected(false);
        }
        return result;
    }

    private boolean checkItemsHasEmpty(boolean isShowHint) {
        if (TextUtils.isEmpty(mTvCI.getText().toString())) {
            showHint("认证信息还没有录入~", isShowHint);
            return true;
        }
        if (TextUtils.isEmpty(mTvNick.getText().toString())) {
            showHint("请输入昵称！", isShowHint);
            return true;
        }
        if (TextUtils.isEmpty(mTvIdiograph.getText().toString())) {
            showHint("请输入你的个性签名~", isShowHint);
            return true;
        }
        if (TextUtils.isEmpty(mTvGender.getText().toString())) {
            showHint("请选择你的性别~", isShowHint);
            return true;
        }

        if (TextUtils.isEmpty(mTvWechat.getText().toString())) {
            showHint("请填写你的微信号~", isShowHint);
            return true;
        }
        if (TextUtils.isEmpty(mTvWeibo.getText().toString())) {
            showHint("请填写你的微博号哦！", isShowHint);
            return true;
        }
        if (TextUtils.isEmpty(mTvLivePlatform.getText().toString())) {
            showHint("请填写你的直播平台~", isShowHint);
            return true;
        }
        if (TextUtils.isEmpty(mTvBirthday.getText().toString())) {
            showHint("请录入你的生日哦~", isShowHint);
            return true;
        }
        if (TextUtils.isEmpty(mTvHeight.getText().toString())) {
            showHint("还没选择你的身高呢~", isShowHint);
            return true;
        }
        return false;
    }

    private void showHint(String hint, boolean isShow) {
        if (isShow) {
            ToastUtils.showNormal(hint);
        }
    }

    @Override
    public void UserDetailsInfo(ArrayList<UserBean> data) {
        if (!Utils.ListIsEmpty(data)) {
            mMSV.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            mUser = data.get(0);
            loadData();
        } else {
            mMSV.setViewState(MultiStateView.VIEW_STATE_ERROR);
        }
    }

    @Override
    public void UserLebelInfo(ArrayList<LabelBean> data) {

    }

    @Override
    public void onError(int code, String message) {
        super.onError(code, message);
        if (code == 1) {//获取用户信息失败
            ToastUtils.showError("获取用户信息失败！");
            mMSV.setViewState(MultiStateView.VIEW_STATE_ERROR);
        }
    }

}
