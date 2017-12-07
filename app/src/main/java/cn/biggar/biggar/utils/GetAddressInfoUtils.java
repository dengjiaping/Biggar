package cn.biggar.biggar.utils;

import android.content.Context;

import cn.biggar.biggar.R;
import cn.biggar.biggar.bean.CityBean;
import cn.biggar.biggar.bean.ProvinceBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by 张炼 on 2016/8/31.
 * 获取省市区
 */
public class GetAddressInfoUtils {

    public static ArrayList<ProvinceBean> mProvinces = new ArrayList<>();
    public static ArrayList<CityBean> mCitys = new ArrayList<>();
    public static ArrayList<CityBean> mDistricts = new ArrayList<>();
    /**
     * 获取省份信息
     *
     * @param c
     * @return
     */
    public static ArrayList<ProvinceBean> getProvinceData(Context c) {
        ArrayList<ProvinceBean> mInfos = new ArrayList<>();
        try {
            InputStream in = c.getResources().openRawResource(R.raw.province);
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
            String info = EncodingUtils.getString(buffer, "UTF-8");
            in.close();

            JSONArray array = new JSONArray(info);
            for (int i = 0; i < array.length(); i++) {
                String str = array.getString(i);
                Gson gson = new Gson();
                Type type = new TypeToken<ProvinceBean>() {
                }.getType();
                ProvinceBean bean = gson.fromJson(str, type);
                mInfos.add(bean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mInfos;
    }

    /**
     * 获取城市信息
     *
     * @param c
     * @return
     */
    public static ArrayList<CityBean> getCityData(Context c) {
        ArrayList<CityBean> mInfos = new ArrayList<>();
        try {
            InputStream in = c.getResources().openRawResource(R.raw.city);
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
            String info = EncodingUtils.getString(buffer, "UTF-8");
            in.close();

            JSONArray array = new JSONArray(info);
            for (int i = 0; i < array.length(); i++) {
                String str = array.getString(i);
                Gson gson = new Gson();
                Type type = new TypeToken<CityBean>() {
                }.getType();
                CityBean bean = gson.fromJson(str, type);
                mInfos.add(bean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mInfos;
    }

    /**
     * 获取县区信息
     *
     * @param c
     * @return
     */
    public static ArrayList<CityBean> getDistrictData(Context c) {
        ArrayList<CityBean> mInfos = new ArrayList<>();
        try {
            InputStream in = c.getResources().openRawResource(R.raw.district);
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
            String info = EncodingUtils.getString(buffer, "UTF-8");
            in.close();

            JSONArray array = new JSONArray(info);
            for (int i = 0; i < array.length(); i++) {
                String str = array.getString(i);
                Gson gson = new Gson();
                Type type = new TypeToken<CityBean>() {
                }.getType();
                CityBean bean = gson.fromJson(str, type);
                mInfos.add(bean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mInfos;
    }
}
