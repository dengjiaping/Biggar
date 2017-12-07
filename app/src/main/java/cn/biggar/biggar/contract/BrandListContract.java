package cn.biggar.biggar.contract;

import java.util.List;

import cn.biggar.biggar.base.BasePresenter;
import cn.biggar.biggar.base.BaseView;
import cn.biggar.biggar.bean.BrandBean;

/**
 * Created by Chenwy on 2017/10/27.
 */

public class BrandListContract {
    public interface View extends BaseView {
        void showRecommendBrandList(List<BrandBean> brandBeen);

        void loadMoreRecommendBrandList(List<BrandBean> brandBeen);

        void showFollowBrandList(List<BrandBean> brandBeen);

        void loadMoewFollowBrandList(List<BrandBean> brandBeen);
    }

    public static abstract class Presenter extends BasePresenter<View> {
        public abstract void requestRecommendBrandList(boolean isLoadMore, String searchName, int p, int pages);

        public abstract void requestFollowBrandList(boolean isLoadMore, String searchName, String MID, int flag, int p, int pages);
    }
}
