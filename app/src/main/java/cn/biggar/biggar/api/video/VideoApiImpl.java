package cn.biggar.biggar.api.video;

import android.content.Context;

import cn.biggar.biggar.api.APIObserver;
import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.api.BaseParser;
import cn.biggar.biggar.bean.ConcersBean;

import cn.biggar.biggar.bean.LiWuBean;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.bean.VideoBean;
import cn.biggar.biggar.bean.VideoImageBean;
import cn.biggar.biggar.bean.VideoImageDetailsBean;
import cn.biggar.biggar.bean.VideoTagBean;
import cn.biggar.biggar.bean.VideoTypeBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import per.sue.gear2.net.ApiConnectionFactory;
import rx.Observable;

/*
* 文件名：
* 描 述：
* 作 者：苏昭强
* 时 间：2016/5/6
* 版 权：比格科技有限公司
*/
public class VideoApiImpl implements IVideoAPI {

    private int testTime;
    private Context context;

    public VideoApiImpl(Context context) {
        testTime = 0;
        this.context = context;
    }

    @Override
    public Observable<VideoBean> publishVideo(String userId, String userName, String content, String path, String leng, String type, String cover, String tag, String first) {
        String url = BaseAPI.API_VIDEO_PUBLISH;
        Map<String, String> params = new HashMap<String, String>();
        params.put("E_MemberID", userId);
        params.put("E_User", userName);
        params.put("E_Content", content);
        params.put("E_Path", path);
        params.put("E_VLeng", leng);
        params.put("E_Type", type);
        params.put("E_Img1", cover);
        params.put("E_Tags", tag);
        params.put("E_Img2", first);
        return new APIObserver(context, ApiConnectionFactory.createPOST(url, params))
                .observeOnMainThread(new BaseParser<VideoBean>(VideoBean.class));
    }

    @Override
    public Observable<VideoBean> publishVideo(Map<String, String> params) {
        String url = BaseAPI.API_VIDEO_PUBLISH;
        return new APIObserver(context, ApiConnectionFactory.createPOST(url, params))
                .observeOnMainThread(new BaseParser<VideoBean>(VideoBean.class));
    }

    @Override
    public Observable<VideoBean> publishImage(Map<String, String> params) {
        String url = BaseAPI.API_VIDEO_PUBLISH;
        return new APIObserver(context, ApiConnectionFactory.createPOST(url, params))
                .observeOnMainThread(new BaseParser<VideoBean>(VideoBean.class));
    }

    @Override
    public Observable<ArrayList<VideoTypeBean>> queryVideoTypes(String type) {
        String url = BaseAPI.API_VIDEO_TYPE+"?type="+type;
        return new APIObserver(context, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<VideoTypeBean>(VideoTypeBean.class));
    }

    @Override
    public Observable<ArrayList<VideoTagBean>> queryVideoTags(String id) {
        String url = BaseAPI.API_VIDEO_TAG+"&E_Path="+id;
        return new APIObserver(context, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<VideoTagBean>(VideoTagBean.class));
    }

    @Override
    public Observable<ArrayList<VideoImageBean>> queryRecommendVideo(int page, int pages, String type) {
        String url = BaseAPI.API_VIDEO_RECOMMEND_LIST + "?" + "p=" + page + "&" + "pages=" + pages + "&" + "type=" + type;
        return new APIObserver(context, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<VideoImageBean>(VideoImageBean.class));
    }

    @Override
    public Observable<ArrayList<ConcersBean>> queryConcers(int page, int pages, String uid, int tag, String name, String brand_id) {
        String url = null;

        if (tag == 0) {
            url = BaseAPI.API_ACCOUNT_CONCER_LIST + "?" + "p=" + page + "&" + "pages=" + pages + "&" + "uid=" + uid;

        } else {
            url = BaseAPI.API_MEMBER_VIDEO + "?" + "p=" + page + "&" + "pages=" + pages + "&" + "MID=" + uid+"&brand_id="+brand_id+"&name="+name;
        }


        return new APIObserver(context, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<ConcersBean>(ConcersBean.class));
    }

    @Override
    public Observable<VideoImageDetailsBean> queryVideoImageDetails(String ID, String uid) {
        String url = BaseAPI.API_VIDEO_DETAILS + "?ID=" + ID + "&E_UID=" + uid;
        return new APIObserver(context, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<VideoImageDetailsBean>(VideoImageDetailsBean.class));
    }

    @Override
    public Observable<LiWuBean> queryLiWuList(String uid, int p, int pages, int postion) {
        String url = BaseAPI.API_LIWU_LIST + "?p=" + p + "&" + "pages=" + pages+"&uid="+uid;
        return new APIObserver(context, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<LiWuBean>(LiWuBean.class));
    }

    @Override
    public Observable<LiWuBean> queryMyLiWuList(int p, int pages, String MembID, String OID, String TYPE) {
        String url = BaseAPI.API_MY_LIWU_LIST+"?p="+p+"&pages="+pages+"&MembID="+MembID+"&OID="+OID+"&TYPE="+TYPE;
        return new APIObserver(context, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<LiWuBean>(LiWuBean.class));
    }

    @Override
    public Observable<ArrayList<VideoImageBean>> queryActivityShowList(int page, int pages) {
        String url = BaseAPI.API_ACTIVITY_SHOW_LIST + "?" + "p=" + page + "&" + "pages=" + pages ;
        return new APIObserver(context, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<VideoImageBean>(VideoImageBean.class));
    }

    @Override
    public Observable<ArrayList<UserBean>> queryHallofFameList(String searchKey,int page, int pages) {
        String url = BaseAPI.API_GET_HALL_OF_FAME_LIST + "?" + "p=" + page + "&" + "pages=" + pages ;
        if (searchKey!=null){
            url+="&name="+searchKey;
        }
        return new APIObserver(context, ApiConnectionFactory.createGET(url))
                .observeOnMainThread(new BaseParser<UserBean>(UserBean.class));
    }


}
