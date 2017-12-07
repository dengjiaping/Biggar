package cn.biggar.biggar.api;/*
* 描 述：
* 作 者：ld
* 时 间：2015/12/26
* 版 权：比格科技有限公司
*/


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import per.sue.gear2.net.exception.ParseException;
import per.sue.gear2.net.parser.Parser;

public class BaseParser<T> implements Parser {
    private Class cls;

    public BaseParser(Class cls) {
        this.cls = cls;
    }


    @Override
    public T parse(String jsonString) throws ParseException, JSONException {

        if(null == jsonString){
            jsonString  = "";
        }
        if(String.class.equals(cls)) {
            return (T)jsonString.toString();
        }
        Gson gson = new Gson();

        if (jsonString.startsWith("[")) {
            JSONArray jsonArray = new JSONArray(jsonString);
            ArrayList arr = new ArrayList();
            for (int i= 0;i<jsonArray.length();i++){
                Object bean = gson.fromJson(jsonArray.getString(i),cls);
                arr.add(bean);
            }
            return (T)arr;
        } else {
            T t = (T) gson.fromJson(jsonString, cls);
            return t;
        }

    }
}
