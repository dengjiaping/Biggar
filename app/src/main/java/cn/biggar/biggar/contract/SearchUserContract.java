package cn.biggar.biggar.contract;

import java.util.List;

import cn.biggar.biggar.base.BasePresenter;
import cn.biggar.biggar.base.BaseView;
import cn.biggar.biggar.bean.FansListBean;
import cn.biggar.biggar.bean.UserBean;

/**
 * Created by Chenwy on 2017/9/19.
 */

public class SearchUserContract {
    public interface View extends BaseView{
        void showUsers(List<FansListBean> userBeen);
        void showMoreUsers(List<FansListBean> userBeen);
        void addFollowSuccess();

        void addFollowError(String errorMsg);

        void canccelFollowSuccess();

        void canccelFollowError(String errorMsg);
    }

    public abstract static class Presenter extends BasePresenter<View>{
       public abstract void requestUser(String typeId,String uid,int pages,int p,String name);
        public abstract void loadMoreUser(String typeId,String uid,int pages,int p,String name);
        public abstract void addFollow(String id, String nickname, int type, String uid, String usernick);
        public abstract void cancelFollow(String uid, String oid);
    }
}
