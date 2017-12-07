package cn.biggar.biggar.contract;

import cn.biggar.biggar.base.BasePresenter;
import cn.biggar.biggar.base.BaseView;
import cn.biggar.biggar.bean.BannerBean;
import cn.biggar.biggar.bean.VideoTypeBean;

import java.util.List;

/**
 * Created by Chenwy on 2017/5/2.
 */

public class ShowRecommendContract {
    public interface View extends BaseView{
        void showRecommendList(List<VideoTypeBean> videoTypeBeens);
        void showBannerList(List<BannerBean> bannerBeens);
    }

    public abstract static class Presenter extends BasePresenter<View> {
        public abstract void requestShowRecommend(String type);
        public abstract void requestBanner(String type);
    }
}
