package cn.biggar.biggar.contract;

import java.util.List;

import cn.biggar.biggar.base.BasePresenter;
import cn.biggar.biggar.base.BaseView;
import cn.biggar.biggar.bean.VideoImageBean;

/**
 * Created by Chenwy on 2017/9/19.
 */

public class SearchContentContract {
    public interface View extends BaseView {
        void showContents(List<VideoImageBean> videoImageBeen);
        void showMoreContents(List<VideoImageBean> videoImageBeen);
    }

    public abstract static class Presenter extends BasePresenter<View> {
        public abstract void requestContent(String typeId,String uid,int pages,int p,String name);
        public abstract void loadMoreContent(String typeId,String uid,int pages,int p,String name);
    }
}
