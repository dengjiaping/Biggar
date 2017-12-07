package cn.biggar.biggar.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences 工具类
 * Created by Administrator on 2015/12/31.
 */
public class SharedPreferencesHelper {

    private static SharedPreferencesHelper instance;

    private SharedPreferences sharePref;

    private SharedPreferences.Editor editor;

    public static SharedPreferencesHelper getInstance(Context context, String name) {
        if (instance == null) {
            instance = new SharedPreferencesHelper(context, name);
        }
        return instance;
    }

    private SharedPreferencesHelper(Context context, String name) {
        sharePref = context.getSharedPreferences(name, 0);
        editor = sharePref.edit();
    }

    public void putValue(String key, String value) {
        if (null == editor) {
            editor = sharePref.edit();
        }

        editor.putString(key, value);
        editor.commit();
    }
    public boolean putStringValue(String key, String value) {
        if (null == editor) {
            editor = sharePref.edit();
        }

        editor.putString(key, value);
        editor.commit();
        return true;
    }
    public void putValue(String key, int value) {
        if (null == editor) {
            editor = sharePref.edit();
        }

        editor.putInt(key, value);
        editor.commit();
    }
    public void clearValue() {
        if (null == editor) {
            editor = sharePref.edit();
        }
        editor.clear();
        editor.commit();
    }
    public void putValue(String key, boolean value) {
        if (null == editor) {
            editor = sharePref.edit();
        }
        editor.putBoolean(key, value);
        editor.commit();
    }


    public String getValue(String key) {
        return sharePref.getString(key, null);
    }

    public int getInt(String key) {
        return sharePref.getInt(key, 0);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return sharePref.getBoolean(key, defValue);
    }

}
