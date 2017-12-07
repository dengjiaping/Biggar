package cn.biggar.biggar.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.biggar.biggar.R;
import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.event.LoginSucessEvent;
import cn.biggar.biggar.preference.AppPrefrences;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.presenter.RegistPresenter;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.utils.Utils;
import cn.biggar.biggar.view.ClearEditText;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by zl on 2017/2/9.
 */

public class RegisterSucceedActivity extends BiggarActivity implements EasyPermissions.PermissionCallbacks, RegistPresenter.RegistView {
    @BindView(R.id.success_register_ie)
    CircleImageView successRegisterIe;
    @BindView(R.id.success_register_et)
    ClearEditText successRegisterEt;
    @BindView(R.id.success_register_bt)
    TextView successRegisterBt;
    @BindView(R.id.success_register_xie_yi)
    TextView successRegisterXieYi;
    String mImageUrl;//头像图片

    RegistPresenter registPresenter;
    private String mModel, mPassWord, mUserName;
    private UploadManager uploadManager;
    private final int REQUEST_EDIT_HEAD = 0x153;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_succeed_register;
    }

    public static Intent startActivity(Context context, String model, String code, String pawssWord) {
        Intent intent = new Intent(context, RegisterSucceedActivity.class);
        intent.putExtra("model", model);
        intent.putExtra("code", code);
        intent.putExtra("password", pawssWord);

        return intent;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        mModel = getIntent().getStringExtra("model");
        mPassWord = getIntent().getStringExtra("password");
        successRegisterEt.addTextChangedListener(new DetectionEditView(successRegisterEt, "昵称不能超过20个字符"));
        registPresenter = new RegistPresenter(getActivity(), this);
        successRegisterBt.setClickable(false);
        uploadManager = new UploadManager();
    }

    private void upLoadAvatar(final File avatarFile) {
        if (avatarFile == null || !avatarFile.exists()) {
            dismissLoading();
            ToastUtils.showError("图片文件不存在");
            return;
        }

        OkGo.<String>get(BaseAPI.QINIU_TOKEN)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String s = response.body();
                            JSONObject j = new JSONObject(s);
                            String token = j.getString("token");
                            String expectKey = UUID.randomUUID().toString();
                            uploadManager.put(avatarFile, expectKey, token, new UpCompletionHandler() {
                                @Override
                                public void complete(String key, ResponseInfo info, JSONObject response) {
                                    dismissLoading();
                                    try {
                                        if (!info.isOK()) {
                                            ToastUtils.showError("上传失败");
                                            return;
                                        }
                                        avatarFile.delete();
                                        String path = Utils.getQiniuUrl(response.getString("key"));
                                        Glide.with(RegisterSucceedActivity.this).load(path).apply(new RequestOptions().centerCrop()).into(successRegisterIe);
                                        mImageUrl = path;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_EDIT_HEAD) {
                List<LocalMedia> localMedias = PictureSelector.obtainMultipleResult(data);
                if (localMedias.size() > 0) {
                    showLoading();
                    upLoadAvatar(new File(localMedias.get(0).getCompressPath()));
                }
            }
        }
    }

    @OnClick({R.id.success_register_ie, R.id.layout_xieyi_view, R.id.success_register_bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.success_register_ie:
                record();
                break;
            case R.id.layout_xieyi_view:
                XieYiActivity.startIntent(getActivity());
                break;
            case R.id.success_register_bt:
                hideKeyboard();
                regist();
                break;
        }
    }

    /**
     * 注册
     */
    private void regist() {
        mUserName = successRegisterEt.getText().toString();

        registPresenter.regist(mModel, mImageUrl, mUserName, mPassWord);

    }

    protected static final int RC_CAMERA_PERM = 123;

    @AfterPermissionGranted(RC_CAMERA_PERM)
    public void record() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

        if (EasyPermissions.hasPermissions(this, perms)) {
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
            EasyPermissions.requestPermissions(this, "需要开启摄像头和读写文件权限",
                    RC_CAMERA_PERM, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
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
    public void onRegistSucess(UserBean userBean) {
        if (null != userBean && !TextUtils.isEmpty(userBean.getId())) {
            Toast.makeText(getApplication(), "注册成功", Toast.LENGTH_SHORT).show();
            MobclickAgent.onProfileSignIn(userBean.getId());
            Preferences.storeUserBean(getApplication(), userBean);
            AppPrefrences.getInstance(getApplication()).setToken(userBean.getId());
            AppPrefrences.getInstance(getApplication()).setLastAccount(mModel);
            EventBus.getDefault().post(new LoginSucessEvent(userBean));
            getActivity().startActivity(MainActivity.startIntent(getActivity()));
            RegistActivity.mFlag.finish();
            finish();

        } else {
            Toast.makeText(getApplication(), "获取数据异常， 请联系相关人员", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCode(String code) {

    }

    @Override
    public void onError(int code, String message) {
        super.onError(code, message);
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
    }

    public class DetectionEditView implements TextWatcher {
        EditText editText;
        private int mMaxLenth = 20;
        private int mCharCount;
        private String mToastText;
        private Toast mToast;

        /**
         * 避免多次重复弹出toast
         *
         * @param text
         */
        private void showToast(String text) {
            if (mToast == null) {
                mToast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
            } else {
                mToast.setText(text);
                mToast.setDuration(Toast.LENGTH_SHORT);
            }
            mToast.show();
        }

        public DetectionEditView(EditText editText, String mToastText) {
            this.editText = editText;
            this.mToastText = mToastText;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            mCharCount = i1 + i2;
            if (mCharCount > mMaxLenth) {
                editText.setSelection(editText.length());
            }
            try {
                mCharCount = editText.getText().toString().getBytes("GBK").length;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (mCharCount > mMaxLenth) {
                CharSequence subSequence = null;
                for (int i = 0; i < editable.length(); i++) {
                    subSequence = editable.subSequence(0, i);
                    try {

                        if (subSequence.toString().getBytes("GBK").length == mCharCount) {
                            editText.setText(subSequence.toString());
                            break;
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                if (!TextUtils.isEmpty(mToastText)) {
                    showToast(mToastText);
                }
                String androidVersion = android.os.Build.VERSION.RELEASE;
                if (androidVersion.charAt(0) >= '4') {
                    editText.setText(subSequence.toString());
                }
            }

            if (successRegisterEt.getText().length() > 0) {
                successRegisterBt.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.shape_rounded_corners_red_big));
                successRegisterBt.setTextColor(getActivity().getResources().getColor(R.color.whites));
                successRegisterBt.setClickable(true);
            } else {
                successRegisterBt.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.shape_rad_border_gray_white));
                successRegisterBt.setTextColor(getActivity().getResources().getColor(R.color.app_text_color_g));
                successRegisterBt.setClickable(false);
            }
        }
    }
}
