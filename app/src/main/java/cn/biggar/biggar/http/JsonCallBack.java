package cn.biggar.biggar.http;

import cn.biggar.biggar.bean.BgResponse;
import cn.biggar.biggar.bean.SimpleResponse;
import cn.biggar.biggar.utils.Convert;

import com.google.gson.stream.JsonReader;
import com.lzy.okgo.callback.AbsCallback;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;

/**
 * Created by Chenwy on 2017/4/18.
 */

public abstract class JsonCallBack<T> extends AbsCallback<T> {
    public static final int TYPE_ARRAY = 0;
    public static final int TYPE_SUPER = 1;
    private int jsonType = TYPE_ARRAY;

    public JsonCallBack(int jsonType) {
        this.jsonType = jsonType;
    }

    //该方法是子线程处理，不能做ui相关的工作
    @Override
    public T convertResponse(Response response) throws Exception {
        if (jsonType == TYPE_ARRAY) {
            return convertSimple(response);
        }
        return convertSuper(response);
    }

    private T convertSimple(Response response) {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        Type type = params[0];
        JsonReader jsonReader = new JsonReader(response.body().charStream());
        T data = Convert.fromJson(jsonReader, type);
        response.close();
        return data;
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
}
