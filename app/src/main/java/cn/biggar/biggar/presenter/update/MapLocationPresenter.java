package cn.biggar.biggar.presenter.update;

import java.util.List;

import cn.biggar.biggar.bean.BgResponse;
import cn.biggar.biggar.bean.update.MapLocationBean;
import cn.biggar.biggar.contract.MapLocationContract;
import cn.biggar.biggar.http.BaseObserver;
import cn.biggar.biggar.http.HttpMethods;
import cn.biggar.biggar.http.RxSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by Chenwy on 2017/11/7.
 */

public class MapLocationPresenter extends MapLocationContract.Presenter {
    @Override
    public void requestMarkers(double latitude, double longitude, String zoom) {
        HttpMethods.getInstance().getApiService()
                .requestMapMarkers(latitude, longitude, zoom)
                .compose(RxSchedulers.<BgResponse<MapLocationBean>>compose())
                .subscribe(new BaseObserver<MapLocationBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    protected void onHandleSuccess(MapLocationBean mapLocationBeen) {
                        mView.showMarkers(mapLocationBeen, true);
                    }

                    @Override
                    protected void onHandleError(int code, String msg) {

                    }
                });
    }

    @Override
    public void requestMarkers(double latitude, double longitude, double lng1, double lng4, double lat1, double lat4) {
        HttpMethods.getInstance().getApiService()
                .requestMapMarkers(latitude, longitude, lng1, lng4, lat1, lat4)
                .compose(RxSchedulers.<BgResponse<MapLocationBean>>compose())
                .subscribe(new BaseObserver<MapLocationBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    protected void onHandleSuccess(MapLocationBean mapLocationBeen) {
                        mView.showMarkers(mapLocationBeen, false);
                    }

                    @Override
                    protected void onHandleError(int code, String msg) {

                    }
                });
    }
//
//    @Override
//    public void requestMarkers(double latitude, double longitude, double latitude1, double latitude4, double longitude1, double longitude4) {
//        HttpMethods.getInstance().getApiService()
//                .requestMapMarkers(latitude, longitude, latitude1, latitude4, longitude1, longitude4)
//                .compose(RxSchedulers.<BgResponse<MapLocationBean>>compose())
//                .subscribe(new BaseObserver<MapLocationBean>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//                        addDisposable(d);
//                    }
//
//                    @Override
//                    protected void onHandleSuccess(MapLocationBean mapLocationBeen) {
//                        mView.showMarkers(mapLocationBeen, false);
//                    }
//
//                    @Override
//                    protected void onHandleError(int code, String msg) {
//
//                    }
//                });
//    }
}
