package cn.biggar.biggar.presenter;

import android.content.Context;

import cn.biggar.biggar.api.DataApiFactory;
import cn.biggar.biggar.api.account.IUserAPI;
import cn.biggar.biggar.api.common.ICommonAPI;
import cn.biggar.biggar.api.userDetails.IUserDetailsAPI;
import cn.biggar.biggar.bean.LiWuBean;
import cn.biggar.biggar.bean.TalentLabelBean;

import java.util.ArrayList;

import per.sue.gear2.net.exception.ResultException;
import per.sue.gear2.presenter.AbsPresenter;
import per.sue.gear2.presenter.BaseListener;
import per.sue.gear2.presenter.OnObjectListener;
import per.sue.gear2.presenter.OnObjectsListener;
import rx.Subscriber;

/**
 * Created by mx on 2016/8/9.
 * 常用 的
 */
public class CommonPresenter extends AbsPresenter {
    private Context context;
    private ICommonAPI commonAPI;
    private IUserAPI userAPI;
    private IUserDetailsAPI userDetailsAPI;
    private CommonSongliListener mSongliListener;

    public CommonPresenter(Context context) {
        this.context = context;
        this.commonAPI = DataApiFactory.getInstance().createICommonAPI(context);
        this.userAPI = DataApiFactory.getInstance().createIUserAPI(context);
        this.userDetailsAPI = DataApiFactory.getInstance().createIUserDetais(context);
    }

    public CommonPresenter(Context context, CommonSongliListener mSongliListener) {
        this.context = context;
        this.mSongliListener = mSongliListener;
        this.commonAPI = DataApiFactory.getInstance().createICommonAPI(context);
        this.userAPI = DataApiFactory.getInstance().createIUserAPI(context);
        this.userDetailsAPI = DataApiFactory.getInstance().createIUserDetais(context);
    }

    /**
     * 获取验证码
     *
     * @param mobile   手机
     * @param type     UserReg  / ForgetPassword / BindMobile
     * @param listener
     */
    public void getCode(String mobile, String type, final OnObjectListener<String> listener) {
        userAPI.getCode(mobile, type).subscribe(
                new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        listener.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ResultException exception = ResultException.createResultException(e);
                        listener.onError(exception.getCode(), e.getMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        listener.onSuccess(s);
                    }
                });
    }

    /**
     * 验证验证码
     *
     * @param mobile   手机
     * @param code     code
     * @param listener
     */
    public void verifyCode(String mobile, String code, final OnObjectListener<String> listener) {
        commonAPI.verifyCode(mobile, code).subscribe(
                new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        listener.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(0, e.getMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        listener.onSuccess(s);
                    }
                });
    }

    /**
     * 评论
     *
     * @param id
     * @param content
     * @param type
     * @param uid
     * @param usernick
     */
    public void comment(String id, String content, String type, String uid, String usernick, final OnObjectListener<String> listener) {
        userAPI.comment(id, content, type, uid, usernick).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                listener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(-1, e.getMessage());
            }

            @Override
            public void onNext(String result) {
                listener.onSuccess(result);
            }
        });
    }

    /**
     * 关注
     *
     * @param id       关注对像ID、如会员ID
     * @param tit      关注对像呢称、如会员呢称
     * @param type     关注类型：1:会员、2:品牌、3:产品、4视频、5主题、只可单选
     * @param userId
     * @param username
     */
    public void concern(String id, String tit, final int type, String userId, String username, final OnObjectListener<String> listener) {
        userAPI.concern(id, tit, type, userId, username).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                listener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(0, e.getMessage());
            }

            @Override
            public void onNext(String result) {
                listener.onSuccess(result);
                listener.onSuccessRequest(type, result);
            }
        });
    }

    /**
     * 取消关注
     *
     * @param uid
     * @param oid      关注对像ID、如会员ID
     * @param type     关注类型：1:会员、2:品牌、3:产品、4视频、5主题、只可单选
     * @param listener
     */
    public void cancelConcern(String uid, String oid, final int type, final OnObjectListener<String> listener) {
        userAPI.cancelConcern(uid, oid, type).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                listener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(0, e.getMessage());
            }

            @Override
            public void onNext(String result) {
                listener.onSuccess(result);
                listener.onSuccessRequest(type, result);
            }
        });
    }

    /**
     * 举报用户
     *
     * @param type   1 用户  2 视频
     * @param userid 用户的ID
     */
    public void report(int type, String userid, final OnObjectListener<String> listener) {
        userAPI.report(type, userid).subscribe(new Subscriber<String>() {
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
     * 验证是否 关注
     *
     * @param id        关注对像ID、如会员ID、多个对像用|串起来、如: 10|12|30
     * @param type      关注类型：1:会员、2:品牌、3:产品、4视频、5主题、只可单选
     * @param userid
     * @param checktype 验证我关注的（ME）、验证关注我的（TA）、省略时默认为：ME
     */
    public void checkConcern(String id, final int type, String userid, String checktype, final OnObjectListener<String> listener) {
        userAPI.checkConcern(id, type, userid, checktype).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                listener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(type, e.getMessage());
            }

            @Override
            public void onNext(String result) {
                listener.onSuccess(result);
                listener.onSuccessRequest(type, result);
            }
        });
    }

    /**
     * 送礼
     *
     * @param tmid 收到礼物的会员ID、如视频所属用户
     * @param id   礼物ID
     * @param type 类型：视频（1）、任务（2）、商品（3）、品牌（4）、网红（5）默认：1
     * @param oid  类型对像ID：如视频ID
     * @param uid  登录用户ID
     * @return
     */
    public void songLi(String tmid, String id, String type, String oid, String uid) {

        userAPI.songLi(tmid, id, type, oid, uid).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mSongliListener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                mSongliListener.onErrorSongli(e.getMessage());
            }

            @Override
            public void onNext(String s) {
                mSongliListener.onSuccessSongLi(s);
            }
        });

    }

    /**
     * @param gift_num 礼物个数
     * @param gift_id  礼物id
     * @param member   接收会员的id
     * @param cmember  送礼物会员的id
     * @param type     视频（1）、任务（2）、商品（3）、品牌（4）、网红（5）
     * @param OID      对象id
     */
    public void sendGift(String gift_num, String gift_id, String member, String cmember, String type, String OID, final OnObjectListener<String> listener) {
        userAPI.sendGift(gift_num, gift_id, member, cmember, type, OID).subscribe(new Subscriber<String>() {
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
     * 点赞 评论
     *
     * @param ID
     * @param listener
     */
    public void commentLike(String ID, String uid, final OnObjectListener<String> listener) {
        commonAPI.commentLike(ID, uid).subscribe(new Subscriber<String>() {
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
     * 实现愿望
     *
     * @param modelId  宝箱ID
     * @param userId   用户ID
     * @param userName 用户名
     */
    public void achieveDesire(String modelId, String userId, String userName, final OnObjectListener<String> listener) {
        commonAPI.achieveDesire(modelId, userId, userName).subscribe(new Subscriber<String>() {
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
     * 分享成功
     *
     * @param oid
     * @param uid
     * @param type
     * @param listener
     */
    public void shareSuccess(String oid, String uid, String type, final OnObjectListener<String> listener) {
        commonAPI.shareSuccess(oid, uid, type).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                listener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(0, e.getMessage());
            }

            @Override
            public void onNext(String s) {
                listener.onSuccess(s);
            }
        });
    }

    /**
     * 删除发布的图片或者视频
     *
     * @param uid
     * @param mid
     * @param listener
     */
    public void deleteRelease(String uid, String mid, final OnObjectListener<String> listener) {
        commonAPI.deleteRelease(uid, mid).subscribe(new Subscriber<String>() {
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

    public void deleteImage(String uid, String url, final OnObjectListener<String> listener) {
        commonAPI.deleteImage(uid, url).subscribe(new Subscriber<String>() {
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
     * 意见反馈
     *
     * @param feedText
     * @param phone
     * @param email
     * @param userId
     */
    public void feedBack(String feedText, String phone, String email, String userId, String userName, final OnObjectListener<String> listener) {
        userAPI.feedBack(feedText, phone, email, userId, userName).subscribe(new Subscriber<String>() {
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
     * @param key 红包id
     * @param uid 用户id
     */
    public void drawRedPacket(String key, String uid, int state, final OnObjectListener<String> listener) {
        userAPI.drawRedPacket(key, uid, state).subscribe(new Subscriber<String>() {
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
     * 获取 达人认证标签
     *
     * @param pages
     * @param listener
     */
    public void getTalentLabelList(String pages, final OnObjectsListener<TalentLabelBean> listener) {
        commonAPI.getTalentLabelList(pages).subscribe(new Subscriber<ArrayList<TalentLabelBean>>() {
            @Override
            public void onCompleted() {
                listener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(-1, e.getMessage());
            }

            @Override
            public void onNext(ArrayList<TalentLabelBean> talentLabelBeen) {
                listener.onSuccess(talentLabelBeen);
            }
        });
    }

    /**
     * 判断密码是否正确
     *
     * @param userId
     * @param password
     */
    public void getVerifyPassword(String userId, String password, String phoneNum, final OnObjectListener<String> listener) {
        commonAPI.getVerifyPassword(userId, password, phoneNum).subscribe(new Subscriber<String>() {

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
     * 取消绑定
     *
     * @param userId
     * @param platform 第三方名称
     */
    public void unbindThird(String userId, String platform, final OnObjectListener<String> listener) {
        userAPI.unbindThird(userId, platform).subscribe(new Subscriber<String>() {
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
     * 获取余额
     *
     * @param userId
     * @param listener
     */
    public void getBalance(String userId, final OnObjectListener<String> listener) {
        userDetailsAPI.getBalance(userId).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                listener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(-1, e.getMessage());
            }

            @Override
            public void onNext(Integer s) {
                listener.onSuccess(String.valueOf(s));
            }
        });
    }

    /**
     * 获取礼物列表
     *
     * @param userId
     * @param listener
     */
    public void getGiftList(String userId, final OnObjectListener<ArrayList<LiWuBean>> listener) {
        userDetailsAPI.getGiftList(userId).subscribe(new Subscriber<ArrayList<LiWuBean>>() {
            @Override
            public void onCompleted() {
                listener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(-1, e.getMessage());
            }

            @Override
            public void onNext(ArrayList<LiWuBean> liWuBeen) {
                listener.onSuccess(liWuBeen);
            }
        });
    }

    public interface CommonSongliListener extends BaseListener {
        void onSuccessSongLi(String result);

        void onErrorSongli(String err);
    }
}
