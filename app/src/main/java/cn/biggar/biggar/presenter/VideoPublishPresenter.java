package cn.biggar.biggar.presenter;

import android.content.Context;

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
import com.orhanobut.logger.Logger;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


import per.sue.gear2.presenter.AbsPresenter;
import per.sue.gear2.presenter.BaseResultView;
import rx.Subscriber;

/**
 * update by chenwy 20170619
 */
public class VideoPublishPresenter extends AbsPresenter {

    private Context context;
    private VideoPublishSubmitView videoPublishView;

    //上传后返回的链接
    private String videoUpLoadpath;
    private String coverUpLoadpath;
    private String firstUpLoadpath;

    //本地文件路径
    private String videoFile;
    private String coverFile;
    private String firstFile;

    private String videoDescribe;
    private String videoLeng;
    private String mTitle;
    private String goodsId;
    private IVideoAPI videoAPI;

    private String mToken;
    private UploadManager uploadManager;
//    private static Handler mHandler = new Handler();

    public VideoPublishPresenter(Context context, VideoPublishSubmitView videoPublishView) {
        this.context = context;
        this.videoPublishView = videoPublishView;
        videoAPI = DataApiFactory.getInstance().createIVideoAPI(context);
        uploadManager = new UploadManager();
    }

    private void submit() {
        UserBean userBean = Preferences.getUserBean(context);

        Map<String, String> params = new HashMap<String, String>();
        params.put("E_MemberID", userBean.getId());
        params.put("E_User", userBean.geteName());
        params.put("E_Content", videoDescribe);
        params.put("E_Path", videoUpLoadpath);
        params.put("E_VLeng", videoLeng);
        params.put("E_Img1", coverUpLoadpath);
        params.put("E_Img2", firstUpLoadpath);
        params.put("E_Name", mTitle);
        params.put("goods_id", goodsId);

        videoAPI.publishVideo(params).subscribe(new Subscriber<VideoBean>() {
            @Override
            public void onCompleted() {
                videoPublishView.onCompleted();
            }

            @Override
            public void onError(Throwable throwable) {
                videoPublishView.onError(-1, throwable.getMessage());
            }

            @Override
            public void onNext(VideoBean s) {
                videoPublishView.onSuccess(s);
            }
        });

    }

    public void init(String videoFile, String title, String coverFile, String firstFile, String describe, String leng, String goodsId) {
        this.videoDescribe = describe;
        this.videoLeng = leng;
        this.videoFile = videoFile;
        this.coverFile = coverFile;
        this.firstFile = firstFile;
        this.mTitle = title;
        this.goodsId = goodsId;
    }

    /**
     * 开始上传
     */
    public void startTask() {
        OkGo.<String>get(BaseAPI.QINIU_TOKEN)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String s = response.body();
                        try {
                            mToken = new JSONObject(s).getString("token");
                            Logger.e("qiniu token ===> " + mToken);
                            //先上传视频
                            upLoadVideo();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 上传视频
     */
    private void upLoadVideo() {
        videoPublishView.prepareUpload("上传视频");
        uploadManager.put(videoFile, null, mToken, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                try {
                    if (!info.isOK()) {
                        Logger.e("ResponseInfo : " + info + "\nresponse : " + response);
                        videoPublishView.onError(-1, "视频上传失败：" + info.error);
                    } else {
                        //七牛视频链接
                        videoUpLoadpath = Utils.getQiniuUrl(response.getString("key"));
                        //视频上传完了就上传封面1
                        upLoadCoverFile1();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, uploadOptions);
    }

    /**
     * 上传封面1
     */
    private void upLoadCoverFile1() {
        boolean exists = new File(coverFile).exists();
        Logger.e("coverFile exists : " + exists);
        videoPublishView.prepareUpload("上传封面1/2");
        uploadManager.put(coverFile, null, mToken, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                try {
                    if (!info.isOK()) {
                        Logger.e("ResponseInfo : " + info + "\nresponse : " + response);
                        videoPublishView.onError(-1, "封面1上传失败：" + info.error);
                    } else {
                        //七牛封面1链接
                        coverUpLoadpath = Utils.getQiniuUrl(response.getString("key"));
                        //封面1上传完了就上传封面2
                        upLoadCoverFile2();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, uploadOptions);
    }

    /**
     * 上传封面2
     */
    private void upLoadCoverFile2() {
        videoPublishView.prepareUpload("上传封面2/2");
        uploadManager.put(firstFile, null, mToken, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                try {
                    if (!info.isOK()) {
                        Logger.e("ResponseInfo : " + info + "\nresponse : " + response);
                        videoPublishView.onError(-1, "封面2上传失败：" + info.error);
                    } else {
                        //七牛封面2链接
                        firstUpLoadpath = Utils.getQiniuUrl(response.getString("key"));
                        //封面2上传完了提交
                        submit();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, uploadOptions);
    }

    UploadOptions uploadOptions = new UploadOptions(null, null, false, new UpProgressHandler() {
        @Override
        public void progress(String key, double percent) {
            Logger.e("percent ===> " + percent);
            videoPublishView.uploadProgress(percent);
        }
    }, null);

    public interface VideoPublishSubmitView extends BaseResultView<VideoBean> {
        void uploadProgress(double percent);

        void prepareUpload(String message);
    }
}
