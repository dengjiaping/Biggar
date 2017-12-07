package cn.biggar.biggar.presenter;

import android.content.Context;
import android.text.TextUtils;

import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.api.DataApiFactory;
import cn.biggar.biggar.api.userDetails.IUserDetailsAPI;
import cn.biggar.biggar.utils.Utils;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import per.sue.gear2.presenter.BaseResultView;
import rx.Subscriber;

/**
 * Created by zl on 2016/12/12.
 * 个人主页上传相册
 */

public class PersonUploadPhotoPerenter {
    Context context;
    private PersonImagePublishView imagePublishView;
    private IUserDetailsAPI detailsAPI;
    private ArrayList<String> files;
    private int size;
    private int current;
    String path = null;
    private String id;
    private List<String> returnPaths;
    private UploadManager uploadManager;
    private String mToken;

    public PersonUploadPhotoPerenter(Context context, PersonImagePublishView imagePublishView) {
        this.context = context;
        this.imagePublishView = imagePublishView;
        this.detailsAPI = DataApiFactory.getInstance().createIUserDetais(context);
    }

    public void publishImage(String id, ArrayList<String> files) {
        this.files = files;
        size = files.size();
        this.id = id;
        returnPaths = new ArrayList<>();
        uploadManager = new UploadManager();

        OkGo.<String>get(BaseAPI.QINIU_TOKEN)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String s = response.body();
                            JSONObject j = new JSONObject(s);
                            mToken = j.getString("token");
                            startUploadTask();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    /**
     * 将图片上传到七牛
     */
    private void uploadFiles(File file) {
        if (!file.exists()) return;

        uploadManager.put(file, null, mToken, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                try {
                    if (!info.isOK()) {
                        imagePublishView.onError(-1, "上传失败 : " + info.error);
                        return;
                    }
                    if (TextUtils.isEmpty(path)) {
                        path = new StringBuilder(Utils.getQiniuUrl(response.getString("key"))).toString();
                    } else {
                        path = new StringBuilder(Utils.getQiniuUrl(response.getString("key"))).append(",") + path;
                    }

                    returnPaths.add(Utils.getQiniuUrl(response.getString("key")));
                    startUploadTask();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, uploadOptions);
    }


    UploadOptions uploadOptions = new UploadOptions(null, null, false, new UpProgressHandler() {
        @Override
        public void progress(String key, double percent) {
            imagePublishView.uploadProgress(percent);
        }
    }, null);

    private void startUploadTask() {
        if (files.size() > 0) {
            current = size - files.size() + 1;
            imagePublishView.prepareUpload("上传图片" + current + "/" + size);
            try {
                File file = new File(files.get(0));
                uploadFiles(file);
                files.remove(0);
            } catch (Exception e) {
                imagePublishView.onError(2, "图片上传失败");
            }
        } else {
            submit();
        }
    }

    /**
     * 提交
     */
    private void submit() {
        detailsAPI.uploadPhoto(id, path).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                imagePublishView.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                imagePublishView.onError(-1, e.toString());
            }

            @Override
            public void onNext(String s) {
                imagePublishView.returnPath(returnPaths);
            }
        });
    }

    public interface PersonImagePublishView extends BaseResultView<String> {

        void uploadProgress(double percent);

        void prepareUpload(String message);

        void returnPath(List<String> paths);
    }
}
