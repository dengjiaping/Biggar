package cn.biggar.biggar.fragment.update;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ZoomControls;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.blankj.utilcode.util.SPUtils;
import com.orhanobut.logger.Logger;
import com.yinglan.keyboard.HideUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.biggar.biggar.R;
import cn.biggar.biggar.activity.WebViewActivity;
import cn.biggar.biggar.activity.update.MapShopDetailActivity;
import cn.biggar.biggar.base.BiggarFragment;
import cn.biggar.biggar.bean.update.MapLocationBean;
import cn.biggar.biggar.bean.update.MapSearchBean;
import cn.biggar.biggar.contract.MapLocationContract;
import cn.biggar.biggar.presenter.update.MapLocationPresenter;
import cn.biggar.biggar.utils.JsonUtils;
import cn.biggar.biggar.utils.NumberUtils;
import cn.biggar.biggar.view.MapSearchShopView;
import cn.biggar.biggar.view.MapSearchView;

/**
 * Created by Chenwy on 2017/11/13.
 */

public class MapFragment extends BiggarFragment<MapLocationPresenter> implements MapLocationContract.View
        , MapSearchView.OnSearchChangeListener, MapSearchShopView.MapShopClickListener {
    public static String TAG = MapFragment.class.getSimpleName();
    @BindView(R.id.bmapView)
    MapView mMapView;
    @BindView(R.id.map_search_view)
    MapSearchView mapSearchView;
    @BindView(R.id.iv_location)
    ImageView ivLocation;
    @BindView(R.id.rl_map)
    RelativeLayout rlMap;
    @BindView(R.id.map_search_shop_view)
    MapSearchShopView mapSearchShopView;

    private BaiduMap mBaiduMap;
    private LocationClientOption option;
    private MyLocationlistener myLocationlistener;
    private String mCity = "广州市";
    private List<MapLocationBean.Arr> mMapLocationBeen;
    private MapLocationBean.Arr clickMapLocationBean;

    private LocationClient locationClient;
    private GeoCoder geoCoder;
    private SuggestionSearch suggestionSearch;


    private static final int MARKER_DEFAULT_ZINDEX = 0;
    private int curZIndex = MARKER_DEFAULT_ZINDEX;

    /**
     * 默认缩放倍数
     */
    private static final float DEFAULT_ZOOM = 18f;

    /**
     * 当前缩放倍数
     */
    private float curZoom = DEFAULT_ZOOM;

    /**
     * 是否需要刷新缩放级别
     */
    private boolean isRefreshCurZoom = true;

    /**
     * 当前位置
     */
    private LatLng curLatLng;

    /**
     * 是否可以更新当前位置，作用：防止其它因素导致的重复刷新
     */
    private boolean isCanLocation = true;

    /**
     * 是否完成处理点击marker
     */
    private boolean isFinishHandleClickMarker = true;

    /**
     * 标记物集合
     */
    private List<Marker> markers;
    private List<MapLocationBean.Arr> makerBeen;

    public static Fragment getInstance() {
        return new MapFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.frg_map;
    }

    @Override
    public void onInitialize(Bundle bundle) {
        markers = new ArrayList<>();
        makerBeen = new ArrayList<>();
        mMapLocationBeen = new ArrayList<>();
        initMap();
        initOptions();
        initMapListener();
        //先读取上一次的缓存定位
        readCacheMapStatus();
        locationClient.start();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        HideUtil.init(getActivity());
    }

    /**
     * 刷新当前位置
     */
    public void refreshCurLocation() {
        if (locationClient != null) {
            isRefreshCurZoom = true;
            isCanLocation = true;
            locationClient.requestLocation();
        }
    }

    /**
     * 清除地图以外的其他view
     */
    private void clearWithoutMapView() {
        if (clickMapLocationBean != null) {
            resetClickMarker();
            clickMapLocationBean = null;
            mapSearchShopView.hide();
        }
        if (!mapSearchView.isClickItem()) {
            mapSearchView.cancel();
        }
        mapSearchView.setClickItem(false);
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
        mBaiduMap.setMaxAndMinZoomLevel(21f, 5f);
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //定位
        locationClient = new LocationClient(getContext());
        //定位回调
        myLocationlistener = new MyLocationlistener();
        //地理检索
        geoCoder = GeoCoder.newInstance();
        //关键词搜索
        suggestionSearch = SuggestionSearch.newInstance();
    }

    /**
     * 读取缓存地图
     */
    private void readCacheMapStatus() {
        String latlng = SPUtils.getInstance().getString("latlng");
        if (!TextUtils.isEmpty(latlng)) {
            double latitude = JsonUtils.json2Latitude(latlng);
            double longitude = JsonUtils.json2Longitude(latlng);
            if (latitude > 0 && longitude > 0) {
                setMapStatus(new LatLng(latitude, longitude), DEFAULT_ZOOM, false);
            } else {
                setDefaultMapStatus();
            }
        } else {
            setDefaultMapStatus();
        }
    }

    /**
     * 默认地图
     */
    private void setDefaultMapStatus() {
        setMapStatus(new LatLng(23.148452, 113.330225), DEFAULT_ZOOM, false);
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

        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
        locationClient.setLocOption(option);
    }


    /**
     * 初始化地图一些监听器
     */
    private void initMapListener() {
        //地图状态改变事件
        mBaiduMap.setOnMapStatusChangeListener(onMapStatusChangeListener);

        // 地图点击事件
        mBaiduMap.setOnMapClickListener(onMapClickListener);

        //  覆盖物点击事件
        mBaiduMap.setOnMarkerClickListener(onMarkerClickListener);

        //定位监听
        locationClient.registerLocationListener(myLocationlistener);

        // 店铺事件监听
        mapSearchShopView.setMapShopCloseListener(this);

        // 点击搜索地理位置检索监听
        geoCoder.setOnGetGeoCodeResultListener(onGetGeoCoderResultListener);

        // 搜索框事件监听
        mapSearchView.setOnSearchChangeListener(this);

        // 关键词地点检索监听
        suggestionSearch.setOnGetSuggestionResultListener(onGetSuggestionResultListener);
    }

    @Override
    public void showError(String errMsg) {
    }

    /**
     * 请求回来的数据
     *
     * @param mapLocationBeen
     */
    @Override
    public void showMarkers(MapLocationBean mapLocationBeen, boolean isShouldPlus) {
        mMapLocationBeen.clear();
        mMapLocationBeen.addAll(mapLocationBeen.arr);


        float level = NumberUtils.parseFloat(mapLocationBeen.level);
        //如果是刷新当前定位的话，就使用接口返回的缩放级别进行缩放,否则就不用处理
        if (isRefreshCurZoom) {
            //如果当前缩放级别不等于接口返回的缩放级别才进行缩放，否则无需处理，省掉功夫
            if (curZoom != level) {
                curZoom = level <= 0 || !isShouldPlus ? DEFAULT_ZOOM : level + 2;
                setMapStatus(curLatLng, curZoom, false);
                isRefreshCurZoom = false;
            }
        }

        //新数据返回更新标记
        updateMarkers();
    }

    /**
     * 地图点击事件
     */
    private BaiduMap.OnMapClickListener onMapClickListener = new BaiduMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            clearWithoutMapView();
        }

        @Override
        public boolean onMapPoiClick(MapPoi mapPoi) {
            return false;
        }
    };

    /**
     * 地图状态改变事件
     */
    private BaiduMap.OnMapStatusChangeListener onMapStatusChangeListener = new BaiduMap.OnMapStatusChangeListener() {
        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus) {
            //清除未完成的请求
            mPresenter.clearDisposable();

            // 如果不是点击marker导致的地图移动才处理
            if (isFinishHandleClickMarker) {
                clearWithoutMapView();
            }
        }

        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

        }

        @Override
        public void onMapStatusChange(MapStatus mapStatus) {

        }

        @Override
        public void onMapStatusChangeFinish(MapStatus mapStatus) {
            curZoom = mapStatus.zoom;
            LatLng target = mapStatus.target;
            //中心点纬度
            double latitude = target.latitude;
            //中心点经度
            double longitude = target.longitude;

            LatLngBounds bound = mapStatus.bound;
            LatLng northeast = bound.northeast;
            LatLng southwest = bound.southwest;

            Logger.e("地图状态改变完成 : " + mapStatus.zoom + " , " + latitude + " , " + longitude);

            double lng1 = southwest.longitude;
            double lng4 = northeast.longitude;
            double lat1 = northeast.latitude;
            double lat4 = southwest.latitude;

            Logger.e("lng1 :" + lng1 + "\nlng4：" + lng4 + "\nlat1：" + lat1 + "\nlat4：" + lat4);
            mPresenter.requestMarkers(latitude, longitude, lng1, lng4, lat1, lat4);

            //重置完成处理点击marker为true
            isFinishHandleClickMarker = true;
        }
    };

    /**
     * 点击标记物事件
     */
    private BaiduMap.OnMarkerClickListener onMarkerClickListener = new BaiduMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            Logger.e("点击了覆盖物...");
            isFinishHandleClickMarker = false;
            ivLocation.setVisibility(View.GONE);
            clickMapLocationBean = (MapLocationBean.Arr) marker.getExtraInfo().getSerializable("info");
            mapSearchShopView.updateData(clickMapLocationBean);
            setClickMarker();
            return true;
        }
    };

    /**
     * 重置点击覆盖物
     */
    private void resetClickMarker() {
        ivLocation.setVisibility(View.VISIBLE);
        for (Marker marker : markers) {
            MapLocationBean.Arr info = (MapLocationBean.Arr) marker.getExtraInfo().getSerializable("info");
            if (info.ID.equals(clickMapLocationBean.ID)) {
                marker.setIcon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.car_location));
                return;
            }
        }
    }

    /**
     * 设置点击覆盖物样式
     */
    private void setClickMarker() {
        for (Marker marker : markers) {
            MapLocationBean.Arr info = (MapLocationBean.Arr) marker.getExtraInfo().getSerializable("info");
            if (info.ID.equals(clickMapLocationBean.ID)) {
                marker.setZIndex(++curZIndex);
                marker.setIcon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.markers_sel));
                LatLng clickLatLng = new LatLng(NumberUtils.parseDouble(info.E_Add_Y)
                        , NumberUtils.parseDouble(info.E_Add_X));
                setMapStatus(clickLatLng, -1, true);
            } else {
                marker.setIcon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.car_location));
            }
        }
    }

    /**
     * 刷新标记物
     */
    private void updateMarkers() {
        //目前地图上一个标记物都没有
        if (markers == null || markers.size() == 0) {
            mBaiduMap.clear();
            addNewMarkerData();
        }
        //目前地图上已有标记物
        else {
            //如果一条数据也没有，则直接清除即可
            if (mMapLocationBeen.size() == 0) {
                mBaiduMap.clear();
                markers.clear();
                makerBeen.clear();
                return;
            }

            int requestSize;
            int shouldRemoveSize;
            int addSize;
            int allSize;

            //请求回来的数据
            List<MapLocationBean.Arr> requestData = new ArrayList<>();
            requestData.addAll(mMapLocationBeen);
            requestSize = requestData.size();

            //要移除的数据
            List<MapLocationBean.Arr> shouldRemoveData = new ArrayList<>();
            shouldRemoveData.addAll(makerBeen);

            //筛选出地图上没有的标记物数据
            mMapLocationBeen.removeAll(makerBeen);
            addSize = mMapLocationBeen.size();

            //筛选出要移除的数据
            shouldRemoveData.removeAll(requestData);
            shouldRemoveSize = shouldRemoveData.size();

            //移除数据
            for (MapLocationBean.Arr arr : shouldRemoveData) {
                Iterator<Marker> iterator = markers.iterator();
                while (iterator.hasNext()) {
                    Marker marker = iterator.next();
                    MapLocationBean.Arr info = (MapLocationBean.Arr) marker.getExtraInfo().getSerializable("info");
                    if (info.ID.equals(arr.ID)) {
                        marker.remove();
                        makerBeen.remove(arr);
                        iterator.remove();
                    }
                }
            }

            //添加新的标记物
            addNewMarkerData();

            //最后结果
            allSize = makerBeen.size();

            Logger.e("请求回来的条目：" + requestSize + "\n新增的数目：" + addSize
                    + "\n要移除的数目 : " + shouldRemoveSize + "\n最后的数目 : " + allSize);
        }
    }

    /**
     * 添加标记物
     */
    private void addNewMarkerData() {
        for (MapLocationBean.Arr mapLocationBean : mMapLocationBeen) {
            LatLng latLng = new LatLng(NumberUtils.parseDouble(mapLocationBean.E_Add_Y)
                    , NumberUtils.parseDouble(mapLocationBean.E_Add_X));
            OverlayOptions overlayOptions = new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.mipmap.car_location))
                    .zIndex(MARKER_DEFAULT_ZINDEX)
                    .animateType(MarkerOptions.MarkerAnimateType.grow);
            Marker marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
            Bundle bundle = new Bundle();
            bundle.putSerializable("info", mapLocationBean);
            marker.setExtraInfo(bundle);
            markers.add(marker);
            makerBeen.add(mapLocationBean);
        }
    }

    /**
     * 移动地图中心点
     *
     * @param latLng
     * @param zoomF
     * @param isAnim
     */
    private void setMapStatus(LatLng latLng, float zoomF, boolean isAnim) {
        MapStatus.Builder builder = new MapStatus.Builder();
        if (null != latLng) {
            builder.target(latLng);
        }
        if (zoomF > 0) {
            builder.zoom(zoomF);
        }
        MapStatus mapStatus = builder.build();
        if (isAnim) {
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
        } else {
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
        }
    }

    /**
     * 关键词改变后检索地点关键词
     *
     * @param key
     */
    @Override
    public void onSearchChange(String key) {
        if (TextUtils.isEmpty(mCity) || TextUtils.isEmpty(key)) {
            return;
        }
        suggestionSearch.requestSuggestion(new SuggestionSearchOption().city(mCity)
                .keyword(key));
    }

    /**
     * 检索地点关键词结果
     */
    private OnGetSuggestionResultListener onGetSuggestionResultListener = new OnGetSuggestionResultListener() {
        public void onGetSuggestionResult(SuggestionResult res) {

            //未找到相关结果
            if (res == null || res.getAllSuggestions() == null || res.getAllSuggestions().size() == 0) {
                return;
            }

            //获取在线建议检索结果
            List<SuggestionResult.SuggestionInfo> suggestionInfos = res.getAllSuggestions();
            List<MapSearchBean> mapSearchBeen = new ArrayList<>();
            for (SuggestionResult.SuggestionInfo suggestionInfo : suggestionInfos) {
                MapSearchBean mapSearchBean = new MapSearchBean();
                mapSearchBean.city = suggestionInfo.city;
                mapSearchBean.pt = suggestionInfo.pt;
                mapSearchBean.key = suggestionInfo.key;
                mapSearchBean.district = suggestionInfo.district;
                mapSearchBean.uid = suggestionInfo.uid;
                mapSearchBeen.add(mapSearchBean);
            }
            mapSearchView.updateData(mapSearchBeen);
        }
    };

    /**
     * 点击地点后在地图上搜索，移到该点
     *
     * @param data
     */
    @Override
    public void startSearch(MapSearchBean data) {
        setMapStatus(data.pt, -1, true);
    }

    /**
     * 点击搜索键盘，开始搜索
     *
     * @param keyword
     */
    @Override
    public void startKeySearch(String keyword) {
        if (TextUtils.isEmpty(keyword) || TextUtils.isEmpty(mCity)) {
            return;
        }
        geoCoder.geocode(new GeoCodeOption().address(keyword).city(mCity));
    }

    /**
     * 点击键盘搜索，地理位置检索结果
     */
    private OnGetGeoCoderResultListener onGetGeoCoderResultListener = new OnGetGeoCoderResultListener() {
        @Override
        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
            String address = geoCodeResult.getAddress();
            if (TextUtils.isEmpty(address)) {
                return;
            }
            LatLng location = geoCodeResult.getLocation();
            setMapStatus(location, -1, true);
            MapSearchBean mapSearchBean = new MapSearchBean();
            mapSearchBean.key = address;
            mapSearchBean.pt = location;
            mapSearchBean.city = mCity;
            mapSearchView.updateHistory(mapSearchBean);
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

        }
    };

    /**
     * 店铺关闭
     */
    @Override
    public void mapShopClose() {
        resetClickMarker();
        clickMapLocationBean = null;
    }

    /**
     * 店铺点击
     */
    @Override
    public void mapShopClick() {
        Intent intent = new Intent(getActivity(), MapShopDetailActivity.class);
        intent.putExtra("mapLocationBean", clickMapLocationBean);
        intent.putExtra("curLati", curLatLng.latitude);
        intent.putExtra("curLongi", curLatLng.longitude);
        startActivity(intent);
    }

    @OnClick({R.id.iv_refresh_locatin, R.id.iv_shop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_refresh_locatin:
                refreshCurLocation();
                break;
            case R.id.iv_shop:
                String url = "http://www.biggar.cn/shop.php/Business/shop_index?ID=6";
                WebViewActivity.getInstance(getActivity(), url, true);
                break;
        }
    }

    /**
     * 定位回调
     */
    private class MyLocationlistener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            mCity = location.getCity();
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

            //将地图位置移动到当前位置
            if (isCanLocation) {
                isCanLocation = false;
                //当前定位经纬度
                curLatLng = new LatLng(latitude,
                        longitude);
                //显示当前定位
                setMapStatus(curLatLng, -1, false);
                //清除未完成的请求
                mPresenter.clearDisposable();
                //清除地图外的其他图层
                clearWithoutMapView();
                //请求商家数据
                mPresenter.requestMarkers(latitude, longitude, null);
                //缓存定位
                SPUtils.getInstance().put("latlng", JsonUtils.latLng2Json(latitude, longitude));
            }
        }
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
    public void onDestroyView() {
        //关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        //地图销毁
        geoCoder.destroy();
        suggestionSearch.destroy();
        locationClient.stop();
        mMapView.onDestroy();
        super.onDestroyView();
    }
}
