package cn.biggar.biggar.http.api;

import java.util.List;
import java.util.Map;

import cn.biggar.biggar.bean.BgResponse;
import cn.biggar.biggar.bean.CardBean;
import cn.biggar.biggar.bean.VideoImageBean;
import cn.biggar.biggar.bean.update.ContentBean;
import cn.biggar.biggar.bean.update.DiscoverBean;
import io.reactivex.Observable;
import me.dudu.livegiftview.GiftModel;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by Chenwy on 2017/11/9.
 */

public interface ContentApi {
    @GET("Index/newVideo?type_id=is_hot&type=ADV_INDEX")
    Observable<BgResponse<DiscoverBean>> requestHotContentList(@Query("p") int p, @Query("pages") int pages);

    @GET("Index/index_concern_json")
    Observable<List<VideoImageBean>> requestFollowContentList(
            @Query("p") int p, @Query("pages") int pages, @Query("uid") String uid);

    @GET("Index/userSearch")
    Observable<List<VideoImageBean>> requestSearchContent(@QueryMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("Index/video_detail")
    Observable<ContentBean> requestContentData(@Field("ID") String id, @Field("E_UID") String uid);

    @GET("Index/comment_list_json.html")
    Observable<List<ContentBean.CommentList>> loadMoreComments(@QueryMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("Index/add_comment_jsons")
    Observable<BgResponse<ContentBean.CommentList>> comment(@FieldMap Map<String, Object> params);

    @GET("Index/comment_click_json.html")
    Observable<BgResponse<String>> like(@Query("ID") String id, @Query("UID") String uid);

    @GET("Gift/sysgift")
    Observable<List<GiftModel>> requestGift(@QueryMap Map<String, Object> params);

    /**
     * 送礼
     *
     * @return
     */
    @FormUrlEncoded
    @POST("Gift/member_gift")
    Observable<BgResponse<List<CardBean>>> sendGift(@FieldMap Map<String, Object> params);

    /**
     * 分享成功记录
     *
     * @return
     */
    @GET("Api/share_member_action.html")
    Observable<BgResponse<String>> shareSuccess(@QueryMap Map<String, Object> params);
}
