package cn.biggar.biggar.presenter.update;

import java.util.List;
import cn.biggar.biggar.app.Constants;
import cn.biggar.biggar.bean.BgResponse;
import cn.biggar.biggar.bean.CardBean;
import cn.biggar.biggar.bean.update.ContentBean;
import cn.biggar.biggar.contract.ContentContract;
import cn.biggar.biggar.http.BaseArrayObserver;
import cn.biggar.biggar.http.BaseObjectObserver;
import cn.biggar.biggar.http.BaseObserver;
import cn.biggar.biggar.http.HttpMethods;
import cn.biggar.biggar.http.ParamsUtils;
import cn.biggar.biggar.http.RxSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import me.dudu.livegiftview.GiftModel;

/**
 * Created by Chenwy on 2017/9/4.
 */

public class ContentPresenter extends ContentContract.Presenter {
    @Override
    public void requestContents(String id, String uid) {
        HttpMethods.getInstance().getContentApi()
                .requestContentData(id, uid)
                .compose(RxSchedulers.<ContentBean>compose())
                .subscribe(new BaseObjectObserver<ContentBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    protected void onHandleSuccess(ContentBean contentBean) {
                        mView.showContents(contentBean);
                    }

                    @Override
                    protected void onHandleError(int code, String msg) {
                        mView.showError(msg);
                    }
                });
    }

    @Override
    public void loadMoreComments(String id, String uid, int pages, int p) {
        HttpMethods.getInstance().getContentApi()
                .loadMoreComments(ParamsUtils.params("ID", id)
                        .params("UID", uid)
                        .params("p", p)
                        .params("pages", pages)
                        .params("VTYPE", "VIDEO")
                        .commitParams())
                .compose(RxSchedulers.<List<ContentBean.CommentList>>compose())
                .subscribe(new BaseArrayObserver<ContentBean.CommentList>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    protected void onHandleSuccess(List<ContentBean.CommentList> t) {
                        mView.addComments(t);
                    }

                    @Override
                    protected void onHandleError(int code, String msg) {
                        if (code == Constants.CODE_NETWORK_ERROR) {
                            mView.loadMoreCommentError(msg);
                        } else {
                            mView.loadMoreCommentError("加载失败");
                        }
                    }
                });
    }

    @Override
    public void addFollow(String id, String nickname, int type, String uid, String usernick) {
        HttpMethods.getInstance().getApiService()
                .addFollow(ParamsUtils.params("ID", id)
                        .params("TIT", nickname)
                        .params("TYPE", "1")
                        .params("E_UID", uid)
                        .params("E_NAME", usernick)
                        .commitParams())
                .compose(RxSchedulers.<BgResponse<String>>compose())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    protected void onHandleSuccess(String s) {
                        mView.addFollowSuccess();
                    }

                    @Override
                    protected void onHandleError(int code, String msg) {
                        mView.addFollowError("关注失败：" + msg);
                    }
                });
    }

    @Override
    public void cancelFollow(String uid, String oid) {
        HttpMethods.getInstance().getApiService()
                .cancelFollow(ParamsUtils.params("uid", uid)
                        .params("conid", oid)
                        .params("typeid", "1")
                        .commitParams())
                .compose(RxSchedulers.<BgResponse<String>>compose())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    protected void onHandleSuccess(String s) {
                        mView.canccelFollowSuccess();
                    }

                    @Override
                    protected void onHandleError(int code, String msg) {
                        mView.canccelFollowError("取消关注失败：" + msg);
                    }
                });
    }

    @Override
    public void comment(String id, String content, String type, String uid, String usernick) {
        HttpMethods.getInstance().getContentApi()
                .comment(ParamsUtils.params("E_RelationID", id)
                        .params("E_Content", content)
                        .params("E_Type", type)
                        .params("E_UID", uid)
                        .params("E_NAME", usernick)
                        .commitParams())
                .compose(RxSchedulers.<BgResponse<ContentBean.CommentList>>compose())
                .subscribe(new BaseObserver<ContentBean.CommentList>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    protected void onHandleSuccess(ContentBean.CommentList commentList) {
                        mView.commentSuccess(commentList);
                    }

                    @Override
                    protected void onHandleError(int code, String msg) {
                        if (code == Constants.CODE_NETWORK_ERROR) {
                            mView.commentError(msg);
                        } else {
                            mView.commentError("评论失败");
                        }
                    }
                });
    }

    @Override
    public void like(final int position, String id, String userId) {
        HttpMethods.getInstance().getContentApi()
                .like(id, userId)
                .compose(RxSchedulers.<BgResponse<String>>compose())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    protected void onHandleSuccess(String s) {
                        mView.likeResult(position, s, "1");
                    }

                    @Override
                    protected void onHandleError(int code, String msg) {
                        if (code != Constants.CODE_NETWORK_ERROR) {
                            mView.likeResult(position, msg, "0");
                        }
                    }
                });
    }

    @Override
    public void requestGift(String isFloat, String uid) {
        HttpMethods.getInstance().getContentApi()
                .requestGift(ParamsUtils.params("p", "1")
                        .params("pages", "100")
                        .params("uid", uid)
                        .params("is_Float", isFloat)
                        .commitParams())
                .compose(RxSchedulers.<List<GiftModel>>compose())
                .subscribe(new BaseArrayObserver<GiftModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    protected void onHandleSuccess(List<GiftModel> t) {
                        mView.showGiftList(t);
                    }

                    @Override
                    protected void onHandleError(int code, String msg) {
                        mView.requestGiftFail();
                    }
                });
    }

    @Override
    public void sendGift(int giftNum, String giftId, String member, String cmember, String type, String OID) {
        HttpMethods.getInstance().getContentApi()
                .sendGift(ParamsUtils.params("gift_num", giftNum)
                        .params("gift_id", giftId)
                        .params("member", member)
                        .params("cmember", cmember)
                        .params("type", type)
                        .params("OID", OID)
                        .commitParams())
                .compose(RxSchedulers.<BgResponse<List<CardBean>>>compose())
                .subscribe(new BaseObjectObserver<BgResponse<List<CardBean>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    protected void onHandleSuccess(BgResponse<List<CardBean>> listBgResponse) {
                        mView.sendGiftSuccess(listBgResponse.url, listBgResponse.info);
                    }

                    @Override
                    protected void onHandleError(int code, String msg) {
                        mView.sendGiftFail();
                    }
                });
    }

    @Override
    public void shareSuccess(String oid, String type) {
        HttpMethods.getInstance().getContentApi()
                .shareSuccess(ParamsUtils.params("SHARE_ID",oid)
                .params("SHARE_TYPE",type)
                .commitParams())
                .compose(RxSchedulers.<BgResponse<String>>compose())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    protected void onHandleSuccess(String s) {
                    }

                    @Override
                    protected void onHandleError(int code, String msg) {
                    }
                });
    }
}
