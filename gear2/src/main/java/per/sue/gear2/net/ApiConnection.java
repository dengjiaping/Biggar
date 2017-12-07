/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package per.sue.gear2.net;


import android.content.Context;

import  okhttp3.Call;
import  okhttp3.Callback;
import  okhttp3.Interceptor;
import  okhttp3.OkHttpClient;
import  okhttp3.Request;
import  okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import per.sue.gear2.net.bean.InputFile;
import per.sue.gear2.net.component.ApiRequest;
import per.sue.gear2.net.component.HTTPType;
import per.sue.gear2.net.progress.ProgressRequestListener;
import per.sue.gear2.net.progress.ProgressResponseBody;
import per.sue.gear2.net.progress.ProgressResponseListener;
import per.sue.gear2.utils.GearLog;


/**
 * Api Connection class used to retrieve data from the cloud.
 * Implements {@link Callable} so when executed asynchronously can
 * return a value.
 */
public class ApiConnection  {

    private ApiRequest apiRequest;
    private Call call;
    private Context context;


    public ApiConnection(ApiRequest apiRequest, Context context) {
       this.apiRequest = apiRequest;
        if(null!= context)
        this.context = context.getApplicationContext();
    }

    /**
     * Do a request to an api synchronously.
     * It should not be executed in the main thread of the application.
     * @return A string response
     */
    public void requestSyncCall(Callback responseCallback) throws IOException {
        this.call =  connectToApi();
        call.enqueue(responseCallback);
    }


    public Response requestCall() throws IOException {
        this.call =  connectToApi();
        return this.call.execute();
    }

    public void cancel(){
        if(null != this.call)this.call.cancel();
    }

    private Call connectToApi() throws IOException {
        OkHttpClient okHttpClient = this.createClient();
        return okHttpClient.newCall(apiRequest.request());
    }

    private OkHttpClient createClient() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .build();
                GearLog.e("url=", request.url().toString());
                GearLog.e("mothod:", request.method());
                Response response = chain.proceed(request);
                if(null != apiRequest.getProgressResponseListener()){
                    //包装响应体并返回
                    return response.newBuilder()
                            .body(new ProgressResponseBody(response.body(), apiRequest.getProgressResponseListener()))
                            .build();
                }else{
                    return response;
                }

            }
        }).connectTimeout(8000, TimeUnit.MILLISECONDS)
                .readTimeout(8000, TimeUnit.MILLISECONDS);

        final OkHttpClient okHttpClient = builder
                .build();

        return okHttpClient;
    }

    public static class Builder {
        private String url;
        private HTTPType type;
        private Map<String, String> map;
        private ArrayList<InputFile> files;
        private ProgressRequestListener progressRequestListener;
        private ProgressResponseListener progressResponseListener;
        public Builder() {
            this.type = HTTPType.GET;
        }

        public Builder post(Map<String, String> map) {
            this.map = map;
            this.type = HTTPType.POST;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }
        public Builder progressRequestListener(ProgressRequestListener progressRequestListener) {
            this.progressRequestListener = progressRequestListener;
            return this;
        }

        public Builder progressResponseListener(ProgressResponseListener progressResponseListener) {
            this.progressResponseListener = progressResponseListener;
            return this;
        }

        public Builder files(ArrayList<InputFile> files) {
            this.files = files;
            return this;
        }

        public ApiConnection builder(Context context) {
            ApiRequest apiRequest = ApiRequest.createApiRequest(this.url, this.type, this.map, this.files);
            apiRequest.setProgressRequestListener(this.progressRequestListener);
            apiRequest.setProgressResponseListener(this.progressResponseListener);
            ApiConnection apiConnection = new ApiConnection(apiRequest, context);
            return apiConnection;
        }

        public ApiConnection builder() {
            ApiRequest apiRequest = ApiRequest.createApiRequest(this.url, this.type, this.map, this.files);
            apiRequest.setProgressRequestListener(this.progressRequestListener);
            apiRequest.setProgressResponseListener(this.progressResponseListener);
            ApiConnection apiConnection = new ApiConnection(apiRequest, null);
            return apiConnection;
        }
    }
}
