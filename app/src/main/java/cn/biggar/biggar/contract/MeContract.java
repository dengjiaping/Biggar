package cn.biggar.biggar.contract;

import cn.biggar.biggar.base.BasePresenter;
import cn.biggar.biggar.base.BaseView;
import cn.biggar.biggar.bean.UserBean;

/**
 * Created by Chenwy on 2017/5/31.
 */

public class MeContract {
    public interface View extends BaseView {
        void showMeData(UserBean userBean);
    }

    public static abstract class Presenter extends BasePresenter<View> {
        public abstract void requestMeData(String tid, String uid);
    }
}
