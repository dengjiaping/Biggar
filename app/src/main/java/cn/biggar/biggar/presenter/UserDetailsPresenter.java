package cn.biggar.biggar.presenter;

import android.content.Context;
import cn.biggar.biggar.api.DataApiFactory;
import cn.biggar.biggar.api.userDetails.IUserDetailsAPI;
import cn.biggar.biggar.bean.BrokerBean;
import cn.biggar.biggar.bean.CardBean;
import cn.biggar.biggar.bean.LabelBean;
import cn.biggar.biggar.bean.UserBean;
import java.util.ArrayList;

import per.sue.gear2.net.exception.ResultException;
import per.sue.gear2.presenter.AbsPresenter;
import per.sue.gear2.presenter.BaseListener;
import per.sue.gear2.presenter.OnObjectListener;
import rx.Subscriber;

/**
 * Created by 张炼 on 2016/8/8.
 * 用户详情的p
 */
public class UserDetailsPresenter extends AbsPresenter {
    private Context mContext;
    private UserDetailsData mUserDetailsData;
    private IUserDetailsAPI iUserDetailsAPI;
    private LabelData mLabelData;
    public UserDetailsPresenter(Context context, UserDetailsData userDetails) {
        mContext = context;
        mUserDetailsData = userDetails;
        iUserDetailsAPI = DataApiFactory.getInstance().createIUserDetais(context);
    }
    public UserDetailsPresenter(Context context, LabelData labelData) {
        mContext = context;
        mLabelData = labelData;
        iUserDetailsAPI = DataApiFactory.getInstance().createIUserDetais(context);
    }
    public UserDetailsPresenter(Context context) {
        mContext = context;

        iUserDetailsAPI = DataApiFactory.getInstance().createIUserDetais(context);
    }
    /**
     * 根据用户ID查询用户信息
     * @param
     */
    public void queryUserDetails(String tId,String uId) {
        iUserDetailsAPI.queryUserDetails(tId,uId).subscribe(new Subscriber<ArrayList<UserBean>>() {
            @Override
            public void onCompleted() {
                mUserDetailsData.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                mUserDetailsData.onError(1, e.getMessage());
            }

            @Override
            public void onNext(ArrayList<UserBean> userDetailsBean) {
                mUserDetailsData.UserDetailsInfo(userDetailsBean);
            }
        });
    }

    /**
     * 根据用户ID查询标签
     * @param userId
     */
    public void queryUserLabel(String userId) {
        iUserDetailsAPI.queryUserLabel(userId).subscribe(new Subscriber<ArrayList<LabelBean>>() {
            @Override
            public void onCompleted() {
                mUserDetailsData.onCompleted();
            }

            @Override
            public void onError(Throwable e) {

                mUserDetailsData.onError(-1, e.getMessage());
            }

            @Override
            public void onNext(ArrayList<LabelBean> LabelBean) {
                mUserDetailsData.UserLebelInfo(LabelBean);
            }
        });

    }

    /**
     * 提交标签
     *
     * @param userId 登录会员ID
     * @param mId    被打标签的会员ID
     * @param tagId  标签ID、多个用|分开、可为空
     * @param e_name 录入新标签、可为空
     * @return
     */
    public void commitLabel(String userId, String mId, String tagId, String e_name) {
            iUserDetailsAPI.commitLabel(userId,mId,tagId,e_name).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mLabelData.onCompleted();
            }

            @Override
            public void onError(Throwable e) {

                mLabelData.onError(-1, e.getMessage());
            }

            @Override
            public void onNext(String resultBean) {
                mLabelData.LabelDataInfo(resultBean);
            }
        });

    }

    /**
     * 赠送卡券
     * @param cardid
     * @param uid
     * @param fromuser
     */
    public void presented(String cardid, String uid, String fromuser , final OnObjectListener<String> listener){
        iUserDetailsAPI.presented(cardid,uid,fromuser).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                listener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(-1, e.getMessage());
            }

            @Override
            public void onNext(String s) {
                listener.onSuccess(s);
            }
        });
    }

    /**
     * 加入公会
     * @param userid
     * @param brindId  公会ID
     */
    public void joinGuilds(String userid, String brindId, String content, final OnObjectListener<String> listener){
        iUserDetailsAPI.joinGuilds(userid,brindId,content).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                listener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(-1, e.getMessage());
            }

            @Override
            public void onNext(String s) {
                listener.onSuccess(s);
            }
        });
    }

    /**
     * 我的经纪跳转判断
     * @param userId
     */
    public void skipJudge(String userId, String bid, final OnObjectListener<ArrayList<BrokerBean>> listener){

        iUserDetailsAPI.querySkipJudge(userId,bid).subscribe(new Subscriber<ArrayList<BrokerBean>>() {
            @Override
            public void onCompleted() {
                listener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(-1, e.getMessage());
            }

            @Override
            public void onNext(ArrayList<BrokerBean> brokerBeen) {
                listener.onSuccess(brokerBeen);

            }
        });
    }

    /**
     * 退出公会
     * @param uid
     */
    public void logout(String bid, String uid, final OnObjectListener<String> listener){
        iUserDetailsAPI.logout(bid,uid).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                listener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(-1,e.getMessage());
            }

            @Override
            public void onNext(String s) {
                listener.onSuccess(s);
            }
        });
    }
    public void getCard(String type, String id, final OnObjectListener<CardBean> listener){
        iUserDetailsAPI.getCard(type,id).subscribe(new Subscriber<CardBean>() {
            @Override
            public void onCompleted() {
                listener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof ResultException) {
                    ResultException exception = (ResultException) e;
                    listener.onError(exception.getCode(), e.getMessage());
                }else{
                    listener.onError(-1, e.getMessage());
                }
            }

            @Override
            public void onNext(CardBean cardBean) {
                listener.onSuccess(cardBean);
            }
        });
    }

    public void getGiftCard(String type, String id, String member_id, final OnObjectListener<CardBean> listener){
        iUserDetailsAPI.getGiftCard(type,id,member_id).subscribe(new Subscriber<CardBean>() {
            @Override
            public void onCompleted() {
                listener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof ResultException) {
                    ResultException exception = (ResultException) e;
                    listener.onError(exception.getCode(), e.getMessage());
                }else{
                    listener.onError(-1, e.getMessage());
                }
            }

            @Override
            public void onNext(CardBean cardBean) {
                listener.onSuccess(cardBean);
            }
        });
    }

    public void getBrandCard(String type, String id, String uid, final OnObjectListener<CardBean> listener){
        iUserDetailsAPI.getBrandCard(type,id,uid).subscribe(new Subscriber<CardBean>() {
            @Override
            public void onCompleted() {
                listener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof ResultException) {
                    ResultException exception = (ResultException) e;
                    listener.onError(exception.getCode(), e.getMessage());
                }else{
                    listener.onError(-1, e.getMessage());
                }
            }

            @Override
            public void onNext(CardBean cardBean) {
                listener.onSuccess(cardBean);
            }
        });
    }

    public interface UserDetailsData extends BaseListener {
        void UserDetailsInfo(ArrayList<UserBean> data);

        void UserLebelInfo(ArrayList<LabelBean> data);

    }
    public interface LabelData extends BaseListener {

        void LabelDataInfo(String data);
    }
}
