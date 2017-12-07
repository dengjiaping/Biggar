package cn.biggar.biggar.contract;

import cn.biggar.biggar.base.BasePresenter;
import cn.biggar.biggar.base.BaseView;
import cn.biggar.biggar.bean.update.XbBean;

import java.util.List;

/**
 * Created by Chenwy on 2017/6/1.
 */

public class StarRecordContract {
    public interface View extends BaseView {
        void showResults(List<XbBean> xbBeens);

        void showMoreResults(List<XbBean> xbBeens);
    }

    public static abstract class Presenter extends BasePresenter<View> {
        public abstract void requestDatas(String uid, String type, String ctype, String getType, int p, int pages);

        public abstract void requestMoreDatas(String uid, String type, String ctype, String getType, int p, int pages);
    }
}
