package cn.biggar.biggar.contract;

import cn.biggar.biggar.base.BasePresenter;
import cn.biggar.biggar.base.BaseView;
import cn.biggar.biggar.bean.update.OptionsBean;

/**
 * Created by Chenwy on 2017/10/17.
 */

public class SplashContract {
    public interface View extends BaseView {
        void showOptions(OptionsBean optionsBean);
    }

    public static abstract class Presenter extends BasePresenter<View> {
        public abstract void requestOptions();
    }
}
