package cn.biggar.biggar.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;

import per.sue.gear2.net.ApiConnection;
import per.sue.gear2.net.exception.NetworkConnectionException;
import per.sue.gear2.net.exception.ParseException;
import per.sue.gear2.net.exception.ResultException;
import per.sue.gear2.net.parser.Parser;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/*
* 文件名：
* 描 述：
* 作 者：苏昭强
* 时 间：2015/12/22
*/
public class APIObserver<T> {

    private final Context context;
    private ApiConnection apiConnection;
    private IResponseIntercept iResponseIntercept;
    private static final int BUFFER_SIZE = 10 * 1024; // 8k ~ 32K

    public APIObserver(Context context, ApiConnection apiConnection) {
       this.context = context;
       this.apiConnection = apiConnection;
    }

    public Observable<File> observeFileOnMainThread(final  File file){
        return Observable.create(new Observable.OnSubscribe<File>() {

            @Override
            public void call(Subscriber<? super File> subscriber) {
                InputStream in=null;
                FileOutputStream out = null;
                int byteread = 0;
                // File tmpFile = new File (context.getExternalCacheDir().getAbsoluteFile()  + File.separator + "biggar.apk") ;
                try {
                    out = new FileOutputStream(file);
                    in = apiConnection.requestCall().body().byteStream();
                    byte[] buffer = new byte[BUFFER_SIZE];
                        while ((byteread = in.read(buffer)) != -1) {
                            out.write(buffer, 0, byteread);
                        }
                    subscriber.onNext(file);
                    subscriber.onCompleted();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    subscriber.onError(new Throwable("文件未发现。"));
                    subscriber.onCompleted();
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(new Throwable("数据读取出错。"));
                    subscriber.onCompleted();
                }finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(in!=null){
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<T> observeOnMainThread(Parser<T> parser){

               return observe(parser)
                       .subscribeOn(Schedulers.io())
                       .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<T> observe(final  Parser<T> parser) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                {

                    if (isThereInternetConnection()) {
                        try {
                            //AppPrefrences appPrefrences = AppPrefrences.getInstance(context);
                            String json = apiConnection.requestCall().body().string();
                            Logger.e(json.toString());

                            if(null != iResponseIntercept){
                                json =  iResponseIntercept.onRespone(json);
                            }
                            if(!isJsonObject(json.toString())){
                                subscriber.onNext(parser.parse(json));
                                subscriber.onCompleted();
                                return;
                            }
                            JSONObject objectJson = new JSONObject(json);
                            if(objectJson.has("info")) {
                                int status = new JSONObject(json).getInt("status");

                                if (1 == status) {
                                    if (objectJson.has("info")) {
                                        String result = objectJson.getString("info");

                                        if (null == result || "null".equals(result)) {
                                            result = "";
                                        }
                                        subscriber.onNext(parser.parse(result));
                                        subscriber.onCompleted();
                                    }
                                } else {
                                    subscriber.onError(new ResultException(status,objectJson.getString("info")));
                                    subscriber.onCompleted();
                                }
                            }else{
                                subscriber.onNext(parser.parse(json));
                                subscriber.onCompleted();
                            }
                        } catch (Exception e) {
                            Logger.e(e.getMessage());
                            e.printStackTrace();
                            if (e instanceof ParseException) {
                                subscriber.onError(new ParseException("解析异常"));
                                subscriber.onCompleted();
                            } else if(e instanceof SocketTimeoutException){
                                subscriber.onError(new NetworkConnectionException("连接超时"));
                                subscriber.onCompleted();
                            } else {
                                subscriber.onError(new NetworkConnectionException("请求异常"));
                                subscriber.onCompleted();
                            }
                        }
                    } else {

                        subscriber.onError(new NetworkConnectionException("网络连接失败"));
                     subscriber.onCompleted();
                    }
                }
            }
        });
    }

    /**
     * Checks if the device has any active internet connection.
     * @return true device with internet connection, otherwise false.
     */
    private boolean isThereInternetConnection() {
        boolean isConnected;
        ConnectivityManager connectivityManager =
                (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = (networkInfo != null && networkInfo.isConnectedOrConnecting());
        return isConnected;
    }

    public void setResponseIntercept(IResponseIntercept iResponseIntercept) {
        this.iResponseIntercept = iResponseIntercept;
    }

    /**
     * 判断 是否为 jsonObject
     * @param str
     * @return
     */
    public static boolean isJsonObject(String str){
        if(TextUtils.isEmpty(str)){
            return true;
        }

        if((str.charAt(0))== '{'){
            return true;
        }else if((str.charAt(0)) == '['){
            return false;
        }
        return true;
    }
}
