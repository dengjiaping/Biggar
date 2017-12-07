package cn.biggar.biggar.contract;

import java.util.List;

import cn.biggar.biggar.base.BasePresenter;
import cn.biggar.biggar.base.BaseView;
import cn.biggar.biggar.bean.FansListBean;

/**
 * Created by Chenwy on 2017/5/11.
 */

public class FollowContract {
    public interface View extends BaseView {
        void showFollows(List<FansListBean> fansListBeen);

        void showMoreFollows(List<FansListBean> fansListBeen);

        void addFollowSuccess();

        void addFollowError(String errorMsg);

        void canccelFollowSuccess();

        void canccelFollowError(String errorMsg);
    }

    public abstract static class Presenter extends BasePresenter<View> {
        public abstract void requestFollows(String uid, String cid, int p, int pages, boolean isLoadMore);

        public abstract void addFollow(String id, String nickname, int type, String uid, String usernick);

        public abstract void cancelFollow(String uid, String oid);
    }
}
