package cn.biggar.biggar.http;

import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chenwy on 2017/10/27.
 */

public class ParamsHelper {
    private Map<String, Object> params;
    private Map<String, Object> headers;

    public ParamsHelper() {
        if (params == null) {
            params = new HashMap<>();
        }
        if (headers == null) {
            headers = new HashMap<>();
        }
    }

    private static class ParamsHolder {
        private static final ParamsHelper PARAMS_HELPER = new ParamsHelper();
    }

    public static ParamsHelper getInstance() {
        return ParamsHolder.PARAMS_HELPER;
    }

    public ParamsHelper params(String key, Object value) {
        params.put(key, value);
        return this;
    }

    public ParamsHelper header(String key, Object value) {
        headers.put(key, value);
        return this;
    }

    public void clearHeader() {
        headers.clear();
    }

    public void clearParams(){
        params.clear();
    }

    public Map<String, Object> commitParams() {
//        StringBuilder sb = new StringBuilder();
//        for (Map.Entry<String, Object> entry : params.entrySet()) {
//            sb.append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
//        }
//        Logger.e(sb.toString() + params.size());
        return params;
    }

    public Map<String, Object> commitHeaders() {
        return headers;
    }
}
