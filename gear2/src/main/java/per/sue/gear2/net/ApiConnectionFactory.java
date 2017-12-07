package per.sue.gear2.net;

import android.content.Context;

import java.util.ArrayList;
import java.util.Map;

import per.sue.gear2.net.bean.InputFile;
import per.sue.gear2.net.progress.ProgressRequestListener;

/*
* 文件名：
* 描 述：
* 作 者：苏昭强
* 时 间：2016/4/29
*/
public class ApiConnectionFactory {
    public static  Context context;
    private static ApiConnectionFactory apiConnectionFactory = new ApiConnectionFactory();

    public static ApiConnectionFactory getInstance() {
        return apiConnectionFactory;
    }

    public void  initialize(Context context){
        this.context = context.getApplicationContext();
    }


    public  static ApiConnection createGET(String url){
        return  new  ApiConnection.Builder()
                .url(url).builder();

    }

    public static ApiConnection createPOST(String url, Map<String, String> params){
        return  create(url, params, null, null);
    }

    public static  ApiConnection create(String url, Map<String, String> params, ArrayList<InputFile> files, ProgressRequestListener progressRequestListener){
        return  new  ApiConnection.Builder().url(url)
                .post(params)
                .files(files)
                .progressRequestListener(progressRequestListener)
                .builder(context);
    }




}
