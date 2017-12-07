package cn.biggar.biggar.contract;

import java.util.List;

import cn.biggar.biggar.base.BasePresenter;
import cn.biggar.biggar.base.BaseView;
import cn.biggar.biggar.bean.update.MapLocationBean;

/**
 * Created by Chenwy on 2017/11/7.
 */

public class MapLocationContract {
    public interface View extends BaseView {
        void showMarkers(MapLocationBean mapLocationBeen,boolean isShouldPlus);
    }

    public abstract static class Presenter extends BasePresenter<View> {
        public abstract void requestMarkers(double latitude, double longitude,String zoom);
        public abstract void requestMarkers(double latitude, double longitude,double longitude1, double longitude4,double latitude1, double latitude4);
    }
}
