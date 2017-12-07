package cn.biggar.biggar.http;

import java.util.concurrent.TimeUnit;

import cn.biggar.biggar.api.BaseAPI;
import cn.biggar.biggar.http.api.ApiService;
import cn.biggar.biggar.http.api.ContentApi;
import cn.biggar.biggar.http.api.DistributionService;
import cn.biggar.biggar.http.api.MallApiService;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by Chenwy on 2017/10/16.
 */

public class HttpMethods {
    private Retrofit retrofit;
    private static final int TIME_OUT = 3;

    private ApiService apiService;
    private MallApiService mallApiService;
    private ContentApi contentApi;
    private DistributionService distributionService;

    public HttpMethods() {
        OkHttpClient client = new OkHttpClient();
        client.newBuilder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .baseUrl(BaseAPI.BASE_URL)
                .client(client)
//                .addConverterFactory(new Converter.Factory() {//添加对纯String 的支持
//                    @Override
//                    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
//                        return new Converter<ResponseBody, String>() {
//
//                            @Override
//                            public String convert(ResponseBody value) throws IOException {
//                                Logger.e(value.string());
//                                return value.string();
//                            }
//                        };
//                    }
//                })
                .addConverterFactory(ResponseConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
        mallApiService = retrofit.create(MallApiService.class);
        contentApi = retrofit.create(ContentApi.class);
        distributionService = retrofit.create(DistributionService.class);
    }

    private static class HttpMethodsHolder {
        private static HttpMethods httpMethods = new HttpMethods();
    }

    public static HttpMethods getInstance() {
        return HttpMethodsHolder.httpMethods;
    }

    public ApiService getApiService() {
        return apiService;
    }

    public MallApiService getMallApiService() {
        return mallApiService;
    }

    public ContentApi getContentApi() {
        return contentApi;
    }

    public DistributionService getDistributionService() {
        return distributionService;
    }
}
