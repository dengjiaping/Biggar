package cn.biggar.biggar.contract;

import java.util.List;

import cn.biggar.biggar.base.BasePresenter;
import cn.biggar.biggar.base.BaseView;
import cn.biggar.biggar.bean.CardBean;
import cn.biggar.biggar.bean.update.ContentBean;
import me.dudu.livegiftview.GiftModel;

/**
 * Created by Chenwy on 2017/9/4.
 */

public class ContentContract {
    public interface View extends BaseView {
        void showContents(ContentBean contentBean);

        void loadMoreCommentError(String errorMsg);

        void addComments(List<ContentBean.CommentList> commentLists);

        void addFollowSuccess();

        void addFollowError(String errorMsg);

        void canccelFollowSuccess();

        void canccelFollowError(String errorMsg);

        void commentSuccess(ContentBean.CommentList commentBean);

        void commentError(String errorMsg);

        void likeResult(int position, String clicks, String fabulous);

        void showGiftList(List<GiftModel> liWuBeen);

        void requestGiftFail();

        void sendGiftSuccess(String isRed, List<CardBean> cardBeens);

        void sendGiftFail();

    }

    public abstract static class Presenter extends BasePresenter<View> {

        public abstract void requestContents(String id, String uid);

        public abstract void loadMoreComments(String id, String uid, int pages, int p);

        public abstract void addFollow(String id, String nickname, int type, String uid, String usernick);

        public abstract void cancelFollow(String uid, String oid);

        public abstract void comment(String id, String content, String type, String uid, String usernick);

        public abstract void like(int position, String id, String userId);

        public abstract void requestGift(String isFloat, String uid);

        public abstract void sendGift(int giftNum, String giftId, String member, String cmember, String type, String OID);

        public abstract void shareSuccess(String oid,String type);
    }
}
