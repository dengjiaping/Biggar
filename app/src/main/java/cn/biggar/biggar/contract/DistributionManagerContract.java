package cn.biggar.biggar.contract;

import cn.biggar.biggar.base.BasePresenter;
import cn.biggar.biggar.base.BaseView;
import cn.biggar.biggar.bean.update.DistriManagerBean;

/**
 * Created by Chenwy on 2017/11/13.
 */

public class DistributionManagerContract {
    public interface View extends BaseView {
        void showDatas(DistriManagerBean distriManagerBean);
    }

    public abstract static class Presenter extends BasePresenter<View> {
        public abstract void requestData(String uid);
    }
}
