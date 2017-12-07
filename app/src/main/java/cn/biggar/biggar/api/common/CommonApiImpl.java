package cn.biggar.biggar.api.common;

import android.content.Context;


import cn.biggar.biggar.api.APIObserver;
import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.api.BaseParser;
import cn.biggar.biggar.bean.ApkInfoBean;
import cn.biggar.biggar.bean.BannerBean;
import cn.biggar.biggar.bean.CommentBean;
import cn.biggar.biggar.bean.ServerConfigBean;
import cn.biggar.biggar.bean.TalentLabelBean;
import cn.biggar.biggar.wxapi.WXPayInfo;
import com.nostra13.universalimageloader.utils.L;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import per.sue.gear2.net.ApiConnection;
import per.sue.gear2.net.ApiConnectionFactory;
import per.sue.gear2.net.progress.ProgressResponseListener;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by mx on 16/5/19.
 */
public class CommonApiImpl implements ICommonAPI {


    private Context context;

    public CommonApiImpl(Context context) {
        this.context = context;
    }


    @Override
    public Observable<ApkInfoBean> checkApkVersion() {
        String url = BaseAPI.API_COMMON_VERSION + "?Time=" + new Date().getTime();

        return new APIObserver(context, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<ApkInfoBean>(ApkInfoBean.class));
    }

    @Override
    public Observable<ServerConfigBean> getServerConfig() {
//        String url = BaseAPI.API_COMMON_SERVER_CONFIG;
//        return new APIObserver(context, ApiConnectionFactory.createGET(url))
//                .observeOnMainThread(new BaseParser<ServerConfigBean>(ServerConfigBean.class));

        return Observable.create(new Observable.OnSubscribe<ServerConfigBean>() {
                                     @Override
                                     public void call(Subscriber<? super ServerConfigBean> subscriber) {
                                         ServerConfigBean bean = new ServerConfigBean();
                                         bean.setSplashUrl("http://biggar.image.alimmdn.com/1464166847649");
                                         subscriber.onNext(bean);
                                     }
                                 }

        );
    }

    @Override
    public Observable<File> loadFile(String url, File file, ProgressResponseListener progressResponseListener) {
        ApiConnection apiConnection = new ApiConnection.Builder().url(url)
                .progressResponseListener(progressResponseListener)
                .builder();
        return new APIObserver(context, apiConnection)
                .observeFileOnMainThread(file);


    }

    @Override
    public Observable<WXPayInfo> loadWXPayInfo() {
        String url = "http://www.biggar.cn/App.php/Leyuan/GoWxWebPay.html?PayMoney=10";
        ApiConnection apiConnection = new ApiConnection.Builder().url(url)
                .builder();
        return new APIObserver(context, apiConnection)
                .observeOnMainThread(new BaseParser<WXPayInfo>(WXPayInfo.class));
    }

    @Override
    public Observable<ArrayList<BannerBean>> queryBannerByType(String type) {
        String url = BaseAPI.API_BANNER_GET + "?type=" + type;
        L.d("MX", "url_" + url);
        return new APIObserver(context, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<BannerBean>(BannerBean.class));
    }

    @Override
    public Observable<ArrayList<CommentBean>> queryCommentList(String id, String uid, String vitype, int pages, int p) {
        String url = BaseAPI.API_COMMENT_LIST + "?ID=" + id + "&UID=" + uid + "&pages=" + pages + "&VTYPE=" + vitype + "&p=" + p;
        return new APIObserver(context, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<CommentBean>(CommentBean.class));
    }

    @Override
    public Observable<String> commentLike(String id, String uid) {
        String url = BaseAPI.API_COMMENT_LIKE + "?ID=" + id + "&UID=" + uid;
        return new APIObserver(context, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<String> achieveDesire(String modelId, String userId, String userName) {
        String url = BaseAPI.GET_ACHIEVE_DESIRE_URL + "ID=" + modelId + "&E_UID=" + userId + "&E_NAME=" + userName;

        return new APIObserver(context, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<String> shareSuccess(String oid, String uid, String type) {
        String url = BaseAPI.GET_SHARE_SUCCESS + "?SHARE_ID=" + oid + "&SHARE_MID=" + uid + "&SHARE_TYPE=" + type;
        return new APIObserver(context, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<String> deleteRelease(String uid, String mid) {
        String url = BaseAPI.API_DELVIDEO + "?uid=" + uid + "&vid=" + mid;

        return new APIObserver(context, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<String> deleteImage(String uid, String imageUrl) {
        String url = BaseAPI.API_DELETE_IMAGE + "?uid=" + uid + "&img=" + imageUrl;

        return new APIObserver(context, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<String> verifyCode(String phone, String code) {
        String url = BaseAPI.API_GET_VERIFY_CODE + "?mobile=" + phone + "&code=" + code;
        return new APIObserver(context, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<String> getVerifyPassword(String userId, String password, String phone) {
        String url = BaseAPI.API_VERIFY_PASS;
        Map<String, String> params = new HashMap<>();
        params.put("uid", userId);
        params.put("password", password);
        params.put("mobile", phone);
        return new APIObserver(context, ApiConnectionFactory.createPOST(url, params))
                .observeOnMainThread(new BaseParser<String>(String.class));
    }

    @Override
    public Observable<ArrayList<TalentLabelBean>> getTalentLabelList(String pages) {
        String url = BaseAPI.API_TALENT_LABEL + "?pages=" + pages;
        return new APIObserver(context, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<TalentLabelBean>(TalentLabelBean.class));
    }
}
