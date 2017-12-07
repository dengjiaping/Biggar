package cn.biggar.biggar.http.api;

import java.util.List;
import java.util.Map;

import cn.biggar.biggar.bean.BrandBean;
import cn.biggar.biggar.bean.update.SearchGoodsBean;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Chenwy on 2017/10/27.
 * 商城模块api
 */

public interface MallApiService {
    @GET("Brand/pinpai_index_json")
    Observable<List<BrandBean>> requestRecommendBrandList(@QueryMap Map<String, Object> params);

    @GET("Brand/pinpai_follow_json")
    Observable<List<BrandBean>> requestFollowBrandList(@QueryMap Map<String, Object> params);

    @GET("Index/userSearch")
    Observable<List<SearchGoodsBean>> requestSearchGoods(@QueryMap Map<String, Object> params);
}
