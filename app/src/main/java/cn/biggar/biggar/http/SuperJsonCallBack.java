package cn.biggar.biggar.http;

import com.google.gson.stream.JsonReader;
import com.lzy.okgo.callback.AbsCallback;

import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import cn.biggar.biggar.bean.BgResponse;
import cn.biggar.biggar.bean.SimpleResponse;
import cn.biggar.biggar.utils.Convert;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Chenwy on 2017/9/28.
 */

public abstract class SuperJsonCallBack<T> extends AbsCallback<T> {
    public SuperJsonCallBack(){

    }


    @Override
    public T convertResponse(Response response) throws Throwable {
        String jsonStr = response.body().string();
        final char[] strChar = jsonStr.substring(0, 1).toCharArray();
        final char firstChar = strChar[0];

        if(firstChar == '{'){
            JSONObject objectJson = new JSONObject(jsonStr);
            if(objectJson.has("info")){
                int status = objectJson.getInt("status");
                if (status == 1){
                    convertSuper(response);
                }else {
                }
            }
        }else if (firstChar == '['){
            return convertArray(response);
        }

        return null;
    }

    private T convertSuper(Response response) {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        Type type = params[0];
        if (!(type instanceof ParameterizedType)) throw new IllegalStateException("没有填写泛型参数");
        Type rawType = ((ParameterizedType) type).getRawType();
        Type typeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];
        JsonReader jsonReader = new JsonReader(response.body().charStream());
        if (typeArgument == Void.class) {
            SimpleResponse simpleResponse = Convert.fromJson(jsonReader, SimpleResponse.class);
            response.close();
            return (T) simpleResponse.toBgResponse();
        } else if (rawType == BgResponse.class) {
            //有数据类型，表示有data
            BgResponse bgResponse = Convert.fromJson(jsonReader, type);
            response.close();
            //这里的0是以下意思
            //一般来说服务器会和客户端约定一个数表示成功，其余的表示失败，这里根据实际情况修改
            return (T) bgResponse;
        } else {
            response.close();
            throw new IllegalStateException("基类错误无法解析!");
        }
    }

    private T convertArray(Response response) {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        Type type = params[0];
        JsonReader jsonReader = new JsonReader(response.body().charStream());
        T data = Convert.fromJson(jsonReader, type);
        response.close();
        return data;
    }
}
