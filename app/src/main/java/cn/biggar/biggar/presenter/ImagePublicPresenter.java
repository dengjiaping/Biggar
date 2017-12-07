package cn.biggar.biggar.presenter;

import android.content.Context;
import android.text.TextUtils;

import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.api.DataApiFactory;
import cn.biggar.biggar.api.video.IVideoAPI;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.bean.VideoBean;
import cn.biggar.biggar.preference.Preferences;
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
import java.util.HashMap;
import java.util.Map;

import per.sue.gear2.presenter.AbsPresenter;
import per.sue.gear2.presenter.BaseResultView;
import rx.Subscriber;

/**
 * Created by langgu on 16/6/2.
 */
public class ImagePublicPresenter extends AbsPresenter {
    private static final String TAG = "ImagePublicPresenter";

    private Context context;
    private ImagePublishView imagePublishView;
    private IVideoAPI iVideoAPI;
    private String describe;
    private String title;
    private String image3;
    private String image1;
    private String goodsId;

    private ArrayList<String> files;
    private String coverFile;
    private int size = 0;
    private int current;
    private UploadManager uploadManager;
    private String mToken;

    public ImagePublicPresenter(Context context, ImagePublishView imagePublishView) {
        this.context = context;
        this.imagePublishView = imagePublishView;
        this.iVideoAPI = DataApiFactory.getInstance().createIVideoAPI(context);
        uploadManager = new UploadManager();
    }


    private void submit() {
        imagePublishView.prepareUpload("发布作品");
        UserBean userBean = Preferences.getUserBean(context);
        Map<String, String> params = new HashMap<String, String>();
        params.put("E_MemberID", userBean.getId());
        params.put("E_User", userBean.geteName());
        params.put("E_Name", title);
        params.put("E_Content", describe);
        params.put("E_Img3", image3);
        params.put("E_TypeVal", "1");//相册类型
        params.put("E_Img1", image1);
        params.put("goods_id", goodsId);
        this.iVideoAPI.publishImage(params).subscribe(new Subscriber<VideoBean>() {
            @Override
            public void onCompleted() {
                imagePublishView.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                imagePublishView.onError(-1, e.getMessage());
            }

            @Override
            public void onNext(VideoBean videoBean) {
                imagePublishView.onSuccess(videoBean);
            }
        });
    }


    public void publishImage(String title, String describe, ArrayList<String> files, String coverFile, String goodsId) {
        this.goodsId = goodsId;
        this.describe = describe;
        this.title = title;
        this.files = files;
        this.coverFile = coverFile;
        size = files.size();
        UserBean userBean = Preferences.getUserBean(context);
        if (null == userBean) {
            imagePublishView.onError(-1, "数据异常，请重新登录");
            imagePublishView.onCompleted();
            return;
        }
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

    private void startUploadTask() {

        if (TextUtils.isEmpty(image1)) {
            imagePublishView.prepareUpload("上传封面");
            File file = new File(coverFile);
            uploadFiles(file);
        } else if (files.size() > 0) {
            current = size - files.size() + 1;
            imagePublishView.prepareUpload("上传图片" + current + "/" + size);
            File file = new File(files.get(0));
            uploadFiles(file);
            files.remove(0);
        } else {
            submit();
        }
    }


    private void uploadFiles(final File file) {

        if (!file.exists()) return;

        uploadManager.put(file, null, mToken, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                try {
                    if (!info.isOK()) {
                        imagePublishView.onError(-1, "上传失败：" + info.error);
                        return;
                    }
                    String path = Utils.getQiniuUrl(response.getString("key"));
                    if (TextUtils.isEmpty(image1)) {
                        image1 = path;
                    } else {
                        if (TextUtils.isEmpty(image3)) {
                            image3 = new StringBuilder(path).toString();
                        } else {
                            image3 = new StringBuilder(image3).append(",").append(path).toString();
                        }
                    }
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

    public interface ImagePublishView extends BaseResultView<VideoBean> {
        void uploadProgress(double percent);

        void prepareUpload(String message);
    }
}

