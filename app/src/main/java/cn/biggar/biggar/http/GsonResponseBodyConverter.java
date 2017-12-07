package cn.biggar.biggar.http;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import cn.biggar.biggar.bean.update.ErrorRespon;
import cn.biggar.biggar.bean.update.Response;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by Chenwy on 2017/10/20.
 */

public class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private Gson gson;
    private Type type;

    public GsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            String response = value.string();
            Logger.e(response);

            //jsonArray
            if (isJsonArray(response)) {
                return gson.fromJson(response, type);
            }

            //jsonObject
            JSONObject objectJson = new JSONObject(response);
            if(!objectJson.has("info")) {
                return gson.fromJson(response, type);
            }

            Response httpResult = gson.fromJson(response, Response.class);
            if (httpResult.status == 1 || httpResult.code == 200) {
                return gson.fromJson(response, type);
            } else {
                ErrorRespon errorRespon = gson.fromJson(response, ErrorRespon.class);
                //融云token获取错误
                if (errorRespon.code > 0 && !TextUtils.isEmpty(errorRespon.errorMessage)) {
                    throw new ResultException(errorRespon.code, errorRespon.errorMessage);
                }
                //比格接口错误
                else if (errorRespon.code == 0 && !TextUtils.isEmpty(errorRespon.info)) {
                    throw new ResultException(errorRespon.status, errorRespon.info);
                }
                //其他错误
                else{
                    throw new ResultException(-1, "on error");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            //其他错误
            throw new ResultException(-1, "on error");
        }
    }

    /**
     * 判断 是否为 jsonArray
     */
    public static boolean isJsonArray(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }

        if ((str.charAt(0)) == '{') {
            return false;
        } else if ((str.charAt(0)) == '[') {
            return true;
        }
        return false;
    }
}
