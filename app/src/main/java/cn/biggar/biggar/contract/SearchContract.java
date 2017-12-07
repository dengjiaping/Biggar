package cn.biggar.biggar.contract;

import cn.biggar.biggar.base.BasePresenter;
import cn.biggar.biggar.base.BaseView;
import cn.biggar.biggar.bean.update.SeachBean;

/**
 * Created by Chenwy on 2017/9/6.
 */

public class SearchContract {
    public interface View extends BaseView{
        void showSearch(SeachBean seachBean);
    }

    public static abstract class Presenter extends BasePresenter<View> {
        public abstract void requestSearch();
    }
}
