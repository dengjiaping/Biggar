package cn.biggar.biggar.activity.update;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.orhanobut.logger.Logger;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.OnClick;
import cn.biggar.biggar.R;
import cn.biggar.biggar.base.BiggarActivity;
import cn.biggar.biggar.bean.update.MapLocationBean;
import cn.biggar.biggar.overly.WalkingRouteOverlay;
import cn.biggar.biggar.utils.NumberUtils;
import cn.biggar.biggar.utils.ToastUtils;
import cn.biggar.biggar.utils.Utils;

/**
 * Created by Chenwy on 2017/11/22.
 */

public class MapNavigationActivity extends BiggarActivity {
    @BindView(R.id.bmapView)
    MapView mMapView;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_distance)
    TextView tvDistance;
    @BindView(R.id.tv_duration)
    TextView tvDuration;
    @BindView(R.id.ll_info)
    LinearLayout llInfo;

    private BaiduMap mBaiduMap;
    private MapLocationBean.Arr mapLocationBean;
    private RoutePlanSearch routePlanSearch;
    private LatLng targetLatLng;
    private LatLng curLatLng;

    private LocationClient locationClient;
    private LocationClientOption option;
    private MyLocationlistener myLocationlistener;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_map_navigation;
    }

    @Override
    public void onInitialize(@Nullable Bundle savedInstanceState) {
        mapLocationBean = (MapLocationBean.Arr) getIntent().getExtras().getSerializable("mapLocationBean");
        double latitude = NumberUtils.parseDouble(mapLocationBean.E_Add_Y);
        double longitude = NumberUtils.parseDouble(mapLocationBean.E_Add_X);
        targetLatLng = new LatLng(latitude, longitude);

        double curLati = getIntent().getDoubleExtra("curLati", 0);
        double curLongi = getIntent().getDoubleExtra("curLongi", 0);
        curLatLng = new LatLng(curLati, curLongi);
        llInfo.setVisibility(View.GONE);
        mMapView.setVisibility(View.INVISIBLE);
        tvAddress.setText(mapLocationBean.E_Address);
        showLoading();
        initMap();
        initOptions();
        startLocation();
    }

    @Override
    public boolean isCanSwipeBack() {
        return false;
    }

    /**
     * 地图一些初始化
     */
    private void initMap() {
        //是否显示缩放按钮
        mMapView.showZoomControls(false);
        //是否显示比例尺
        mMapView.showScaleControl(false);
        //隐藏百度地图logo
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }
        mBaiduMap = mMapView.getMap();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //定位
        locationClient = new LocationClient(this);
        //定位回调
        myLocationlistener = new MyLocationlistener();
        locationClient.registerLocationListener(myLocationlistener);

        routePlanSearch = RoutePlanSearch.newInstance();
        routePlanSearch.setOnGetRoutePlanResultListener(onGetRoutePlanResultListener);
    }

    /**
     * 地图配置
     */
    private void initOptions() {
        option = new LocationClientOption();

        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

        //可选，设置返回经纬度坐标类型，默认gcj02
        //gcj02：国测局坐标；
        //bd09ll：百度经纬度坐标；
        //bd09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标
        option.setCoorType("bd09ll");

        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效
        option.setScanSpan(0);

        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true
        option.setOpenGps(true);

        //地址信息
        option.setIsNeedAddress(true);

        locationClient.setLocOption(option);
    }

    @OnClick({R.id.iv_locatoin, R.id.iv_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_locatoin:
                locationClient.requestLocation();
                break;
            case R.id.iv_close:
                finish();
                break;
        }
    }

    /**
     * 定位回调
     */
    private class MyLocationlistener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            dismissLoading();
            mMapView.setVisibility(View.VISIBLE);
            double latitude = location.getLatitude();    //获取纬度信息
            double longitude = location.getLongitude();    //获取经度信息
            float radius = location.getRadius();//获取定位精度，默认值为0.0f

            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
            String coorType = location.getCoorType();

            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
            int errorCode = location.getLocType();

            Logger.e("errorCode：" + errorCode + "\n纬度：" + latitude + "\n经度：" + longitude + "\n定位精度：" + radius + "\n经纬度类型：" + coorType);

            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    //定位精度
                    .accuracy(0)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(0)
                    //纬度
                    .latitude(latitude)
                    //经度
                    .longitude(location.getLongitude())
                    .build();

            mBaiduMap.setMyLocationData(locData);

            // 设置自定义图标
            MyLocationConfiguration config = new MyLocationConfiguration(
                    //当前定位的模式
                    MyLocationConfiguration.LocationMode.NORMAL,
                    true,
                    BitmapDescriptorFactory
                            .fromResource(R.mipmap.position_d));
            mBaiduMap.setMyLocationConfiguration(config);

            //将地图位置移动到当前位置
            curLatLng = new LatLng(latitude,
                    longitude);
            setMapStatus(curLatLng, 18f, true);

            walkingRoutePlanSearch();
        }
    }

    private void setMapStatus(LatLng latLng, float zoomF, boolean isAnim) {
        MapStatus mapStatus = new MapStatus.Builder()
                .target(latLng)
                .zoom(zoomF)
                .build();
        if (isAnim) {
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
        } else {
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
        }
    }

    OnGetRoutePlanResultListener onGetRoutePlanResultListener = new OnGetRoutePlanResultListener() {
        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
            if (walkingRouteResult == null || walkingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
                ToastUtils.showNormal("抱歉，未找到结果");
            }
            if (walkingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                // result.getSuggestAddrInfo()
                return;
            }

            if (walkingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
                llInfo.setVisibility(View.VISIBLE);
                WalkingRouteLine walkingRouteLine = walkingRouteResult.getRouteLines().get(0);
                int distance = walkingRouteLine.getDistance();
                int duration = walkingRouteLine.getDuration();
                tvDistance.setText(getDistance(distance));
                tvDuration.setText(Utils.mapSec2Time(duration));

                WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaiduMap, curLatLng, targetLatLng);
                mBaiduMap.setOnMarkerClickListener(overlay);
                //设置路线数据
                overlay.setData(walkingRouteLine);
                overlay.addToMap();  //将所有overlay添加到地图中
                overlay.zoomToSpan();//缩放地图
            }
        }

        @Override
        public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

        }

        @Override
        public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

        }

        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

        }

        @Override
        public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

        }
    };

    private String getDistance(int distance) {
        String result;
        if (distance < 1000) {
            result = distance + "米";
        } else if (distance == 1000) {
            result = "1公里";
        } else {
            float f = distance / 1000f;
            BigDecimal b = new BigDecimal(f);
            float f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
            result = f1 + "公里";
        }
        return result;
    }

    /**
     * 用于显示步行路线的overlay，自3.4.0版本起可实例化多个添加在地图中显示
     */
    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {
        public MyWalkingRouteOverlay(BaiduMap baiduMap, LatLng startLatLng, LatLng endLatLng) {
            super(baiduMap, startLatLng, endLatLng);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory
                    .fromResource(R.mipmap.map_center);
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory
                    .fromResource(R.mipmap.car_location);
        }

        @Override
        public int getLineColor() {
            return Color.parseColor("#34a0fc");
        }
    }

    /**
     * 开始请求定位
     */
    private void startLocation() {
        locationClient.start();
    }

    /**
     * 步行路线规划
     */
    private void walkingRoutePlanSearch() {
        PlanNode sPlanNode = PlanNode.withLocation(curLatLng);
        PlanNode ePlanNode = PlanNode.withLocation(targetLatLng);
        routePlanSearch.walkingSearch(new WalkingRoutePlanOption().from(sPlanNode).to(ePlanNode));
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        routePlanSearch.destroy();
        locationClient.stop();
        //关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        //地图销毁
        mMapView.onDestroy();
        super.onDestroy();
    }
}
