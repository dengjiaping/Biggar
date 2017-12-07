package cn.biggar.biggar.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import cn.biggar.biggar.R;
import cn.biggar.biggar.bean.CityBean;
import cn.biggar.biggar.bean.ProvinceBean;
import cn.biggar.biggar.utils.GetAddressInfoUtils;
import cn.biggar.biggar.utils.Utils;
import cn.biggar.biggar.view.LoopView.LoopView;
import cn.biggar.biggar.view.LoopView.OnItemSelectedListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 张炼 on 2016/8/31.
 * 选择地址
 */
public class SelectorAddressDialog {

    Context mContext;
    Dialog mDia;
    View view;
    @BindView(R.id.selector_province)
    LoopView mSelectorProvince;
    @BindView(R.id.selector_city)
    LoopView mSelectorCity;
    @BindView(R.id.selector_district)
    LoopView mSelectorDistrict;

    ArrayList<String> mData1 = new ArrayList<>();
    ArrayList<String> mData2 = new ArrayList<>();
    ArrayList<String> mData3 = new ArrayList<>();

    String mProvinceID = "", mCityID = "", mDistrictID = "";
    @BindView(R.id.select_address_cancel)
    TextView mCancel;
    @BindView(R.id.select_address_ok)
    TextView mOk;


    public void setListener(CallBackListener listener) {
        this.listener = listener;
    }

    CallBackListener listener;

    public SelectorAddressDialog(Context context) {
        mContext = context;

    }

    public void showDialog() {
        mDia = new AlertDialog.Builder(mContext, R.style.style_dialog).create();
        view = LayoutInflater.from(mContext).inflate(R.layout.dialog_selector_address, null);
        ButterKnife.bind(this, view);

        mDia.show();
        mDia.setContentView(view);
        mDia.setCancelable(true);
        mDia.setCanceledOnTouchOutside(true);
        Window window = mDia.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.dialog_Eject_animation);
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
        WindowManager.LayoutParams lp = window.getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        dm = mContext.getResources().getDisplayMetrics();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //设置宽度充满屏幕
        double h = (double) dm.widthPixels * 0.5f;
        lp.height = (int) h;
        window.setAttributes(lp);

        loadDate();
        initEvents();
    }

    private void initEvents() {
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDia.dismiss();
            }
        });
        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDia.dismiss();
                if (listener != null) {
                    String mAddress = Utils.getAreaText(mData1.get(mSelectorProvince.getSelectedItem()), mData2.get(mSelectorCity.getSelectedItem()), mData3.get(mSelectorDistrict.getSelectedItem()));
                    listener.callBack(mAddress, mData1.get(mSelectorProvince.getSelectedItem()), mData2.get(mSelectorCity.getSelectedItem()), mData3.get(mSelectorDistrict.getSelectedItem()));
                }
            }
        });
        mSelectorProvince.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {

                String id = GetAddressInfoUtils.mProvinces.get(index).region_id;

                if (!id.equals(mProvinceID)) {
                    mProvinceID = id;
                    mCityID = "";
                    mDistrictID = "";
                    setDataByProvince();
                }
            }
        });
        mSelectorCity.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                String name = mData2.get(index);
                for (CityBean bean : GetAddressInfoUtils.mCitys) {
                    if (bean.region_name.equals(name)) {
                        mCityID = bean.region_id;
                        mDistrictID = "";
                        setDataByCity();
                        break;
                    }
                }
            }
        });
        mSelectorDistrict.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                String name = mData3.get(index);
                for (CityBean bean : GetAddressInfoUtils.mDistricts) {
                    if (bean.region_name.equals(name)) {
                        mDistrictID = bean.region_id;
                        break;
                    }
                }
            }
        });
    }

    private void setDataByCity() {
        mData3.clear();
        for (CityBean bean : GetAddressInfoUtils.mDistricts) {
            if (bean.parent_id.equals(mCityID)) {
                if (mDistrictID.length() == 0) {
                    mDistrictID = bean.region_id;
                }
                mData3.add(bean.region_name);
            }
        }
        mSelectorDistrict.setItems(mData3);
        mSelectorDistrict.requestLayout();
    }

    private void setDataByProvince() {
        mData2.clear();
        mData3.clear();

        for (CityBean bean : GetAddressInfoUtils.mCitys) {
            if (bean.parent_id.equals(mProvinceID)) {
                if (mCityID.length() == 0) {
                    mCityID = bean.region_id;
                }
                mData2.add(bean.region_name);
            }
        }

        for (CityBean bean : GetAddressInfoUtils.mDistricts) {
            if (bean.parent_id.equals(mCityID)) {
                if (mDistrictID.length() == 0) {
                    mDistrictID = bean.region_id;
                }
                mData3.add(bean.region_name);
            }
        }
        mSelectorCity.setItems(mData2);
        mSelectorDistrict.setItems(mData3);
        mSelectorCity.requestLayout();
        mSelectorDistrict.requestLayout();
    }

    private void loadDate() {

        mSelectorProvince.setNotLoop();//不循环
        mSelectorCity.setNotLoop();//不循环
        mSelectorDistrict.setNotLoop();//不循环
        if (GetAddressInfoUtils.mProvinces.size() == 0) {
            GetAddressInfoUtils.mProvinces = GetAddressInfoUtils.getProvinceData(mContext);
            GetAddressInfoUtils.mCitys = GetAddressInfoUtils.getCityData(mContext);
            GetAddressInfoUtils.mDistricts = GetAddressInfoUtils.getDistrictData(mContext);
        }
        mProvinceID = GetAddressInfoUtils.mProvinces.get(0).region_id;

        for (ProvinceBean bean : GetAddressInfoUtils.mProvinces) {
            mData1.add(bean.region_name);
        }

        for (CityBean bean : GetAddressInfoUtils.mCitys) {

            if (bean.parent_id.equals(mProvinceID)) {
                if (mCityID.length() == 0) {
                    mCityID = bean.region_id;
                }

                mData2.add(bean.region_name);
            }
        }
        for (CityBean bean : GetAddressInfoUtils.mDistricts) {
            if (bean.parent_id.equals(mCityID)) {
                if (mDistrictID.length() == 0) {
                    mDistrictID = bean.region_id;
                }
                mData3.add(bean.region_name);
            }
        }

        mSelectorProvince.setItems(mData1);
        int postion = mData1.indexOf("北京");
        mSelectorProvince.setInitPosition(postion);
        mSelectorCity.setItems(mData2);

        mSelectorDistrict.setItems(mData3);

    }

    public interface CallBackListener {
        void callBack(String data, String prov, String city, String dist);
    }
}
