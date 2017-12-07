package cn.biggar.biggar.contract;

import cn.biggar.biggar.base.BasePresenter;
import cn.biggar.biggar.base.BaseView;
import cn.biggar.biggar.bean.UserBean;
import io.rong.imlib.RongIMClient;

/**
 * Created by Chenwy on 2017/10/20.
 */

public class LoginContract {
    public interface View extends BaseView {
        void loginSuccess(String platformStr, UserBean userBean);

        void requestRongTokenSuccess(String rongToken);

        void onTokenIncorrect();

        void connectRongSuccess();

        void connectRongError(RongIMClient.ErrorCode errorCode);
    }

    public static abstract class Presenter extends BasePresenter<View> {
        public abstract void login(String userName, String password, String registrationID);

        public abstract void loginByThird(String platform, String openId, String taken, String infoJson, String registrationID);

        public abstract void requestRongToken(String userId, String name, String portraitUri);

        public abstract void connectRong(String rongToken);
    }
}
