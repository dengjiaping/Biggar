package cn.biggar.biggar.http.api;

import cn.biggar.biggar.bean.BgResponse;
import cn.biggar.biggar.bean.update.DistriManagerBean;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Chenwy on 2017/11/13.
 */

public interface DistributionService {
    @FormUrlEncoded
    @POST("Brand/distribution")
    Observable<BgResponse<DistriManagerBean>> requestDatas(@Field("uid") String uid);
}
