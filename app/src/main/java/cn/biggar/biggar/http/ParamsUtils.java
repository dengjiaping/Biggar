package cn.biggar.biggar.http;

/**
 * Created by Chenwy on 2017/10/27.
 */

public class ParamsUtils {
    public static ParamsHelper params(String key, Object value) {
        ParamsHelper paramsHelper = ParamsHelper.getInstance();
        paramsHelper.clearParams();
        paramsHelper.params(key, value);
        return paramsHelper;
    }

    public static ParamsHelper header(String key, Object value) {
        ParamsHelper paramsHelper = ParamsHelper.getInstance();
        paramsHelper.clearHeader();
        paramsHelper.header(key, value);
        return paramsHelper;
    }
}
