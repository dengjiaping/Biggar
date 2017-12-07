package cn.biggar.biggar.presenter;

import android.content.Context;

import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.api.DataApiFactory;
import cn.biggar.biggar.api.account.IUserAPI;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.preference.Preferences;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.utils.Utils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.UUID;

import per.sue.gear2.presenter.AbsPresenter;
import rx.Subscriber;

public class UpdataHeadImgPresenter extends AbsPresenter {
    private Context mContext;
    private IUserAPI userAPI;
    private UploadManager uploadManager;

    public UpdataHeadImgPresenter(Context context) {
        mContext = context;

        userAPI = DataApiFactory.getInstance().createIUserAPI(context);

        uploadManager = new UploadManager();
    }

    public void uploadHeadFiles(final File file, final boolean isDel, final UpdataHeadImg headImg, final String state) {

        if (!file.exists()) return;

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
                                        if (isDel) {
                                            file.delete();
                                        }
                                        String userId = Preferences.getUserBean(mContext).getId();
                                        String path = Utils.getQiniuUrl(response.getString("key"));
                                        if (state.equals(Constants.KEY_USER_HEAD)) {
                                            updataHeadImg(userId, path, headImg);
                                        } else if (state.equals(Constants.KEY_GUILD_HEAD)) {
                                            updataGuildImg(userId, path, headImg);
                                        }
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

    public void updataHeadImg(String userId, final String avartUrl, final UpdataHeadImg headImg) {
        userAPI.updateUserAvart(userId, avartUrl).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                headImg.onError(-1, e.getMessage());
            }

            @Override
            public void onNext(String s) {
                headImg.onUpDataSucess(avartUrl, s);
            }
        });
    }

    /**
     * 上传公会头像
     *
     * @param userId
     * @param headImgUrl
     * @param headImg
     */
    public void updataGuildImg(String userId, final String headImgUrl, final UpdataHeadImg headImg) {
        userAPI.updataGuildImg(userId, headImgUrl).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                headImg.onError(-1, e.getMessage());
            }

            @Override
            public void onNext(String s) {
                headImg.onUpDataSucess(headImgUrl, s);
            }
        });
    }

    public interface UpdataHeadImg {
        void onUpDataSucess(String url, String s);

        void onError(int code, String message);
    }


}
