package cn.biggar.biggar.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.adapter.ApplyForTalentAdapter;
import cn.biggar.biggar.base.BiggarFragment;
import cn.biggar.biggar.dialog.OrdinaryPromptDialog;
import cn.biggar.biggar.presenter.FilesUploadPersenter;
import cn.biggar.biggar.presenter.UserPersenter;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.view.ItemDecoration.SpaceItemDecoration;
import cn.biggar.biggar.view.RecyclerAdapter.OnItemClickListener;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import per.sue.gear2.dialog.GearCustomDialog;
import per.sue.gear2.presenter.OnObjectListener;
import per.sue.gear2.utils.UnitUtils;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by mx on 2016/12/12.
 * 申请认证比格达人  2
 */

public class ApplyForTalentFragment2 extends BiggarFragment implements EasyPermissions.PermissionCallbacks, FilesUploadPersenter.FileUploadView {

    @BindView(R.id.aft_recycler)
    RecyclerView mRecyclerView;
    private ApplyForTalentAdapter mAdapter;
    private final int IMAGE_MIN_SIZE = 3;
    private final int IMAGE_MAX_SIZE = 5;

    private UserPersenter mUserP;

    private FilesUploadPersenter mFileUploadP;
    private NumberProgressBar mProgressBar;
    private Dialog mProgressDialog;
    private TextView mProgressDialogDescribeTV;
    private String mUserId, mLable;

    public static ApplyForTalentFragment2 getInstance(String userId, String label) {
        ApplyForTalentFragment2 fragment = new ApplyForTalentFragment2();
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        bundle.putString("label", label);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_apply_for_talent2;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        if (getArguments() == null) {
            ToastUtils.showError("数据异常");
            getActivity().finish();
            return;
        }
        mUserId = getArguments().getString("userId");
        mLable = getArguments().getString("label");
        initView();
        initEvent();
        initData();
    }

    private void initView() {
        mAdapter = new ApplyForTalentAdapter(getActivity());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(LinearLayoutManager.HORIZONTAL, UnitUtils.dip2px_new(getActivity(), 3)));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.addData("这是添加按钮");
    }

    private void initEvent() {
        mAdapter.setOnItemClickListener(new OnItemClickListener<String>() {
            @Override
            public void onClick(int position, View view, String item) {
                if (position == 0) {
                    record();
                    return;
                }
                mAdapter.remove(position);
            }
        });
    }

    private void initData() {
        mFileUploadP = new FilesUploadPersenter(getActivity(), this, true, true);
        mUserP = new UserPersenter(getActivity());
    }

    @OnClick(R.id.aft_submit_tv)
    public void OnClick(View view) {
        int size = mAdapter.getCount() - 1;
        if (size < IMAGE_MIN_SIZE) {
            ToastUtils.showError("最少需要上传" + IMAGE_MIN_SIZE + "张照片");
            return;
        } else if (size > IMAGE_MAX_SIZE) {
            ToastUtils.showError("最多可以上传" + IMAGE_MAX_SIZE + "张照片");
            return;
        }
        List<String> list = mAdapter.getData();
        for (String s : list) {
            Logger.d("path：" + s);
        }
        //上传图片
        uploadImages();
    }

    /**
     * 检查并调转
     */
    private void checkJump() {
        int count = IMAGE_MAX_SIZE - mAdapter.getCount() + 1;
        if (count <= 0) {
            ToastUtils.showError("抱歉，最多可以上传" + IMAGE_MAX_SIZE + "张.");
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
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    protected static final int RC_CAMERA_PERM = 123;

    @AfterPermissionGranted(RC_CAMERA_PERM)
    public void record() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(getActivity(), perms)) {
            checkJump();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && requestCode == PictureConfig.CHOOSE_REQUEST) {
            if (data != null) {
                List<LocalMedia> localMedias = PictureSelector.obtainMultipleResult(data);
                for (LocalMedia localMedia : localMedias) {
                    mAdapter.addData(localMedia.getCompressPath());
                }
            }
        }
    }

    /**
     * 上传照片
     */
    private void uploadImages() {
        List<String> paths = mAdapter.getData();
        paths.remove("这是添加按钮");
        mFileUploadP.setFiles(paths);
        mFileUploadP.start();
    }

    private void submit(List<String> list) {
        showProgressDialog("提交中..");

        StringBuffer images = new StringBuffer();
        for (String s : list) {
            Logger.d("url - " + s);
            images.append(s);
            images.append(',');
        }
        String image = images.toString();
        if (TextUtils.isEmpty(image)) {
            ToastUtils.showError("数据异常！");
            return;
        }
        image = image.substring(0, image.length() - 1);
        mUserP.applyForTalent(mUserId, image, mLable, new OnObjectListener<String>() {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                dismissProgressDialog();
                showHintDialog("你的申请请求已提交成功请耐心等待审核");
            }

            @Override
            public void onError(int code, String msg) {
                super.onError(code, msg);
                if (code == 0) {
                    ToastUtils.showError(msg);
                } else {
                    ToastUtils.showError("提交申请失败！");
                }
                dismissProgressDialog();
            }
        });
    }


    @Override
    public void uploadProgress(long bytesWrite, long contentLength, boolean done) {
        if (null != mProgressBar) {
            int progress = (int) ((bytesWrite * 100) / contentLength);
            mProgressBar.setProgress(progress);
        }
    }

    @Override
    public void prepareUpload(String message) {
        showProgressDialog(message);
        mProgressBar.setProgress(0);
    }

    @Override
    public void onSuccess(List<String> msg) {
        submit(msg);
    }

    @Override
    public void onError(int code, String message) {
        super.onError(code, message);
        if (code == -2 || code == -3) {
            dismissProgressDialog();
            ToastUtils.showError("上传失败:" + message);
        }
    }


    /**
     * 单键 提示
     *
     * @param hint
     */
    private void showHintDialog(String hint) {
        OrdinaryPromptDialog dialog = new OrdinaryPromptDialog(getActivity(), false);
        dialog.setOnSelectedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setResult(Activity.RESULT_OK, new Intent());
                getActivity().finish();
            }
        });

        dialog.setTitle(hint);
        dialog.show();
    }


    public void showProgressDialog(String tip) {
        if (null == mProgressDialog) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_progress, null);
            mProgressDialogDescribeTV = (TextView) view.findViewById(R.id.dialog_describe_tv);
            mProgressBar = (NumberProgressBar) view.findViewById(R.id.number_progress_bar);
            mProgressBar.setMax(100);
            mProgressDialog = new GearCustomDialog.Builder(getActivity()).setContentView(view).create();
            mProgressDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        }
        mProgressDialogDescribeTV.setText(tip);
        mProgressDialog.show();
    }

    protected void dismissProgressDialog() {
        if (null != mProgressDialog) {
            mProgressDialog.dismiss();
        }
    }

}
