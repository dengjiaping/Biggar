package cn.biggar.biggar.presenter;

import android.content.Context;

import cn.biggar.biggar.api.DataApiFactory;
import cn.biggar.biggar.api.account.IUserAPI;

import java.util.HashMap;

import per.sue.gear2.net.exception.ResultException;
import per.sue.gear2.presenter.AbsPresenter;
import per.sue.gear2.presenter.OnObjectListener;
import rx.Subscriber;

/**
 * Created by mx on 2016/8/30.
 * 用户
 */
public class UserPersenter extends AbsPresenter {

    private Context context;
    private IUserAPI iUserAPI;

    public UserPersenter(Context context) {
        this.context = context;
        iUserAPI = DataApiFactory.getInstance().createIUserAPI(context);
    }

    /**
     * 修改用户资料
     * @param userId
     * @param parameter
     * @param listener
     */
    public void updateUserInfo(String userId, HashMap<String,String> parameter, final OnObjectListener<String> listener){
        iUserAPI.updateUserInfo(userId,parameter).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                listener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(0,e.getMessage());
            }

            @Override
            public void onNext(String s) {
                listener.onSuccess(s);
            }
        });

    }

    /**
     * 修改手机号码
     * @param code
     * @param userId
     * @param phone
     * @param listener
     */
    public void updatePhone(String code, String userId, String phone, final OnObjectListener<String> listener){
        iUserAPI.updatePhone(code,userId,phone).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                listener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(0,e.getMessage());
            }

            @Override
            public void onNext(String s) {
                listener.onSuccess(s);
            }
        });
    }

    /**
     * 第三方绑定手机
     * @param code
     * @param mobile
     * @param uid
     * @param passWord
     * @param listener
     */
    public void topsBindingPhone(String code, String mobile, String uid, String passWord, final OnObjectListener<String> listener){
        iUserAPI.topsBindingPhone(code,mobile,uid,passWord).subscribe(new Subscriber<String>() {
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
    /**
     * 商家绑定手机
     * @param username
     * @param password
     * @param mobile
     */
    public void businessBoundPhone(String username, String password, String mobile, final OnObjectListener<String> listener){
        iUserAPI.businessBoundPhone(username,password,mobile).subscribe(new Subscriber<String>() {
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

    /**
     * 检查 达人认证状态
     * @param uid
     * @param listener   0 没有申请过 ，1 通过审核 info  E_Worker          2 您的申请正在审核中
     */
    public void checkCertificationStatus(String uid, final OnObjectListener<String> listener){
        iUserAPI.checkCertificationStatus(uid).subscribe(new Subscriber<String>() {
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
            public void onNext(String s) {
                listener.onSuccess(s);
            }
        });
    }

    /**
     *  申请 达人认证
     * @param uid
     * @param images
     * @param label
     */
    public void applyForTalent(String uid, String images, String label, final OnObjectListener<String> listener){
        iUserAPI.applyForTalent(uid,images,label).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                listener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof ResultException) {
                    listener.onError(((ResultException) e).getCode(), e.getMessage());
                } else {
                    listener.onError(-1, e.getMessage());
                }
            }

            @Override
            public void onNext(String s) {
                listener.onSuccess(s);
            }
        });
    }

    /**
     * 添加微信  比格币
     * @param uid 我的ID
     * @param tid 他的ID
     * @param price 价格  biggar币
     * @param wechatAccount 微信号
     * @param type 1比格币支付 2、微信支付
     * @param listener
     */
    public void payAddWehatAccount(String uid, String tid, String price, String wechatAccount, String orderId, String type, final OnObjectListener<String> listener){
        iUserAPI.payAddWehatAccount(uid,tid,price,wechatAccount,orderId,type).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                listener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof ResultException){
                    ResultException exception= (ResultException) e;
                    listener.onError(exception.getCode(),e.getMessage());
                }else{
                    listener.onError(-1,e.getMessage());
                }
            }

            @Override
            public void onNext(String s) {
                listener.onSuccess(s);
            }
        });
    }

    /**
     * 确认 添加微信了
     * @param uid
     * @param tid
     * @param id
     * @param listener
     */
    public void confirmAddWeChat(String uid, String tid, String id, final OnObjectListener<String> listener){
        iUserAPI.confirmAddWeChat(uid,tid,id).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                listener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                ResultException exception=ResultException.createResultException(e);
                listener.onError(exception.getCode(),e.getMessage());
            }

            @Override
            public void onNext(String s) {
                listener.onSuccess(s);
            }
        });
    }

    /**
     * 添加微信号功能  举报
     * @param uid
     * @param oid
     * @param content
     * @param name
     * @param listener
     */
    public void complaintWeChat(String uid, String oid, String content, String name, final OnObjectListener<String> listener){
        iUserAPI.complaintWeChat(uid,oid,content,name).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                listener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                ResultException exception=ResultException.createResultException(e);
                listener.onError(exception.getCode(),e.getMessage());
            }

            @Override
            public void onNext(String s) {
                listener.onSuccess(s);
            }
        });
    }

    /**
     * 是否拥有查看微信号的权力
     * @param uid
     * @param tid
     * @param listener
     */
    public void getIsHasLookWeChatPower(String uid, String tid, final OnObjectListener<String> listener){
        iUserAPI.getIsPayWeChatLookPower(uid,tid).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                listener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                ResultException exception= ResultException.createResultException(e);
                listener.onError(exception.getCode(),exception.getMessage());
            }

            @Override
            public void onNext(String s) {
                listener.onSuccess(s);
            }
        });
    }

}