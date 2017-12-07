package cn.biggar.biggar.contract;

import java.util.List;

import cn.biggar.biggar.base.BasePresenter;
import cn.biggar.biggar.base.BaseView;
import cn.biggar.biggar.bean.FansListBean;

/**
 * Created by Chenwy on 2017/9/6.
 */

public class MyFriendContract {
    public interface View extends BaseView{
        void requestFriendFinish(List<FansListBean> fansListBeens);
        void loadMoreFriendFinish(List<FansListBean> fansListBeens);
    }

    public static abstract class Presenter extends BasePresenter<View>{
        public abstract void requestFriend(String uid,String cid,String keyName,int pages,int p);
        public abstract void loadMoreFriend(String uid,String cid,String keyName,int pages,int p);
    }
}
