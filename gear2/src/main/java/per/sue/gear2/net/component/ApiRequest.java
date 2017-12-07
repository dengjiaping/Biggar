package per.sue.gear2.net.component;

import okhttp3.FormBody;
import  okhttp3.MediaType;
import  okhttp3.MultipartBody;
import  okhttp3.Request;
import  okhttp3.RequestBody;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import per.sue.gear2.net.bean.InputFile;
import per.sue.gear2.net.progress.ProgressHelper;
import per.sue.gear2.net.progress.ProgressRequestListener;
import per.sue.gear2.net.progress.ProgressResponseListener;
import per.sue.gear2.utils.GearLog;

/*
* 文件名：
* 描 述：
* 作 者：苏昭强
* 时 间：2016/4/29
*/
public class ApiRequest {

    private static final String CONTENT_TYPE_LABEL = "Content-Type";
    private static final String CONTENT_TYPE_VALUE_JSON = "application/json; charset=utf-8";

    private String url;
    private HTTPType type;
    private Map<String, String> params;
    private ArrayList<InputFile> files;
    private Map<String, String> heads;
    private ProgressRequestListener progressRequestListener;
    private ProgressResponseListener progressResponseListener;

    public ApiRequest(String url) {
        this.type = HTTPType.GET;
        this.url = url;
    }

    public static ApiRequest  createApiRequest(String url, Map<String, String> params) {
        return createApiRequest(url, HTTPType.POST, params, null);
    }

    public static ApiRequest createApiRequest(String url, HTTPType type, Map<String, String> params, ArrayList<InputFile> files) {
        ApiRequest request = new ApiRequest(url);
        request.type = type;
        request.params = params;
        request.files = files;
        request.url = url;
        return request;
    }
    public Request request(){
        Request.Builder builder;
        if (type == HTTPType.POST) {
            if(null != files){
                builder = createPostRequestWithFiles();
            }else{
                builder = createPostRequest();
            }

        } else {
            builder = createGetRequest();
        }
        builder.url(this.url);
        if(null != this.heads){
            for(String key:  this.heads.keySet()){
                if(null != this.heads.get(key)){
                    builder.addHeader(key, this.heads.get(key));
                }
            }
        }
        builder.addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE_JSON);
        Request request = builder.build();
        return request;
    }

    private Request.Builder createGetRequest() {
        StringBuffer sb = new StringBuffer();
        sb.append("url:").append(url);
        GearLog.e("get", sb.toString());
        Request.Builder builder = new Request.Builder().get();
        return builder;
    }

    private Request.Builder createPostRequest() {
        StringBuffer sb = new StringBuffer();
        sb.append("url:").append(url+"?");
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        if (null != params) {
            int   n  = 0;
            for (String key : params.keySet()) {
                if(null == params.get(key))
                    continue;
                bodyBuilder.add(key, params.get(key));
                if (n == 0){
                    sb.append(key+"=");
                }else{
                    sb.append("&"+key+"=");
                }
                sb.append(params.get(key));
                n++;
            }
        }

        GearLog.e("post", sb.toString());
        Request.Builder builder = new Request.Builder()
                .post(bodyBuilder.build());
        return builder;
    }

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpeg");
    private Request.Builder createPostRequestWithFiles() {
        StringBuffer sb = new StringBuffer();
        sb.append("url:").append(url+"?");
        MultipartBody.Builder multipartBody = new MultipartBody.Builder();
        multipartBody.setType(MultipartBody.FORM);
       // MultipartBuilder paramsBuilder = new MultipartBuilder().type(MultipartBuilder.FORM);
        if (null != params) {
            int   n  = 0;
            for (String key : params.keySet()) {
                if(null == params.get(key))
                    continue;
               // paramsBuilder.addFormDataPart(key, params.get(key));
                multipartBody.addFormDataPart(key, params.get(key));
                if (n == 0){
                    sb.append(key+"=");
                }else{
                    sb.append("&"+key+"=");
                }
                sb.append(params.get(key));
                n++;
            }
        }

        if(null != files){
            for(InputFile inputFile : files){
                if(null != inputFile && null != inputFile.getFile()){
                    File file = inputFile.getFile();
                   // paramsBuilder.addFormDataPart(inputFile.getKey(), file.getName(), RequestBody.create(MEDIA_TYPE_PNG,file));
                    multipartBody.addFormDataPart(inputFile.getKey(), file.getName(), RequestBody.create(MEDIA_TYPE_PNG,file));
                    sb.append("(fileName="+file.getName()+"fileKey="+inputFile.getKey()+")");
                }
            }
        }
        Request.Builder builder;
        GearLog.e("post with file", sb.toString());
        if(progressRequestListener != null){
            builder = new Request.Builder()
                    .post(ProgressHelper.addProgressRequestListener(multipartBody.build(), progressRequestListener));
        }else{
            builder = new Request.Builder()
                    .post(multipartBody.build());
        }

        return builder;
    }



    public ProgressResponseListener getProgressResponseListener() {
        return progressResponseListener;
    }

    public void setProgressResponseListener(ProgressResponseListener progressResponseListener) {
        this.progressResponseListener = progressResponseListener;
    }

    public ProgressRequestListener getProgressRequestListener() {
        return progressRequestListener;
    }

    public void setProgressRequestListener(ProgressRequestListener progressRequestListener) {
        this.progressRequestListener = progressRequestListener;
    }
}
