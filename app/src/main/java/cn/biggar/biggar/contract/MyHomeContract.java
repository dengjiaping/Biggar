package cn.biggar.biggar.contract;

import cn.biggar.biggar.base.BasePresenter;
import cn.biggar.biggar.base.BaseView;
import cn.biggar.biggar.bean.ConcersBean;

import java.util.List;

/**
 * Created by Chenwy on 2017/5/26.
 */

public class MyHomeContract {
    public interface View extends BaseView {
        void showFollows(List<ConcersBean> concersBeens);

        void showMoreFollows(List<ConcersBean> concersBeens);

//        void showMembers(List<ConcersBean> concersBeens);
//
//        void showMoreMembers(List<ConcersBean> concersBeens);
    }

    public static abstract class Presenter extends BasePresenter<View> {
        public abstract void requestFollows(int page, int pages, String uid);

        public abstract void requestMembers(int page, int pages, String uid);
    }
}
