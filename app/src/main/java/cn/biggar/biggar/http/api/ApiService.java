package cn.biggar.biggar.http.api;

import java.util.List;
import java.util.Map;

import cn.biggar.biggar.bean.BgResponse;
import cn.biggar.biggar.bean.BuyStarMoneyBean;
import cn.biggar.biggar.bean.FansListBean;
import cn.biggar.biggar.bean.MessageBean;
import cn.biggar.biggar.bean.UserBean;
import cn.biggar.biggar.bean.update.MapLocationBean;
import cn.biggar.biggar.bean.update.OptionsBean;
import cn.biggar.biggar.bean.update.RongTokenBean;
import cn.biggar.biggar.bean.update.SeachBean;
import cn.biggar.biggar.wxapi.WXPayInfo;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by Chenwy on 2017/10/16.
 */

public interface ApiService {
    /**
     * 登录
     *
     * @param username
     * @param password
     * @param registrationID
     * @param device
     * @return
     */
    @FormUrlEncoded
    @POST("Api/login")
    Observable<BgResponse<UserBean>> login(@Field("username") String username, @Field("password") String password
            , @Field("regId") String registrationID, @Field("device") String device);

    /**
     * 第三方登录
     *
     * @param platform
     * @param openId
     * @param accessToken
     * @param info
     * @param regId
     * @param device
     * @return
     */
    @FormUrlEncoded
    @POST("Api/api_account")
    Observable<BgResponse<UserBean>> loginByThird(@Field("platform") String platform, @Field("openId") String openId
            , @Field("accessToken") String accessToken, @Field("info") String info, @Field("regId") String regId
            , @Field("device") String device);


    /**
     * 获取融云token
     *
     * @param headers
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("http://api.cn.ronghub.com/user/getToken.json")
    Observable<RongTokenBean> requestRongToken(
            @HeaderMap Map<String, Object> headers,
            @FieldMap Map<String, Object> params);

    /**
     * 获取广告配置
     *
     * @return
     */
    @GET("Index/adv?type=ADV_SECONDS")
    Observable<BgResponse<OptionsBean>> requestOptions();


    /**
     * 获取地图Markers
     */
    @GET("Index/baidumap")
    Observable<BgResponse<MapLocationBean>> requestMapMarkers(@Query("lat") double lat, @Query("lng") double lng, @Query("level") String zoom);

    @GET("Index/baidumapnew")
    Observable<BgResponse<MapLocationBean>> requestMapMarkers(
            @Query("lat") double lat, @Query("lng") double lng,
            @Query("lng1") double lng1, @Query("lng4") double lng4,
            @Query("lat1") double lat1, @Query("lat4") double lat4);

    /**
     * 搜索用户
     */
    @GET("Index/userSearch")
    Observable<List<FansListBean>> requestSearchUser(@QueryMap Map<String, Object> params);

    /**
     * 关注用户
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("Index/add_concern_json.html")
    Observable<BgResponse<String>> addFollow(@FieldMap Map<String, Object> params);

    /**
     * 取消关注用户
     */
    @GET("User/DelFollow")
    Observable<BgResponse<String>> cancelFollow(@QueryMap Map<String, Object> params);

    /**
     * 搜索首页
     */
    @GET("Index/search")
    Observable<SeachBean> requestSearchIndex();

    /**
     * 获取星币价格数据
     */
    @GET("Gift/rechargeConfig")
    Observable<List<BuyStarMoneyBean>> requestStarDatas(@Query("p") int p, @Query("pages") int pages);

    /**
     * 购买星币支付
     *
     * @param uid
     * @param PayMoney
     * @return
     */
    @FormUrlEncoded
    @POST("Gift/GoWxWebPay")
    Observable<BgResponse<WXPayInfo>> payBuyStar(@Field("uid") String uid, @Field("PayMoney") String PayMoney);

    /**
     * 获取粉丝列表
     */
    @GET("User/ToMyFans")
    Observable<List<FansListBean>> requestFans(@QueryMap Map<String, Object> params);

    /**
     * 获取关注列表
     */
    @GET("User/MyConcern")
    Observable<List<FansListBean>> requestFollows(@QueryMap Map<String, Object> params);

    /**
     * 获取通知消息
     */
    @FormUrlEncoded
    @POST("Mynews/sysMessage")
    Observable<BgResponse<List<MessageBean>>> requestNoticeNum(@Field("uid") String uid);
}
