package cn.biggar.biggar.contract;

import cn.biggar.biggar.base.BasePresenter;
import cn.biggar.biggar.base.BaseView;
import cn.biggar.biggar.bean.UserBean;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;

/**
 * Created by Chenwy on 2017/10/10.
 */

public class MainContract {
    public interface View extends BaseView {
        void requestRongTokenSuccess(String rongToken);
        void connectRongSuccess();
        void connectRongError(RongIMClient.ErrorCode errorCode);
        void onTokenIncorrect();
        void requestNoticeNumSuccess(int msgCount);
        void requestRongMsgNumSuccess(int msgCount);
    }

    public abstract static class Presenter extends BasePresenter<View> {
        public abstract void requestRongToken(UserBean userBean);

        public abstract void connectRong(String rongToken);

        public abstract void requestNoticeNum(String userId);

        public abstract void requestRongMsgNum();
    }
}
