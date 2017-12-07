package cn.biggar.biggar.utils;

import android.content.Context;
import android.content.res.AssetManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Chenwy on 2017/11/30.
 */

public class JsonUtils {
    /**
     * 将经纬度转成json
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return
     */
    public static String latLng2Json(double latitude, double longitude) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("latitude", latitude)
                    .put("longitude", longitude);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取json纬度
     *
     * @param latlngStr
     * @return
     */
    public static double json2Latitude(String latlngStr) {
        try {
            JSONObject jsonObject = new JSONObject(latlngStr);
            double latitude = jsonObject.optDouble("latitude");
            return latitude;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取josn经度
     *
     * @param longitudeStr
     * @return
     */
    public static double json2Longitude(String longitudeStr) {
        try {
            JSONObject jsonObject = new JSONObject(longitudeStr);
            double longitude = jsonObject.optDouble("longitude");
            return longitude;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 读取文件json
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String file2Json(Context context, String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
