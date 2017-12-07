package cn.biggar.biggar.presenter;

import android.content.Context;

import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.utils.Utils;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import per.sue.gear2.presenter.AbsPresenter;
import per.sue.gear2.presenter.BaseResultView;

/**
 * Created by mx on 2016/12/13.
 * 文化 上传 P
 */

public class FilesUploadPersenter extends AbsPresenter {
    private Context context;
    private FileUploadView mFileUploadView;
    private List<String> mPaths, mUrls;//文件路径 / URl路径
    private int mCurrentPosition;//当前上传的位置
    private boolean mIsImageFile;//是否是图片文件
    private boolean mIsDel;//是否删除上传成功的文件
    private int mUploadingState;//上传状态 -1失败 0未操作  1成功 2上传中
    private UploadManager uploadManager;

    public FilesUploadPersenter(Context context, FileUploadView fileUploadView) {
        this(context, fileUploadView, false, false);
    }

    /**
     * @param context
     * @param fileUploadView
     * @param isImageFile    是否是图片类型的文件
     * @param isDel          //是否删除上传成功的文件
     */
    public FilesUploadPersenter(Context context, FileUploadView fileUploadView, boolean isImageFile, boolean isDel) {
        this.context = context;
        this.mFileUploadView = fileUploadView;
        mPaths = new ArrayList<>();
        mUrls = new ArrayList<>();
        this.mIsImageFile = isImageFile;
        this.mIsDel = isDel;
        uploadManager = new UploadManager();
    }

    public void setFiles(List<String> paths) {
        if (mUploadingState != 2) {
            mPaths.clear();
            mPaths.addAll(paths);
            mUploadingState = 0;
        }
    }

    /**
     * 开始
     */
    public void start() {
        if (mUploadingState == 1 && Utils.ListIsEmpty(mUrls)) {
            mFileUploadView.onSuccess(mUrls);
            mFileUploadView.onCompleted();
        }
        if (Utils.ListIsEmpty(mPaths)) {
            mFileUploadView.onError(0, "没有可上传的文件");
            mFileUploadView.onCompleted();
        }
        mCurrentPosition = 0;
        mUrls.clear();
        startUploadTask();
    }

    private void startUploadTask() {
        if (mCurrentPosition >= mPaths.size()) {//上传完毕
            mUploadingState = 1;
            mFileUploadView.onSuccess(mUrls);
            mFileUploadView.onCompleted();
            return;
        }
        mFileUploadView.prepareUpload("上传" + (mIsImageFile ? "图片" : "文件") + (mCurrentPosition + 1) + "/" + mPaths.size());
        if (mIsImageFile) {
            File file = new File(mPaths.get(mCurrentPosition));
            uploadFiles(file, mIsDel);
        } else {
            uploadFiles(new File(mPaths.get(mCurrentPosition)), mIsDel);
        }
    }


    private void uploadFiles(final File file, final boolean isDel) {
        mUploadingState = 2;
        if (!file.exists()) {
            mCurrentPosition++;
            startUploadTask();
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
                            uploadManager.put(file, expectKey, token, new UpCompletionHandler() {
                                @Override
                                public void complete(String key, ResponseInfo info, JSONObject response) {
                                    try {
                                        if (!info.isOK()) {
                                            ToastUtils.showError("上传失败");
                                            return;
                                        }
                                        String path = Utils.getQiniuUrl(response.getString("key"));
                                        if (isDel) {
                                            if (file.delete()) {
                                                Logger.d("delete yes");
                                            } else {
                                                Logger.d("delete no");
                                            }
                                        }
                                        mUrls.add(path);
                                        mCurrentPosition++;
                                        startUploadTask();
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


    public interface FileUploadView extends BaseResultView<List<String>> {
        void uploadProgress(long bytesWrite, long contentLength, boolean done);

        void prepareUpload(String message);
    }
}
