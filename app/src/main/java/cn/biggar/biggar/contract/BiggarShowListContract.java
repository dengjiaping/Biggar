package cn.biggar.biggar.contract;

import cn.biggar.biggar.base.BasePresenter;
import cn.biggar.biggar.base.BaseView;
import cn.biggar.biggar.bean.BannerBean;
import cn.biggar.biggar.bean.VideoImageBean;
import cn.biggar.biggar.bean.update.DiscoverBean;

import java.util.List;

/**
 * Created by Chenwy on 2017/4/18.
 */

public class BiggarShowListContract {
    public interface View extends BaseView {
        void showResultList(List<VideoImageBean> videoImageBeen);

        void showMoreResultList(List<VideoImageBean> videoImageBeen);

        void showBanners(List<BannerBean> banners);
    }

    public abstract static class Presenter extends BasePresenter<View> {
        public abstract void requestHotList(int page, int pages);

        public abstract void loadMoreHotList(int page, int pages);

        public abstract void requestFollowList(int page, int pages, String uid);

        public abstract void loadMoreFollowList(int page, int pages, String uid);
    }
}
